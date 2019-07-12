package exercise;

import util.ADGraph;

import java.util.*;


/***
 * Bitte fuellen sie die folgenden Felder mit ihren Daten aus!
 *
 * Name:
 * Vorname:
 * Matrikelnummer:
 */
public class E2 {

    /***
     * Setzen Sie die foglenden Flags auf true wenn Sie die entsprechende Methode implementiert haben.
     */
    public static boolean DIJKSTRA_IS_IMPLEMENTED = true;
    public static boolean ASTAR_IS_IMPLEMENTED = true;

    /*
     * Implementieren Sie hier ihre Version des A* Algorithmus. G ist der Graph, s und t die IDs
     * der Knoten, zwischen denen Sie den kuerzesten s->t Pfad finden sollen. Q ist eine bereits korrekt
     * initialisiert, leere Priority Queue.
     *
     * Als Rueckgabe erwartet das Framework das Gewicht eines kuerzesten Pfades von s nach t. In path
     * speichern Sie bitte die IDs der besuchten Knoten von s nach t. Dass heisst am Ende sollte path
     * an Position Null s enthalten, gefolgt von den Knoten auf dem Weg von s nach t und auf dem letzten
     * Arrayplatz den Knoten t.
     */
    public static double astar(ADGraph G, Integer s, Integer t, ArrayList<Integer> path, MyPriorityQueue Q) {
        double shortestPath = 0;
        if(G.numVertices() > 0){
            boolean[] discovered = new boolean[G.numVertices()];
            double[] distance = new double[G.numVertices()];
            int[] priorNode = new int[G.numVertices()];
            for(int i = 0; i < discovered.length;i++){
                discovered[i] = false;
                distance[i] = Double.MAX_VALUE;
                Q.add(i,distance[i]);
            }
            distance[s] = 0.0;
            Q.decreaseKey(s,distance[s]);
            while(Q.contains(t) && !Q.isEmpty()){
                int current = Q.removeMin();
                discovered[current] = true;
                ArrayList<Integer> currentHood = G.outNeighbors(current);
                for (int neighbor : currentHood) {
                    if(!discovered[neighbor]){
                        double currentDistanceToNeighbor = distance[current]+G.weight(current,neighbor);
                        if(distance[neighbor] > currentDistanceToNeighbor){
                            distance[neighbor] = currentDistanceToNeighbor;
                            Q.decreaseKey(neighbor,currentDistanceToNeighbor);
                            priorNode[neighbor] = current;
                        }
                    }
                }
            }
            shortestPath = distance[t];

            path.add(t);
            int target = t;
            while(priorNode[target] != s){
                target = priorNode[target];
                path.add(target);
            }
            path.add(s);
        }
        return shortestPath;
    }

    /*
     * Implementieren Sie hier ihre Version von Dijkstras Algorithmus. G ist der Graph, s und t die IDs
     * der Knoten, zwischen denen Sie den kuerzesten s->t Pfad finden sollen. Q ist eine bereits korrekt
     * initialisiert, leere Priority Queue.
     *
     * Als Rueckgabe erwartet das Framework das Gewicht eines kuerzesten Pfades von s nach t. In path
     * speichern Sie bitte die IDs der besuchten Knoten von s nach t. Dass heisst am Ende sollte path
     * an Position Null s enthalten, gefolgt von den Knoten auf dem Weg von s nach t und auf dem letzten
     * Arrayplatz den Knoten t.
     */
    public static double dijkstra(ADGraph G, Integer s, Integer t, ArrayList<Integer> path, MyPriorityQueue Q) {

        double shortestPath = 0;
        if(G.numVertices() > 0){
            boolean[] discovered = new boolean[G.numVertices()];
            double[] distance = new double[G.numVertices()];
            int[] priorNode = new int[G.numVertices()];
            for(int i = 0; i < discovered.length;i++){
                discovered[i] = false;
                distance[i] = Double.MAX_VALUE;
                Q.add(i,distance[i]);
            }
            distance[s] = 0.0;
            Q.decreaseKey(s,distance[s]);
            while(Q.contains(t) && !Q.isEmpty()){
                int current = Q.removeMin();
                discovered[current] = true;
                ArrayList<Integer> currentHood = G.outNeighbors(current);
                for (int neighbor : currentHood) {
                    if(!discovered[neighbor]){
                        double currentDistanceToNeighbor = distance[current]+G.weight(current,neighbor);
                        if(distance[neighbor] > currentDistanceToNeighbor){
                            distance[neighbor] = currentDistanceToNeighbor;
                            Q.decreaseKey(neighbor,currentDistanceToNeighbor);
                            priorNode[neighbor] = current;
                        }
                    }
                }
            }
            shortestPath = distance[t];

            path.add(t);
            int target = t;
            while(priorNode[target] != s){
                target = priorNode[target];
                path.add(target);
            }
            path.add(s);
        }
        return shortestPath;
    }
}
