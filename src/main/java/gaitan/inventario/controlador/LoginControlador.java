package gaitan.inventario.controlador;

import gaitan.inventario.modelo.Usuario;
import gaitan.inventario.servicio.UsuarioServicio;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Data
@ViewScoped
public class LoginControlador {

    @Autowired
    UsuarioServicio usuarioServicio;

    @Autowired
    SesionUsuario sesionUsuario;

    // Objeto Usuario para capturar las credenciales del formulario
    private Usuario usuario = new Usuario();

    /**
     * Intenta iniciar sesión y redirige a index.xhtml si es exitoso.
     * @return String de navegación (redirección)
     */
    public String iniciarSesion() {
        // 1. Lógica de autenticación (debes implementarla en tu servicio)
        Usuario usuarioAutenticado = usuarioServicio.autenticarUsuario(
                this.usuario.getUsuario(),
                this.usuario.getPassword()
        );

        if (usuarioAutenticado != null) {
            // 2. ÉXITO: Establecer el estado de la sesión
            sesionUsuario.setUsuarioLogueado(usuarioAutenticado);
            sesionUsuario.setLogueado(true);

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Bienvenido", "Sesión iniciada como " + usuarioAutenticado.getRol()));

            // Redirigir al index
            return "/index.xhtml?faces-redirect=true";

        } else {
            // FALLO
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de Acceso", "Usuario o contraseña incorrectos."));
            return null;
        }
    }

}
