package Model;

public class Repuesto {
    private String nombre_repuesto;
    private double precio;
    private int stock;
    private String tipo_repuesto;
    private String modelo;

    // Constructor
    public Repuesto(String nombre_repuesto, double precio, int stock, String tipo_repuesto, String modelo) {
        this.nombre_repuesto = nombre_repuesto;
        this.precio = precio;
        this.stock = stock;
        this.tipo_repuesto = tipo_repuesto;
        this.modelo = modelo;
    }

    // Getters
    public String getNombre_repuesto() {
        return nombre_repuesto;
    }

    public double getPrecio() {
        return precio;
    }

    public int getStock() {
        return stock;
    }

    public String getTipo_repuesto() {
        return tipo_repuesto;
    }

    public String getModelo() {
        return modelo;
    }

    // Setters
    public void setNombre_repuesto(String nombre_repuesto) {
        this.nombre_repuesto = nombre_repuesto;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setTipo_repuesto(String tipo_repuesto) {
        this.tipo_repuesto = tipo_repuesto;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }
}


