package pl.edu.elka.prm2t.checkers;

import java.util.ArrayList;

public class Game {
    private final ArrayList<String> movesHistory = new ArrayList<>();
    private String gameStatus;

    // public static void load()

    // public static void save(){board.saveGrid(gameName);}

    // tworzenie atrybutów, aby można było się do następnie odnosić
    private int turn = 1;
    private final Board mainBoard;
    private final Player playerWhite;
    private final Player playerBlack;

    Game() {
        mainBoard = new Board();
        playerWhite = new WhitePlayer(mainBoard);
        playerBlack = new BlackPlayer(mainBoard);
    }

    public int getTurn() {
        return turn;
    }

    public void nextTurn(){
        System.out.println("Ruch " + turn);
        turn++;

        if(turn % 2 == 0){
            System.out.println("Tura czarnych");
            mainBoard.checkForCapture("black");
        }

        if(turn % 2 != 0){
            System.out.println("Tura białych");
            mainBoard.checkForCapture("white");

        }


    }

    public Man getFigure(int x, int y){
        return mainBoard.getGrid()[x][y];
    }

    public void checkForPlayerPromotion(){
        for (int i = 0; i < 8; i++) {
//            System.out.println(mainBoard.getGrid()[i][0]);
            if(mainBoard.getGrid()[i][0] instanceof WhiteMan){
                playerWhite.promoteMan(mainBoard.getGrid()[i][0]);
            }
        }

        for (int i = 0; i < 8; i++) {
            if (mainBoard.getGrid()[i][7] instanceof BlackMan) {
                playerBlack.promoteMan(mainBoard.getGrid()[i][7]);
            }
        }
    }

    public Board getMainBoard() {
        return mainBoard;
    }
}


