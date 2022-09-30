import java.util.Scanner;

public class Application {

//	static boolean flag = true;
	
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		
		System.out.println("Choose a game: TicTacToe (N X N) (or)  Order and Chaos");
		System.out.println();
		System.out.println("Enter '0' for TicTacToe (or) '1' for Order and Chaos");
		
		int choice = scan.nextInt();
		
		boolean isGameEnded = false;
		
		if(choice == 0) {
			
			while(!isGameEnded) {
				
				TicTacToe tictactoe = new TicTacToe();
				tictactoe.startTicTacToe();
				
				System.out.println();
				System.out.println("Care for another game (Y/N) ??? ");
				
				char yesOrNo = scan.next().charAt(0);
				
				@SuppressWarnings("removal")
				Character character = new Character(yesOrNo);
				
				if(character.equals('N') || character.equals('n')) {
					isGameEnded = true;
					
				}
				
			}
			
			
		} else if(choice == 1) {
			
			while(!isGameEnded) {
				
				OrderAndChaos orderAndChaos = new OrderAndChaos();
				orderAndChaos.playOrderAndChaos();
				
				System.out.println();
				System.out.println("Care for another game (Y/N) ???");
				
				char yesOrNo = scan.next().charAt(0);
				
				@SuppressWarnings("removal")
				Character character = new Character(yesOrNo);
				
				if(character.equals('N') || character.equals('n')) {
					isGameEnded = true;
					
				}
				
				
			} // End of While-loop
			
		} // End of Else-if statement
		
		
		scan.close();
		
	}
	
	
	
}
