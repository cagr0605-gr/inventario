package gaitan.inventario.controlador;

import gaitan.inventario.modelo.Usuario;
import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.io.Serializable;

@Component
@Data
@SessionScope
public class SesionUsuario implements Serializable {

    private static final long serialVersionUID = 1L;

    private Usuario usuarioLogueado;
    private boolean logueado = false; // Estado de la sesión

    /**
     * @return true si el usuario logueado tiene el rol "ADMIN".
     */
    public boolean esAdmin() {
        return logueado && usuarioLogueado != null && "admin".equals(usuarioLogueado.getRol());
    }

    /**
     * Cierra la sesión, limpia los datos y prepara la redirección.
     */
    public String cerrarSesion() {
        this.logueado = false;
        this.usuarioLogueado = null;
        // Redirige al login.
        return "/login.xhtml?faces-redirect=true";
    }

}
