package Client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.net.Socket;

public class ClientStartpoint {
    private static final String IP = "127.0.0.1"; //localhost
    private static final int PORT = 8080;
    Message messageObj;

    public void start() {
        try {
            Socket socket = new Socket(IP, PORT);
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
            //Сначала отправляем логин!!!

            System.out.println("Введите имя");
            String username = consoleReader.readLine();

            writer.println(username);
            writer.flush();

            MessageHandler handler = new MessageHandler(socket);
            handler.start();

            while (true) {
                String message = consoleReader.readLine();
                if (message == null) {
                    break;
                }
                if (message.equals("exit")) {
                    break;
                }
                String[] s = message.split("@", 3);

                messageObj = new Message(username, s[1], s[2]);

                Gson gson = new GsonBuilder()
                        .create();

                String messageJson = gson.toJson(messageObj);

                writer.println(messageJson);
                writer.flush();

            }
            handler.stopHandler();
            writer.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
