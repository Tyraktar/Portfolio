public class HelloWorld {
    private String message;

    public void setMessage(String message){
        this.message = message;
    }

    public  void getMessage(){
        System.out.println("Your Message: "+message);
    }

    public void init(){
        this.setMessage("Initiated");
        this.getMessage();
    }

    public void destruct(){
        System.out.println("Destroyed");
    }
}
