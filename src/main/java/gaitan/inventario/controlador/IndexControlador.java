package gaitan.inventario.controlador;

import gaitan.inventario.modelo.Producto;
import gaitan.inventario.servicio.ProductoServicio;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import lombok.Data;
import org.primefaces.PrimeFaces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component // para que pueda ser usada desde la fabrica de spring
@Data
@ViewScoped
public class IndexControlador {
    @Autowired
    ProductoServicio productoServicio;
    private List<Producto> productos;
    private Producto productoSeleccionado;
    private static final Logger logger = LoggerFactory.getLogger(IndexControlador.class);

    @PostConstruct
    public void init(){
        cargarDatos();
    }

    public void cargarDatos(){
        this.productos = productoServicio.listarProductos();
        productos.forEach((producto -> logger.info(producto.toString())));
    }

    public void agregarProducto(){
        this.productoSeleccionado = new Producto();
    }

    public void guardarProducto(){
        logger.info("Producto a guardar: " + this.productoSeleccionado);
        if(this.productoSeleccionado.getId() == null){
            this.productoServicio.guardarProducto(this.productoSeleccionado);
            this.productos.add(this.productoSeleccionado);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Producto agregado"));
        }else{ //hacer update
            this.productoServicio.guardarProducto(this.productoSeleccionado);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Producto Actualizado"));
        }

        //ocultar la ventana modal
        PrimeFaces.current().executeScript("PF('ventanaModalProducto').hide()");

        //actualizar la tabla
        PrimeFaces.current().ajax().update("forma-productos:mensajes","forma-productos:productos-tabla");

        //Reset objeto
        this.productoSeleccionado = null;
    }

    public void eliminarProducto(){
        logger.info("Producto a eliminar: " + this.productoSeleccionado);
        this.productoServicio.eliminarProducto(productoSeleccionado);
        this.productos.remove(this.productoSeleccionado);
        //Reset el objeto de la tabla
        this.productoSeleccionado = null;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Producto eliminado"));
        PrimeFaces.current().ajax().update("forma-productos:mensajes","forma-productos:productos-tabla");
    }
}
