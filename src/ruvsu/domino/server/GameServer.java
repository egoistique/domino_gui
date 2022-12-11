package ruvsu.domino.server;

import ruvsu.domino.client.network.Helping;
import ruvsu.domino.model.IGameProcess;
import ruvsu.domino.model.LocalGameProcess;
import ruvsu.domino.model.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class GameServer {

    public static void main(String[] args) throws IOException {
        GameServer server = new GameServer(9999);
        server.start();
    }

    private final int port;

    private ServerSocket serverSocket;

    private IGameProcess process;

    private Helping help;

    public GameServer(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        System.out.printf("server started on: %d%n",  port);
        serverSocket = new ServerSocket(port);
        Socket socket = serverSocket.accept();

        System.out.printf("Client connected from: %s%n",  socket.getInetAddress());

        process = new LocalGameProcess(new Player());

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        String request;
        boolean gameOver = false;
        while (!gameOver){
            if((request = in.readLine()) != null){
                //если запрос на создание игрков, вызываем у локального процесса метод createPlayers
                if(request.contains("createPlayers")){
                    Helping.players = process.createPlayers(Integer.parseInt(request.substring(request.length() - 1)));
                    //отдаем команду что они созданы
                    out.println("players created");
                }


                //если запрос представляет собой число участников, у процесса запускается новая игра
                if(request.equals("2") || request.equals("3") || request.equals("4")){
                    process.beginGamePr(3, Integer.parseInt(request));
                    out.println();
                }


                System.out.println(request);
            }

//            if(request.equals("99999")){ //TODO придумать вариант команды "конец игры", 35 минуты до конца
//                gameOver = true;
//                socket.close();
//            } else {
//                networkStrategy.setValue(request);
//                process.gameStep(3, "");
//            }

        }
    }
}
