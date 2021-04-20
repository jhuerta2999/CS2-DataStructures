import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.awt.Color;
import java.util.Random;


/**
 * Contains information about the world (i.e., the grid of squares)<br>
 * and handles most of the game play work that is NOT GUI specific
 */
public class FlyWorld
{
    protected int numRows;
    protected int numCols;

    protected GridLocation [][] world;

    protected GridLocation start;
    protected GridLocation goal;

    protected Frog[] frogs;
	
    protected Fly mosca;

    protected Spider[] spider;

    /**
     * Reads a file containing information about<br>
     * the grid setup.  Initializes the grid<br>
     * and other instance variables for use by<br>
     * FlyWorldGUI and other pieces of code.
     *
     *@param fileName the file containing the world grid information
     */
    public FlyWorld(String fileName){
		
		File gridInfo = new File(fileName);
		//Will scan the file line per line
		Scanner scanGrid = null;

		//In case the finle is not found it will run this code
		try{
			scanGrid = new Scanner(gridInfo);
		} catch (FileNotFoundException fnf) {
			System.out.println("File not found!");
			System.exit(1);
		}
		
		String line = scanGrid.nextLine();
		//Will scan each character in each line
		Scanner positionValue = new Scanner(line);
		positionValue.useDelimiter(" ");
			
		numRows = positionValue.nextInt();
		numCols = positionValue.nextInt();

		positionValue.close();

		char charPos;
		//Creates a grid based on the length of the file
		//Will look for predators(frogs and spiders)
		world = new GridLocation[numRows][numCols];

		frogs = new Frog[numRows * numCols];
		int frogCount = 0;

		spider = new Spider[numRows * numCols];
		int spiderCount = 0;

		int rowNum = 0;

		//Will continue to scan until there is no more lines
		while(scanGrid.hasNextLine()){
			int colNum = 0;
			line = scanGrid.nextLine();

			//will scan each letter in each line and search for certain characters
			Scanner charScan = new Scanner(line);

			while(charScan.hasNext()){
			charScan.useDelimiter("");
			String readChar = charScan.next();
			charPos = readChar.charAt(0);
		
			//Will convert each character pos to a pos in the grid
			//Depending on the char will determine what it becomes
			world[rowNum][colNum] = new GridLocation(rowNum, colNum);
			
			if(charPos == 'h'){
				goal = world[rowNum][colNum];
				goal.setBackground(Color.RED);		    
			}
			else if(charPos == 's'){
				start = world[rowNum][colNum];
				start.setBackground(Color.GREEN);
				mosca = new Fly(world[rowNum][colNum], this);
				start.setFly(mosca);
			}
			else if(charPos == 'f'){
				start = world[rowNum][colNum];
				frogs[frogCount] = new Frog(world[rowNum][colNum], this);
				start.setFrog(frogs[frogCount]);
				frogCount++;
			}	
			else if(charPos == 'a'){
				start = world[rowNum][colNum];
				spider[spiderCount] = new Spider(world[rowNum][colNum], this);
				start.setSpider(spider[spiderCount]);
				spiderCount++;
			}							
			colNum= colNum+1;					
			}
			rowNum = rowNum + 1;
			charScan.close();
		}	

			System.out.println("start : "+ start);
		}
	

    /**
     * @return int, the number of rows in the world
     */
    public int getNumRows()
    {
	return numRows;
    }

    /**
     * @return int, the number of columns in the world
     */
    public int getNumCols()
    {
	return numCols;
    }

    /**
     * Deterimes if a specific row/column location is<br>
     * a valid location in the world (i.e., it is not out of bounds)
     *
     * @param r a row
     * @param c a column
     *
     * @return boolean
     */                                                                                                                                                                                                                                                                                                  
    public boolean isValidLoc(int r, int c)
    {
	
		//Stays within the bounds of the grid to prove validity of location
		if(r >= numRows || r < 0){
			return false;
		}
		else if(c >= numCols || c < 0){
			return false;
		}
		else{
			return true; // THIS LINE IS JUST SO CODE COMPILES
		}
    }

    /**
     * Returns a specific location based on the given row and column
     *
     * @param r the row
     * @param c the column
     *
     * @return GridLocation
     */
    public GridLocation getLocation(int r, int c)
    {
	return world[r][c];
    }

    /**
     * @return FlyWorldLocation, the location of the fly in the world
     */
    public GridLocation getFlyLocation()
    {
	return mosca.getLocation();
    }

    /**
     * Moves the fly in the given direction (if possible)
     * Checks if the fly got home or was eaten
     *
     * @param direction the direction, N,S,E,W to move
     *
     * @return int, determines the outcome of moving fly<br>
     *              there are three possibilities<br>
     *              1. fly is at home, return ATHOME (defined in FlyWorldGUI)<br>
     *              2. fly is eaten, return EATEN (defined in FlyWorldGUI)<br>
     *              3. fly not at home or eaten, return NOACTION (defined in FlyWorldGUI)
     */
    public int moveFly(int direction)
    {
		//Will update posmof fly
		mosca.update(direction);
		
		//Depending on the location ogf the fly it will test to see if 
		// it has died or if it has made it home
		if(mosca.getLocation() == goal){
			return FlyWorldGUI.ATHOME;
		}
			
		for(int i = 0; i < frogs.length; i++){
			if(frogs[i] != null){
				if(frogs[i].eatsFly()){
					return FlyWorldGUI.EATEN;
				}
			}
		}
			
		for(int i = 0; i < spider.length; i++){
			if(spider[i] != null){
				if(spider[i].eatsFly()){
					return FlyWorldGUI.EATEN;
				}
			}
		}
		
		return FlyWorldGUI.NOACTION;
    }
	    
    /**
     * Moves all predators. After it moves a predator, checks if it eats fly
     *
     * @return boolean, return true if any predator eats fly, false otherwise
     */
    public boolean movePredators()
    {
	
	//Moves predators found in the file and chesck to see if it has the 
	//opportunity to eat the fly 
	for(int i = 0; i < frogs.length; i++){
	    if(frogs[i] != null){
			frogs[i].update();
			if(frogs[i].eatsFly()){
		    	return true;
			}	
	    }
	}
	for(int i = 0; i < spider.length; i++){
	    if(spider[i] != null){
			spider[i].update();
			if(spider[i].eatsFly()){
		    	return true;
				}
	    	}
		}	
	return false; 
    }
}
