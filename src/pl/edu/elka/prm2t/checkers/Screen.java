package pl.edu.elka.prm2t.checkers;


import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Screen extends JPanel {

    private final int fieldSize = 64;
    private Board boardRef;
    private int selectedFieldX = -1;
    private int selectedFieldY = -1;
    private int offSetX = 44;
    private int offSetY = 24;
    private int turn = 1;
    private ArrayList<Man> obligatedMen = new ArrayList<>();

    public void setBoardRef(Board boardRef){
        this.boardRef = boardRef;
    }

    public void nextTurn() {
        turn++;
    }

    public int getOffSetX() {
        return offSetX;
    }

    public int getOffSetY(){
        return offSetY;
    }

    public void setChosenField(int fieldX, int fieldY){
        selectedFieldX = fieldX;
        selectedFieldY = fieldY;
    }

    public void setObligatedMenFields(ArrayList<Man> obligatedMen){
        this.obligatedMen = obligatedMen;
    }

    public void paint(Graphics g){
        super.paintComponent(g);
        this.setBackground(Color.WHITE);

        drawBoard(g);
        drawFieldsOfManWhoMustTakes(g);
        drawAvailableFields(g);
        drawMen(g);

    }

    /**
     * Rysowanie planszy
     * @param g
     */
    private void drawBoard(Graphics g){
        g.setColor(Color.BLACK);
        g.fillRect(offSetX - 6, offSetY -6, 8*fieldSize+12, 8*fieldSize+12);
        g.setColor(Color.WHITE);
        g.fillRect(offSetX, offSetY, 8*fieldSize, 8*fieldSize);
        g.setColor(Color.BLACK);
        for (int  i= 1;  i<= 64; i+=2) {
            g.fillRect(offSetX + (i - (i/8)*8 - (i/8)%2)*fieldSize, offSetY + (i/8) * fieldSize, fieldSize, fieldSize);
        }


    }

    /**
     * Zaznaczanie przymusów bicia
     * @param g
     */
    private void drawFieldsOfManWhoMustTakes(Graphics g){
        g.setColor(Color.RED);

        obligatedMen.forEach((man) -> {
            g.fillRect(offSetX + man.getX()*fieldSize, offSetY + man.getY()*fieldSize, fieldSize, fieldSize);
        });

    }

    /**
     * Zaznaczanie pionka, który może się ruszyć
     * @param g
     */
    private void drawAvailableFields(Graphics g){
        if(selectedFieldY != -1 && selectedFieldX != -1){
            g.setColor(Color.ORANGE);
            g.fillRect(offSetX + selectedFieldX*fieldSize, offSetY + selectedFieldY*fieldSize, fieldSize, fieldSize);
        }
    }

    /**
     * Rysowanie pionków na planszy
     * @param g
     */
    private void drawMen(Graphics g){
        Man[][] grid = boardRef.getGrid();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                    if(grid[i][j] != null ){
                        if(grid[i][j] instanceof WhiteMan) g.setColor(Color.WHITE);
                        if(grid[i][j] instanceof BlackMan) g.setColor(Color.GRAY);
                        if(grid[i][j] instanceof WhiteKing) g.setColor(Color.WHITE);
                        if(grid[i][j] instanceof BlackKing) g.setColor(Color.GRAY);
                        g.fillOval(offSetX + i*fieldSize + 8, offSetY + j*fieldSize + 8, 48, 48);
                        if(grid[i][j] instanceof King){
                            g.setColor(Color.RED);
                            g.fillOval(offSetX + i*fieldSize + 26, offSetY + j*fieldSize + 26, 12, 12);

                        }
                    }
            }
        }
    }



}
