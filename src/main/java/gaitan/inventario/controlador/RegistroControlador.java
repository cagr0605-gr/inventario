package gaitan.inventario.controlador;

import gaitan.inventario.modelo.Usuario;
import gaitan.inventario.servicio.UsuarioServicio;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
@Data
@ViewScoped
public class RegistroControlador {


    @Autowired
    UsuarioServicio usuarioServicio;
    private Usuario usuario = new Usuario();
    private static final Logger logger = LoggerFactory.getLogger(IndexControlador.class);

    public void registrar() {
        logger.info("Usuario registrado correctamente: " + this.usuario);
        usuarioServicio.guardarUsuario(usuario);

        // 🚀 CAMBIO AQUÍ: Usar SEVERITY_INFO para un mensaje de éxito
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Registro Exitoso", "¡La cuenta ha sido creada correctamente!"));

        // Reiniciar el objeto para limpiar los campos del formulario
        usuario = new Usuario();
    }
}
