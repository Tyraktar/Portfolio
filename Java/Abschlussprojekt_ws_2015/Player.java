
import javax.swing.*;
import java.util.LinkedList;

public class Player {

    private final String name;
    //Troops stellt die Verstärkungen dar, die man zu Beginn der Runde bekommt und sollte nach der Verstärkungsphase immer 0 sein.
    private int troops = 0;
    private World world;


    public Player(String name, World world){
        this.name = name;
        this.world = world;
    }
    public String getName(){
        return name;
    }
    public int getTroops(){
        return troops;
    }
    public boolean equals(Object other) {
        return !(other == null || !(other instanceof Player)) && this.name.equals(((Player) other).name);
    }
    public void addTroops(int troops){
        this.troops += troops;
    }
    public String toString(){
        return name;
    }
    public World getWorld(){
        return world;
    }
    //-------------------------------------------------------------------------------------------------------
    // Gibt die Anzahl an Truppen zurück, die der Spieler als Verstärkung bekommt;
    public int receiveTroops(){
        int count = territoryCount() / 3;

        if(world.getContinents() != null){
            for (Continent c : world.getContinents()){
                if (c.getOccupiedBy() != null && this.equals(c.getOccupiedBy())){
                    count += c.getBonus();
                }
        }
        }
        JOptionPane.showMessageDialog(null, name + " erhält " + count + " Verstärkungen.");
        return count >= 3 ? count : 1;
    }
    //Gibt die Anzahl an Truppen zurück, die sich gerade im Kampf befinden;
    public int fightTroopNum(Territory territory, boolean attacker){

        if (attacker){

            return territory.getStationaryTroops() >= 4 ? 3 : territory.getStationaryTroops() -1;

        }
        else {
            return territory.getStationaryTroops() >= 2 ? 2 : 1;
        }
    }
    //Gibt ein Array mit Würfelergebnissen zurück, wobei eine höhere Augenzahl weiter vorne im Array steht;
    public int[] diceResult(int troopNum){

        int[] current = new int[troopNum];
        for (int i = 0; i < current.length; i++){
            current[i] = rollDice();
        }

        for (int i = 0; i < troopNum - 1; i++){
            int tmp = Math.max(current[i], current[i + 1]);
            if (current[i] != tmp){
                current[i + 1] = current[i];
                current[i] = tmp;
            }
        }

        int[] result = new int[troopNum];
        for (int i = 0; i < result.length; i++){
            result[i] = current[i];
        }
        return result;
    }

    public int rollDice(){
        return (int)((Math.random() * 6) + 1);
    }

    // Simuliert einen Angriff.
    public void attack(Territory from, Territory to){

        if(from.getStationaryTroops() != 1){

        if (from.getOccupiedBy().equals(this) && !(to.getOccupiedBy().equals(this)) && from.getStationaryTroops() >= 1 && from.getNeighbors().contains(to)) {

            int attackingTroops = fightTroopNum(from, true);
            int defendingTroops = fightTroopNum(to,false);

            while (from.getStationaryTroops() > 1 && to.getStationaryTroops() > 0){


                int[] attackersDice = diceResult(attackingTroops);
                int[] defendersDice = diceResult(defendingTroops);

                for (int i = 0; i < Math.min(attackersDice.length, defendersDice.length); i++){
                    if (attackersDice[i] > defendersDice[i]){
                        to.addStationaryTroops(-1);
                    }
                    else {
                        from.addStationaryTroops(-1);
                        attackingTroops --;
                    }
                }
            }

            if (to.getStationaryTroops() == 0){
                JOptionPane.showMessageDialog(null, "Sie haben " + to.getName() + " eingenommen.");
                to.setOccupiedBy(this);
                to.addStationaryTroops(attackingTroops);
                from.addStationaryTroops(-attackingTroops);
            }
            else {
                JOptionPane.showMessageDialog(null, to.getName() + " hat Ihren Angriff erfolgreich abgewehrt.");
            }
        }else if (!(from.getNeighbors().contains(to))){
            JOptionPane.showMessageDialog(null, "Es können nur benachbarte Territorien angegriffen werden.");
        }

        }else{
            JOptionPane.showMessageDialog(null, "Nicht genügend Truppen für einen Angriff vorhanden.");
        }
    }

    //Subtrahiert eine Truppe aus Territory from und addiert sie zu Territory to;

    public void moveTroops(Territory from, Territory to){

       if (from.getOccupiedBy().equals(this) && to.getOccupiedBy().equals(this) && from.getNeighbors().contains(to)){
            if (from.getStationaryTroops() > 1){
             /*   int troopsToMove = Integer.parseInt(JOptionPane.showInputDialog(
                        "Sie haben " + (from.getStationaryTroops() - 1) +
                        " Truppen zur Verfügung.Wie viele Truppen sollen von " + from.getName() + " nach " + to.getName() + " bewegt werden?"));
                if(troopsToMove >= from.getStationaryTroops()){
                    JOptionPane.showMessageDialog(null, "Nicht genügend Truppen vorhanden!");
                    moveTroops(from,to);
                }
                else {
                    from.addStationaryTroops(-troopsToMove);
                    to.addStationaryTroops(troopsToMove);
                }
            }
            else {
                JOptionPane.showMessageDialog(null, "Nicht genügend Truppen vorhanden!");*/
                from.addStationaryTroops(-1);
                to.addStationaryTroops(1);
            }
        }
    }

    //Subtrahiert eine Truppe von troops und addiert sie zum ausgewählten Territory;
    public void placeTroops(Territory territory){

       /* if (territory.getOccupiedBy().equals(this)){
            int troopsToPlace = Integer.parseInt(JOptionPane.showInputDialog("Sie haben " + getTroops() + " Truppen zur Verfügung. Wie viele Truppen sollen platziert werden?"));
            if (troopsToPlace > getTroops()){
                JOptionPane.showMessageDialog(null, "Zahl zu groß!");
                placeTroops(territory);
            }
            else {
                territory.addStationaryTroops(troopsToPlace);
                addTroops(-troopsToPlace);
            }
        } */
        territory.addStationaryTroops(1);
        addTroops(-1);
    }

    //Liefert eine LinkedList mit den eigenen Territories zurück;
    public LinkedList<Territory> getTerritories(){
        LinkedList<Territory> ownTerritories = new LinkedList<>();
        for (Territory t : world.getTerritories()){

            if (t.getOccupiedBy() != null && t.getOccupiedBy().equals(this)){
                ownTerritories.add(t);
            }
        }
        return ownTerritories;
    }

    public LinkedList<Continent> getContinents(){
        LinkedList<Continent> ownContinents = new LinkedList<>();
        for (Continent c : world.getContinents()){
            if (this.equals(c.getOccupiedBy())){
                ownContinents.add(c);
            }
        }
        return ownContinents;
    }

    public int territoryCount(){
        return getTerritories().size();
    }
}
