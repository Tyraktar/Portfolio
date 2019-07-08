package exercise;

import util.ADGraph;

import java.util.ArrayList;

/***
 * Bitte fuellen Sie die folgenden Felder mit Ihren Daten aus.
 *
 * Name: Friedl
 * Vorname: Thomas
 * Matrikelnummer: 01427615
 */
public class E5 {

    /*
     * Implementieren Sie hier ihre Methode zum loesen des k-Faerbungsproblems.
     */
    static Integer kColoring(ADGraph<ADGraph.Vertex, ADGraph.Edge> G, Integer[] colors, ColoringAlgorithm solver) {

        //BinarySearch für Färbung k
        int leftBorder = 1;
        int rightBorder = G.numVertices();
        int k = 0;
        int kBest = 0;
        Integer[] tempColors;

        while(leftBorder <= rightBorder){
            k = leftBorder+((rightBorder-leftBorder)/2);
            String code = "";

            //Berechne Anzahl Klauseln für jede Kante mithilfe Tiefensuche
            int numEdges = 0;
            ArrayList<Integer> discovered = new ArrayList<Integer>();
            String edgeFormeln = "";
            for(int j = 0; j < G.numVertices();j++){
                discovered.add(G.getVertex(j).getM_id());
                for (Integer neighbor: G.outNeighbors(j)) {
                    if(!discovered.contains(neighbor)){
                        numEdges++;
                        for(int i = 1;i <= k;i++){
                            edgeFormeln += (-(i+(j*k)))+" "+(-(i+(neighbor*k)))+" 0\n";
                        }
                    }
                }
            }

            String formel = "p cnf "+(G.numVertices()*k)+" "+(G.numVertices()+(numEdges*k))+"\n";
            for(int j = 0;j < G.numVertices();j++){
                for(int i = 1; i <= k;i++) { //Achtung Grenzenverschiebung!
                    formel += (i+(j*k))+" ";
                }
                formel += "0\n";
            }
            formel += edgeFormeln;
            //Überprüfe ob gewähltes k färbbar, formuliere SAT-Formel

            code = solver.solve(k,formel);
            if(code != ""){
                String variable = "";
                for(int i=0;i<code.length();i++){
                    int belegung = 0;
                    switch (code.charAt(i)){
                        case '-' : variable += "-";break;
                        case ' ' :
                                belegung = Integer.parseInt(variable);
                                variable = "";
                                break;
                        default : variable += code.charAt(i)+"";
                    }
                    if(belegung > 0){
                        colors[Math.floorDiv(Math.abs((belegung-1)),k)] = belegung%k;
                    }
                }
                kBest = k;
                if(leftBorder == rightBorder){
                    return kBest;
                }else{
                    rightBorder = k-1;
                }
            }else{ //k ist nicht färbbar, verschiebe Grenzen und versuche in nächster Hälfte
                    leftBorder = k+1; //probiere rechte Hälfte
                }
            }
        return kBest;
    }


}
