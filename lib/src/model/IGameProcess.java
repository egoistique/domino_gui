package model;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface IGameProcess {
    int getCheckFor();

    Player getPl();

    boolean isGameOver();

    List<Player> getPlayers();

    Map<Coordinates, Integer> getActiveTiles();

    void setActiveTiles(Map<Coordinates, Integer> activeTiles);

    //начало игры, первый ход
    Player beginGamePr(int view, int numPl) throws IOException, ClassNotFoundException;

    //шаг
    Player gameStep(int view, String code) throws IOException, ClassNotFoundException;

    //проверить условие конца игры
    void gameOverCheck(List<Player> players);

    Heap getHeap();

    void firstStep() throws IOException, ClassNotFoundException;

    GameBoard getBoard();
}
