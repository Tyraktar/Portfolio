package exercise;

import util.Exercise;

import java.util.ArrayList;

/***
 * Bitte fuellen sie die folgenden Felder mit ihren Daten aus!
 *
 * Name: Friedl
 * Vorname: Thomas
 * Matrikelnummer: 01427615
 */
public class E3 {
    static boolean QUICKSORT_IS_IMPLEMENTED = true;
    static boolean SAMPLESORT_IS_IMPLEMENTED = true;

    static int P = 30;
    static int K = 5;
    static int Threshold = (P*(K-1)+2);

    // Implementieren Sie hier ihre Version des Quicksort-Algorithmus
    static void quickSort(ArrayList<Integer> numbers, int low, int high) {
    if(high > 0) {
        int pivotIndex = Exercise.getRandInt(low, high);
        int pivot = numbers.get(pivotIndex);
        ArrayList<Integer> leftList = new ArrayList<Integer>();
        ArrayList<Integer> rightList = new ArrayList<Integer>();
        for (int i = low; i <= high; i++) {
            int current = numbers.get(i);
            if (i != pivotIndex) {
                if (current <= pivot) {
                    leftList.add(current);
                }
                if (current > pivot) {
                    rightList.add(current);
                }
            }
        }
        quickSort(leftList, 0, leftList.size() - 1);
        quickSort(rightList, 0, rightList.size() - 1);

        numbers.clear();

        for (int n:
             leftList) {
            numbers.add(n);
        }
        numbers.add(pivot);
        for (int n:
             rightList) {
            numbers.add(n);
        }
    }
    }

    // Implementieren Sie hier ihre Version des Samplesort-Algorithmus
    static void sampleSort(ArrayList<Integer> numbers, int k, int p) {
        if((numbers.size()/k) <= Threshold){
            quickSort(numbers,0,numbers.size()-1);
        }else{
            ArrayList<Integer> sample = new ArrayList<Integer>();
            ArrayList<ArrayList<Integer>> buckets = new ArrayList<ArrayList<Integer>>(); //Liste aus Buckets(Listen)
            for(int i = 0;i<(p*(k-1));i++){
                sample.add(numbers.get(Exercise.getRandInt(0,(numbers.size()-1))));
            }
            quickSort(sample,0,(sample.size()-1));
            ArrayList<Integer> pivots = new ArrayList<Integer>();
            pivots.add(Integer.MIN_VALUE);
            for(int i = 1;i <= k-1;i++){
                pivots.add(sample.get((p*i)-1));
            }
            pivots.add(Integer.MAX_VALUE);
            for(int i = 0; i < pivots.size();i++){
                buckets.add(new ArrayList<Integer>());
            }
            for(int i = 0;i<numbers.size();i++){
                int current = numbers.get(i);
                //Binäre Suche hier
                int leftBorder = 1;
                int rightBorder = pivots.size()-1;
                int n;

                while(leftBorder <= rightBorder){
                    n = (int) Math.floor((leftBorder+rightBorder)/2);
                   if((pivots.get(n-1) < current) && (current <= pivots.get(n))){
                        buckets.get(n).add(current);
                        break;
                    }else{
                        if(pivots.get(n) < current){
                            leftBorder = n+1;
                        }
                        if(pivots.get(n) > current){
                            rightBorder = n-1;
                        }
                    }

                }

                }
          for(int i = 0;i < buckets.size();i++){
              if(!(buckets.get(i).isEmpty())){
                  sampleSort((buckets.get(i)),k,p);
              }
          }
            numbers.clear();
            for(int i = 0;i<buckets.size();i++){
                for(int j=0;j<buckets.get(i).size();j++){
                    numbers.add(buckets.get(i).get(j));
                }
            }
        }
    }
    // Ende der Implementierung
}
