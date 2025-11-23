package gaitan.inventario.servicio;

import gaitan.inventario.modelo.Producto;
import java.util.*;

public interface IProductoServicio {
    public List<Producto> listarProductos();

    public Producto buscarProductoPorId(Integer idProducto);

    public void guardarProducto(Producto producto);

    public void eliminarProducto(Producto producto);

}
