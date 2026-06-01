package shared.conexion_server;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
 
public class ConexionGUI extends JFrame {
 
    public static final String CONECTAR = "#CONECTAR#";

    private JTextField campoIP;
    private JTextField campoPuerto;
    private JTextArea areaLog;
    private JButton btnConectar;
 
    public ConexionGUI() {
        setTitle("Conectar al servidor");
        setSize(420, 260);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
 
        // Panel principal con padding
        JPanel panelPrincipal = new JPanel(new BorderLayout(0, 10));
        panelPrincipal.setBorder(new EmptyBorder(14, 14, 14, 14));
        setContentPane(panelPrincipal);
 
        // ── Fila superior: IP y Puerto ──
        JPanel panelCampos = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
 
        JLabel lblIP = new JLabel("IP:");
        campoIP = new JTextField("localhost", 14);
 
        JLabel lblPuerto = new JLabel("Puerto:");
        campoPuerto = new JTextField("1337", 6);
 
        panelCampos.add(lblIP);
        panelCampos.add(campoIP);
        panelCampos.add(lblPuerto);
        panelCampos.add(campoPuerto);
 
        panelPrincipal.add(panelCampos, BorderLayout.NORTH);
 
        // ── Centro: área de log ──
        areaLog = new JTextArea();
        areaLog.setEditable(false);
        areaLog.setLineWrap(true);
        areaLog.setWrapStyleWord(true);
        areaLog.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
 
        JScrollPane scroll = new JScrollPane(areaLog);
        scroll.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        panelPrincipal.add(scroll, BorderLayout.CENTER);
 
        // ── Sur: botón Conectar ──
        JPanel panelBoton = new JPanel(new BorderLayout());
        btnConectar = new JButton("Conectar");
        btnConectar.setActionCommand(CONECTAR);
        btnConectar.setPreferredSize(new Dimension(0, 34));
        panelBoton.add(btnConectar, BorderLayout.CENTER);
        panelPrincipal.add(panelBoton, BorderLayout.SOUTH);
    }
 
    // ── Getters para el controlador ──
 
    public String getIP() {
        return campoIP.getText().trim();
    }
 
    public String getPuerto() {
        return campoPuerto.getText().trim();
    }
 
    // ── Métodos para actualizar el log ──
 
    public void appendLog(String mensaje) {
        SwingUtilities.invokeLater(() -> {
            areaLog.append(mensaje + "\n");
            areaLog.setCaretPosition(areaLog.getDocument().getLength());
        });
    }
 
    public void appendLogError(String mensaje) {
        SwingUtilities.invokeLater(() -> {
            areaLog.append("[ERROR] " + mensaje + "\n");
            areaLog.setCaretPosition(areaLog.getDocument().getLength());
        });
    }
 
    // ── Listener del botón para el controlador ──
 
    public void setActionListener(ActionListener listener) {
        btnConectar.addActionListener(listener);
    }
 
    // ── Habilitar / deshabilitar el botón mientras conecta ──
 
    public void setBtnConectarEnabled(boolean enabled) {
        SwingUtilities.invokeLater(() -> btnConectar.setEnabled(enabled));
    }
 
    // ── Transición a la ventana del tótem ──
 
    public void transicionVista(JFrame vista) {
        SwingUtilities.invokeLater(() -> { // ¿Por que esta expresion tan rara? Mezcla expresiones lambda (instancia anonima que nace y muere en esa ejecucion). invokeLater   
            vista.setVisible(true);    // lo que hace es decirle a SWING (que dibuja en un hilo y maneja eventos en su hilo) que ejecute el hilo de la
            this.dispose();              // expresion lambda sin paralelismo.
        });                             // En caso de no contar con esto, el controaldor le diria directamente al hilo que dibuja que maneje las cosas, y se freezearia todo.
    }

    public void ejecutarNoBloqueante(Runnable tarea) { //Este metodo (ingeniado con IA) es para abstraerme del problema de swing con los hilos. Cuando algo deba trabajar en paralelo
        new SwingWorker<Void, Void>() {               // con alguna vista, debe ejecutar las sentencias como expresion lambda en esa funcion.
            @Override
            protected Void doInBackground() {
                tarea.run();
            return null;
        }
    }.execute();
}
 
}