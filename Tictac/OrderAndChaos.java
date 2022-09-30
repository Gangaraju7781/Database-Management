import java.util.Scanner;

public class OrderAndChaos extends HelperMethods {

	public void playOrderAndChaos() {

		Scanner scan = new Scanner(System.in);

		InitializeVariables initialize = new InitializeVariables();

		// Size of the board
		initialize.setBoardSize(6);

		// Creating the board
		char[][] board = new char[initialize.getBoardSize()][initialize.getBoardSize()];

		// Filling the board with dashes for representation only
		for (int i = 0; i < initialize.getBoardSize(); i++) {
			for (int j = 0; j < initialize.getBoardSize(); j++) {
				board[i][j] = '-';
			}
		} // End of Outer for-loop

		// Player names
		System.out.println("Order, What is your name? ");
		initialize.setPlayer1Name(scan.nextLine());

		System.out.println("Chaos, What is your name? ");
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
				System.out.println(initialize.getPlayer1Name() + "'s Turn (ORDER): (Choose: X or O)");
			} else {
				System.out.println(initialize.getPlayer2Name() + "'s Turn (CHAOS): (Choose: X or O)");
			}

			// Creating a char variable to store 'X' or 'O'
			char c = '-';

			if (isPlayer1) {

				while (true) {
					c = scan.next().charAt(0);

					Character character = new Character(c);

					if (character.equals('O') || character.equals('o') || character.equals('X')
							|| character.equals('x')) {
						break;

					} else {
						System.out.println("Please enter X or O");
					}

				}

			} else {

				while (true) {
					c = scan.next().charAt(0);

					Character character = new Character(c);

					if (character.equals('O') || character.equals('o') || character.equals('X')
							|| character.equals('x')) {
						break;

					} else {
						System.out.println("Please enter X or O");
					}

				}

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
			Character character = new Character(c);
			board[row][column] = character.toUpperCase(c);
			
//			playerHasWonForOrderAndChaos(board);
			
			if(playerHasWonForOrderAndChaos(board) == 'R' || playerHasWonForOrderAndChaos(board) == 'C' || playerHasWonForOrderAndChaos(board) == 'D') {
				break;
			}
			

		} // End of While-loop

		// Drawing the board at the end of the game
		drawBoard(board);

	}

}
