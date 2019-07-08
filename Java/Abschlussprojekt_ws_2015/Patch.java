
public class Patch {

    private int[] xCoordinates;
    private int[] yCoordinates;
    private  int length;



    public int[] getXCoordinates(){
        return xCoordinates;
    }
    public int[] getYCoordinates(){
        return yCoordinates;
    }
    public int getLength() {
        return length ;
    }

    public Patch(String[] patchBorder) {
        if(patchBorder.length%2==0) {
            length = patchBorder.length / 2;
            xCoordinates = new int[length];
            yCoordinates = new int[length];

            for (int i = 0, j = 0; i < patchBorder.length; i += 2, j++) {
                xCoordinates[j] = Integer.parseInt(patchBorder[i]);
                yCoordinates[j] = Integer.parseInt(patchBorder[i + 1]);
            }
        }
    }
}
