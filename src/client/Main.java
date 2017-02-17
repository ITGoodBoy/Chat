package client;


public class Main {

    public static void main(String[] args) throws InterruptedException {
      //  AbstractClient consoleClient = AbstractClient.create(AbstractClient.CONSOLE_CLIENT);
       // consoleClient.start();
       // AbstractClient guiClient = AbstractClient.create(AbstractClient.GUI_CLIENT);
        //guiClient.start();
        AbstractClient adminGuiClient = AbstractClient.create(AbstractClient.ADMIN_CLIENT);
        adminGuiClient.start();
        Thread.sleep(9000);
        AbstractClient botClient = AbstractClient.create(AbstractClient.BOT_CLIENT);
        botClient.start();
    }
}
