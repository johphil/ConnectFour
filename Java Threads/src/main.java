import java.util.Scanner;
import Sinulids.Board;

public class main {

	private static final int BOARD_ROWS = 6;
	private static final int BOARD_COLS = 7;
	private static final char[] CHIPS = {'X','O'};
	private static char[] board;
	public static int h;
	public static int v;
	public static int d;
	
	public main() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}

	static void printBoard(char[] board)
	{
	    int col;

	    System.out.println("\n    ****Connect Four****\n");
	    for(int row = 0; row < BOARD_ROWS; row++)
	    {
	        for(col = 0; col < BOARD_COLS; col++)
	        {
	        	System.out.print(String.format("| %c ",  board[BOARD_COLS * row + col]));
	        }
	        System.out.println("|");
	        System.out.println("-----------------------------");
	    }
	    System.out.println("  1   2   3   4   5   6   7\n");
	}
}
