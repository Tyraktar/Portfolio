import org.springframework.context.ApplicationListener;

public class CostumEventHandler implements ApplicationListener<CostumEvent>{
    public void onApplicationEvent(CostumEvent event){
        System.out.println(event.toString());
    }
}
