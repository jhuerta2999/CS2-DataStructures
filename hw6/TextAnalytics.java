import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.File;  
import java.util.Arrays;  

class TextAnalytics{

    public static void main(String[] args){
        MyHashMap dictionary = new MyHashMap();

        File book = new File(args[0]);
        Scanner read = null;
        //Prints message if file not found
        try{
            read = new Scanner(book);
        }
        catch(FileNotFoundException fnf){
            System.out.println("File Not Found!");
            System.exit(1);
        }
        //vars that will check when to stop/start reading the file
        String startReading = "START OF THIS";
        String endReading = "END OF THIS";
        String line = read.nextLine();

        //reads lines until it has cerrtain substring
        while(!line.contains(startReading)){
            line = read.nextLine();
    
        }
        line = read.nextLine();
        //reads file if it has more lines and does not contain certain substring
        while(read.hasNextLine() && !line.contains(endReading)){
            line = line.toLowerCase();

            String word[] = line.split("\\s+");
            
            //for each word in the line it will add or update values in the dictionary
            for(int i = 0; i < word.length; i++){     
                word[i] = word[i].replaceAll("[^a-z]", "");

                //if key exists it will take this path
                if(dictionary.containsKey(word[i])){
                    int occur = (Integer)dictionary.get(word[i]);
                    occur++;
                    dictionary.put(word[i], occur++);
                }
                //creates a new key in the dictionary
                else{
                    if(!word[i].equals("")){
                        dictionary.put(word[i], 1);   
                        }
                    }
                }
                line = read.nextLine();
            }
        
        Scanner input = new Scanner(System.in);

        System.out.println("--Top 5 Most Frequent Words--");
        
        //Prints out the top five words used
        for(int i = 0; i <= 4; i++){
          System.out.println(i + 1 +") " + dictionary.getEntries()[i].key + " used " + (int)dictionary.getEntries()[i].value + " times!");
        }
        while(true){
        try{
            //will continue to ask for words to sear until user quits
            System.out.print("Type a word or type 'q' to quit: ");
            String wordSearch = input.nextLine().toLowerCase();
            
            if(wordSearch.equals("q")){
                System.out.println("BYE!");
                System.exit(0);
            }
            else if(dictionary.containsKey(wordSearch)){
                int occur = (Integer)dictionary.get(wordSearch);
                System.out.println("The word " +wordSearch + " occurs " + occur + " times.");  
            }
            else{
                System.out.println("No Occurence of " + wordSearch);   
            }
        }
        catch(NumberFormatException nfe){
            System.out.println("Invalid Command");
            System.exit(1);
        }
        read.close();
        }                  
    }  
}