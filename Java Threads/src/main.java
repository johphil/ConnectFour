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
	
	public static void main(String[] args) {
		int turn, done = 0;
		
		//board size = 6*7 = 42
		board = new char[BOARD_ROWS * BOARD_COLS];
		
		// memset | fill in board with spaces 42 times
		for (int i = 0; i < BOARD_ROWS * BOARD_COLS; i++)
		{
			board[i] = ' ';
		}
		
		for (turn = 0; turn < BOARD_ROWS * BOARD_COLS && done == 0; turn++)
		{
			printBoard(board);
			while(takeTurn(board, turn % 2, CHIPS) == 0)
	        {
	            printBoard(board);
	            System.out.println("**Column full!**\n");
	        }
	        done = checkWin();
		}
		printBoard(board);
		
	    if(turn == BOARD_ROWS * BOARD_COLS && done == 0)
	    {
	        System.out.println("It's a tie!");
	    }
	    else
	    {
	        turn--;
	        System.out.println(String.format("Player %d (%c) wins!\n", turn % 2 + 1, CHIPS[turn % 2]));
	    }
		
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
