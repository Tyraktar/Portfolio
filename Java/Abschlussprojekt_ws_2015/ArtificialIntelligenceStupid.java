import javax.swing.*;
import java.util.Iterator;
import java.util.LinkedList;


public class ArtificialIntelligenceStupid extends Player{


    public ArtificialIntelligenceStupid(String name, World world) {
        super(name, world);
    }


    //Gibt ein zufällig gewähltes Territorium zurück, das die AI besitzt.
    public Territory chooseRandomTerritory(LinkedList<Territory> list){
        int index = (int) (Math.random() * list.size());
        return list.get(index);
    }



    //Gibt ein zufällig gewähltes Territorium zurück, das ein Nachbar des Terriroriums from ist und von einem Gegenspieler besetzt wird.
    public Territory chooseDefendingTerritory(Territory from){

        LinkedList<Territory> possibleDefTer = new LinkedList<>();

        for (int i = 0; i < from.getNeighbors().size(); i++){
            if (!(this.equals(from.getNeighbors().get(i).getOccupiedBy()))){
                possibleDefTer.add(from.getNeighbors().get(i));
            }
        }

        return chooseRandomTerritory(possibleDefTer);
    }

    //Gibt ein zufällig gewähltes Territorium zur�ük, das ein gegnerisches Territorium als Nachbarn hat.
    public Territory chooseAttackingTerritory(){

        LinkedList <Territory> possibleAttackingTerritories = new LinkedList<>();

        for (Territory currT : getTerritories()){
            if (currT.getStationaryTroops() > 1){
                for (Territory neighbor : currT.getNeighbors()){
                    if (!this.equals(neighbor.getOccupiedBy())){
                        possibleAttackingTerritories.add(currT);
                        break;
                    }
                }
            }
        }
        if (possibleAttackingTerritories.isEmpty()){
            return null;
        }

       return chooseRandomTerritory(possibleAttackingTerritories);
    }

    public void attack(){
        Territory from = chooseAttackingTerritory();
        if (from == null){
            return;
        }
        Territory to = chooseDefendingTerritory(from);

        JOptionPane.showMessageDialog(null, getName() + " greift von " + from.getName() + " nach " + to.getName() + " an.");

        int attackingTroops = fightTroopNum(from, true);
        int defendingTroops = fightTroopNum(to,false);

        while (from.getStationaryTroops() > 1 && to.getStationaryTroops() > 0){

            int[] attackersDice = diceResult(attackingTroops);
            int[] defendersDice = diceResult(defendingTroops);

            for (int i = 0;i < Math.min(attackersDice.length, defendersDice.length); i++){
                if (attackersDice[i] > defendersDice[i]){
                    to.addStationaryTroops(-1);
                }
                else {
                    from.addStationaryTroops(-1);
                    attackingTroops--;
                }
            }
        }

        if (to.getStationaryTroops() == 0){
            JOptionPane.showMessageDialog(null, getName() + " hat " + to.getName() + " eingenommen.");
            to.setOccupiedBy(this);
            to.addStationaryTroops(attackingTroops);
            from.addStationaryTroops(-attackingTroops);
        }
        else {
            JOptionPane.showMessageDialog(null, to.getName() + " hat den Angriff erfolgreich abgewehrt.");
        }
    }

    //Gibt ein zufällig gewähltes Territorium zurück, das ein von der AI besetztes Nachbarterritorium hat.
    public Territory chooseMovingTerritory(){

        LinkedList<Territory> possibleTerritories = new LinkedList<>();

        for (int i = 0; i < getTerritories().size(); i++){
            for (int j = 0; j < getTerritories().get(i).getNeighbors().size(); j++){
                if (getTerritories().get(i).getNeighbors().get(j).getOccupiedBy().equals(this) && getTerritories().get(i).getStationaryTroops() > 1){
                    possibleTerritories.add(getTerritories().get(i));
                    break;
                }
            }
        }
        if (possibleTerritories.isEmpty()){
            return null;
        }
        return chooseRandomTerritory(possibleTerritories);
    }

    //Bewegt eine zufällige Anzahl an Truppen aus einem zufällig gewählten, eigenen Territorium in ein zufällig gewähltes,eigenes Nachbarterritorium.
    public void moveTroops(){

        Territory from = chooseMovingTerritory();

        if (from == null){
            return;
        }

        LinkedList<Territory> possibleTerritories = new LinkedList<>();

        for (Territory neighbor : from.getNeighbors()){
            if (neighbor.getOccupiedBy().equals(this)){
                possibleTerritories.add(neighbor);
            }
        }
        Territory to = chooseRandomTerritory(possibleTerritories);
        int troopsToMove = (int) (Math.random() * from.getStationaryTroops() - 1) + 1;

        if(troopsToMove < 1){
            troopsToMove = 1;
        }

        from.addStationaryTroops(-troopsToMove);
        to.addStationaryTroops(troopsToMove);

        JOptionPane.showMessageDialog(null, getName() + " bewegt " + troopsToMove + " Truppen von " + from.getName() + " nach " + to.getName() + ".");
    }

    //Platziert die troops in zufällig gewählten, eigenen Territories.
    public void placeTroops(){

        while (getTroops() > 0){
            int troopsToPlace = (int)(Math.random() * (getTroops() - 1) + 1);
            Territory random = chooseRandomTerritory(getTerritories());
            random.addStationaryTroops(troopsToPlace);
            addTroops(-troopsToPlace);
            JOptionPane.showMessageDialog(null, getName() + " platziert "+ troopsToPlace + " Truppen in " + random.getName()+ ".");
        }
    }
    //Für die erste Runde. Wählt Territorymit occupiedBy = null zufällig aus und setzt occypiedBy = this;
    public void chooseTerritoriesFirsRound(Iterator<Territory> i){
        LinkedList<Territory> availableTerritories = new LinkedList<>();
        Iterator<Territory> m = i;
        Territory cur;
        while(m.hasNext()){
            cur = m.next();
            System.out.println(cur);
            if(cur.getOccupiedBy() == null){
                availableTerritories.add(cur);
            }
        }
        if(availableTerritories.size() != 0){
            Territory random = chooseRandomTerritory(availableTerritories);
            random.setOccupiedBy(this);
            random.addStationaryTroops(1);
        }
    }
}
