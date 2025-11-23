package gaitan.inventario.servicio;

import gaitan.inventario.modelo.Usuario;
import gaitan.inventario.repositorio.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServicio implements IUsuarioServicio{

    @Autowired
    UsuarioRepositorio usuarioRepositorio;

    @Override
    public List<Usuario> listarUsuarios() {
        return usuarioRepositorio.findAll();
    }

    @Override
    public Usuario buscarUsuarioPorId(Integer idUsuario) {
        Usuario usuario = usuarioRepositorio.findById(idUsuario).orElse(null);
        return usuario;
    }

    @Override
    public void guardarUsuario(Usuario usuario) {
        usuarioRepositorio.save(usuario);
    }

    @Override
    public void eliminarUsuario(Usuario usuario) {
        usuarioRepositorio.delete(usuario);
    }

    @Override
    public boolean verificarCredenciales(String username, String rawPassword) {
        // Busca el usuario por el username
        Optional<Usuario> usuarioOpt = usuarioRepositorio.findByUsuario(username);

        if (usuarioOpt.isEmpty()) {
            return false; // Usuario no encontrado
        }

        Usuario usuarioDB = usuarioOpt.get();
        String storedPassword = usuarioDB.getPassword();

        // Compara contraseñas en texto plano (NO SEGURO)
        return storedPassword.equals(rawPassword);
    }

    public Usuario autenticarUsuario(String username, String rawPassword) {

        Optional<Usuario> usuarioOpt = usuarioRepositorio.findByUsuario(username);

        if (usuarioOpt.isEmpty()) {
            return null; // Usuario no encontrado
        }

        Usuario usuarioDB = usuarioOpt.get();
        String storedPassword = usuarioDB.getPassword();

        // Comparación de texto plano (como lo tienes actualmente)
        if (storedPassword.equals(rawPassword)) {
            return usuarioDB; // Retorna el objeto completo si las credenciales coinciden
        } else {
            return null; // Contraseña incorrecta
        }
    }

}
