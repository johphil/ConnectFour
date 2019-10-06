package Sinulids;

public class Board implements Runnable {
	
	private int col;
	private char[] board;
	private int BOARD_ROWS;
	private int BOARD_COLS;
	private char CHIP;
	private volatile int ReturnValue;
	
	public Board(int col, char[] board, char CHIP, int BOARD_ROWS, int BOARD_COLS)
	{
		this.col = col;
		this.board = board;
		this.CHIP = CHIP;
		this.BOARD_ROWS = BOARD_ROWS;
		this.BOARD_COLS = BOARD_COLS;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		ReturnValue = putChip();
		printBoard(this.board);
	}
	
	int putChip()
	{
	    int NextRow, CurrentRow;

	    for (int row = 0; row < BOARD_ROWS; row++)
	    {
	        NextRow = BOARD_COLS * (row+1) + col;
	        CurrentRow = BOARD_COLS * row + col;

	        if (board[NextRow] == ' ' && board[CurrentRow] == ' ')
	        {
	            board[CurrentRow] = CHIP;
	            printBoard(board);
	            board[CurrentRow] = ' ';

	            if (NextRow >= 35)
	            {
	                board[NextRow] = CHIP;
	                return 1;
	            }
	        }
	        else if (board[NextRow] != ' ' && board[CurrentRow] == ' ')
	        {
	            board[CurrentRow] = CHIP;
	            return 1;
	        }
	        else if (board[NextRow] != ' ' && board[CurrentRow] != ' ')
	        	return 0;
	        try {
				Thread.sleep(150);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    return 0;
	}
	
	void printBoard(char[] board)
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
	
	public int GetReturnValue()
	{
		return this.ReturnValue;
	}
}
