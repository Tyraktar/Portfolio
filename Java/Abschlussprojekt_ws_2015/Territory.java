
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.util.LinkedList;

public class Territory {

    private final String name;
    private Point capital;
    private final LinkedList<Territory> neighbors;
    private Player occupiedBy;
    private int stationaryTroops;
    private GeneralPath border;
    private Area area;
    private World world;

    public Territory(String name, World world){
        this.name = name;
        neighbors = new LinkedList<>();
        occupiedBy = null;
        stationaryTroops = 0;
        this.world = world;
    }

    public String getName(){
        return name;
    }
    public Point getCapital(){
        return capital;
    }
    public LinkedList<Territory> getNeighbors(){
        return neighbors;
    }
    public Player getOccupiedBy(){
        return occupiedBy;
    }
    public int getStationaryTroops(){
        return stationaryTroops;
    }
    public void addStationaryTroops(int troops){
        this.stationaryTroops += troops;
    }
    public void setOccupiedBy(Player player){
        occupiedBy = player;
    }
    public void setCapital(int x, int y) {
        this.capital = new Point(x,y) ;
    }
    public Area getShape(){return area;}

    public String toString(){
        return name + " : Occupied By: " + getOccupiedBy() + ", Troops: " + getStationaryTroops();
    }

    public void addNeighbor(Territory neighbor) {
        if(neighbor!=null) {
            neighbors.add(neighbor) ;
        }
    }
    public void addPatch(Patch patch) {
        if (border == null) {
            border = new GeneralPath(new Polygon(patch.getXCoordinates(), patch.getYCoordinates(), patch.getLength()));
        }
        else {
            border.append(new Polygon(patch.getXCoordinates(), patch.getYCoordinates(), patch.getLength()), false);
        }
        border.closePath();

        if(area==null) {
            area = new Area() ;
        }
        area.add(new Area(border));
    }
}
