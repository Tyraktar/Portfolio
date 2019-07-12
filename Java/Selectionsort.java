package exercise;

/***
 * Bitte fuellen sie die folgenden Felder mit ihren Daten aus!
 *
 * Name: Friedl
 * Vorname: Thomas
 * Matrikelnummer: 01427615
 */
public class E1 {
    // Implementieren Sie hier ihre Version des Algorithmus Selectionsort
    static void sort(Integer[] numbers) {
        if(numbers.length != 0){
            Integer smallest;
            for(int i=0;i<numbers.length-1;i++){
                smallest = i;
                for(int j=i+1;j<numbers.length;j++){
                    if(numbers[j] < numbers[smallest]){
                        smallest = j;
                    }
                }
                Integer zs = numbers[i];
                numbers[i] = numbers[smallest];
                numbers[smallest] = zs;
            }
        }
    }


}
