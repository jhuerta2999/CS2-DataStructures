import java.util.ArrayList;

public class QueueAgenda extends Agenda{

	//Holds a list of coord that will be accesed in a FIFO method 
	protected static ArrayList<MazeGridLocation> mazeLoc = new ArrayList<MazeGridLocation>();
	protected MazeGridLocation location;

	//Adds the provided coord to the list
	public void addLocation(MazeGridLocation newLocation){
		mazeLoc.add(newLocation);
	}

	//Gets the first coord in the list, simulating FIFO
	public MazeGridLocation getLocation(){
		MazeGridLocation pos = mazeLoc.get(0);
		mazeLoc.remove(0);
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
		QueueAgenda newQ = new QueueAgenda();
		MazeGridLocation newCord = new MazeGridLocation(5, 5, 'c');
		newQ.addLocation(newCord);
		MazeGridLocation newCord2 = new MazeGridLocation(6, 6, 'j');
		newQ.addLocation(newCord2);
		System.out.println((mazeLoc));
		System.out.println(mazeLoc.toString());
		System.out.println(mazeLoc.get(0));
		System.out.println(mazeLoc.isEmpty());
		mazeLoc.clear();
		System.out.println((mazeLoc));
	}
*/
}