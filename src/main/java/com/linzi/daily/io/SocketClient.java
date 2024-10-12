package com.linzi.daily.io;

import java.io.*;
import java.net.Socket;

public class SocketClient {
    public static void main(String[] args) throws IOException {
        try (Socket socket = new Socket("192.168.80.163", 9100)) {
            InputStream input = socket.getInputStream();

            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);

            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            String line = reader.readLine();
            System.out.println(line);

            writer.println("helo 192.168.80.163");

            line = reader.readLine();
            System.out.println(line);

            writer.println("quit");
            line = reader.readLine();
            System.out.println(line);
        }
    }
}
