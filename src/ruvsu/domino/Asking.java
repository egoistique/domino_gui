package ruvsu.domino;

import java.util.Scanner;

public class Asking {
    Scanner sc = new Scanner(System.in);

    public int askNumberOfPlayers(){
        System.out.println("Выберите число игроков: 2/3/4 ");
        return sc.nextInt();
    }

}
