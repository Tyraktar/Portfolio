import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.ApplicationContext;

public class MainApp {
    public static void main(String[] args){
        ApplicationContext context = new ClassPathXmlApplicationContext("Beans.xml");
        JavaCollection jC = (JavaCollection) context.getBean("javaCollection");

        jC.getAddressList();
        jC.getAddressSet();
        jC.getAddressMap();
        jC.getAdressProp();
    }
}
