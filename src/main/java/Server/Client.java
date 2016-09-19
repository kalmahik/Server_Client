package Server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private SendingThread sender;
    private String username;

    public Client(Socket socket) {
        this.socket = socket;
        prepareStreams();
    }

    /**
     * Метод открытия потоков ввода-вывода
     */
    private void prepareStreams() {
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод авторизации
     */
    public void login() {
        while (username == null) {
            try {
                String username = reader.readLine();
                if (ClientManager.getInstance().hasClient(username)) {
                    writer.println("Client with same username exists\nTry another username");
                    writer.flush();
                } else {
                    this.username = username;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ClientManager.getInstance().onClientSignedIn(this);
    }

    /**
     * Запуск основной работы с клиентом (открывается возможность переписки)
     */
    public void startMessaging() {
        sender = new SendingThread();
        sender.start();
        try {
            while (true) {
                String message = reader.readLine();
                if (message == null) {
                    stopClient();
                    break;
                }
                onMessageReceived(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Отключение клиента
     */
    public void stopClient() {
        ClientManager clientManager = ClientManager.getInstance();
        clientManager.onClientDisconnected(this);
        sender.stopSending();
        try {
            writer.close();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Событие получения сообщения
     *
     * @param messageJson полученное сообщение в необработанном виде
     */
    public void onMessageReceived(String messageJson) {
        //TODO: message (json) -> message (object)
        Gson gson = new Gson();
        Message messageObj = gson.fromJson(messageJson, Message.class);
        sender.addMessage(messageObj);
    }

    /**
     * Метод отправки сообщений
     *
     * @param messageObj отправляемое сообщение //TODO: type -> Message
     */
    public void sendMessage(Message messageObj) {
        //TODO: message to json
        Gson gson = new GsonBuilder()
                .create();
        String messageJson = gson.toJson(messageObj);
        writer.println(messageJson);
        writer.flush();
    }

    public String getUsername() {
        return username;
    }
}
