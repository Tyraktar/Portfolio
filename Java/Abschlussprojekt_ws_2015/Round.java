import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;


public class Round {

    private  Player human;
    private  ArtificialIntelligenceStupid ai;
    private  World world;
    private  String phase = new String("P"); //3 Phasen: Preparation(P), Verstärkung(Supply), Eroberung(Conquer)
    private Territory choice;
    private boolean allChosen = false;

    private Territory S;
    private Territory T;
    private Territory lastAttacking;
    private Territory lastAttacked;
    private int movedTroops;

    public Round(Player human,ArtificialIntelligenceStupid ai, World world){
        this.human = human;
        this.ai = ai;
        this.world = world;
    }
    // Simuliert die Verstärkungsphase.
    public void supply(int x, int y){
         if(human.getTroops() != 0){
            Iterator<Territory> c = world.getTerritories().iterator();
            while(c.hasNext()){
                Territory cur = c.next();
                if(cur.getShape().contains(x,y)){
                    if(cur.getOccupiedBy().equals(human)){
                        human.placeTroops(cur);

                    }
                }
            }
        }

        if(human.getTroops() == 0){
            ai.addTroops(ai.receiveTroops());
            ai.placeTroops();
            phase = "C";
            JOptionPane.showMessageDialog(null,"Eroberungsphase beginnt!");
        }
    }

    //Simuliert die Eroberungsphase.
    public void conquer(int x, int y,MouseEvent e,Iterator<Territory> i) {
        Iterator<Territory> c = i;
        while (c.hasNext()) {
            Territory cur = c.next();
            if (cur.getShape().contains(x, y)) {
                if (choice == null && cur.getOccupiedBy().equals(human) && e.getButton() == MouseEvent.BUTTON1) {
                    choice = cur;
                    break;
                }else{
                    if(choice != null){
                        if(choice.equals(cur) && e.getButton() == MouseEvent.BUTTON1){
                            choice = null;
                            break;
                        }

                        if(lastAttacked != null && lastAttacking != null){
                            if (lastAttacked.equals(cur) && e.getButton() == (MouseEvent.BUTTON3) && lastAttacking.equals(choice)) {
                                human.moveTroops(choice, cur);
                                choice = null;
                                break;
                            }

                        if (S == null && T == null && cur.getOccupiedBy().equals(human) && e.getButton() == MouseEvent.BUTTON3) {
                            if (choice.getNeighbors().contains(cur)) {
                                //movedTroops = cur.getStationaryTroops();
                                human.moveTroops(choice, cur);
                                //movedTroops = cur.getStationaryTroops() - movedTroops;
                                S = choice;
                                T = cur;
                                choice = null;
                                break;
                            } else {
                                JOptionPane.showMessageDialog(null, "Sie können Truppen nur in ein benachbartes Territorium bewegen!");
                                break;
                            }

                        }

                        if ((choice.equals(T) && cur.equals(S) || choice.equals(S) && cur.equals(T)) && e.getButton() == (MouseEvent.BUTTON3)){
                            human.moveTroops(choice,cur);
                            //  T.addStationaryTroops(-movedTroops);
                          //  S.addStationaryTroops(movedTroops);
                            choice = null;
                            break;
                        }

                        if (cur.getOccupiedBy().equals(ai) && e.getButton() == (MouseEvent.BUTTON1)) {
                            movedTroops -= choice.getStationaryTroops() >= 4 ? 3 : choice.getStationaryTroops() - 1;
                            human.attack(choice, cur);

                            if (cur.getOccupiedBy().equals(human)) {
                                lastAttacked = cur;
                                lastAttacking = choice;
                                choice = null;
                                break;
                            }
                            choice = null;
                            break;
                        }
                        }
                    }
                }
            }
        }
    }

    //Wird für das Auswählen der Territories in der ersten Runde benötigt.
    //Wenn alle Territorien von einem Player bestzt werden, wird die Verstärkungsphase gestartet.
    public void chooseTerritoriesFirsRound(int x, int y,Iterator<Territory> i,Iterator<Territory> k){
        Iterator<Territory> c = i;
        allChosen = true;
        while(c.hasNext()){
            Territory cur = c.next();
            if(cur.getShape().contains(x,y) && cur.getOccupiedBy() == null){
                cur.setOccupiedBy(human);
                cur.addStationaryTroops(1);
                ai.chooseTerritoriesFirsRound(k);
            }
            if(cur.getOccupiedBy() == null){
                allChosen = false;
            }
        }
        if(allChosen){
            phase = "S";
            JOptionPane.showMessageDialog(null,"Verstärkungsphase beginnt!");
            checkContinentsOccupied();
            human.addTroops(human.receiveTroops());
        }
    }

    public void aiTurn(){
        ai.attack();
        ai.moveTroops();
    }

    private int territoryCount(){
        return world.getTerritories().size();
    }
    private int continentCount(){
        return world.getContinents().size();
    }
    public String getPhase(){return phase;}
    public void setPhase(String phase){this.phase = phase;}
    public Territory getChoice(){return choice;}

    //Überprüft, ob alle Territories besetzt sind.
    //Wenn ja, wird für jeden Continent überprüft, ob all seine Territories vom selben Player besetzt sind.
    public void checkContinentsOccupied() {
        boolean allTerritoriesOccupied = true;

        if (world.getContinents() != null) {
            for (Continent c : world.getContinents()) {
                for (Territory t : c.getTerritories()) {
                    if (t.getOccupiedBy() == null) {
                        allTerritoriesOccupied = false;
                        break;
                    }
                }
                if (!allTerritoriesOccupied) {
                    break;
                }
            }

            if (allTerritoriesOccupied) {
                for (int i = 0; i < world.getContinents().size(); i++) {
                    boolean continentOccupied = true;
                    for (int j = 1; j < world.getContinents().get(i).getTerritories().size(); j++) {
                        if (!(world.getContinents().get(i).getTerritories().get(0).getOccupiedBy().equals(world.getContinents().get(i).getTerritories().get(j).getOccupiedBy()))) {
                            continentOccupied = false;
                            break;
                        }
                    }
                    if (continentOccupied) {
                        world.getContinents().get(i).setOccupiedBy(world.getContinents().get(i).getTerritories().get(0).getOccupiedBy());
                    }
                }
            }
        }
    }
}
