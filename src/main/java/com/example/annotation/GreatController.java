public class GreatController {

    @UrlMethod(path = "/hello")
    public void sayHello() {
        System.out.println("Hello!");
    }

    @UrlMethod(path = "/bye")
    public void sayBye() {
        System.out.println("Bye!");
    }

    public void noAnnotation() {
        System.out.println("Not annotated");
    }
}