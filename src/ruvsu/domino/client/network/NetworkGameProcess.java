package ruvsu.domino.client.network;

import ruvsu.domino.client.console.ConsoleUtils;
import ruvsu.domino.client.ui.utils.UIDominoUtils;
import ruvsu.domino.model.*;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class NetworkGameProcess extends AbstractGame {

    private List<Player> players = new ArrayList<>();
    private Map<Coordinates, Integer> activeTiles = new HashMap<>();

    private Tile currTile;

    private int checkFor = 0;

    Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public NetworkGameProcess(Player pl, String server, int port) {
        super(pl);
        try {
            socket = new Socket(server, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            //TODO handle exception properly
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
    //создать игроков
    @Override
    public List<Player> createPlayers(int numPlayers) throws IOException {
        //послать запрос на сервер о создании игроков
        out.println("createPlayers" + numPlayers);
        //получив инфо о игроках
        String s;
        if((s = in.readLine()) != null){
            if(s.equals("players created")){
                players = Helping.players;
            }
        }
        return players;
    }

    @Override
    public Table giving(int numPlayers) {
//        //послать запрос на сервер о раздаче
//        out.println("createPlayers" + numPlayers);
//        //получив инфо раздаче
//        String s;
//        if((s = in.readLine()) != null){
//            if(s.equals("players created")){
//                players = Helping.players;
//            }
//        }
//

        return table;
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
    public Player beginGamePr(int view, int numPl) throws IOException {
        //создать игроков
        createPlayers(numPl);

//        //послать запрос на сервер
//        out.println(numPl);
//        //получив инфо о розданных костях
//        in.readLine()


        //раздать кости игрокам
        table.givingTilesToPlayers(players, heap);

        //послать запрос на сервер, получив инфо кто ходит первым и вернуть
        pl = table.whoIsFirstMove(players);

        System.out.println("remote process started");

        return pl;
    }

    @Override
    public Player gameStep(int view, String code) {
//        socket.send(pl.getPackOfTiles());
//        socket.getResponse();

        //послать запрос на сервер, получив инфо кто ходит
        //определить кто ходит
        pl = table.defineMover(players, pl);

        //получить кость которой игрок хочет походить
        if (UIDominoUtils.listEqualsIgnoreOrder(players.get(0).getPackOfTiles(), pl.getPackOfTiles()) && !code.equals("")) {
            currTile = pl.makeInteractiveMove(code, board);

            out.println(Character.toChars(ConsoleUtils.tileToString(currTile, board)));

        } else{
            currTile = pl.makeAMove(activeTiles, heap);
        }

        //отправить currTile на сервер, сервер кладает кость, присылает обратно игроку новую конфигурацию стола,
        //клиент это обновляет на экране
        //положить на стол кость

        activeTiles = board.putTile(currTile, activeTiles);

        if (currTile.first == 99 && currTile.last == 99) {//если игрок не смог походить (makeAMove вернул Tile(99,99)) он добрал из базара
            currTile = pl.makeAMove(activeTiles, heap); //еще одна попытка походить
            activeTiles = board.putTile(currTile, activeTiles);
        }

        if (currTile.first == 99 && currTile.last == 99) {//если игрок не смог походить после добирания из базара счетчик увеличивается
            checkFor++;
        } else { //иначе обнуляется
            checkFor = 0;
        }

        return pl;

    }

    @Override
    public void gameOverCheck(List<Player> players) {

    }
}
