import java.util.Scanner;
import java.util.Random;

class guessingGame{
    
	public static void main(String[] args){

	Random rand = new Random();
	Scanner scan = new Scanner(System.in);
	//create a random number which within 1 and 100
	int randomNum = rand.nextInt(100) + 1;
	int guessCount = 0;

	System.out.println("Guess a number between 1-100 (or'q' to quit): ");

	//Game will continue running until the player enters q to quit the game
	while(true){
	 
	    System.out.print(":");
	    String guess = scan.nextLine();
	    
	    if(guess.equals("q")){
		System.out.println("Bye");
		return;
	    }

	    //If the player enters something that is not a number it will run this code
	    try{
		//All code in the try are game requierments and boundaries
		int guessNum = Integer.parseInt(guess);
		
		if(guessNum > 100 || guessNum < 0){
		    System.out.println("Number out of range!");
		}
		else if(guessNum < randomNum){
		    System.out.println("Incorrect, guess too low!");
		    guessCount = guessCount + 1;
		}
		else if(guessNum > randomNum){
		    System.out.println("Incorrect, guess too high!");
		    guessCount = guessCount + 1;
		}
		else if(guessNum == randomNum){
		    //Ask player to play again when he wins
		    System.out.println("That is correct, It took you " + guessCount + " tries!");
		    System.out.println("Play again? (y/n): ");
		    System.out.print(":");
		    guess = scan.nextLine();
		    if(guess.equals("y")){
			randomNum = rand.nextInt(100) + 1;
			System.out.println("Guess a number between 1-100 (or'q' to quit): ");
		    }
		    else if(guess.equals("n")){
			System.out.println("Bye");
			return;
		    }
		}
	    
	    }
	    //When ever the player deosnt input a number it will print this 
	    catch(NumberFormatException nfe){
		System.out.println("Not number!");
	    }
	}
    }
}



