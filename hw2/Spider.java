import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.Random;
import java.util.Arrays;

/**
 * Handles display, movement, and fly eating capabalities for frogs
 */
public class Spider
{
    protected static final String imgFile = "spider.jpg";

    protected GridLocation location;

    protected FlyWorld world;

    protected BufferedImage image;

    /**
     * Creates a new Frog object.<br>
     * The image file for a frog is frog.jpg<br>
     *
     * @param loc a GridLocation
     * @param fw the FlyWorld the frog is in
     */
    public Spider(GridLocation loc, FlyWorld fw){
	location = loc;
	world = fw;

	try{
	    image = ImageIO.read(new File(imgFile));
	}
	catch (IOException ioe){
	    System.out.println("Unable to read image file: " + imgFile);
	    System.exit(0);
	}
	location.setSpider(this);
    }
    /**
     * @return BufferedImage the image of the frog
     */
    public BufferedImage getImage()
    {
	return image;
    }

    /**
     * @return GridLocation the location of the frog
     */
    public GridLocation getLocation()
    {
	return location;
    }

    /**
     * @return boolean, always true
     */
    public boolean isPredator()
    {
	return true;
    }

    /**
     * Returns a string representation of this Frog showing
     * the location coordinates and the world.
     *
     * @return the string representation
     */
    public String toString(){
	String s = "Frog in world:  " + this.world + "  at location (" + this.location.getRow() + ", " + this.location.getCol() + ")";
	return s;
    }

    /**
     * Generates a list of <strong>ALL</strong> possible legal moves<br>
     * for a frog.<br>
     * You should select all possible grid locations from<br>
     * the <strong>world</strong> based on the following restrictions<br>
     * Frogs can move one space in any of the four cardinal directions but<br>
     * 1. Can not move off the grid<br>
     * 2. Can not move onto a square that already has frog on it<br>
     * GridLocation has a method to help you determine if there is a frog<br>
     * on a location or not.<br>
     *
     * @return GridLocation[] a collection of legal grid locations from<br>
     * the <strong>world</strong> that the frog can move to
     */
    public GridLocation[] generateLegalMoves()
    {
        GridLocation flyLocation = world.getFlyLocation();
        //current location of fly
        int flyRow = flyLocation.getRow();
        int flyCol = flyLocation.getCol();

        GridLocation[] possibleMoves = new GridLocation[4];
        //current location of spider
	    int r = this.location.getRow();
        int c = this.location.getCol();
        GridLocation newLoc = world.getLocation(r, c);
        //number of rows and cols in the world
	    int row = world.getNumRows();
        int col = world.getNumCols();

        //Checks the validity of each position around its current position to see if
        //it is able to move there, if so it is added to a list, possibleMOves
        if(r - 1 >= 0){
	    newLoc = world.getLocation(r-1, c);
	    if(world.isValidLoc(r-1,c) == true && newLoc.hasPredator() == false && newLoc.hasSpider() == false){
            if(r - 1 >= flyRow){
		        possibleMoves[0] = world.getLocation(r-1,c);
                }
            }
	    }   
        if(r + 1 < row){
            System.out.println(row);
            newLoc = world.getLocation(r+1, c);
            if(world.isValidLoc(r+1,c) == true && newLoc.hasPredator() == false && newLoc.hasSpider() == false){
                if(r + 1 <= flyRow){
                    possibleMoves[1] = world.getLocation(r+1,c);
                    }
                }   
            }
        if(c - 1 >= 0){
            newLoc = world.getLocation(r, c-1);
            if(world.isValidLoc(r,c-1) == true && newLoc.hasPredator() == false && newLoc.hasSpider() == false){
                if(c-1 >= flyCol){
                    possibleMoves[3] = world.getLocation(r,c-1);
                    }
                }	
            }
        if(c+1 < col){
            System.out.println(col);
            newLoc = world.getLocation(r, c+1);
            if(world.isValidLoc(r,c+1) == true && newLoc.hasPredator() == false && newLoc.hasSpider() == false){
                if(c + 1 <= flyCol){
                    possibleMoves[2] = world.getLocation(r,c+1);
                    }
                }
            }
        return possibleMoves;
    }
    /**
     * This method updates the frog's position.<br>
     * It should randomly select one of the legal locations(if there any)<br>
     * and set the frog's location to the chosen updated location.
     */
    public void update()
    {
	int row = location.getRow();
	int col = location.getCol();
	GridLocation oldLocation = location;
	GridLocation newLocation = world.getLocation(row, col);
	GridLocation[] possibleMoves = generateLegalMoves();
	Random random = new Random();
    
    //Creates a loop in where if the position chosen empty it will re run until 
    // it obtains one with a coordinate that is availbale to move to
	newLocation = null;
	while(newLocation == null){

	    newLocation = possibleMoves[random.nextInt(4)];
	
	    if(newLocation != null && newLocation.hasPredator() == false && newLocation.hasSpider() == false){
            oldLocation.removeSpider();
            newLocation.setSpider(this);
            location = newLocation;
            System.out.println(location);
            return;
            }
        }
    }	
    /**
     * This method helps determine if a frog is in a location<br>
     * where it can eat a fly or not. A frog can eat the fly if it<br>
     * is on the same square as the fly or 1 spaces away in<br>
     * one of the cardinal directions
     *
     * @return boolean true if the fly can be eaten, false otherwise
     */ 
    public boolean eatsFly()
    {
	int row = location.getRow();
	int col = location.getCol();
    
    //if the position of the fly and the spider are the same it will eat it
    // also eats it if it is next to it
	if(location.equals(world.getFlyLocation()) == true){
	    return true;
	}
	else if(world.isValidLoc(row+1, col) == true && world.getLocation(row+1, col).equals(world.getFlyLocation()) || world.isValidLoc(row-1, col) == true && this.world.getLocation(row-1, col).equals(world.getFlyLocation())){
	    return true;
	}
	else if(world.isValidLoc(row, col+1) == true && this.world.getLocation(row, col+1).equals(world.getFlyLocation()) || world.isValidLoc(row, col-1) == true && this.world.getLocation(row, col-1).equals(world.getFlyLocation())){
		return true;
	}
	return false; 
	 
    }
}
