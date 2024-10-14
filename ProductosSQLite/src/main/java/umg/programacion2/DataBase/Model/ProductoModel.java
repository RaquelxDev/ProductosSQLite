package umg.programacion2.DataBase.Model;

public class ProductoModel {
    private int idProducto;
    private String descripcion;
    private String origen;
    private double precio;
    private int existencia;

    public ProductoModel(int idProducto, String descripcion, String origen, double precio, int existencia) {
        this.idProducto = idProducto;
        this.descripcion = descripcion;
        this.origen = origen;
        this.precio = precio;    // Cambiado a double
        this.existencia = existencia;
    }

    public ProductoModel() {}

    // Getters y Setters

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getExistencia() {
        return existencia;
    }

    public void setExistencia(int existencia) {
        this.existencia = existencia;
    }
}
