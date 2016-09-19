package Client;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class MessageHandler extends Thread {
    private BufferedReader reader;
    private boolean alive = true;

    public MessageHandler(Socket socket) {
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (alive) {

                String messageJson = reader.readLine();
                if (messageJson == null) {
                    break;
                }
                Gson gson = new Gson();
                Message messageObj = gson.fromJson(messageJson, Message.class);
                System.out.println("Receiver: " + messageObj.getReceiver());
                System.out.println("Message:" +  messageObj.getMessage());


            }
            stopHandler();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void stopHandler() {
        if (alive && isAlive()) {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
            alive = false;
        }
    }
}
