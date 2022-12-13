package ruvsu.domino.server;

import ruvsu.domino.client.network.Helping;
import ruvsu.domino.client.network.NetworkGameProcess;
import ruvsu.domino.model.Heap;
import ruvsu.domino.model.IGameProcess;
import ruvsu.domino.model.LocalGameProcess;
import ruvsu.domino.model.Player;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class GameServer {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        GameServer server = new GameServer(9999);
        server.start();
    }

    private final int port;

    private ServerSocket serverSocket;

    private IGameProcess process;

    private Helping help = new Helping();

    public GameServer(int port) {
        this.port = port;
    }

    public void start() throws IOException, ClassNotFoundException {
        System.out.printf("server started on: %d%n",  port);
        serverSocket = new ServerSocket(port);
        Socket socket = serverSocket.accept();

        System.out.printf("Client connected from: %s%n",  socket.getInetAddress());

        process = new LocalGameProcess(new Player());

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        ObjectOutputStream outObject = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream inObject = new ObjectInputStream(socket.getInputStream());

        String request;
        boolean gameOver = false;
        while (!gameOver){
            if((request = in.readLine()) != null){
                //если запрос на создание игрков, вызываем у локального процесса метод createPlayers
                if(request.contains("begin")){
                    System.out.println("successful get command");
                    Player pl = process.beginGamePr(3, Integer.parseInt(request.substring(request.length() - 1)));
                    List<Player> players = process.getPlayers();
                    Heap heap = process.getHeap();
                    //отдаем команду что они созданы
                    out.println("began");

                    outObject.writeObject(pl);
                    outObject.writeObject(heap);
                    outObject.writeObject(players);
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
