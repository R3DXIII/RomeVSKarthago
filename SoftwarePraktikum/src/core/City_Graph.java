package core;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;


public class City_Graph implements Iterable<City>{

	public City_Graph(){
		
		
	}
	
	
	int amountOfCitys;
	
	
	/**
	 * loads the map given by the param
	 * Should always be called before using
	 * the city_graph
	 * 
	 * @param mappath String to the mapfile
	 * @return true if loading was sucessfull false if not
	 */
	public boolean loadMap(String mappath){
		

		pathList = new ArrayList<>();
		cityList = new VertexSet();
		
		biggestX = 0;
		biggestY = 0;
		
		BufferedReader fileReader = null;
		String line = null;

		
		try {
			fileReader = new BufferedReader(new FileReader(mappath));
			 line = fileReader.readLine();
		
		} catch (FileNotFoundException e) {
			System.out.println("Failed to read File");
			return false;
		} catch(IOException e) {
			System.out.println("Failed to read File");
			return false;
		}
		
		amountOfCitys = Integer.parseInt(line);

		while(line != null){
			String prefix = line.split(" ")[0];
			
			if(prefix.equals("V")){
				// add City
				
				int x 				= 0;
				int y 				= 0;
				int id				= 0;
				String owner 		= "";
				String splitLine[] 	= new String[4];
				
				try{
					splitLine = line.split(" ");
					id		= Integer.parseInt(splitLine[1]);
					x 		= Integer.parseInt(splitLine[3]);
					y		= Integer.parseInt(splitLine[4]);
					owner	= splitLine[2];
				}catch(NullPointerException ex){
					System.out.println("Too Few Arguments in line: \n" + line);
					return false;
				}catch(ArrayIndexOutOfBoundsException ex){
					System.out.println("Too Few Arguments in line: \n" + line);
					return false;
				}
				
				
				if(!cityList.containsID(id) && id >= 0){
					Owner o = null;
					switch(owner){
					case "N":
						o = Owner.Neutral;
						break;
					case "R":
						o = Owner.Rom;
						break;
					case "C":
						o = Owner.Cathargo;
						break;
					default:
						System.out.println("Wrong Owner set int Line: \n" + line);
						return false;
					}
			
					
					cityList.addVertex(new City(id, x, y, o));
				
					if(x > biggestX){
						biggestX = x;
					}
					if(y > biggestY){
						biggestY = y;
					}
				}else{
					System.out.println("ID already exists or is below 0, at line \n" + line);
					return false;
				}
			
				
				
				
			}else if(prefix.equals("E")){
				// add Path
				
				int firstID = 0;
				int secondID = 0;
				
				try{
					firstID 	= Integer.parseInt(line.split(" ")[1]);
					secondID 	= Integer.parseInt(line.split(" ")[2]);
				}catch(ArrayIndexOutOfBoundsException ex){
					System.out.println("Too Few Arguments in line: \n" + line);
					return false;
				}
				
				if(cityList.containsID(firstID) && cityList.containsID(secondID)){
					if(firstID == secondID){
						System.out.println("IDs are equal in this line: \n" + line);
						return false;
					}
					pathList.add(new Path(cityList.getCityByID(firstID),
							cityList.getCityByID(secondID)));
					
				}else{
					System.out.println("Found Unknown ID in this line: \n"+
							line);
					return false;
				}
				
			}
			try{
				line = fileReader.readLine();
			}catch(IOException ex){
				System.out.println("error while reading mapfile");
				return false;
			}
		}
		return true;
	}

	
	

	private ArrayList<Path> pathList;
	private VertexSet cityList;
	
	private int  biggestX;
	private int  biggestY;
	
	
	/**
	 * 
	 * @return returns the X-Value of the City with the hightest X-Value in the Graph
	 */
	public int getBiggestX(){ return biggestX; }
	
	
	/**
	 * 
	 * @return the X-Value of the City with the hightest Y-Value in the Graph
	 */
	public int getBiggestY(){ return biggestY; }
	
	
	public Iterator<City> iterator(){
		return cityList.iterator();
	}
	public Iterator<Path> getPathIterator(){
		return pathList.iterator();
	}
	
	/**
	 * 
	 * @return returns how much citys the graph contains
	 */
	public int getAmountOfCitys(){
		return amountOfCitys;
	}
	
	
	/**
	 * 
	 * @param id id of the wanted city
	 * @return City of the given id
	 */
	public City getCity(int id){
		return cityList.getCityByID(id);
	}
	
	
	/**
	 * 
	 * @param city
	 * @return returns all Citys with are Connected to the given City
	 * 			not including the city Itself
	 */
	public VertexSet getNeighbourhood(City city){
		
		 if (cityList.containsID(city.getID())) {
	            
	            VertexSet result = new VertexSet();
	            City cityOne;
	            City cityTwo;
	            for (Path p : pathList) {
	                cityOne = p.getFirstCity();
	                cityTwo = p.getSecondCity();
	                if (cityOne.equals(city)) {
	                    if (!result.containsID(cityTwo.getID())) {
	                        result.addVertex(cityTwo);
	                    }
	                    
	                } else if (cityTwo.equals(city)) {
	                    if (!result.containsID(cityOne.getID())) {
	                        result.addVertex(cityOne);
	                    }
	                }
	            }
	            
	            return result;
	        }else{
	            return null;
	        }        
	}
	
	
	
	
	/**
	 * 
	 * @param S
	 * @return returns all Neighbours from all Citys in the Set not
	 * 			including the Citys in the Set itself
	 */
	public VertexSet getVertexSetNeighbourhood(VertexSet S){
		
		VertexSet vertexSetNeighbourhood = new VertexSet();
        VertexSet vertexSetTemp = null;
        Iterator<City> cityIterator = S.iterator();
        
        while (cityIterator.hasNext()){
            vertexSetTemp = getNeighbourhood(cityIterator.next());
            Iterator<City> vertexSetTempIterator = vertexSetTemp.iterator();    
                while (vertexSetTempIterator.hasNext()){
                	City nextCity = vertexSetTempIterator.next();
                    if (!vertexSetNeighbourhood.containsID(nextCity.getID()))
                        vertexSetNeighbourhood.addVertex(nextCity);
                }
        }
        
        // added by johannes (remove elements from S)
        for(City c : S){
        	if(vertexSetNeighbourhood.containsID(c.getID())){
        		vertexSetNeighbourhood.removeID(c.getID());
        	}
        }
        return vertexSetNeighbourhood;
	}
	
	
	
	
	/**
	 * 
	 * @param city
	 * @return
	 */
	public VertexSet getConnectedComponents(City city){
		
		 VertexSet set = getNeighbourhood(city);
	               
	        if(set == null){     
	            return set;
	        }else{
	            
	            VertexSet result = new VertexSet();
	            result.addVertex(city);
	            
	            getFriendlyNeighbours(city, result);
	            
	            return result;
	            
	        }
	}
	
	private void getFriendlyNeighbours(City city, VertexSet result){
		
		VertexSet set = getNeighbourhood(city);
		for(City c : set){
            if(c.getOwner() == city.getOwner() && !result.containsID(c.getID())){
                result.addVertex(c);
                getFriendlyNeighbours(city, result);
            }
        } 
	}
	
	
	
	/**
	 * 
	 * @param owner
	 * @return returns the current Score of the given Owner
	 */
	public int getScore(Owner owner){
	  if (owner==Owner.Neutral) return -1;
        
        int Score = 0;
        City tempCity;
        Iterator<City> CityIterator = iterator();
        
        while(CityIterator.hasNext()){
            tempCity = CityIterator.next();
            if (tempCity.getOwner() == owner) Score++;
            else if (tempCity.getOwner() == Owner.Neutral) {
                if(isCutOffBy(tempCity, owner)) Score++;
            }
        }
        return Score;
    }
	    
	
	
    private boolean isCutOffBy(City city, Owner owner){
        
 
        VertexSet neighbours = getVertexSetNeighbourhood(getConnectedComponents(city));
        
        for(City c : neighbours){
        	if(c.getOwner() == ReturnOtherPlayer(owner)) return false;
        }
        return true;
    }
	    
    
    private Owner ReturnOtherPlayer(Owner owner){
        if (owner==owner.Rom) return owner.Cathargo;
        if (owner==owner.Cathargo) return owner.Rom;
        else return owner;
	}
    
    
    // maurice
    public String convertGameStateToString() {
    	
    	return null;
    }
    
    
    
    // johannes
    /**
	 * 
	 * @param move
	 * @return returns the new graph after the move if the move is valid
	 * otherwise it returns the unchanged graph
	 */
    public City_Graph gameStateTransition(Move move) {
		
		return null;
	}
    
    
}
