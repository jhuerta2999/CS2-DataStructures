import java.util.Scanner;

class isbn{

    static String isbn10 (int isbnNum){
		int product = 0;
		int result = 0;
		for(int i = 9; i > 0; i--){
			product = ((isbnNum % 10) * i);
			isbnNum = isbnNum / 10;
			result += product;

			System.out.println(product);
			System.out.println(result);
		}

		int digit10 = result % 11;

		if(digit10 == 10){
			char digitX = 'X';
			return digitX;
		}	
		else{
			return digit10;
		}
	}
	
	static String isbn13(int isbnNum){
		int product = 0;
		int result = 0;
		for(int i = 12; i > 0; i--){
			if(i % 2 == 0){
				product = ((isbnNum % 10) * 3);
				isbnNum = isbnNum / 10;
				result += product;

				System.out.println(product);
				System.out.println(result);
			}
			else{
				product = isbnNum % 10;
				isbnNum = isbnNum / 10;
				result += product;

				System.out.println(product);
				System.out.println(result);
			}

		int digit13 = 10 - (result % 10);

		if(digit13 == 10){
			digit13 = 0;
			return digit13;
		}
		else{
			return digit13;
		}
	}
    
    public static void main(String[] args){
	
		Scanner scan = new Scanner(System.in);
		
		System.out.println("Enter isbn: ");
		
		int isbnNum = scan.nextInt();

		if(isbnNum.length() == 8){
			System.out.println(isbn10(isbnNum));
		}
		else if(isbnNum.length() == 11){
			System.out.println(isbn13(isbnNum));
		}
		else{
			System.out.println("Invalid ISBN!");
		}	
	}
}
