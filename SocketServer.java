package Sockets;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class SocketServer {

    private static final Logger serverLogger = Logger.getLogger(SocketServer.class.getName());

    public static void main(String[] args) {

        Properties config = new Properties();
        try (InputStream input = new FileInputStream("./src/Sockets/config.properties")) {
            config.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int interval = Integer.parseInt(config.getProperty("interval"));

        try {
            FileHandler fh = new FileHandler("ServerLogs.log", true);
            serverLogger.setUseParentHandlers(false);
            serverLogger.addHandler(fh);
            fh.setFormatter(new SimpleFormatter());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (ServerSocket serverSocket = new ServerSocket(8080)) {

            serverLogger.info("Сервер запущен");
            while(true) {
                try(Socket socket = serverSocket.accept();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {

                    serverLogger.info("Клиент подключился");

                        String request = reader.readLine();
                        serverLogger.info("Сообщение получено " + request);

                        String response = reverseString(request) + " Сервер написан Титовым Ю.П. М3О-207Б-23";
                        Thread.sleep(interval);
                        writer.write(response);
                        serverLogger.info("Сообщение отправлено " + response);
                        writer.newLine();
                        writer.flush();

                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
                serverLogger.info("Клиент отключился");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    static String reverseString(String s) {
        StringBuilder sb = new StringBuilder();
        String[] parse = s.split(" ");
        sb.append(parse[0]).append(" ");
        sb.append(parse[1]).append(" ");
        sb.append(parse[2]);
        return sb.reverse().toString();
    }

}

