package santiagoyandres.proyectoquesorbeto;

public class objCliente {

    int id;
    String nombre;
    String telefono;
    String nacionalidad;

    public objCliente(int id, String nombre, String telefono, String nacionalidad) {
        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
        this.nacionalidad = nacionalidad;
    }

    public objCliente(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }
}
