package client.network;

import model.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NetworkGameProcess extends AbstractGame {

    private List<Player> players = new ArrayList<>();
    private Map<Coordinates, Integer> activeTiles = new HashMap<>();

    private int checkFor = 0;

    Socket socket;
    private PrintWriter out;

    ObjectInputStream inObject;

    SendData sendData;

    public NetworkGameProcess(Player pl, String server, int port) {
        super(pl);
        try {
            socket = new Socket(server, port);

            out = new PrintWriter(socket.getOutputStream(), true);
            inObject = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getCheckFor() {
        return checkFor;
    }

    @Override
    public Player getPl() {
        return pl;
    }

    @Override
    public boolean isGameOver() {
        return gameOver;
    }

    @Override
    public List<Player> getPlayers() {
        return players;
    }

    @Override
    public Map<Coordinates, Integer> getActiveTiles() {
        return activeTiles;
    }

    @Override
    public void setActiveTiles(Map<Coordinates, Integer> activeTiles) {
        this.activeTiles = activeTiles;
    }

    @Override
    public Heap getHeap() {
        return heap;
    }

    @Override
    public GameBoard getBoard() {
        return board;
    }

    /**процесс, который хранит домино, юзеров, поле должен  запускаться на СЕРВЕРЕ (+)
 *
 * на КЛИЕНТЕ запускаем networkGameProcess (+)
 *
 * обновлять table после получения данных с СЕРВЕРА
 *
 *
 * при запуске он должен сходить к СЕРВЕРУ и сделать раздачу костей (в beginGamePr)
 *
 * СЕРВЕР должен вернуть все кости, которые лежат на КЛИЕНТЕ (у игроков, колоду и поле)
 *
 * createPlayers должен сходить в СЕРВЕР и запросить создание 2 игроков
 *
 * СЕРВЕР возвращает 2 игроков
 *
 * дальше говорим СЕРВЕР, какие кости у игрока
 *
 * сервер раздает кости игрокам в локальной версии, и возврщает какие у кого кости
 *
 * потом отдать клиенту, он получает, расшифровыевает и обновляет свое состояние в networkGameProcess
 *
 * прочитали команду от сервера, отдали ее локальному процессу
 *
 *
 *
 * клиент посылает команду -> сервер расшифровывает, вызывает нужную команду у локальной игры -> игра обновляется ->
 * -> сервер читает обновленные данные из локальной игры и отдает их клиенту -> клиент читает команду от сервера ->
 * -> смотрит что обновилось на сервере -> обновляет это у себя -> показывает игроков -
 *
 * */
    @Override
    public Player beginGamePr(int view, int numPl) throws IOException, ClassNotFoundException {
        System.out.println("remote process started");

        out.println("BEGIN" + numPl);
        Object s;
        if((s = inObject.readObject().toString()) != null){
            if(s.toString().equals("BEGIN_COMPLETE")){
                updateData();
            }
        }
        return pl;
    }

    @Override
    public void firstStep() throws IOException, ClassNotFoundException {
        out.println("FIRST_STEP");
        Object s;
        if((s = inObject.readObject().toString()) != null){
            if(s.toString().contains("FIRST_STEP_COMPLETE")){
                //обновить данные на клиенте
                updateData();
            }
        }
    }

    @Override
    public Player gameStep(int view, String code) throws IOException, ClassNotFoundException {
        out.println("NEXT_STEP" + code);
        Object s;
        if ((s = inObject.readObject().toString()) != null) {
            if (s.toString().contains("NEXT_STEP_COMPLETE")) {
                //обновить данные на клиенте
                updateData();
            }
        }
        return pl;
    }

    @Override
    public void gameOverCheck(List<Player> players) throws IOException, ClassNotFoundException {
        out.println("GAME_OVER_CHECK");
        Object s;
        if ((s = inObject.readObject().toString()) != null) {
            if (s.toString().contains("GAME_OVER_CHECK_COMPLETE")) {
                //обновить данные на клиенте
                updateData();
            }
        }
    }

    @Override
    public void takeTileFromBazar() throws IOException, ClassNotFoundException {
        out.println("TAKE_FROM_BAZAR");
        Object s;
        if ((s = inObject.readObject().toString()) != null) {
            if (s.toString().contains("TAKE_FROM_BAZAR_COMPLETE")) {
                //обновить данные на клиенте
                updateData();
            }
        }
    }

    public void updateData() throws IOException, ClassNotFoundException {
        //обновить данные на клиенте
        Object o;
        if((o = inObject.readObject()) != null){
            sendData = (SendData) o;
        }

        board = sendData.getGameBoard();
        heap = sendData.getHeap();
        pl = sendData.getPl();
        players = sendData.getPlayers();
        gameOver = sendData.isGameOver();
        checkFor = sendData.getCheckFor();
    }
}
