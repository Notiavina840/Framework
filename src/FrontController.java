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
            throws IOException, ServletException {
        
        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();
        String servletPath = request.getServletPath();
        
        String pathInfo = uri.substring(contextPath.length() + servletPath.length());
        
        if (pathInfo.isEmpty() || "/".equals(pathInfo)) {
            showDefaultPage(request, response);
            return;
        }
        
        if (pathInfo.startsWith("/")) {
            pathInfo = pathInfo.substring(1);
        }
        
        if (isStaticFile(pathInfo)) {
            serveStaticFile(pathInfo, request, response);
        } else {
            serveController(pathInfo, request, response);
        }
    }
    
    private boolean isStaticFile(String path) {
        return path.endsWith(".jsp") || path.endsWith(".html") || path.endsWith(".css") || 
               path.endsWith(".js") || path.endsWith(".jpg") || path.endsWith(".png");
    }
    
    private void serveStaticFile(String filename, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String filePath = "/" + filename;
        RequestDispatcher dispatcher = request.getRequestDispatcher(filePath);
        if (dispatcher != null) {
            dispatcher.forward(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "fichier non trouve: " + filename);
        }
    }
    
    private void serveController(String controllerName, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        out.println("<html>");
        out.println("<head><title>" + controllerName + "</title></head>");
        out.println("<body>");
        out.println("<h1>controller: " + controllerName + "</h1>");
        
        try {
            Class<?> cls = Class.forName("app.controllers." + controllerName + "Controller");
            Object obj = cls.getDeclaredConstructor().newInstance();
            String result = (String) cls.getMethod("execute", HttpServletRequest.class).invoke(obj, request);
            out.println("<p>ok</p>");
            out.println("<p>" + result + "</p>");
        } catch (ClassNotFoundException e) {
            out.println("<p>controller non trouve: " + controllerName + "Controller</p>");
            out.println("<p>" + e.getMessage() + "</p>");
        } catch (Exception e) {
            out.println("<p>erreur exec</p>");
            out.println("<p>" + e.getMessage() + "</p>");
        }
        
        out.println("<hr>");
        out.println("<a href='" + request.getContextPath() + request.getServletPath() + "/'>accueil</a>");
        out.println("</body>");
        out.println("</html>");
    }
    
    private void showDefaultPage(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        out.println("<html>");
        out.println("<head><title>accueil</title></head>");
        out.println("<body>");
        out.println("<h1>accueil</h1>");
        
        out.println("<h2>controllers:</h2>");
        out.println("<ul>");
        out.println("<li><a href='aaa'>aaa</a></li>");
        out.println("<li><a href='bbb'>bbb</a></li>");
        out.println("</ul>");
        
        
        out.println("</body>");
        out.println("</html>");
    }
}
