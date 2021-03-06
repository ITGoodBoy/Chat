package client.gui_client;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Sergey on 12.02.2017.
 */
//Our database in which are stored the names of connected users
public class ClientGuiModel {

    private final Set<String> allUserNames = new HashSet<>();
    private String newMessage;

    public Set<String> getAllUserNames() {

        return Collections.unmodifiableSet(allUserNames);
    }

    public String getNewMessage() {
        return newMessage;
    }

    public void setNewMessage(String newMessage) {
        this.newMessage = newMessage;
    }

    public void addUser(String newUserName)
    {
        allUserNames.add(newUserName);
    }
    public void deleteUser(String userName)
    {
        allUserNames.remove(userName);
    }

}
