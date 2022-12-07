package ruvsu.domino.console;

import ruvsu.domino.model.Tile;
import ruvsu.domino.model.LocalGameProcess;

public class ConsoleGameProcess {

    private LocalGameProcess gameProcess = new LocalGameProcess();
    private Tile firstTile;

    public void processConsole(int numPl){
        gameProcess.beginGamePr(numPl);

        System.out.println("до 1 хода распределение костей такое: ");
        ConsoleUtils.output(gameProcess.getPlayers(), gameProcess.board);

        firstTile = gameProcess.getPl().makeAFirstMove();
        gameProcess.setActiveTiles(gameProcess.board.putFirstTile(firstTile, gameProcess.getActiveTiles()));

        gameProcess.board.output();

        while (!gameProcess.isGameOver() && gameProcess.getCheckFor() < gameProcess.getPlayers().size()){
            //сделать ход
            gameProcess.gameStep(1, "");

            //вывести состояние гейм борда
            gameProcess.board.output();

            System.out.println("распределение костей такое: ");
            ConsoleUtils.output(gameProcess.getPlayers(), gameProcess.board);
            gameProcess.gameOverCheck(gameProcess.getPlayers());
        }

        System.out.println("Конец игры " + "\n Финальный результат игроков ");

        ConsoleUtils.outPoints(gameProcess.getPlayers());
    }
}
