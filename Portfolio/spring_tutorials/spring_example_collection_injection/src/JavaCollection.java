import java.util.*;

public class JavaCollection {
    List addressList;
    Set addressSet;
    Map addressMap;
    Properties addressProp;

    public void setAddressList(List addressList){
        this.addressList = addressList;
    }

    public void getAddressList(){
        System.out.println("List Elements: "+addressList);
    }

    public void setAddressSet(Set addressSet){
        this.addressSet = addressSet;
    }

    public void getAddressSet() {
        System.out.println("Set Elements: "+addressSet);
    }

    public void setAddressMap(Map addressMap){
        this.addressMap = addressMap;
    }

    public void getAddressMap(){
        System.out.println("Map Elements: "+addressMap);
    }

    public void setAddressProp(Properties addressProp){
        this.addressProp = addressProp;
    }

    public void getAdressProp(){
        System.out.println("Properties Elements: "+addressProp);
    }
}
