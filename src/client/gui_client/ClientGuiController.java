package client.gui_client;

import client.console_client.ConsoleClient;
import common.Connection;
import common.Message;
import common.MessageType;

import java.io.IOException;

/**
 * Created by Sergey on 16.02.2017.
 */
public class ClientGuiController extends ConsoleClient {


    private ClientGuiModel model = new ClientGuiModel();
    private ClientGuiView view = new ClientGuiView(this);

    protected class GuiServerHandler extends ConsoleServerHandler
    {

        @Override
        public void clientHandshake(Connection connection) throws IOException {
            view.setClientConnected(isClientConnected());
            super.clientHandshake(connection);
        }


        @Override
        public void processIncomingMessage(String message) {
            model.setNewMessage(message);
            view.refreshMessages();
        }

        @Override
        public void informAboutAddingNewUser(String text) {
            model.addUser(text);
            view.refreshUsers();
        }

        @Override
        public void informAboutDeletingNewUser(String text) {
            model.deleteUser(text);
            view.refreshUsers();
        }

        @Override
        public String getUserName() {
            return view.getUserName();
        }
    }

    protected void sendTextMessage(String text)
    {
        try {
            getConnection().sendMessage(new Message(MessageType.TEXT, text));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public ClientGuiModel getModel() {
        return model;
    }

    public ClientGuiView getView() {
        return view;
    }

    @Override
    public void start() {
        new GuiServerHandler().start();
    }


}
