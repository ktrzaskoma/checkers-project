package pl.edu.elka.prm2t.checkers;

public class WhiteKing extends King {

    WhiteKing(int x, int y, Board board) {
        super(x, y, board);
    }

    @Override
    public boolean checkForTakes() {
        return checkIfTakePossible(x - 2, y - 2) || checkIfTakePossible(x + 2, y - 2) || checkIfTakePossible(x - 2, y + 2) || checkIfTakePossible(x + 2, y + 2);
    }

    @Override
    public boolean checkIfTakePossible(int toX, int toY){

        if(toX < 0 || toX > 7) return false;
        if(toY < 0 || toY > 7) return false;
        int targetX = (x + toX) / 2;
        int targetY = (y + toY) / 2;

        if (x - toX == 2 || x - toX == -2) {
            if (y - toY == 2 || y - toY == -2)
                if (grid[targetX][targetY] instanceof BlackMan || grid[targetX][targetY] instanceof BlackKing) {
                    if (grid[toX][toY] == null) return true;
                }
        }
        return false;
    }
    @Override
    public boolean checkIfMoveForwardPossible(int toX, int toY) {
        return super.checkIfMoveForwardPossible(toX, toY);
    }


}
