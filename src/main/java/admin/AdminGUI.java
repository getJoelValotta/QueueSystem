package admin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import com.formdev.flatlaf.FlatLightLaf;

import shared.VistasUtils;

public class AdminGUI extends JFrame {
    public static final String XML = "#XML#", JSON = "#JSON#", TXT = "#TXT#",
            CHACHA20 = "CHACHA20", AES = "AES", ENVIAR_CLAVE = "#ENVIAR_CLAVE#";

    // Componentes de Estado de Servidores
    private JLabel lblServerPrincipal;
    private JLabel lblServerRespaldo;

    // Componentes de Persistencia
    private JRadioButton rbXml;
    private JRadioButton rbJson;
    private JRadioButton rbTextoPlano;
    private ButtonGroup groupPersistencia;

    // Componentes de Encriptación
    private JRadioButton rbchacha20;
    private JRadioButton rbaes;
    private ButtonGroup groupEncriptacion;

    // Campo y botón de clave de encriptación — atributos de clase para acceso desde setActionListener
    private JTextField campoClaveEncriptacion;
    private JButton btnEnviarClave;

    // Consola de eventos
    private JTextPane txtConsola;

    // Variables de control de estado actual (Para el Rollback del diálogo)
    private JRadioButton rbPersistenciaActivo;
    private JRadioButton rbEncriptacionActivo;

    // Referencia al controlador (Inyección de dependencias / Delegación)
    private ActionListener controlador;

    public AdminGUI() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            System.err.println("No se pudo inicializar FlatLightLaf, usando defecto.");
        }

        setTitle("Panel de Administración - Sistema de Filas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(500, 680));
        setLocationRelativeTo(null);

        inicializarComponentes();
    }

    private void inicializarComponentes() {
        JPanel panelPrincipal = new JPanel(new GridBagLayout());
        panelPrincipal.setBorder(new EmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 5, 6, 5);
        gbc.weightx = 1.0;

        int fila = 0;

        // --- SECCIÓN 1: ESTADO DE LOS SERVIDORES ---
        gbc.gridx = 0; gbc.gridy = fila++; gbc.gridwidth = 2;
        JLabel lblTituloServer = new JLabel("Estado de los Servidores");
        lblTituloServer.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panelPrincipal.add(lblTituloServer, gbc);

        gbc.gridy = fila++; gbc.gridwidth = 1; gbc.weightx = 0.5;
        gbc.gridx = 0; panelPrincipal.add(new JLabel("Servidor Principal:"), gbc);
        gbc.gridx = 1; lblServerPrincipal = new JLabel("Desconectado");
        lblServerPrincipal.setForeground(Color.RED);
        panelPrincipal.add(lblServerPrincipal, gbc);

        gbc.gridy = fila++;
        gbc.gridx = 0; panelPrincipal.add(new JLabel("Servidor de Respaldo:"), gbc);
        gbc.gridx = 1; lblServerRespaldo = new JLabel("Desconectado");
        lblServerRespaldo.setForeground(Color.RED);
        panelPrincipal.add(lblServerRespaldo, gbc);

        gbc.gridx = 0; gbc.gridy = fila++; gbc.gridwidth = 2; gbc.weightx = 1.0;
        panelPrincipal.add(new JSeparator(), gbc);

        // --- SECCIÓN 2: METODO DE PERSISTENCIA ---
        gbc.gridy = fila++;
        JLabel lblTituloPersistencia = new JLabel("Método de Persistencia");
        lblTituloPersistencia.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panelPrincipal.add(lblTituloPersistencia, gbc);

        rbXml = new JRadioButton("XML");
        rbJson = new JRadioButton("JSON");
        rbTextoPlano = new JRadioButton("Texto Plano");
        groupPersistencia = new ButtonGroup();
        groupPersistencia.add(rbXml);
        groupPersistencia.add(rbJson);
        groupPersistencia.add(rbTextoPlano);

        rbXml.setSelected(true);
        rbPersistenciaActivo = rbXml;

        gbc.gridy = fila++; panelPrincipal.add(rbXml, gbc);
        gbc.gridy = fila++; panelPrincipal.add(rbJson, gbc);
        gbc.gridy = fila++; panelPrincipal.add(rbTextoPlano, gbc);

        gbc.gridy = fila++;
        panelPrincipal.add(new JSeparator(), gbc);

        // --- SECCIÓN 3: METODO DE ENCRIPTACIÓN ---
        gbc.gridy = fila++;
        JLabel lblTituloEncriptacion = new JLabel("Método de Encriptación");
        lblTituloEncriptacion.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panelPrincipal.add(lblTituloEncriptacion, gbc);

        rbchacha20 = new JRadioButton("CHACHA20");
        rbaes = new JRadioButton("AES");
        groupEncriptacion = new ButtonGroup();
        groupEncriptacion.add(rbchacha20);
        groupEncriptacion.add(rbaes);

        rbchacha20.setSelected(true);
        rbEncriptacionActivo = rbchacha20;

        gbc.gridy = fila++; panelPrincipal.add(rbchacha20, gbc);
        gbc.gridy = fila++; panelPrincipal.add(rbaes, gbc);

        // ── Clave de Encriptación ──
        gbc.gridy = fila++;
        gbc.insets = new Insets(10, 5, 2, 5);
        JLabel lblClave = new JLabel("Clave de Encriptación:");
        lblClave.setFont(new Font("Segoe UI", Font.BOLD, 12));
        panelPrincipal.add(lblClave, gbc);

        JPanel panelClave = new JPanel(new BorderLayout(5, 0));
        campoClaveEncriptacion = new JTextField();
        campoClaveEncriptacion.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        campoClaveEncriptacion.setPreferredSize(new Dimension(0, 32));

        // btnEnviarClave ahora es atributo de clase — setActionListener puede referenciarlo
        btnEnviarClave = new JButton("Enviar");
        btnEnviarClave.setPreferredSize(new Dimension(80, 32));
        btnEnviarClave.setActionCommand(ENVIAR_CLAVE);

        panelClave.add(campoClaveEncriptacion, BorderLayout.CENTER);
        panelClave.add(btnEnviarClave, BorderLayout.EAST);
        gbc.gridy = fila++;
        gbc.insets = new Insets(2, 5, 6, 5);
        panelPrincipal.add(panelClave, gbc);

        gbc.gridy = fila++;
        panelPrincipal.add(new JSeparator(), gbc);

        // --- SECCIÓN 4: CONSOLA INFORMATIVA CON SCROLL ---
        gbc.gridy = fila++;
        JLabel lblTituloConsola = new JLabel("Consola del Sistema");
        lblTituloConsola.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panelPrincipal.add(lblTituloConsola, gbc);

        txtConsola = new JTextPane();
        txtConsola.setEditable(false);
        txtConsola.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollConsola = new JScrollPane(txtConsola);
        scrollConsola.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        gbc.gridy = fila++;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panelPrincipal.add(scrollConsola, gbc);

        configurarAccionesDeCambio();

        add(panelPrincipal);
    }

    /**
     * Listeners internos de vista: muestran diálogo de confirmación y hacen rollback visual.
     * Cuando el usuario confirma, delegan al controlador vía su ActionCommand.
     */
    private void configurarAccionesDeCambio() {
        ActionListener persistenciaListener = e -> {
            JRadioButton source = (JRadioButton) e.getSource();
            if (source == rbPersistenciaActivo) return;

            boolean confirmar = mostrarDialogoConfirmacion("Usted está por cambiar el método de persistencia, ¿está seguro?");

            if (confirmar) {
                rbPersistenciaActivo = source;
                agregarLogConColor("[ADMIN] Persistencia cambiada a: " + source.getText(), Color.GRAY);
                if (controlador != null)
                    controlador.actionPerformed(
                        new ActionEvent(source, ActionEvent.ACTION_PERFORMED, source.getActionCommand()));
            } else {
                rbPersistenciaActivo.setSelected(true);
            }
        };

        rbXml.setActionCommand(XML);
        rbJson.setActionCommand(JSON);
        rbTextoPlano.setActionCommand(TXT);
        rbXml.addActionListener(persistenciaListener);
        rbJson.addActionListener(persistenciaListener);
        rbTextoPlano.addActionListener(persistenciaListener);

        ActionListener encriptacionListener = e -> {
            JRadioButton source = (JRadioButton) e.getSource();
            if (source == rbEncriptacionActivo) return;

            boolean confirmar = mostrarDialogoConfirmacion("Usted está por cambiar el método de encriptación, ¿está seguro?");

            if (confirmar) {
                rbEncriptacionActivo = source;
                agregarLogConColor("[ADMIN] Encriptación cambiada a: " + source.getText(), Color.GRAY);
                if (controlador != null)
                    controlador.actionPerformed(
                        new ActionEvent(source, ActionEvent.ACTION_PERFORMED, source.getActionCommand()));
            } else {
                rbEncriptacionActivo.setSelected(true);
            }
        };

        rbchacha20.setActionCommand(CHACHA20);
        rbaes.setActionCommand(AES);
        rbchacha20.addActionListener(encriptacionListener);
        rbaes.addActionListener(encriptacionListener);
    }

    /**
     * Inyecta el controlador y conecta el botón Enviar.
     * Los radio buttons ya tienen sus ActionCommands seteados en configurarAccionesDeCambio(),
     * y delegan al controlador desde ahí cuando el usuario confirma el diálogo.
     */
    public void setActionListener(ActionListener controlador) {
        this.controlador = controlador;
        btnEnviarClave.addActionListener(controlador);
    }

    private boolean mostrarDialogoConfirmacion(String mensaje) {
        JDialog dialog = new JDialog(this, "Confirmar Acción", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setResizable(false);

        JLabel lblMensaje = new JLabel(mensaje, SwingConstants.CENTER);
        lblMensaje.setBorder(new EmptyBorder(15, 15, 10, 15));
        dialog.add(lblMensaje, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton btnCambiar = new JButton("Cambiar método");
        JButton btnCancelar = new JButton("Cancelar");

        final boolean[] resultado = {false};

        btnCambiar.addActionListener(e -> { resultado[0] = true; dialog.dispose(); });
        btnCancelar.addActionListener(e -> { resultado[0] = false; dialog.dispose(); });

        panelBotones.add(btnCambiar);
        panelBotones.add(btnCancelar);
        dialog.add(panelBotones, BorderLayout.SOUTH);

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);

        return resultado[0];
    }

    // --- MÉTODOS PÚBLICOS DE API ---

    public String getClaveEncriptacion() {
        return campoClaveEncriptacion.getText().trim();
    }

    public void setEstadoServidores(boolean principalConectado, boolean respaldoConectado) {
        VistasUtils.enEDT(() -> {
            setEstadoPrincipal(principalConectado);
            setEstadoRespaldo(respaldoConectado);
        });
    }

    public void setEstadoPrincipal(boolean estado) {
        VistasUtils.enEDT(() -> {
            if (estado) {
                lblServerPrincipal.setText("Conectado");
                lblServerPrincipal.setForeground(new Color(46, 139, 87));
            } else {
                lblServerPrincipal.setText("Desconectado");
                lblServerPrincipal.setForeground(Color.RED);
            }
        });
    }

    public void setEstadoRespaldo(boolean estado) {
        VistasUtils.enEDT(() -> {
            if (estado) {
                lblServerRespaldo.setText("Conectado");
                lblServerRespaldo.setForeground(new Color(46, 139, 87));
            } else {
                lblServerRespaldo.setText("Desconectado");
                lblServerRespaldo.setForeground(Color.RED);
            }
        });
    }

    private void agregarLogConColor(String mensaje, Color color) {
        VistasUtils.enEDT(() -> {
            StyledDocument doc = txtConsola.getStyledDocument();
            Style style = txtConsola.addStyle("colorStyle", null);
            StyleConstants.setForeground(style, color);
            String linea = "[" + java.time.LocalTime.now().toString().substring(0, 8) + "] " + mensaje + "\n";
            try {
                doc.insertString(doc.getLength(), linea, style);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
            txtConsola.setCaretPosition(doc.getLength());
        });
    }

    public void logEventoPrincipal(String msg) {
        agregarLogConColor("[PRINCIPAL] " + msg, Color.BLUE);
    }

    public void logBienPrincipal(String msg) {
        agregarLogConColor("[PRINCIPAL] " + msg, new Color(46, 139, 87));
    }

    public void logMalPrincipal(String msg) {
        agregarLogConColor("[PRINCIPAL] " + msg, Color.RED);
    }

    public void logEventoRespaldo(String msg) {
        agregarLogConColor("[RESPALDO] " + msg, Color.GRAY);
    }

    public void mostrar() {
        VistasUtils.enEDT(() -> this.setVisible(true));
    }

    public void cerrar() {
        VistasUtils.enEDT(() -> this.dispose());
    }
}