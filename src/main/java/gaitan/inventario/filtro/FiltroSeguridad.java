package gaitan.inventario.filtro;

import gaitan.inventario.controlador.SesionUsuario;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import java.io.IOException;
import java.util.logging.LogRecord;
@WebFilter(urlPatterns = {"/index.xhtml", "/inventario.xhtml", "/registro.xhtml"}) // 🚀 Mapea las URL que quieres proteger
public class FiltroSeguridad implements Filter {

    private static final String LOGIN_PAGE = "/login.xhtml";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        // Obtener el bean de SesionUsuario de Spring/JSF
        WebApplicationContext springContext =
                WebApplicationContextUtils.getWebApplicationContext(req.getServletContext());

        SesionUsuario sesionUsuario =
                (SesionUsuario) springContext.getBean("sesionUsuario");

        String path = req.getRequestURI().substring(req.getContextPath().length());

        // 1. Permitir acceso a recursos públicos (CSS, JS, imágenes, etc.)
        if (path.startsWith("/javax.faces.resource/")) {
            chain.doFilter(request, response);
            return;
        }

        // 2. Permitir acceso a la página de login y registro
        if (path.equals(LOGIN_PAGE) || path.equals("/registro.xhtml")) {
            chain.doFilter(request, response);
            return;
        }

        // 3. Validar Sesión
        if (sesionUsuario != null && sesionUsuario.isLogueado()) {
            // Si está logueado, permitir el acceso a la página solicitada
            chain.doFilter(request, response);
        } else {
            // Si no está logueado, redirigir al login
            res.sendRedirect(req.getContextPath() + LOGIN_PAGE);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}
}
