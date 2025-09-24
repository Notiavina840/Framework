import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.*;
import java.util.*;

public class FrontController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String uri = request.getRequestURI(); // ex: /test/aaa
        String context = request.getContextPath(); // ex: /MyWebApp
        String path = uri.substring(context.length() + "/test/".length()); // ici: "aaa"

        try {
            // Charger dynamiquement la classe depuis ton JAR
            Class<?> cls = Class.forName("app.controllers." + path + "Controller");
            Object obj = cls.getDeclaredConstructor().newInstance();

            // Appeler une méthode standard "execute"
            String result = (String) cls.getMethod("execute", HttpServletRequest.class)
                                       .invoke(obj, request);

            // Afficher la réponse
            response.setContentType("text/html");
            response.getWriter().write(result);

        } catch (ClassNotFoundException e) {
            response.getWriter().write("Page non trouvée pour " + path);
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("Erreur serveur : " + e.getMessage());
        }
    }
}
