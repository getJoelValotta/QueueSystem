package totem;

//Se utiliza para la validacion de datos lo que recibe del server y el controller que se comunica con su vista.
public interface TotemEventListener {
    public void mensajeError(String msg);
    public void setClaveEncriptacion(String clave);
    public String getModoEncriptacion();
    public void setModoEncriptacion(String modo);

    public void setModo(String modo);

    public String getModo();
}
