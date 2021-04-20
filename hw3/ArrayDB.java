import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

class ArrayDB{

    public static void main(String[] args){
        //Initiates values of acc and length of array
        Album[] albumStock = new Album[3];
        int numAlbums = 0;        

        File fileInfo = new File(args[0]);
        Scanner scanFile = null;

        //Sends message in case if file does not exist
        try{
            scanFile = new Scanner(fileInfo);
        } catch(FileNotFoundException fnf){
            System.out.println("File Not Found!");
            System.exit(1);
        }

        //scans the file that is given
        while(scanFile.hasNextLine()){
            //each line will be split to obtain strings that we want
            String line = scanFile.nextLine();
            String addRemove[] = line.split(":");
            String artistAlbum[] = addRemove[1].split("-");

            //based on the first position of the original divided string will determine if the
            //album will be added or removed from the data base
            if(addRemove[0].equals("ADD")){
                //creates a bigger array if the original one is full to avoid out of bounds errors
                if(numAlbums == albumStock.length){
                    Album[] newArray = new Album[(3/2) * (albumStock.length) + 1];
                    for(int i = 0; i < albumStock.length; i++){
                        newArray[i] = albumStock[i];
                    }
                    albumStock = newArray;
                }
                //cretes album object and increases the acc to track album amount
                Album albumObj = new Album(artistAlbum);
                albumStock[numAlbums] = albumObj;
                numAlbums++;
            }  
            else if(addRemove[0].equals("REMOVE")){
                Album removed = new Album(artistAlbum);
                int posRemoved;  
                //creates a bigger array if the original one is full to avoid out of bounds errors
                if(numAlbums == albumStock.length){
                    Album[] newArray = new Album[(3/2) * (albumStock.length) + 1];
                    for(int i = 0; i < albumStock.length; i++){
                        newArray[i] = albumStock[i];
                    }
                    albumStock = newArray;             
                }
                //searches through the list and compares the objects to elimate the duplicate from the list
                // then slides all data to the list to fix the list and decreases acc value
                for(int i = 0; i < numAlbums; i++){
                    if(removed.equals(albumStock[i]) == true){
                        posRemoved = i;                        
                        for(int j = posRemoved; j < numAlbums-1; j++){
                            albumStock[j] = albumStock[j+1];   
                        }  
                    }
                }
                numAlbums--;
            }  
        }
        //Scans the input commands of the user
        Scanner scanInput = new Scanner(System.in);
        System.out.println("Transactions Processed: " + numAlbums);
        System.out.println("1 : add an album");
        System.out.println("2 : remove an album");
        System.out.println("3 : list all albums");
        System.out.println("4 : quit the program");
        System.out.print("Enter number for command: ");
        
        //Will keep on running until the the user quits or enters invalid commands
        while(true){
            try{
            //Scans the number entered and will run command based on number
            String scanCommand = scanInput.nextLine();
            int commandNum = Integer.parseInt(scanCommand);

            System.out.println("---Albums "+numAlbums+"---");
            //Will ask the user for album info and will create a new object based on info
            if(commandNum == 1){
                System.out.print("Enter info as artist - album: ");
                String enteredInfo = scanInput.nextLine();
                String[] artistAlbum = enteredInfo.split("-");

                //creates a bigger array if the original one is full to avoid out of bounds errors
                if(numAlbums == albumStock.length){
                    Album[] newArray = new Album[(albumStock.length*3)/2 + 1];
                    for(int i = 0; i < albumStock.length; i++){
                        newArray[i] = albumStock[i];
                    }
                    albumStock = newArray;
                }
                
                if(artistAlbum.length != 2){
                    System.out.println("Invalid Command! Try Again");
                }
                else{
                    Album albumObj = new Album(artistAlbum);
                    albumStock[numAlbums] = albumObj;
                    numAlbums++;
                }

                System.out.print("Enter another command: ");
            }
            //Will ask the user for album location they want to remove from list based on the location number entered
            else if(commandNum == 2){
                for(int i = 0; i < numAlbums; i++){
                    System.out.println(i + 1 +  " : " + albumStock[i]);
                }
                System.out.print("Enter number next to album you wish to remove: ");
                String enteredInfo = scanInput.nextLine();
                int position = Integer.parseInt(enteredInfo);

                //creates a bigger array if the original one is full to avoid out of bounds errors
                if(numAlbums == albumStock.length){
                    Album[] newArray = new Album[(albumStock.length*3)/2 + 1];
                    for(int i = 0; i < albumStock.length; i++){
                        newArray[i] = albumStock[i];
                    }
                    albumStock = newArray;
                }
                //loops through list to fix the positions and decreases the acc
                if(position <= numAlbums && position > 0 ){
                    for(int i = position; i < numAlbums; i++){
                        albumStock[i] = albumStock[i+1];
                    }
                numAlbums--;
                }
                else{
                    System.out.println("Invalid Command! Try Again");
                }
                System.out.print("Enter another command: ");
            }
            //Loops through the list to print them out
            else if(commandNum == 3){
                for(int i = 0; i < numAlbums; i++){
                    System.out.println(i + 1 +  " : " + albumStock[i]);
                }
                System.out.print("Enter another command: ");
            }
            //quits the program
            else if(commandNum == 4){
                System.out.println("Bye!");
                return;
            }
            else{
                System.out.println("Invalid Command! Try Again");
                System.out.print("Enter another command: ");

            }
            //Based on the acc value it will print this if it is 0
            if(numAlbums == 0){
                System.out.println("Your album stock is empty!");
                System.out.print("Enter another command: ");
            }
        }
        //If they input an invalid command it will quit the program
        catch(NumberFormatException nfe){
            System.out.println("Invalid Command");
            System.exit(1);
        }
        scanFile.close();
        }
    }    
}
    
