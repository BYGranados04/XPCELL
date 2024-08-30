package Model;

public class Marca {
    private int id_marca;
    private String nombre_marca;

    // Constructor
    public Marca(int id_marca, String nombre_marca) {
        this.id_marca = id_marca;
        this.nombre_marca = nombre_marca;
    }

    // Getters
    public int getId_marca() {
        return id_marca;
    }

    public String getNombre_marca() {
        return nombre_marca;
    }
}


