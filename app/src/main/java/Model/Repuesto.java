package Model;

public class Repuesto {
    private String nombre_repuesto;
    private double precio;
    private int stock;
    private String tipo_repuesto;
    private String modelo;

    // Getters y Setters
    public String getNombre_repuesto() {
        return nombre_repuesto;
    }

    public void setNombre_repuesto(String nombre_repuesto) {
        this.nombre_repuesto = nombre_repuesto;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getTipo_repuesto() {
        return tipo_repuesto;
    }

    public void setTipo_repuesto(String tipo_repuesto) {
        this.tipo_repuesto = tipo_repuesto;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }
}

