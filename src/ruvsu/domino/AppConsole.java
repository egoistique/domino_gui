package ruvsu.domino;

import ruvsu.domino.model.Asking;
import ruvsu.domino.model.GameProcess;

public class AppConsole {
    public static void main(String[] args) {
        GameProcess game = new GameProcess();
        Asking ask = new Asking();
        int num = ask.askNumberOfPlayers();
        game.processConsole(num);
    }
}
