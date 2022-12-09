package ruvsu.domino.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class GameServer {

    public static void main(String[] args) throws IOException {
        GameServer server = new GameServer(9999);
        server.start();
    }

    private final int port;

    private ServerSocket serverSocket;

    public GameServer(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        System.out.printf("server started on: %d%n",  port);
        serverSocket = new ServerSocket(port);
        Socket socket = serverSocket.accept();

        System.out.printf("Client connected from: %s%n",  socket.getInetAddress());

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        String command;
        while (true){
            if((command = in.readLine()) != null){
                System.out.println(command);
            }
        }
    }
}
