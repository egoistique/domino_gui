package server;

import model.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
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

    private SendData sendData;

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
            if((request = in.readLine()) != null) {
                if (request.contains("BEGIN")) {
                    System.out.println("successful get command begin");
                    process.beginGamePr(3, Integer.parseInt(request.substring(request.length() - 1)));

                    outObject.writeObject("BEGIN_COMPLETE");
                    sendState(outObject);

                } else if (request.contains("FIRST_STEP")) {
                    System.out.println("successful get command first step");
                    process.firstStep();

                    outObject.writeObject("FIRST_STEP_COMPLETE");
                    sendState(outObject);

                } else if (request.contains("NEXT_STEP")) {
                    System.out.println("successful get command next step");
                    String code = request;

                    if (!request.equals("NEXT_STEP")) {
                        code = code.replace("NEXT_STEP", "");
                    } else {
                        code = "";
                    }
                    process.gameStep(3, code);

                    outObject.writeObject("NEXT_STEP_COMPLETE");
                    sendState(outObject);

                } else if(request.contains("TAKE_FROM_BAZAAR")) {
                    process.takeTileFromBazaar();
                    outObject.writeObject("TAKE_FROM_BAZAAR_COMPLETE");
                    sendState(outObject);

                } else if (request.equals("GAME_OVER_CHECK")){
                    process.gameOverCheck(process.getPlayers());
                    outObject.writeObject("GAME_OVER_CHECK_COMPLETE");
                    sendState(outObject);

                    if (process.isGameOver()){
                        gameOver = true;
                        socket.close();
                    }
                }
                System.out.println(request);
            }
        }
    }

    private void sendState(ObjectOutputStream outObject) throws IOException {
        GameBoard gameBoard = process.getBoard();
        List<Player> players = process.getPlayers();
        Heap heap = process.getHeap();
        Player pl = process.getPl();
        boolean gameOver = process.isGameOver();
        int checkFor = process.getCheckFor();

        outObject.reset();
        sendData = new SendData(gameBoard, heap, pl, players, gameOver, checkFor);
        outObject.writeObject(sendData);
        outObject.flush();
    }
}

