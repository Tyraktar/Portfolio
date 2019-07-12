import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;

public class World{

    private HashSet<Territory> territories;
    private LinkedList<Continent> continents;
    private boolean exists = false;


    public World(){
        territories = new HashSet<>();
        continents = new LinkedList<>();
    }

    public HashSet<Territory> getTerritories(){
        return territories;
    }
    public LinkedList<Continent> getContinents(){
        return continents;
    }

    public void readMap(String directory) throws IOException {
        try {
            if(continents != null) {
                continents = null;
            }

            BufferedReader in = new BufferedReader(new FileReader(directory));

            String line;
            while((line = in.readLine()) != null) {
                if(line.contains("patch-of")) {
                    exists = false;
                    if(territories == null) {
                        territories = new HashSet<Territory>();
                    }
                    line = line.replace("patch-of ", "") ;
                    String territoryName = line.replaceAll("[0-9]+", "").trim() ;

                    //Territory territory = null;
                    for(Territory t : territories) {
                        if(t.getName().equals(territoryName)){
                            t.addPatch(new Patch(line.replaceAll("[A-Za-z]+", "").trim().split(" ")));
                            exists = true;
                            break;
                        }
                    }
                    if(exists == false){
                        Territory territory = new Territory(territoryName, this);
                        territory.addPatch(new Patch(line.replaceAll("[A-Za-z]+", "").trim().split(" ")));
                        territories.add(territory);
                    }
                }
                else if(line.contains("capital-of")) {
                    line = line.replace("capital-of", "") ;
                    for (Territory t : territories){
                        if(t.getName().equals(line.replaceAll("[0-9]+" , "").trim())){

                            t.setCapital(
                                    Integer.parseInt(line.replaceAll("[A-Za-z]+", "").trim().split(" ")[0]),
                                    Integer.parseInt(line.replaceAll("[A-Za-z]+", "").trim().split(" ")[1])
                            );
                            System.out.println((t.getCapital().getX()+" "+t.getCapital().getY()));
                        }
                    }
                }
                else if(line.contains("neighbors-of")) {
                    line = line.replace("neighbors-of ", "") ;
                    for(Territory t : territories){
                        if(t.getName().equals(line.split(" : ")[0])){
                            String[] neighbors = line.split(" : ")[1].split(" - ");
                            for(String s : neighbors){
                                for(Territory neighbor : territories){
                                    if(s.equals(neighbor.getName())){
                                        t.addNeighbor(neighbor);
                                        neighbor.addNeighbor(t);
                                    }
                                }
                            }
                            break ;
                        }
                    }
                }
                else if(line.contains("continent")) {
                    if (continents == null) {
                        continents = new LinkedList<Continent>();
                    }

                    line = line.replace("continent ", "");

                    Continent c = new Continent(line.split(" : ")[0].substring(0,line.split(" : ")[0].indexOf(" ")));
                    c.setBonus(Integer.parseInt(line.split(" : ")[0].replaceAll("[^0-9]+", "").trim()));

                    String[] continentMembersArray = line.split(" : ")[1].split(" - ");

                    for(String s : continentMembersArray) {
                        for(Territory t : territories) {
                            if(t.getName().equals(s)) {
                                c.addTerritory(t) ;
                            }
                        }
                    }
                    continents.add(c);
                }
            }
            in.close() ;
        }
        catch (Exception e) {
        }
    }

    public String toString(){
        String result = "Territories:" + "\n";
        int line = 1;


        for (Territory t : territories){
            result += line + ". " + t + "\n";
            line++;
        }
        line = 1;
        result += "\n" + "Continents:" + "\n";
        for (Continent c : continents){
            result += line + ". " + c + "\n";
            line++;
        }

        return result;
    }
}

