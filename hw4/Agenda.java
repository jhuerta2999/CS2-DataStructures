import java.util.ArrayList;

abstract class Agenda{

	//methods and vars that should be implemented to other files
	protected static ArrayList<MazeGridLocation> mazeLoc;

	abstract public void addLocation(MazeGridLocation newLocation);

	abstract public MazeGridLocation getLocation();

	abstract public String toString();

	abstract public Boolean isEmpty();

	abstract public void clear();
}