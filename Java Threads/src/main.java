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
	
	static int takeTurn(char[] board, int player, final char[] CHIPS)
	{
		Scanner input = new Scanner(System.in);
		int col = 0;
	    System.out.println(String.format("Player %d (%c):\nEnter column number: ", player + 1, CHIPS[player]));

	    while(true)
	    {
	    	try 
	    	{
	    	    String inp;
		    	inp = input.next();
		    	col = Integer.parseInt(inp);
		    	
		        if(col < 1 || col > 7 )
		        {
		            System.out.println("Number out of bounds! Try again.");
		            System.out.println("Enter column number: ");
		        }
		        else
		        {
		        	break;
		        }
	    	}
	    	catch (NumberFormatException e)
	    	{
	    		System.out.println("Number out of bounds! Try again.");
	            System.out.println("Enter column number: ");
	    	}
	    }
	    col--;

	    try 
	    {
		    Board b = new Board(col, board, CHIPS[player], BOARD_ROWS, BOARD_COLS);
		    Thread putChip = new Thread(b);
		    putChip.start();
			putChip.join();
		    return b.GetReturnValue();
		} catch (InterruptedException e) 
	    {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}
	
	static int checkWin()
	{
		try {
			Thread t1 = new Thread(new CheckHorizontal());
			Thread t2 = new Thread(new CheckVertical());
			Thread t3 = new Thread(new CheckDiagonal());
			t1.start();
			t2.start();
			t3.start();
			t1.join();
			t2.join();
			t3.join();
			int ret = h + v + d;
			return (ret > 0 ? 1 : 0);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}
	
	static boolean checkFour(int a, int b, int c, int d)
	{
	    return (board[a] == board[b] && board[b] == board[c] && board[c] == board[d] && board[a] != ' ');
	}
	
	static class CheckHorizontal implements Runnable
	{		
		public CheckHorizontal() {}
		
		public void run()
		{
			h = horizontalCheck();
		}
		
		int horizontalCheck()
		{
		    int col, idx;
		    final int WIDTH = 1;

		    for(int row = 0; row < BOARD_ROWS; row++)
		    {
		        for(col = 0; col < BOARD_COLS - 3; col++)
		        {
		            idx = BOARD_COLS * row + col;
		            if(checkFour(idx, idx + WIDTH, idx + WIDTH * 2, idx + WIDTH * 3))
		            {
		                return 1;
		            }
		        }
		    }
		    return 0;
		}
	}
	
	static class CheckVertical implements Runnable
	{		
		public CheckVertical() {}
		
		public void run()
		{
			v = verticalCheck();
		}
		
		int verticalCheck()
		{
		    int col, idx;
		    final int HEIGHT = 7;

		    for(int row = 0; row < BOARD_ROWS - 3; row++)
		    {
		        for(col = 0; col < BOARD_COLS; col++)
		        {
		            idx = BOARD_COLS * row + col;
		            if(checkFour(idx, idx + HEIGHT, idx + HEIGHT * 2, idx + HEIGHT * 3))
		            {
		                return 1;
		            }
		        }
		    }
		    return 0;
		}
	}
	static class CheckDiagonal implements Runnable
	{		
		public CheckDiagonal() {}
		
		public void run()
		{
			d = diagonalCheck();
		}
		
		int diagonalCheck()
		{
		    int col, idx, count = 0;
		    final int DIAG_RGT = 6, DIAG_LFT = 8;

		    for(int row = 0; row < BOARD_ROWS - 3; row++)
		    {
		        for(col = 0; col < BOARD_COLS; col++)
		        {
		            idx = BOARD_COLS * row + col;
		            if((count <= 3 && checkFour(idx, idx + DIAG_LFT, idx + DIAG_LFT * 2, idx + DIAG_LFT * 3)) || (count >= 3 && checkFour(idx, idx + DIAG_RGT, idx + DIAG_RGT * 2, idx + DIAG_RGT * 3)))
		            {
		                return 1;
		            }
		            count++;
		        }
		        count = 0;
		    }
		    return 0;
		}
	}
}
