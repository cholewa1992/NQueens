/**
 * This class implements the logic behind the BDD for the n-queens problem
 * You should implement all the missing methods
 * 
 * @author Stavros Amanatidis
 *
 */
import java.util.*;
import net.sf.javabdd.*;

public class QueensLogic {

    private int x = 0;
    private int y = 0;

    private BDD bdd;
    private int[][] board;
    private BDDFactory fact;


    public void initializeGame(int size) {
        this.x = size;
        this.y = size;
        this.board = new int[x][y];


        fact = JFactory.init(2000000, 200000);
        fact.setVarNum(x * y);

        bdd = fact.one();

        for (int i = 0; i < x; i++) {
            BDD row = fact.ithVar(i * x);
            for (int j = 1; j < x; j++) {
                row.orWith(fact.ithVar(i * x + j));
            }
            bdd.andWith(row);
        }


        for (int i = 0; i < x * y; i++) {
            for (int v : crossings(i, x))
                bdd.andWith(fact.ithVar(i).imp(fact.nithVar(v)));
        }
    }

   
    public int[][] getGameBoard() {
        check();
        return board;
    }

    public boolean insertQueen(int column, int row) {

        if (board[column][row] == -1 || board[column][row] == 1) {
            return true;
        }

        board[column][row] = 1;
        check();

        return true;
    }

    public void check() {
        int i = 0, j = 0;

        for (int k = 0; k < x * y; k++) {
            if (board[i][j] == 0) {
                if (bdd.restrict(fact.nithVar(k)).isZero()) board[i][j] = 1;
                if (bdd.restrict(fact.ithVar(k)).isZero()) board[i][j] = -1;
            }

            if (i++ >= x - 1) {
                i = 0;
                j++;
            }
        }
    }

    public static ArrayList<Integer> crossings(int field, int n){
        ArrayList<Integer> list = new ArrayList<Integer>();

        for(int i = 0; i < n; i++) {

            //Vertically
            int v = field % n + i * n;
            if(v != field) list.add(v);

            //Horizontally
            v = (int) Math.floor(field / n) * n + i;
            if(v != field) list.add(v);

        }

        //Falling
        int x = field % n;
        int y = field / n;

        if(y-x > 0){
            y = y-x; x = 0;
        }else{
            x = (y-x) * -1; y = 0;
        }

        while(y < n && x < n){
            int v = y*n + x;
            if(v != field) list.add(v);
            x++; y++;
        }

        //Rising
        x = field % n;
        y = field / n;

        while(y < n-1 && x > 0){
            y++; x--;
        }

        while(y >= 0 && x < n){
            int v = y*n + x;
            if(v != field) list.add(v);
            x++; y--;
        }

        return list;
    }
}
