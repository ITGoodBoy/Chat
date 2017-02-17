package server;

import java.net.ServerSocket;
import java.net.Socket;

//This is our server, it accepts connections from clients,
// and create a new thread(ClientHandler) that processes the messages from the client
 class Server {

    public static void main(String[] args) {
        while (true)
        {
            try(ServerSocket server = new ServerSocket(80))
            {
                Socket clientSocket = server.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clientHandler.setDaemon(true);
                clientHandler.start();
                Thread.sleep(10);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }
    }
}
