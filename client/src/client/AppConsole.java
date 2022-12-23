package client;

import client.console.ConsoleGameProcess;
import model.Asking;

public class AppConsole {
    public static void main(String[] args) {
        ConsoleGameProcess game = new ConsoleGameProcess();
        Asking ask = new Asking();
        int num = ask.askNumberOfPlayers();
        game.processConsole(num);
    }
}
