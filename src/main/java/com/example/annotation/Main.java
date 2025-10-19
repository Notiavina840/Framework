import java.lang.reflect.Method;

public class Main {
    public static void main(String[] args) {
        Class<GreatController> controllerClass = GreatController.class;
        
        System.out.println("Méthodes annotées avec @UrlMethod :");
        
        for (Method method : controllerClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(UrlMethod.class)) {
                UrlMethod annotation = method.getAnnotation(UrlMethod.class);
                System.out.println("→ Méthode : " + method.getName() +
                                   ", chemin : " + annotation.path());
            }
        }
    }
}