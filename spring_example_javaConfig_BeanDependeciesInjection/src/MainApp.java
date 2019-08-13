import org.springframework.context.annotation.*;
import org.springframework.context.ApplicationContext;

public class MainApp {
    public static void main(String[] args){
        ApplicationContext context = new AnnotationConfigApplicationContext(TextEditorConfig.class);

        TextEditor txt = context.getBean(TextEditor.class);
        SpellChecker spCheck = context.getBean(SpellChecker.class);
        txt.spellCheck();
    }
}
