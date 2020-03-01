package sample;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * создание клиента со всеми необходимыми утилитами, точка входа в программу в классе Client
 */

public class ClientSomething {


    public String bufferString;

    public String chatBoxText;
    private Socket socket;
    private BufferedReader in; // поток чтения из сокета
    private static BufferedWriter out; // поток записи в сокет
    private BufferedReader inputUser; // поток чтения с консоли
    private String addr = ipAddr; // ip адрес клиента
    private int port; // порт соединения
    private String nickname; // имя клиента
    private Date time;
    private String dtime;
    private SimpleDateFormat dt1;
    public static String ipAddr = "127.0.0.1";
    public static int ipPort = 8080;
    public ChatController chatController;

    public ClientSomething(ChatController chat, String nick) {
        this.addr = addr;
        this.port = ipPort;
        this.chatController = chat;
        this.nickname = nick;

        try {
            this.socket = new Socket(addr, port);
        } catch (IOException e) {
            System.err.println("Socket failed");
        }
        try {
            // потоки чтения из сокета / записи в сокет, и чтения с консоли
            inputUser = new BufferedReader(new InputStreamReader(System.in));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.pressNickname(); // перед началом необходимо спросит имя
            new ReadMsg().start(); // нить читающая сообщения из сокета в бесконечном цикле
        } catch (IOException e) {
            // Сокет должен быть закрыт при любой
            // ошибке, кроме ошибки конструктора сокета:
            ClientSomething.this.downService();
        }
        // В противном случае сокет будет закрыт
        // в методе run() нити.
    }

    /**
     * просьба ввести имя,
     * и отсылка эхо с приветсвием на сервер
     */

    public void send(String userWord) {
        time = new Date(); // текущая дата
        dt1 = new SimpleDateFormat("HH:mm:ss"); // берем только время до секунд
        dtime = dt1.format(time); // время
        try {
            out.write("(" + dtime + ") " + nickname + ": " + userWord + "\n"); // отправляем на сервер
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void pressNickname() {
        try {
            out.write("Hello " + nickname + "\n");
            out.flush();
        } catch (IOException ignored) {

        }

    }

    public void sendText() {
        try{
            time = new Date(); // текущая дата
            dt1 = new SimpleDateFormat("HH:mm:ss"); // берем только время до секунд
            dtime = dt1.format(time); // время
            out.write("(" + dtime + ") " + nickname + ": " + chatController.chatText.getText() + "\n"); // отправляем на сервер
            out.flush();
        }catch (IOException e){
            e.printStackTrace();
        }
        chatController.removeTest();
    }
    /**
     * закрытие сокета
     */
    private void downService() {
        try {
            if (!socket.isClosed()) {
                socket.close();
                in.close();
                out.close();
            }
        } catch (IOException ignored) {
        }
    }

    // нить чтения сообщений с сервера
    private class ReadMsg extends Thread {
        @Override
        public void run() {

            String str;
            try {
                while (true) {
                    str = in.readLine(); // ждем сообщения с сервера
                    System.out.println(str);
                    chatController.addText(str);
                    //System.out.println("chatBoxTest equals " + chatBoxText);// пишем сообщение с сервера на консоль
                }
            } catch (IOException e) {
                ClientSomething.this.downService();
            }
        }
    }

}
