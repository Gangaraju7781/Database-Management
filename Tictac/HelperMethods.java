
public class HelperMethods {

	// Printing the board
	public static void drawBoard(char[][] board) {
		System.out.println("Board: ");

		for (int i = 0; i < board.length; i++) {

			for (int j = 0; j < board[i].length; j++) {

				System.out.print(board[i][j] + "   ");

			}

			System.out.println();

		}

	} // End of drawBoard(char[][]) method

	// Checking whether the board is full or not
	public static boolean isBoardFull(char[][] board) {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if (board[i][j] == '-') {
					return false;

				}

			}

		} // End of for-loop

		return true;

	}

	// Checking if the player has won or not (TicTacToe)
	public static char playerHasWonForTicTacToe(char[][] board) {

		for (int i = 0; i < board.length; i++) {

			boolean inARow = true;
			char value = board[i][0];

			if (value == '-') {
				inARow = false;

			} else {
				for (int j = 1; j < board[i].length; j++) {
					if (board[i][j] != value) {
						inARow = false;
						break;
					}

				} // End of inner for-loop

			} // End of else-statement

			if (inARow) {
				return value;

			}

		} // End of the For-loop (For checking rows)

		for (int j = 0; j < board[0].length; j++) {

			boolean inACol = true;
			char value = board[0][j];

			if (value == '-') {
				inACol = false;

			} else {
				for (int i = 1; i < board.length; i++) {
					if (board[i][j] != value) {
						inACol = false;
						break;

					}

				}

			} // End of else-statement

			if (inACol) {
				return value;

			}

		} // End of For-loop (For checking column)

		// Checking for diagonals
		boolean inADiag1 = true;
		char value1 = board[0][0];

		if (value1 == '-') {
			inADiag1 = false;

		} else {
			for (int i = 0; i < board.length; i++) {
				if (board[i][i] != value1) {
					inADiag1 = false;
					break;
				}

			}

		} // End of else-statement

		if (inADiag1) {
			return value1;

		}

		boolean inADiag2 = true;
		char value2 = board[0][board.length - 1];

		if (value2 == '-') {
			inADiag2 = false;

		} else {
			for (int i = 1; i < board.length; i++) {
				if (board[i][board.length - 1 - i] != value2) {
					inADiag2 = false;
					break;

				}

			}

		} // End of else-statement

		if (inADiag2) {
			return value2;

		}

		// Nobody won yet
		return ' ';

	} // End of playerHasWon(char[][])
	
	
	
	
	
	// Checking if the player has won or not (Order and Chaos)
	public static char playerHasWonForOrderAndChaos(char[][] board) {
		
		// Checking for rows
		for(int i = 0; i < board.length; i++) { // 6 rows
			
			boolean inARow = true;
			
			char value = board[i][0];
			
			if(value == '-') {
				inARow = false;
				
			}else {
				int count = 0;
				
				for(int j = 0; j < board[0].length; j++) {
					
					if(board[i][j] == value) {
						count = count + 1;
						
						if(count == 5) {
							System.out.println("=====================");
							System.out.println("ORDER HAS WON !!!");
							System.out.println("=====================");
//							System.exit(0);
							
							return 'R';
							
						}
						
					}
					
				}
				
			} // End of else-statement
			
		} // End of for-loop (Checking for rows)
		
		// Checking for columns
		for(int i = 0; i < board[0].length; i++) { // 6 rows
			
			boolean inARow = true;
			
			char value = board[0][i];
			
			if(value == '-') {
				inARow = false;
				
			}else {
				int count = 0;
				
				for(int j = 0; j < board.length; j++) {
					
					if(board[j][i] == value) {
						count = count + 1;
						
						if(count == 5) {
							System.out.println("=====================");
							System.out.println("ORDER HAS WON !!!");
							System.out.println("=====================");
//							System.exit(0);
							
							return 'C';
							
						}
						
					}
					
				}
				
			} // End of else-statement
			
		} // End of for-loop (Checking for columns)	
		
		
		// Checking for diagonals
		boolean inADiag1 = true;
		char value1 = board[0][0];

		if (value1 == '-') {
			inADiag1 = false;

		} else {
			int count = 0;
			for (int i = 0; i < board.length; i++) {
				if (board[i][i] != value1) {
					inADiag1 = false;
					break;
				}else {
					count = count + 1;
					
					if(count == 5) {
						System.out.println("=====================");
						System.out.println("ORDER HAS WON !!!");
						System.out.println("=====================");
//						System.exit(0);
						
						return 'D';
						
					}
					
				}

			}

		} // End of else-statement

		if (inADiag1) {
			return value1;

		}

		boolean inADiag2 = true;
		char value2 = board[0][board.length - 1];

		if (value2 == '-') {
			inADiag2 = false;

		} else {
			int count = 0;
			for (int i = 1; i < board.length; i++) {
				if (board[i][board.length - 1 - i] != value2) {
					inADiag2 = false;
					break;

				}else {
					System.out.println("afasfafhoah");
					count = count + 1;
					if(count == 5) {
						System.out.println("ORDER HAS WON !!!");
					}
				}

			}

		} // End of else-statement

		if (inADiag2) {
			return value2;

		}

		// Nobody won yet
		return ' ';

	} // End of playerHasWonForOrderAndChaos(char[][])

}
