import org.springframework.context.ApplicationEvent;

public class CostumEvent extends ApplicationEvent{
    public CostumEvent(Object src){
        super(src);
    }
    public String toString(){
        return "My Costum Event";
    }
}
