import java.util.ArrayList;
import java.util.Collections;

class MazeSolver{

	protected Agenda steps;
	//Will equal the previous GridLocation
	protected MazeGridLocation prev;
	//Will equal newly obtained GridLocation
	protected MazeGridLocation newLocation;
	//Holds a list of GridLocations by their row and col
	protected MazeGridLocation[][] prevCord;
	//Will hold a list of the most efficient path
	protected ArrayList<MazeGridLocation> goldenPath;
	//Will hold a list of neighboring GridLocations
	protected ArrayList<MazeGridLocation> locations;

	public MazeSolver(Agenda a){
		steps = a;
	}

	//Will find a path to the maze and if there is one it will create the most efficient path to the goal
	public ArrayList<MazeGridLocation> solveMaze(Maze m, MazeGUI mg){	
		newLocation = m.getStartLocation();
		prevCord = new MazeGridLocation[m.getNumRows()][m.getNumColumns()];
		goldenPath = new ArrayList<MazeGridLocation>();

		steps.addLocation(newLocation);
		
		//Will continue to run the code until the goal has been reached
		while(!newLocation.equals(m.getGoalLocation())){
			//mg.pause(200);
			newLocation = steps.getLocation();
			//mg.visitLoc(newLocation);
			
			//Goal coordinates match with the new location coors it will add the goal coordinates to the list
			//Will save the neighboring coords with the goal coors to backtrace later on
			if(m.getGoalLocation().getRow() == newLocation.getRow() && m.getGoalLocation().getColumn() == newLocation.getColumn()){
				//Updates prev coords
				prev = prevCord[m.getGoalLocation().getRow()][m.getGoalLocation().getColumn()];
				goldenPath.add(prev);

				//Will continue to run until the prev coordintaes equals the start coordinates
				while(!prev.equals(m.getStartLocation())){
					//Updates prev coords
					prev = prevCord[prev.getRow()][prev.getColumn()];
					//adds coord to most efficient path list
					goldenPath.add(prev);
						
				}
				Collections.reverse(goldenPath);
				
				for(int i = 0; i < goldenPath.size(); i++){
					mg.pause(50);
					mg.addLocToPath(goldenPath.get(i));
				}
			}
			else{
				//Gets the current locations neighbors coord
				locations = m.getOpenNeighbors(newLocation);

				//For each valid coord it will be added to the list to be to be checked
				for(int i = 0; i < locations.size(); i++){	
					if(m.isVisited(locations.get(i)) == false){
						steps.addLocation(locations.get(i));
						m.markVisited(locations.get(i));
						//mg.addLocToAgenda(locations.get(i)); 
						//the previous coords will now equal the new locations cords to backtrace
						prevCord[locations.get(i).getRow()][locations.get(i).getColumn()] = newLocation;	
					}
				}
			
				if(steps.isEmpty()){
					return goldenPath;
				}		
			}		
		}
		return goldenPath;
	}	
}
/*
The Stack has a better time complexity compared to the queue, the queue will fan out to all the possible locations 
which will force it to take a longer time, while stack if the way coords are added it will be able to get to the goal 
quicker. The complexity is truly based on how big the maze and how the coords force a stack to explore a dead end.

Stacks perform better if there aren't many sorrounding coords like in maze file 3, stack took a path and executed it 
while for the queue it need t fan out and explore each location because of its functionality. But if there is a big maze 
with several dead ends it is more lickley that the stack will take longer because it will most likely explore the dead
end route, while the queue will fan put and explore each path/location in the agenda allowing it to explore more ground
to find the goal.

But Queues complete the maze more efficiently because they have explored more locations and with more locations it is 
allowed to find the most efficient path, while stacks will just have one specific path based on how the maze is structured
and how the locations are added.
*/