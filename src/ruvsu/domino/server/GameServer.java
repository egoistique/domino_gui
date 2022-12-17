package ruvsu.domino.server;

import ruvsu.domino.model.*;

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
        ObjectOutputStream outObject = new ObjectOutputStream(socket.getOutputStream());

        String request;
        boolean gameOver = false;
        while (!gameOver){
            if((request = in.readLine()) != null){
                if(request.contains("BEGIN")) {
                    System.out.println("successful get command begin");
                    process.beginGamePr(3, Integer.parseInt(request.substring(request.length() - 1)));
                    GameBoard gameBoard = process.getBoard();
                    List<Player> players = process.getPlayers();
                    Heap heap = process.getHeap();

                    //отдаем команду что они созданы
                    outObject.writeObject("BEGIN_COMPLETE");
                    sendState(outObject, gameBoard, heap, players);
                }  else if(request.contains("FIRST_STEP")) {
                    System.out.println("successful get command first step");

                    process.firstStep();

                    GameBoard gameBoard = process.getBoard();
                    List<Player> players = process.getPlayers();
                    Heap heap = process.getHeap();

                    //отдаем команду что они созданы
                    outObject.writeObject("FIRST_STEP_COMPLETE");
                    sendState(outObject, gameBoard, heap, players);
                } else if(request.contains("NEXT_STEP")) {
                    System.out.println("successful get command next step");

                    String code = request;

                    if(!request.equals("NEXT_STEP")){
                        code = code.replace("NEXT_STEP", "");
                    } else {
                        code = "";
                    }
                    process.gameStep(3, code);

                    GameBoard gameBoard = process.getBoard();
                    List<Player> players = process.getPlayers();
                    Heap heap = process.getHeap();

                    //отдаем команду что они созданы
                    outObject.writeObject("NEXT_STEP_COMPLETE");
                    sendState(outObject, gameBoard, heap, players);
                }
                System.out.println(request);
            }


//            if(request.equals("99999")){ //TODO придумать вариант команды "конец игры"
//                gameOver = true;
//                socket.close();
//            } else {
//                networkStrategy.setValue(request);
//                process.gameStep(3, "");
//            }

        }
    }

    private void sendState(ObjectOutputStream outObject, GameBoard gameBoard, Heap heap, List<Player> players) throws IOException {
        outObject.reset();
        outObject.writeObject(gameBoard);
        outObject.writeObject(heap);
        outObject.writeObject(players);
        outObject.flush();
    }
}

