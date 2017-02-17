package client.gui_client;

import common.Message;
import common.MessageType;

import java.io.IOException;

/**
 * Created by Sergey on 17.02.2017.
 */
public class AdminGuiController extends ClientGuiController {
    protected class AdminGuiServerHandler extends GuiServerHandler
    {
        @Override
        public void processIncomingMessage(String message) {
            if (message.contains("-delete user "))
            {
                String user = message.substring(message.lastIndexOf(" ") + 1);
                deleteUserFromChat(user);
            }
            else super.processIncomingMessage(message);
        }

    }
//The method adds the ability to delete users in the chat (-delete user XXX)

    public void deleteUserFromChat(String userName)
    {
        try {
            getConnection().sendMessage(new Message(MessageType.ADMIN_DELETE_USER, userName));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        getView().refreshUsers();
    }


    @Override
    public void start() {
        new AdminGuiServerHandler().start();
    }
}
