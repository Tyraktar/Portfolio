import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

public class CostumEventPublisher implements ApplicationEventPublisherAware {
    private ApplicationEventPublisher publisher;

    public void setApplicationEventPublisher (ApplicationEventPublisher publisher){
        this.publisher = publisher;
    }

    public void publish(){
        CostumEvent ce = new CostumEvent(this);
        publisher.publishEvent(ce);
    }
}
