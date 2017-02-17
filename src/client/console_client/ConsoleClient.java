package client.console_client;

import client.AbstractClient;
import common.Connection;
import common.Message;
import common.MessageType;

import java.io.IOException;

public class ConsoleClient extends AbstractClient {
    private ConsoleHelper consoleHelper;

    protected class ConsoleServerHandler extends AbstractServerHandler
    {
        @Override
        public void processIncomingMessage(String message) {
            consoleHelper.writeTextInConsole(message);
        }

        @Override
        public void run() {
            consoleHelper = new ConsoleHelper();
            try {
                clientHandshake(getConnection());
                mainLoop(getConnection());
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void clientHandshake(Connection connection) throws IOException {
            while (true)
            {
                try {
                    Message message = connection.receiveMessage();
                    if (message.getMessageType() == MessageType.NAME_REQUEST)
                    {
                        consoleHelper.writeTextInConsole("Введите ваше имя");
                        connection.sendMessage(new Message(MessageType.USER_NAME, getUserName()));
                    }
                    else if (message.getMessageType() == MessageType.NAME_ACCEPTED)
                    {
                        consoleHelper.writeTextInConsole("для выхода напишите -exit");
                        setClientConnected(true);
                        return;
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void mainLoop(Connection connection) throws IOException, ClassNotFoundException {
            while (true)
            {
                Message message = connection.receiveMessage();
                if (message.getMessageType() == MessageType.TEXT)
                {
                    processIncomingMessage(message.getText());
                }
                else if (message.getMessageType() == MessageType.USER_ADDED)
                {
                    informAboutAddingNewUser(message.getText());
                }
                else if (message.getMessageType() == MessageType.USER_REMOVED)
                {
                    informAboutDeletingNewUser(message.getText());
                }
                else if (message.getMessageType() == MessageType.ADMIN_DELETE_USER)
                {
                    System.exit(0);
                }
                else throw new IOException("Unexpected MessageType");
            }

        }

        @Override
        public void informAboutAddingNewUser(String text) {
            consoleHelper.writeTextInConsole(text);
        }

        @Override
        public void informAboutDeletingNewUser(String text) {
            consoleHelper.writeTextInConsole(text);
        }

        @Override
        public String getUserName() {
            while (true)
            {
                try {
                    return consoleHelper.readTextFromConsole();
                } catch (IOException e) {
                    consoleHelper.writeTextInConsole("Возникла ошибка при вводе имени, повторите попытку");
                }
            }
        }
    }

    @Override
    public void start() {
        ConsoleServerHandler consoleServerHandler = new ConsoleServerHandler();
        consoleServerHandler.setDaemon(true);
        consoleServerHandler.start();
        //wait until confirm the client's connection to the server
        setClientConnected(false);

        while (true)
        {
            while (isClientConnected())
            {
                try {
                    String text = consoleHelper.readTextFromConsole();
                    if (text.equals("-exit"))
                    {
                        System.exit(0);
                    }
                    getConnection().sendMessage(new Message(MessageType.TEXT, text));
                } catch (IOException e) {
                    consoleHelper.writeTextInConsole("Неудалось отправить сообщение");
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
