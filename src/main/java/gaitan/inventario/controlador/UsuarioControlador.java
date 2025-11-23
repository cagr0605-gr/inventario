package gaitan.inventario.controlador;

import gaitan.inventario.modelo.Usuario;
import gaitan.inventario.servicio.IUsuarioServicio;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

@Component
@Data
@ViewScoped
public class UsuarioControlador implements Serializable {

    @Autowired
    private IUsuarioServicio usuarioServicio;

    private List<Usuario> usuarios;
    private Usuario usuarioSeleccionado;

    // Campo para confirmar la nueva contraseña en el modal
    private String nuevaPasswordConfirmacion;

    @PostConstruct
    public void init() {
        this.cargarUsuarios();
        this.usuarioSeleccionado = new Usuario();
    }

    public void cargarUsuarios() {
        this.usuarios = usuarioServicio.listarUsuarios();
    }

    public void agregarUsuario() {
        this.usuarioSeleccionado = new Usuario();
        // Establecer valores por defecto (como lo tienes en tu modelo)
        this.usuarioSeleccionado.setRol("standard");
        this.usuarioSeleccionado.setActivo(true);
        this.nuevaPasswordConfirmacion = null;
    }

    public void guardarUsuario() {
        try {
            // 🔑 CORRECCIÓN CRÍTICA: La contraseña solo es obligatoria para nuevos usuarios (id == null).
            if (usuarioSeleccionado.getId() == null && (usuarioSeleccionado.getPassword() == null || usuarioSeleccionado.getPassword().isEmpty())) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "La contraseña es obligatoria para nuevos usuarios."));
                return;
            }

            String mensajeExito;

            // Si el ID es nulo, es una creación.
            if (usuarioSeleccionado.getId() == null) {
                usuarioServicio.guardarUsuario(usuarioSeleccionado);
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Usuario creado correctamente."));
            } else {
                // Es una actualización, solo se actualizarán los datos visibles (usuario, rol, activo)
                // Nota: La contraseña no se cambia a menos que se use un modal específico.
                usuarioServicio.guardarUsuario(usuarioSeleccionado);
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Usuario actualizado correctamente."));
            }

            this.cargarUsuarios();
            this.usuarioSeleccionado = new Usuario();
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Error al guardar el usuario: " + e.getMessage()));
        }
    }

    public void eliminarUsuario() {
        try {
            usuarioServicio.eliminarUsuario(usuarioSeleccionado);
            this.cargarUsuarios();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Eliminado", "Usuario eliminado correctamente."));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Error al eliminar el usuario."));
        }
    }

    public void cambiarContrasena() {
        try {
            // La validación de que las contraseñas coinciden se hará en la vista.
            if (!usuarioSeleccionado.getPassword().equals(nuevaPasswordConfirmacion)) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Las contraseñas no coinciden."));
                return;
            }

            // Actualizar solo el campo de contraseña
            usuarioServicio.guardarUsuario(usuarioSeleccionado); // Reutilizamos el método guardar
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Contraseña cambiada."));

            this.cargarUsuarios();
            this.usuarioSeleccionado = new Usuario();
            this.nuevaPasswordConfirmacion = null;

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Error al cambiar contraseña."));
        }
    }
}


