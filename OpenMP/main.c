#ifdef __linux__
#else
#include <Windows.h>
#endif
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <omp.h>
#include <time.h>

#define BOARD_ROWS 6
#define BOARD_COLS 7

void printBoard(char *board);
int takeTurn(char *board, int player, const char* CHIPS);
int checkWinOneThread(char *board);
int checkWinThreeThreads(char *board);
int checkFour(char *board, int, int, int, int);
int horizontalCheck(char *board);
int verticalCheck(char *board);
int diagonalCheck(char *board);
int putChip(char *board, int col, int player, const char* CHIPS);
void clearScreen();

double time1;

int main(int argc, char *argv[])
{
    const char *CHIPS = "XO";
    char board[BOARD_ROWS * BOARD_COLS];
    memset(board, ' ', BOARD_ROWS * BOARD_COLS);

    int turn, done = 0;

    for(turn = 0; turn < BOARD_ROWS * BOARD_COLS && !done; turn++)
    {
        printBoard(board);
	printf("checkWin() Elapsed time: %lf\n\n", time1);
        while(!takeTurn(board, turn % 2, CHIPS))
        {
            printBoard(board);
            puts("**Column full!**\n");
        }
	done = checkWinOneThread(board);
        //done = checkWinThreeThreads(board);

    }
    printBoard(board);
    printf("checkWin() Elapsed time: %lf\n\n", time1);


    if(turn == BOARD_ROWS * BOARD_COLS && !done)
    {
        puts("It's a tie!");
    }
    else
    {
        turn--;
        printf("Player %d (%c) wins!\n", turn % 2 + 1, CHIPS[turn % 2]);
    }


    return 0;

}
void printBoard(char *board)
{
    int row, col;

    clearScreen();
    puts("\n    ****Connect Four****\n");
    for(row = 0; row < BOARD_ROWS; row++)
    {
        for(col = 0; col < BOARD_COLS; col++)
        {
            printf("| %c ",  board[BOARD_COLS * row + col]);
        }
        puts("|");
        puts("-----------------------------");

    }
    puts("  1   2   3   4   5   6   7\n");

}
int takeTurn(char *board, int player, const char *CHIPS)
{
    int col = 0;
    printf("Player %d (%c):\nEnter number coordinate: ", player + 1, CHIPS[player]);

    while(1)
    {
        if(1 != scanf("%d", &col) || col < 1 || col > 7 )
        {
            while(getchar() != '\n');
            puts("Number out of bounds! Try again.");
        }
        else
        {
            break;
        }
    }
    col--;

    int x = putChip(board, col, player, CHIPS);
    return x;

}
int putChip(char *board, int col, int player, const char *CHIPS)
{
    int NextRow, CurrentRow;

    for (int row = 0; row < BOARD_ROWS; row++)
    {
        NextRow = BOARD_COLS * (row+1) + col;
        CurrentRow = BOARD_COLS * row + col;

        if (board[NextRow] == ' ' && board[CurrentRow] == ' ')
        {
            board[CurrentRow] = CHIPS[player];
            printBoard(board);
            board[CurrentRow] = ' ';

            if (NextRow >= 35)
            {
                board[NextRow] = CHIPS[player];
                return 1;
            }
        }
        else if (board[NextRow] != ' ' && board[CurrentRow] == ' ')
        {
            board[CurrentRow] = CHIPS[player];
            return 1;
        }
        else if (board[NextRow] != ' ' && board[CurrentRow] != ' ')
            return 0;
#ifdef __linux__
        fflush(stdout);
        usleep(100000);
#else
        Sleep(100);
#endif
    }
    return 0;
}
int checkWinOneThread(char *board)
{
	int ch1, ch2, ch3;
	double begin, end;

	begin = omp_get_wtime(); //start the timer
	#pragma omp parallel num_threads(1)
	#pragma omp single
	{	
		#pragma critical
		ch1 = horizontalCheck(board);
		ch2 = verticalCheck(board);
		ch3 = diagonalCheck(board);
	}
	end = omp_get_wtime();// end the timer
	
	time1 = (double)(end - begin);
	return (ch1 || ch2 || ch3);
}
int checkWinThreeThreads(char *board)
{
	int ch1, ch2, ch3;
	double begin, end;

	begin = omp_get_wtime(); //start the timer
	#pragma omp parallel num_threads(3)
	#pragma omp single
	{
		#pragma omp task
		{
			#pragma critical
			ch1 = horizontalCheck(board);
		}
		#pragma omp task
		{
			#pragma critical
			ch2 = verticalCheck(board);
		}
		#pragma omp task
		{
			#pragma critical
			ch3 = diagonalCheck(board);
		}
		#pragma omp taskwait //wait task to finish
	}
	end = omp_get_wtime();// end the timer
	
	time1 = (double)(end - begin);
	return (ch1 || ch2 || ch3);
}
int checkFour(char *board, int a, int b, int c, int d)
{
    return (board[a] == board[b] && board[b] == board[c] && board[c] == board[d] && board[a] != ' ');

}
int horizontalCheck(char *board)
{
    int row, col, idx;
    const int WIDTH = 1;

    for(row = 0; row < BOARD_ROWS; row++)
    {
        for(col = 0; col < BOARD_COLS - 3; col++)
        {
            idx = BOARD_COLS * row + col;
            if(checkFour(board, idx, idx + WIDTH, idx + WIDTH * 2, idx + WIDTH * 3))
            {
                return 1;
            }
        }
    }
    return 0;

}
int verticalCheck(char *board)
{
    int row, col, idx;
    const int HEIGHT = 7;

    for(row = 0; row < BOARD_ROWS - 3; row++)
    {
        for(col = 0; col < BOARD_COLS; col++)
        {
            idx = BOARD_COLS * row + col;
            if(checkFour(board, idx, idx + HEIGHT, idx + HEIGHT * 2, idx + HEIGHT * 3))
            {
                return 1;
            }
        }
    }
    return 0;

}
int diagonalCheck(char *board)
{
    int row, col, idx, count = 0;
    const int DIAG_RGT = 6, DIAG_LFT = 8;

    for(row = 0; row < BOARD_ROWS - 3; row++)
    {
        for(col = 0; col < BOARD_COLS; col++)
        {
            idx = BOARD_COLS * row + col;
            if((count <= 3 && checkFour(board, idx, idx + DIAG_LFT, idx + DIAG_LFT * 2, idx + DIAG_LFT * 3)) ||
                    (count >= 3 && checkFour(board, idx, idx + DIAG_RGT, idx + DIAG_RGT * 2, idx + DIAG_RGT * 3)))
            {
                return 1;
            }
            count++;
        }
        count = 0;
    }
    return 0;

}
void clearScreen()
{
#ifdef __linux__
    printf("\e[2J\e[H");
#endif

#ifdef __WIN32
    system("cls");
#else
    system("clear");
#endif
}

