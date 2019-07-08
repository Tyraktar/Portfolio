import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.BeansException;

public class InitHelloWorld implements BeanPostProcessor{
    public Object postProcessBeforeInitialization(Object bean, String beanName)
        throws BeansException{
        System.out.println("Bean "+beanName+" gets initialized");
        return bean;
    }

    public Object postProcessAfterInitialization(Object bean, String beanName)
        throws BeansException{
        System.out.println("Bean "+beanName+" is initialized");
        return bean;
    }
}
