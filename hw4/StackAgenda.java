import java.util.ArrayList;

public class StackAgenda extends Agenda{

	//Holds a list of coord that will be accesed in a FILO method 
	protected static ArrayList<MazeGridLocation> mazeLoc = new ArrayList<MazeGridLocation>();
	protected MazeGridLocation location;

	//Adds the provided coord to the list
	public void addLocation(MazeGridLocation newLocation){
		mazeLoc.add(newLocation);
	}
	
	//Gets the first coord in the list, simulating FIFO
	public MazeGridLocation getLocation(){
		MazeGridLocation pos = mazeLoc.get(mazeLoc.size() - 1);
		mazeLoc.remove(mazeLoc.size() - 1);
		return pos;
	}

	public String toString(){
		String locations = "Coordinates [" + location.getRow() + ", " + location.getColumn() + "]";
		return locations;
	}
	public Boolean isEmpty(){
		return mazeLoc.isEmpty();
	}
	public void clear(){
		mazeLoc.clear();
	}
/*
	public static void main(String[] args){
		MazeGridLocation newCord = new MazeGridLocation(5, 5, 'c');
		mazeLoc.add(newCord);
		System.out.println(mazeLoc.toString());

		MazeGridLocation newCord2 = new MazeGridLocation(6, 6, 'j');
		mazeLoc.add(newCord2);
		System.out.println((mazeLoc));
		System.out.println(mazeLoc.toString());
		System.out.println(mazeLoc.get(mazeLoc.size() - 1));
		System.out.println(mazeLoc.isEmpty());
		mazeLoc.clear();
		System.out.println((mazeLoc));
	}
*/
}