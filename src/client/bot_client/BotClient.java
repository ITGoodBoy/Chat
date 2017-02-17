package client.bot_client;


import client.AbstractClient;
import common.Connection;
import common.Message;
import common.MessageType;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;


public class BotClient extends AbstractClient {
    private final static AtomicInteger botCount = new AtomicInteger(1);

    protected class BotServerHandler extends AbstractServerHandler
    {
        @Override
        public void run() {
            try {

                clientHandshake(getConnection());
                while (true) {
                    mainLoop(getConnection());
                }
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
                        connection.sendMessage(new Message(MessageType.USER_NAME, getUserName()));
                    }
                    else if (message.getMessageType() == MessageType.NAME_ACCEPTED)
                    {
                        getConnection().sendMessage(new Message(MessageType.TEXT,
                                "Всем привет! Я чат бот, понимаю команды: -дата, -день, -месяц, -год, -время, -час, -минуты, -секунды"));
                        return;
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void processIncomingMessage(String message) {
            try {
                getConnection().sendMessage(new Message(MessageType.TEXT, message));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void mainLoop(Connection connection) throws IOException, ClassNotFoundException {
            Message message = connection.receiveMessage();
            if (message.getMessageType() != MessageType.TEXT) return;
            String[] split = message.getText().split(": ");
            if (split.length < 2) return;
            String name = split[0];
            String text = split[1];
            Map<String, String> map = new HashMap<>();
            map.put("-дата", "d.MM.YYYY");
            map.put("-день", "d");
            map.put("-месяц", "MMMM");
            map.put("-год", "YYYY");
            map.put("-время", "H:mm:ss");
            map.put("-час", "H");
            map.put("-минуты", "m");
            map.put("-секунды", "s");
            String answerText = "";
            for (Map.Entry<String, String> pair: map.entrySet())
            {
                if (pair.getKey().equals(text))
                {
                    answerText = pair.getValue();
                }
            }
            if (answerText.length() == 0) return;
            SimpleDateFormat dateFormat = new SimpleDateFormat(answerText);
            processIncomingMessage("Информация для " + name + ": " + dateFormat.format(Calendar.getInstance().getTime()));
        }

        @Override
        public void informAboutAddingNewUser(String text) {

        }

        @Override
        public void informAboutDeletingNewUser(String text) {

        }

        @Override
        public String getUserName() {
            String botName;
            if (botCount.get() < 10) {
                botName = "Bot_number_0" + botCount.get();
            }
            else botName = "Bot_number_" + botCount.get();
            botCount.incrementAndGet();
            return botName;
        }
    }


    @Override
    public void start() {
        new BotServerHandler().start();
    }
}
