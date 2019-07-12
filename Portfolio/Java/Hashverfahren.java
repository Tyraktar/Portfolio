package exercise;

/***
 * Bitte fuellen Sie die folgenden Felder mit Ihren Daten aus.
 *
 * Name: Friedl
 * Vorname: Thomas
 * Matrikelnummer: 01427615
 */
public class E4 {

    // Implementieren Sie hier das, in der Angabe beschriebene Verfahren, zur Berechnung der Pruefnummer
    protected static String checkCode(int[] enteredCode, int v1, int v2, double c1, double c2) {
        String pruefnummer = "";
        for(int l = 0; l < 4;l++){
            int codedTable[] = new int[v1];
            for(int i = 0;i < codedTable.length;i++){
                codedTable[i] = -1;
            }
            int lastPos = 0;
            for(int j = 0; j<enteredCode.length;j++){
                int pos = 0;
                int i = 0;
                int k = enteredCode[j];
                while(true){
                    switch (l){
                        case 0 :
                            pos = ((k%v1)+i)%v1;
                        break;
                        case 1 :
                            pos = (int) ((k%v1)+(c1*i)+(c2*(i*i)))%v1;break;
                        case 2 :
                            pos = (((k%v1)+i*((k%v2)+1))%v1);break;
                        case 3 :
                            pos = (k%v1);
                            boolean once = true;
                            while(codedTable[pos] != -1){
                                int k2 = codedTable[pos];
                                int j1 = (pos+((k%v2)+1))%v1;
                                int j2 = (pos+((k2%v2)+1))%v1;
                                if((codedTable[j1] == -1) || (codedTable[j2] != -1)){
                                    pos = j1;
                                }else{
                                    codedTable[pos] = k;
                                    if(once == true){
                                        lastPos = pos;
                                        once = false;
                                    }
                                    k = k2;
                                    pos = j2;
                                }
                            }
                            codedTable[pos] = k;
                            if(once == true){
                            lastPos = pos;
                            }
                            break;
                    }
                    //Normaler Durchlauf mit i wenn es sich nicht um Brent handelt, ansonsten wird die Schleife sofort abgebrochen
                    if(l != 3){
                        if(codedTable[pos] == -1){
                            codedTable[pos] = k;
                            lastPos = pos;
                            break;
                        }else{
                            i++;
                        }
                    }else{
                     break;
                    }
                }
            }
            pruefnummer += lastPos+"";
        }
        return pruefnummer;
    }
}
