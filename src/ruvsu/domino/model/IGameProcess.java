package ruvsu.domino.model;

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
    Player beginGamePr(int view, int numPl) throws IOException;

    //шаг
    Player gameStep(int view, String code);

    //проверить условие конца игры
    void gameOverCheck(List<Player> players);

    List<Player> createPlayers(int numPlayers) throws IOException;

    Table giving(int numPlayers);
}
