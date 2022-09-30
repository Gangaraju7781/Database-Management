import java.util.Scanner;

public class TicTacToe extends HelperMethods {

	public void startTicTacToe() {

		@SuppressWarnings("resource")
		Scanner scan = new Scanner(System.in);

		InitializeVariables initialize = new InitializeVariables();

		// Size of the board
		System.out.println("Enter the board size: ");
		initialize.setBoardSize(scan.nextInt());

		scan.nextLine();

		// Creating the board
		char[][] board = new char[initialize.getBoardSize()][initialize.getBoardSize()];

		// Filling the board with dashes for representation only
		for (int i = 0; i < initialize.getBoardSize(); i++) {
			for (int j = 0; j < initialize.getBoardSize(); j++) {
				board[i][j] = '-';
			}
		} // End of Outer for-loop

		// Player names
		System.out.println("Player1, What is your name? ");
		initialize.setPlayer1Name(scan.nextLine());

		System.out.println("Player2, What is your name? ");
		initialize.setPlayer2Name(scan.nextLine());

		// Keeping track of whose turn it is
		boolean isPlayer1 = true;

		// Keeping track if the game is ended or not
		boolean isGameEnded = false;

		while (!isGameEnded) {

			// Printing the board
			drawBoard(board);

			// Printing whose turn it is
			if (isPlayer1) {
				System.out.println(initialize.getPlayer1Name() + "'s Turn (X):");
			} else {
				System.out.println(initialize.getPlayer2Name() + "'s Turn (O):");
			}

			// Creating a char variable to store 'X' or 'O'
			char c = '-';

			if (isPlayer1) {
				c = 'X';
			} else {
				c = 'O';
			}

			// Location
			int row = 0;
			int column = 0;

			while (true) {

				System.out.print("Enter a row number: ");
				row = scan.nextInt();
				System.out.print("Enter a column number: ");
				column = scan.nextInt();

				if (row < 0 || column < 0 || row >= initialize.getBoardSize() || column >= initialize.getBoardSize()) {
					System.out.print("This position is off the bounds of the board!!! Please Try Again.");

				} else if (board[row][column] != '-') {
					System.out.println("Someone has already made used this position!!! Please Try Again");

				} else {
					break;

				}

			} // End of the inner while-loop

			// Setting the Position at specified row and column
			board[row][column] = c;

			if (playerHasWonForTicTacToe(board) == 'X') {
				System.out.println("=====================================================");
				System.out.println(initialize.getPlayer1Name() + " has won!!!");
				System.out.println("=====================================================");

				isGameEnded = true;

			} else if (playerHasWonForTicTacToe(board) == 'O') {
				System.out.println("=====================================================");
				System.out.println(initialize.getPlayer2Name() + " has won!!!");
				System.out.println("=====================================================");

				isGameEnded = true;

			} else {

				if (isBoardFull(board)) {
					System.out.println("=====================================================");
					System.out.println("It's a Tie!!!");
					System.out.println("=====================================================");

					isGameEnded = true;

				} else {
					isPlayer1 = !isPlayer1;

				}

			} // End of outer else-statement

		} // End of While-loop

		// Drawing the board at the end of the game
		drawBoard(board);

	}

}
