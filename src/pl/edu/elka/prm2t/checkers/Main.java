package pl.edu.elka.prm2t.checkers;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main {

    private static Man chosenFigure;

    public static void main(String[] args) {
        Screen s = new Screen();
        Game game = new Game(s);
        s.setBoardRef(game.getMainBoard());


        //KOD WYŚWIETLAJĄCY OKNO
        JFrame f = new JFrame("Checkers");
        f.setSize(600, 650);


        //OKNO FRAME GENERUJE SIĘ PO ŚRODKU
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        f.setLocation(dimension.width/2-f.getSize().width/2,dimension.height/2-f.getSize().height/2);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        // MENU --> MenuOptionsBar
        MenuOptionsBar mOB = new MenuOptionsBar(s, game);
        f.setJMenuBar(mOB);

        f.add(s);
        s.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                final int fieldX = (e.getX() - s.getOffSetX()) / 64;
                final int fieldY = (e.getY() - s.getOffSetY()) / 64;

//                System.out.println(chosenFigure);

                if(chosenFigure == null){
                    chosenFigure = game.getFigure(fieldX, fieldY);
                    if(game.getTurn() % 2 == 0 && (chosenFigure instanceof WhiteMan ||chosenFigure instanceof WhiteKing)){
                        System.out.println("Not your turn!");
                        chosenFigure = null;
                        return;
                    }

                    if(game.getTurn() % 2 != 0 && (chosenFigure instanceof BlackMan|| chosenFigure instanceof BlackKing)){
                        System.out.println("Not your turn!");
                        chosenFigure = null;
                        return;
                    }

                    if(game.obligatedMen().size() > 0){
                       AtomicBoolean isLegal = new AtomicBoolean(false);

                        game.obligatedMen().forEach(man -> {
                            if(chosenFigure.equals(man)) isLegal.set(true);
                        });
                        if(!isLegal.get()){
                            chosenFigure = null;
                        }
                    }

                    if(chosenFigure == null){
                        System.out.println("Empty here");
                        s.setChosenField(-1, -1);
                        s.repaint();
                        return;
                    }
                    s.setChosenField(fieldX, fieldY);
                    s.repaint();
                    return;
                }
                if(chosenFigure != null){
                    String stateOfMove = chosenFigure.move(fieldX, fieldY);
                    if(!stateOfMove.equals("none")){
                        game.nextTurn(chosenFigure, stateOfMove);
                        game.obligatedMen();
                    }
                    chosenFigure = null;
                    s.setChosenField(-1, -1);
                    s.repaint();
                    game.checkForPlayerPromotion();
                    return;

                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                //
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                //
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                //
            }
            @Override
            public void mouseExited(MouseEvent e) {
                //
            }
        });
        f.setVisible(true);
    }
}

