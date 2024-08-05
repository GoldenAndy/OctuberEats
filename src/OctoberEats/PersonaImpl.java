package main;



public abstract class PersonaImpl implements Persona
{
    private String userID;
    private String direccion;
    private String contrasenaHush;
    private String nombre;
    private String correo;

    public PersonaImpl(String userID, String direccion, String contrasenaHush, String nombre, String correo) {
        this.userID = userID;
        this.direccion = direccion;
        this.contrasenaHush = contrasenaHush;
        this.nombre = nombre;
        this.correo = correo;
    }

    @Override
    public String getUserID() {
        return userID;
    }

    @Override
    public void setUserID(String userID) {
        this.userID = userID;
    }

    @Override
    public String getDireccion() {
        return direccion;
    }

    @Override
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    @Override
    public String getContrasena() {
        return contrasenaHush;
    }

    @Override
    public void setContrasena(String contrasena) {
        this.contrasenaHush = Seguridad.hashPassword(contrasena);
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String getCorreo() {
        return correo;
    }

    @Override
    public void setCorreo(String correo) {
        this.correo = correo;
    }
}
