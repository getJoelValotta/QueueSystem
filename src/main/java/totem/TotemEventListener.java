package totem;

//Se utiliza para la validacion de datos lo que recibe del server y el controller que se comunica con su vista.
public interface TotemEventListener {
    public void mensajeError(String msg);
    public void setClaveEncriptacion(String clave);
    public String getMetodoEncriptacion();
    public void setMetodoEncriptacion(String modo);

    public void setMetodoPersistencia(String modo);

    public String getMetodoPersistencia();
    public void desconexionForzada();
}
