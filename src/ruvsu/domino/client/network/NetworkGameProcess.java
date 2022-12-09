package ruvsu.domino.client.network;

import ruvsu.domino.client.ui.utils.UIDominoUtils;
import ruvsu.domino.model.*;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class NetworkGameProcess extends AbstractGame {

    private boolean gameOver = false;

    public final Heap heap = new Heap();
    public final Table table = new Table();
    public final GameBoard board = new GameBoard();
    private List<Player> players = new ArrayList<>();
    private Map<Coordinates, Integer> activeTiles = new HashMap<>();

    public Scanner sc = new Scanner(System.in);

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
    private void createPlayers(int numPlayers) {
        for (int i = 0; i < numPlayers; i++) {
            players.add(new Player());
        }
    }


    @Override
    public Player beginGamePr(int numPl) {
        createPlayers(numPl);

        //раздать кости игрокам
        table.givingTilesToPlayers(players, heap);

        //определить кто ходит первым
        pl = table.whoIsFirstMove(players);

        System.out.println("remote process started");

        return pl;
    }

    @Override
    public Player gameStep(int view, String code) {
        out.println(pl.getPackOfTiles().toString());
        //server.send(pl.getPackOfTiles());
        //server.getResponse();
        System.out.println(pl.getPackOfTiles().toString());



//        //определить кто ходит
//        pl = table.defineMover(players, pl);
//
//        //получить кость которой игрок хочет походить
//        if(view == 1){ //если view = 1 то это консоль
//            if (UIDominoUtils.listEqualsIgnoreOrder(players.get(0).getPackOfTiles(), pl.getPackOfTiles())) {
//                System.out.println("Выберите плитку, которой хотите походить");
//                int num = sc.nextInt();
//                currTile = pl.makeInteractiveConsoleMove(num);
//            } else {
//                currTile = pl.makeAMove(activeTiles, heap);
//            }
//        }else if (view == 2){ //если view = 2 то это оконное приложение
//            if (UIDominoUtils.listEqualsIgnoreOrder(players.get(0).getPackOfTiles(), pl.getPackOfTiles()) && !code.equals("")) {
//                currTile = pl.makeInteractiveMove(code, board);
//            } else{
//                currTile = pl.makeAMove(activeTiles, heap);
//            }
//        } else if(view == 3){//если view = 3 то это клиент сервер
//
//        }
//
//        //положить на стол кость
//        activeTiles = board.putTile(currTile, activeTiles);
//
//        if (currTile.first == 99 && currTile.last == 99) {//если игрок не смог походить (makeAMove вернул Tile(99,99)) он добрал из базара
//            currTile = pl.makeAMove(activeTiles, heap); //еще одна попытка походить
//            activeTiles = board.putTile(currTile, activeTiles);
//        }
//
//        if (currTile.first == 99 && currTile.last == 99) {//если игрок не смог походить после добирания из базара счетчик увеличивается
//            checkFor++;
//        } else { //иначе обнуляется
//            checkFor = 0;
//        }
//
//        return pl;


        return null;
    }

    @Override
    public void gameOverCheck(List<Player> players) {

    }
}
