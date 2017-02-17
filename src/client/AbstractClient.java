package client;

import client.bot_client.BotClient;
import client.console_client.ConsoleClient;
import client.gui_client.AdminGuiController;
import client.gui_client.ClientGuiController;
import common.Connection;

import java.io.IOException;
import java.net.Socket;

//this is our abstract class from which we inherited
public abstract class AbstractClient {
    public static final int CONSOLE_CLIENT = 0;
    public static final int BOT_CLIENT = 1;
    public static final int GUI_CLIENT = 2;
    public static final int ADMIN_CLIENT = 3;

    private Connection connection;
    private  boolean clientConnected;

    public AbstractClient()
    {
        try {
            connection = new Connection(new Socket(getServerAddress(), getServerPort()));
            clientConnected = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//factory method
    public static AbstractClient create(int type)
    {

        switch (type)
        {
            case CONSOLE_CLIENT: return new ConsoleClient();
            case BOT_CLIENT: return new BotClient();
            case GUI_CLIENT: return new ClientGuiController();
            case ADMIN_CLIENT: return new AdminGuiController();
            default:return new ConsoleClient();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public boolean isClientConnected() {
        return clientConnected;
    }

    public void setClientConnected(boolean clientConnected) {
        this.clientConnected = clientConnected;
    }

    public String getServerAddress()
    {
        return "localhost";
    }
    public int getServerPort()
    {
        return 80;
    }

    //method to start a new thread(ServerHandler) processing messages from the server
    public abstract void start();

    protected abstract class AbstractServerHandler extends Thread
    {
        //handshake with server -sends a new username
        public abstract void clientHandshake(Connection connection) throws IOException;
        public abstract void processIncomingMessage(String message);
        //Server processes messages
        public abstract void mainLoop(Connection connection) throws IOException, ClassNotFoundException;
        public abstract void informAboutAddingNewUser(String text);
        public abstract void informAboutDeletingNewUser(String text);
        public abstract String getUserName();
    }
}
