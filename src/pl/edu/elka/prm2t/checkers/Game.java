package pl.edu.elka.prm2t.checkers;

import com.sun.tools.jconsole.JConsoleContext;

import java.util.ArrayList;

// to do: zrobić, żeby podczas bicia wielokrotnego pionek mógł się zmienić w króla
// żeby w biciu wielokrotnym można było bić tylko jednym pionkiem
// dobracować wczytywanie/zapisywanie
// zrobić historie i cofanie
// ogarnąć funkcje to wydruku done /


public class Game {
    private final ArrayList<int[]> movesHistory = new ArrayList<>();
    private String gameStatus;


    // tworzenie atrybutów, aby można było się do następnie odnosić
    private int turn = 1;
    private final Board mainBoard;
    private final Player playerWhite;
    private final Player playerBlack;
    private final Screen s;
    private boolean takeAgain;

    Game(Screen s) {
        mainBoard = new Board();
        playerWhite = new WhitePlayer(mainBoard);
        playerBlack = new BlackPlayer(mainBoard);
        this.s = s;

    }

    public String getGameStatus() {
        checkGameStatus();
        return gameStatus;
    }

    public void checkGameStatus(){
        ArrayList<Man> whiteFigures = mainBoard.getWhiteMenList();
        ArrayList<Man> blackFigures = mainBoard.getBlackMenList();
        if(whiteFigures.isEmpty()){
            gameStatus = "Black won";
        }
        if(blackFigures.isEmpty()){
            gameStatus = "White won";
        }
        else
            gameStatus = "Game in progress";

    }

    public void undo(){
        try{
            int[] lastMove = movesHistory.get(movesHistory.size() - 1);

            if(lastMove[5] == 1){
                int capturedManX = (lastMove[1] + lastMove[3])/2;
                int capturedManY = (lastMove[2] + lastMove[4])/2;
                Man capturedMan = null;
                if(lastMove[0] == 0) capturedMan = new BlackMan(capturedManX, capturedManY, mainBoard);
                if (lastMove[0] == 1) capturedMan = new WhiteMan(capturedManX, capturedManY, mainBoard);
                mainBoard.addToGrid(capturedMan);

            }
            mainBoard.changeFigurePosition(lastMove[3], lastMove[4], lastMove[1], lastMove[2]);
            movesHistory.remove(movesHistory.size() - 1);



        }catch (IndexOutOfBoundsException e){
            System.out.println("No more moves to undo!");
        }
    }

    public int getTurn() {
        return turn;
    }

    private void setTurn(int number) {
        turn = number;
    }

    public void nextTurn(int fromX, int fromY, int toX, int toY, String typeOfMove){
//        mainBoard.getGrid()

        int WHITE = 0;
        int BLACK = 1;
        int MOVE = 0;
        int CAPTURE = 1;

        int[] mv = new int[6];

        if(typeOfMove.equals("capture")) MOVE = CAPTURE;

        if(turn % 2 == 0){
            mv = new int[]{BLACK, fromX, fromY, toX, toY, MOVE};
        }

        if(turn % 2 != 0){
            mv = new int[]{WHITE, fromX, fromY, toX, toY, MOVE};
        }

        takeAgain = false;

        movesHistory.add(mv);

        if(fromY - toY == 2 || fromY - toY == -2){
            if(turn % 2 == 0 && mainBoard.checkForCapture("black").size() > 0){
                System.out.println("BLACK TAKES AGAIN");
                takeAgain = true;
                return;
            }

            if(turn % 2 != 0 && mainBoard.checkForCapture("white").size() > 0){
                System.out.println("WHITE TAKES AGAIN");
                takeAgain = true;
                return;
            }
        }

        turn++;
        s.nextTurn();

        if(turn % 2 == 0){
            System.out.println("Black's turn");
        }

        if(turn % 2 != 0){
            System.out.println("White's turn");
        }

    }

    public ArrayList<Man> obligatedMen(){
        String color = "white";
        if (turn % 2 == 0) color = "black";
//        if (turn % 2 != 0) color = "white";
        int[] lastMove = null;
        if(turn > 1) lastMove = movesHistory.get(movesHistory.size() - 1);


        if(takeAgain){
            if(mainBoard.getGrid()[lastMove[3]][lastMove[4]].checkForTakes() == false){
                takeAgain = false;
                System.out.println("No more moves by duble bicie");
                turn++;
                return null;
            }
            ArrayList<Man> obligatedMan = new ArrayList<>();
            obligatedMan.add(mainBoard.getGrid()[lastMove[3]][lastMove[4]]);
            s.setObligatedMenFields(obligatedMan);
            return obligatedMan;
        }
        s.setObligatedMenFields(mainBoard.checkForCapture(color));
        return mainBoard.checkForCapture(color);
    }

    public Man getFigure(int x, int y){
        return mainBoard.getGrid()[x][y];
    }

    public void checkForPlayerPromotion(){
        for (int i = 0; i < 8; i++) {
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


    public String save(){
//        String[][] gridToSave = new String[8][8];
//        Man[][] grid = game.getMainBoard().getGrid();

        String savedGame = "";

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Man man = mainBoard.getGrid()[j][i];
                if (man instanceof WhiteMan) savedGame += "w";
                if (man instanceof WhiteKing) savedGame += "W";
                if (man instanceof BlackMan) savedGame += "b";
                if (man instanceof BlackKing) savedGame += "B";
                if (man == null) savedGame += "x";
            }
        }

        return savedGame + ";" + turn;
    }

    public void load(String savedGame){
        clear();
        setTurn(Character.getNumericValue(savedGame.charAt(savedGame.length() - 1)));
        newGrid(savedGame);
        obligatedMen();
        s.repaint();
    }

    private void clear(){
        mainBoard.clear();
    }

    public void reset(){
        turn = 1;
        mainBoard.clear();
        playerWhite.createNewMen();
        playerBlack.createNewMen();
        obligatedMen();
        s.repaint();
    }

    public void newGrid(String savedBoard){
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                char character = savedBoard.charAt(j + i*8);
                Man newMan = null;
                if(character == 'w' ) newMan = new WhiteMan(j, i, mainBoard);
                if(character == 'W' ) newMan = new WhiteKing(j, i, mainBoard);
                if(character == 'b' ) newMan = new BlackMan(j, i, mainBoard);
                if(character == 'B' ) newMan = new BlackKing(j, i, mainBoard);
                if(character != 'x'){
                    mainBoard.addToGrid(newMan);
                }
            }
        }
    }

    public Board getMainBoard() {
        return mainBoard;
    }
}


