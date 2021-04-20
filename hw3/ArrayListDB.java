import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

class ArrayListDB{

    public static void main(String[] args){
        //Allows us to acces the AlbumArrayList file
        AlbumArrayList albumStock = new AlbumArrayList();

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
            String line = scanFile.nextLine(); 
            String addRemove[] = line.split(":");
            String artistAlbum[] = addRemove[1].split("-");
            
            //based on the first position of the original divided string will determine if the
            //album will be added or removed from the data base and will determine wich method we call
            if(addRemove[0].equals("ADD")){
                Album addAlbum = new Album(artistAlbum);
                albumStock.add(addAlbum);
            }  
            
            else if(addRemove[0].equals("REMOVE")){
                Album removeAlbum = new Album(artistAlbum);
                albumStock.remove(removeAlbum);
            }
        }
        //Scans the input commands of the user
        Scanner scanInput = new Scanner(System.in);
        System.out.println("Transactions Processed: " + albumStock.size());
        System.out.println("1 : add an album");
        System.out.println("2 : remove an album");
        System.out.println("3 : list all albums");
        System.out.println("4 : quit the program");
        System.out.print("Enter number for command: ");

        //Will keep on running until the the user quits or enters invalid commands
        while(true){
            try{
            String scanCommand = scanInput.nextLine();
            int commandNum = Integer.parseInt(scanCommand);
            System.out.println("---Albums "+albumStock.size()+"---");

            //Will ask the user for album info and will create a new object based on info
            // and send it to the add method to add to list
            if(commandNum == 1){
                System.out.print("Enter info as artist - album: ");
                scanCommand = scanInput.nextLine();
                String[] artistAlbum = scanCommand.split("-");

                    //if the info they inputed does not have 2 positions it is an invalid input
                if(artistAlbum.length == 2){
                    Album addAlbum = new Album(artistAlbum);
                    albumStock.add(addAlbum);
                }
                else{
                    System.out.println("Invalid Format! Try Again");
                }
                System.out.print("Enter another command: ");
            }         
                       
            //Will ask the user for album location they want to remove from list based on the location number entered
            else if(commandNum == 2){
                for(int i = 0; i < albumStock.size(); i++){
                    System.out.println(i + 1 +  " : " + albumStock.get(i));
                }    
                    System.out.print("Enter location to remove: ");
                    String enteredInfo = scanInput.nextLine();
                    int position = Integer.parseInt(enteredInfo);
                    
                    //Checks if the number entered is a valid number to call method
                    if(position <= albumStock.size() && position > 0){
                        albumStock.remove(position);
                    }
                    else{
                        System.out.println("Invalid position! Try Again");
                    }    
                System.out.print("Enter another command: ");
            }
            //Calls the size method to obtain info to print out the list 
            else if(commandNum == 3){
                for(int i = 0; i < albumStock.size(); i++){
                    System.out.println(i + 1 +  " : " + albumStock.get(i));
                }                
                System.out.print("Enter another command: ");
            }
            //Quits program
            else if(commandNum == 4){
                System.out.println("Bye!");
                return;
            }
            else{
                System.out.println("Invalid Command! Try Again");
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