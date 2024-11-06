package Sockets;

import java.io.*;
import java.net.Socket;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ClientSocket {

    private static final Logger clientLogger = Logger.getLogger(ClientSocket.class.getName());

    public static void main(String[] args) {

        Properties config = new Properties();
        try (InputStream input = new FileInputStream("./src/Sockets/config.properties")){
            config.load(input);
        } catch (IOException e) {
            System.out.println("Ошибка при чтении конфигурации");
            return;
        }

        String server = config.getProperty("server.host");
        int port = Integer.parseInt(config.getProperty("server.port"));
        int interval = Integer.parseInt(config.getProperty("interval"));


        try {
            FileHandler fh = new FileHandler("ClientLogs.log", true);
            clientLogger.setUseParentHandlers(false);
            clientLogger.addHandler(fh);
            fh.setFormatter(new SimpleFormatter());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (Socket clientSocket = new Socket(server, port);
             BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()))) {

            clientLogger.info("Клиент подключился к " + server);

            Thread.sleep(interval);
            String request = "Воробьев Андрей Николаевич М3О-207Б-23";
            writer.write(request);
            clientLogger.info("Сообщение отправлено " + request);
            writer.newLine();
            writer.flush();
            String response = reader.readLine();
            clientLogger.info("Сообщение получено " + response);
        } catch (IOException | InterruptedException e){
            clientLogger.warning("Сервер не запущен");
        }



    }
}
