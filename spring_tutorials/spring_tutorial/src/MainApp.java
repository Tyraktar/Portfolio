import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MainApp {
    public  static void main(String[] args){
       ApplicationContext context = new ClassPathXmlApplicationContext("Beans.xml");

        HelloAustria obj2 = (HelloAustria) context.getBean("helloAustria");
        obj2.getMessage1();
        obj2.getMessage2();
        obj2.getMessage3();
    }
}
