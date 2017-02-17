package server;

import common.Connection;
import common.Message;
import common.MessageType;

import java.io.IOException;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


 class ClientHandler extends Thread {

     // storage of all connected users
    private static Map<String, Connection> usersMap = new ConcurrentHashMap<>();

    private final Socket socket;

    public ClientHandler(Socket socket)
    {
        this.socket = socket;
    }



    @Override
    public void run() {
        String userName = null;
        try(Connection connection = new Connection(socket)) {
            userName = serverHandshake(connection);
            sendMessageAllUsers(new Message(MessageType.TEXT, userName + " вошёл в чат!"));
            sendMessageAllUsers(new Message(MessageType.USER_ADDED, userName));
            sendListOfUsers(connection);
            serverMainLoop(connection, userName);

        }
        catch (IOException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        finally {
            if (userName != null)
                usersMap.remove(userName);
            try {
                sendMessageAllUsers(new Message(MessageType.TEXT, userName + " вышел с чата"));
                sendMessageAllUsers(new Message(MessageType.USER_REMOVED, userName));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //NAME_REQUEST, and if it is not occupied it sends NAME_ACCEPTED response
    private String serverHandshake(Connection connection) throws IOException
    {
        while (true)
        {
            connection.sendMessage(new Message(MessageType.NAME_REQUEST));
            try {
                Message clientMessage = connection.receiveMessage();
                if (clientMessage.getMessageType() == MessageType.USER_NAME)
                {
                    String userName = clientMessage.getText();
                    if (!usersMap.containsKey(userName))
                    {
                        usersMap.put(userName, connection);
                        connection.sendMessage(new Message(MessageType.NAME_ACCEPTED));
                        return userName;
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
    //sends all current users on the specified connection(one user)
     private void sendListOfUsers(Connection connection) throws IOException
     {
         for (Map.Entry<String, Connection> pair: usersMap.entrySet())
         {
                 String name = pair.getKey();
                 Message message = new Message(MessageType.USER_ADDED, name);
                 connection.sendMessage(message);
         }
     }

    private void sendMessageAllUsers(Message message) throws IOException
    {
        for (Map.Entry<String, Connection> pair: usersMap.entrySet())
        {
            pair.getValue().sendMessage(message);
        }
    }
    //the main message loop, readdress message to all clients
    private void serverMainLoop(Connection connection, String userName) throws IOException, ClassNotFoundException
    {
        while (true)
        {
                Message message = connection.receiveMessage();
                //if the type of the text will send it to all users chat
                if (message.getMessageType() == MessageType.TEXT && userName != null)
                {
                    String text = userName + ": " + message.getText();
                    sendMessageAllUsers(new Message(MessageType.TEXT, text));
                }
                //If the request arrived from the client administrator to delete, delete the user
               else if (message.getMessageType() == MessageType.ADMIN_DELETE_USER)
                {
                    usersMap.get(message.getText()).sendMessage(new Message(MessageType.ADMIN_DELETE_USER));
                    usersMap.remove(message.getText());
                    sendMessageAllUsers(new Message(MessageType.TEXT, message.getText() + " вышел с чата"));
                    sendMessageAllUsers(new Message(MessageType.USER_REMOVED, message.getText()));
                }
        }
    }
}
