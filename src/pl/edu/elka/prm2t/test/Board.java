package pl.edu.elka.prm2t.test;

public class Board {

    private static Man[][] grid = new Man[8][8];

    public Man[][] getGrid(){
        return grid;
    }

    public void displayGrid(){

        for (int j = 0; j < 8; j++) {
            String row = "";
            for (int i = 0; i < 8; i++) {
                try{
                    if(grid[i][j].getColor().equals("white")) row += "1";
                    if(grid[i][j].getColor().equals("black")) row += "2";
                }catch (NullPointerException e){
                    row += "0";
                }

            }
            System.out.println(row);
        }
    }

//    public static void test(){
//        grid[0][0] = new Man(0,0,grid);
//
//        //grid[0][0].showPos();
//
//        grid[0][0].move(7, 7);
//
//
//        try{
//            grid[0][0].showPos();
//
//
//        }catch (NullPointerException e){
//            //
//        }
//        grid[7][7].showPos();
//
//    }

}