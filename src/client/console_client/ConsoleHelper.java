package client.console_client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

//It helps when working with a client console
public class ConsoleHelper {

    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public String readTextFromConsole() throws IOException {
        return reader.readLine();
    }

    public void writeTextInConsole(String text)
    {
        System.out.println(text);
    }
}
