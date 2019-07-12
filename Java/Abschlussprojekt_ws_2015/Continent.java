import java.util.LinkedList;

public class Continent {

    private final String name;
    private LinkedList<Territory> territories;
    private int bonus;
    private Player occupiedBy;

    public Continent(String name){
        this.name = name;
        occupiedBy = null;
    }

    public String getName(){
        return name;
    }
    public LinkedList<Territory> getTerritories(){
        return territories;
    }
    public int getBonus(){
        return bonus;
    }
    public Player getOccupiedBy(){
        return occupiedBy;
    }
    public void setOccupiedBy(Player player){
        occupiedBy = player;
    }
    public void setBonus(int bonus){
        this.bonus = bonus;
    }
    public String toString(){
        String result = name + "\n";

        for (Territory t : territories){
            result += t;
        }
        return result;
    }

    public void addTerritory(Territory territory){
        if(territories == null){
            territories = new LinkedList<>();
        }
        territories.add(territory);
    }

    //Überprüft, ob der Besetzer des ersten Territoriums in territories derselbe ist, wie der der anderen Territorien.
    //Falls ja wird true zurückgegeben und occupiedBy mit dem Player initialisiert, der das erste Territorium in territories besetzt,
    // und der Kontinents wird in continets von Player aufgenommen.
    // Sonst wird false zurückgegeben.
    public boolean isOccupied(){
        boolean continentOccupied = true;

        for (int i = 1; i < territories.size(); i++){
            if (!(territories.peek().getOccupiedBy().equals(territories.get(i).getOccupiedBy()))){
                continentOccupied = false;
            }
        }
        if (continentOccupied){
            setOccupiedBy(territories.peek().getOccupiedBy());
           //occupiedBy.getContinents().add(this);
        }
        return continentOccupied;
    }
}
