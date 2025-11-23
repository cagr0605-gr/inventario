package gaitan.inventario.servicio;

import gaitan.inventario.modelo.Usuario;

import java.util.List;

public interface IUsuarioServicio {
    public List<Usuario> listarUsuarios();

    public Usuario buscarUsuarioPorId(Integer idUsuario);

    public void guardarUsuario(Usuario usuario);

    public void eliminarUsuario(Usuario usuario);

    public boolean verificarCredenciales(String username, String rawPassword);


}
