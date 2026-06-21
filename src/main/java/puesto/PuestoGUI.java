package puesto;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import shared.VistasUtils;


public class PuestoGUI extends JFrame {

    public static final String LLAMAR = "#LLAMAR#", RENOTIFICAR = "#RENOTIFICAR";

    private JPanel panelAuxBtn;
    private JPanel panelAuxLblLista;
    private JPanel panelAuxNorte;
    private JLabel lblCantClientesEspera;
    private JLabel lblClienteActual;
    private JPanel panelAuxLlamar;
    private JPanel panelAuxReLlamar;
    private JButton btnLlamar;
    private JButton btnRenotificar;
    private JLabel lblNumPuesto;
    private JLabel lblMensaje;          // <-- nuevo


    public PuestoGUI() {
        setTitle("Gestión de Turnos - Puesto Operador");
        setLayout(new BorderLayout());
        this.setSize(450, 290);
        this.setLocationRelativeTo(null);

        // Margen perimetral interno para que los componentes respiren y no toquen los bordes
        ((JPanel)getContentPane()).setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 20, 15, 20));

        // 1. ZONA NORTE: Cantidad de clientes en espera (Alineado a la derecha)
        this.panelAuxNorte = new JPanel(new BorderLayout());
        this.lblCantClientesEspera = new JLabel("Hay 0 en cola");
        this.lblCantClientesEspera.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        this.lblCantClientesEspera.setForeground(new Color(120, 120, 120));
        this.panelAuxNorte.add(this.lblCantClientesEspera, BorderLayout.EAST);
        add(this.panelAuxNorte, BorderLayout.NORTH);

        // 2. ZONA CENTRAL: Apilamiento Vertical (Puesto ARRIBA del Cliente actual)
        this.panelAuxLblLista = new JPanel();
        this.panelAuxLblLista.setLayout(new javax.swing.BoxLayout(this.panelAuxLblLista, javax.swing.BoxLayout.Y_AXIS));
        this.panelAuxLblLista.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 0, 15, 0));

        // Número de puesto como encabezado secundario centrado
        this.lblNumPuesto = new JLabel("Puesto: -");
        this.lblNumPuesto.setFont(new Font("Segoe UI", Font.BOLD, 16));
        this.lblNumPuesto.setForeground(new Color(80, 80, 80));
        this.lblNumPuesto.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);

        // Identificador del Cliente (Dato principal destacado)
        this.lblClienteActual = new JLabel("Esperando cliente...");
        this.lblClienteActual.setFont(new Font("Segoe UI", Font.BOLD, 28));
        this.lblClienteActual.setForeground(new Color(0, 102, 204)); // Azul moderno e institucional
        this.lblClienteActual.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);

        // Agregamos al contenedor central con un espaciador rígido en el medio
        this.panelAuxLblLista.add(this.lblNumPuesto);
        this.panelAuxLblLista.add(javax.swing.Box.createVerticalStrut(10)); // Separación de 10 píxeles
        this.panelAuxLblLista.add(this.lblClienteActual);
        add(this.panelAuxLblLista, BorderLayout.CENTER);

        // 3. ZONA SUR: Distribución de Botones y Mensajes de Feedback
        this.panelAuxBtn = new JPanel(new BorderLayout(0, 10));

        // Sub-panel para igualar el tamaño de los dos botones en una sola fila
        JPanel panelContenedorBotones = new JPanel(new GridLayout(1, 2, 15, 0));

        this.btnLlamar = new JButton("Llamar Siguiente");
        this.btnLlamar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        this.btnLlamar.setActionCommand(LLAMAR);
        this.btnLlamar.setPreferredSize(new java.awt.Dimension(150, 42));
        this.btnLlamar.putClientProperty("JButton.buttonType", "roundRect"); // Bordes redondeados FlatLaf

        this.btnRenotificar = new JButton("Re-notificar");
        this.btnRenotificar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        this.btnRenotificar.setActionCommand(RENOTIFICAR);
        this.btnRenotificar.putClientProperty("JButton.buttonType", "roundRect");

        panelContenedorBotones.add(this.btnLlamar);
        panelContenedorBotones.add(this.btnRenotificar);

        // Mensaje de éxito o error del sistema abajo de todo
        this.lblMensaje = new JLabel(" "); // Lo inicializamos con un espacio en blanco
        this.lblMensaje.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        this.lblMensaje.setHorizontalAlignment(JLabel.CENTER);
        this.lblMensaje.setPreferredSize(new java.awt.Dimension(400, 20)); // Reservamos el alto fijo

        this.panelAuxBtn.add(panelContenedorBotones, BorderLayout.CENTER);
        this.panelAuxBtn.add(this.lblMensaje, BorderLayout.SOUTH);
        add(this.panelAuxBtn, BorderLayout.SOUTH);
    }
    public void mostrar() {
        VistasUtils.enEDT(() -> this.setVisible(true));
        //btnRenotificar.setEnabled(false);
    }

    public void cerrar() {
        VistasUtils.enEDT(() -> this.dispose());
    }

    public void setCantClientes(int clientes) {
        VistasUtils.enEDT(() -> this.lblCantClientesEspera.setText("Hay " + clientes + " en cola"));
    }

    public void inhabilitarBtn() {
        this.btnLlamar.setEnabled(false);
    }    

    public void habilitarBtn() {
        this.btnLlamar.setEnabled(true);
    }

    public void cambiaTextRenotificarAabandonado() {
        this.btnRenotificar.setText("Turno Abandonado");
    }

    public void cambiaTextAbandonadoARenotificar() {
        this.btnRenotificar.setText("Re-notificar");
    }

    public void inhabilitaRenotificar() {
        this.btnRenotificar.setEnabled(false);
    }

    public void habilitaRenotificar() {
        this.btnRenotificar.setEnabled(true);
    }

    public void setActionListener(ActionListener controlador) {
        btnLlamar.addActionListener(controlador);
        btnRenotificar.addActionListener(controlador);
    }

    public void setClienteActual(long dni) {
        this.lblClienteActual.setText("Cliente actual: " + dni);
    }

    public void limpiarClienteActual() {
        this.lblClienteActual.setText("");
    }

    public void setNumPuesto(String puesto) {
        this.lblNumPuesto.setText("Puesto: " + puesto);
    }

    // ── métodos de feedback ──────────────────────────────────────

    private void setMensajeInvis() {
        new Thread(() -> {
            try {
                Thread.sleep(3500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            // En lugar de ocultarlo, vaciamos el texto para que mantenga el espacio
            SwingUtilities.invokeLater(() -> this.lblMensaje.setText(" "));
        }).start();
    }

    public void setMensajeError() {
        VistasUtils.enEDT(() -> {
            this.lblMensaje.setText("Error al realizar la petición");
            this.lblMensaje.setForeground(Color.RED);
            setMensajeInvis();
        });
    }

    public void setMensajeExito() {
        VistasUtils.enEDT(() -> {
            this.lblMensaje.setText("Petición enviada correctamente");
            this.lblMensaje.setForeground(new Color(0, 150, 80));
            setMensajeInvis();
        });
    }
    // ─────────────────────────────────────────────────────────────

    public static void main(String[] args) {
		//FlatLightLaf.setup();
		(new PuestoGUI()).setVisible(true);
    }
}