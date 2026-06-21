package admin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
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
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import com.formdev.flatlaf.FlatLightLaf;

import shared.VistasUtils;

public class AdminGUI extends JFrame {
    public static final String XML = "#XML#", JSON = "#JSON#", TXT = "#TXT#", SHA_2 = "SHA_2", MD5 = "MD5";

    // Componentes de Estado de Servidores
    private JLabel lblServerPrincipal;
    private JLabel lblServerRespaldo;

    // Componentes de Persistencia
    private JRadioButton rbXml;
    private JRadioButton rbJson;
    private JRadioButton rbTextoPlano;
    private ButtonGroup groupPersistencia;

    private JRadioButton rbPersistenciaActivo;
    private JRadioButton rbEncriptacionActivo;

    // Componentes de Encriptación
    private JRadioButton rbSha2;
    private JRadioButton rbMd5;
    private ButtonGroup groupEncriptacion;

    // Consola de eventos
    private JTextArea txtConsola;

    // Referencia al controlador (Inyección de dependencias / Delegación)
    private ActionListener controlador;

    public AdminGUI() {
        // Inicializar el Look and Feel de FlatLaf de manera segura
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            System.err.println("No se pudo inicializar FlatLightLaf, usando defecto.");
        }

        setTitle("Panel de Administración - Sistema de Filas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(500, 600));
        setLocationRelativeTo(null);

        inicializarComponentes();
    }

    private void inicializarComponentes() {
        // Panel principal con un margen limpio
        JPanel panelPrincipal = new JPanel(new GridBagLayout());
        panelPrincipal.setBorder(new EmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 5, 8, 5);
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

        // Separador visual
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

        // Por defecto seleccionamos uno inicialmente sin disparar eventos críticos
        rbXml.setSelected(true);
        rbPersistenciaActivo = rbXml;

        gbc.gridy = fila++; gbc.gridwidth = 2;
        panelPrincipal.add(rbXml, gbc);
        gbc.gridy = fila++;
        panelPrincipal.add(rbJson, gbc);
        gbc.gridy = fila++;
        panelPrincipal.add(rbTextoPlano, gbc);

        // Separador visual
        gbc.gridy = fila++;
        panelPrincipal.add(new JSeparator(), gbc);

        // --- SECCIÓN 3: METODO DE ENCRIPTACIÓN ---
        gbc.gridy = fila++;
        JLabel lblTituloEncriptacion = new JLabel("Método de Encriptación");
        lblTituloEncriptacion.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panelPrincipal.add(lblTituloEncriptacion, gbc);

        rbSha2 = new JRadioButton("SHA_2");
        rbMd5 = new JRadioButton("MD5");
        groupEncriptacion = new ButtonGroup();
        groupEncriptacion.add(rbSha2);
        groupEncriptacion.add(rbMd5);
        
        rbSha2.setSelected(true);
        rbEncriptacionActivo = rbSha2;

        gbc.gridy = fila++;
        panelPrincipal.add(rbSha2, gbc);
        gbc.gridy = fila++;
        panelPrincipal.add(rbMd5, gbc);

        // Separador visual
        gbc.gridy = fila++;
        panelPrincipal.add(new JSeparator(), gbc);

        // --- SECCIÓN 4: CONSOLA INFORMATIVA CON SCROLL ---
        gbc.gridy = fila++;
        JLabel lblTituloConsola = new JLabel("Consola del Sistema");
        lblTituloConsola.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panelPrincipal.add(lblTituloConsola, gbc);

        txtConsola = new JTextArea(8, 20);
        txtConsola.setEditable(false);
        txtConsola.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollConsola = new JScrollPane(txtConsola);
        scrollConsola.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Esta sección debe expandirse si la ventana se estira hacia abajo
        gbc.gridy = fila++;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panelPrincipal.add(scrollConsola, gbc);

        // Configurar acciones interactivas controladas
        configurarAccionesDeCambio();

        add(panelPrincipal);
    }

    /**
     * Intercepta el click del usuario para mostrar el diálogo de confirmación.
     * Si cancela, deshace la selección visual usando VistasUtils de forma segura.
     */
   /**
     * Intercepta el click del usuario para mostrar el diálogo de confirmación.
     * Si cancela, deshace la selección visual regresando al último estado confirmado.
     */
    private void configurarAccionesDeCambio() {
        // Listener para Persistencia
        ActionListener persistenciaListener = e -> {
            JRadioButton source = (JRadioButton) e.getSource();
            
            // Si hace click sobre el método que ya está activo, no hacemos nada
            if (source == rbPersistenciaActivo) {
                return;
            }

            boolean confirmar = mostrarDialogoConfirmacion("Usted está por cambiar el método de persistencia, ¿está seguro?");
            
            if (confirmar) {
                rbPersistenciaActivo = source; // Confirmado: actualizamos el estado activo
                agregarLog("Cambio de persistencia solicitado: " + source.getText());
                // Aquí se informará al controlador real en el futuro
            } else {
                // ROLLBACK: Volvemos a marcar visualmente el que estaba activo antes del click
                rbPersistenciaActivo.setSelected(true);
                agregarLog("Cambio de persistencia cancelado.");
            }
        };

        rbXml.addActionListener(persistenciaListener);
        rbJson.addActionListener(persistenciaListener);
        rbTextoPlano.addActionListener(persistenciaListener);

        // Listener para Encriptación
        ActionListener encriptacionListener = e -> {
            JRadioButton source = (JRadioButton) e.getSource();
            
            // Si hace click sobre el método que ya está activo, no hacemos nada
            if (source == rbEncriptacionActivo) {
                return;
            }

            boolean confirmar = mostrarDialogoConfirmacion("Usted está por cambiar el método de encriptación, ¿está seguro?");
            
            if (confirmar) {
                rbEncriptacionActivo = source; // Confirmado: actualizamos el estado activo
                agregarLog("Cambio de encriptación solicitado: " + source.getText());
                // Aquí se informará al controlador real en el futuro
            } else {
                // ROLLBACK: Volvemos a marcar visualmente el que estaba activo antes del click
                rbEncriptacionActivo.setSelected(true);
                agregarLog("Cambio de encriptación cancelado.");
            }
        };

        rbSha2.addActionListener(encriptacionListener);
        rbMd5.addActionListener(encriptacionListener);
    }

    /**
     * Ventana emergente modal de decisión con las especificaciones solicitadas.
     */
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

        btnCambiar.addActionListener(e -> {
            resultado[0] = true;
            dialog.dispose();
        });

        btnCancelar.addActionListener(e -> {
            resultado[0] = false;
            dialog.dispose();
        });

        panelBotones.add(btnCambiar);
        panelBotones.add(btnCancelar);
        dialog.add(panelBotones, BorderLayout.SOUTH);

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);

        return resultado[0];
    }

    // --- MÉTODOS PÚBLICOS DE ACTUALIZACIÓN (Hilos Seguros) ---

    public void setEstadoServidores(boolean principalConectado, boolean respaldoConectado) {
        VistasUtils.enEDT(() -> {
            if (principalConectado) {
                lblServerPrincipal.setText("Conectado");
                lblServerPrincipal.setForeground(new Color(46, 139, 87)); // Verde marino
            } else {
                lblServerPrincipal.setText("Desconectado");
                lblServerPrincipal.setForeground(Color.RED);
            }

            if (respaldoConectado) {
                lblServerRespaldo.setText("Conectado");
                lblServerRespaldo.setForeground(new Color(46, 139, 87));
            } else {
                lblServerRespaldo.setText("Desconectado");
                lblServerRespaldo.setForeground(Color.RED);
            }
        });
    }

    

    public void agregarLog(String mensaje) {
        VistasUtils.enEDT(() -> {
            txtConsola.append("[" + java.time.LocalTime.now().toString().substring(0, 8) + "] " + mensaje + "\n");
        });
    }

    public void logServerPrincipal(String msg){
        agregarLog("[PRINCIPAL]"+ msg);
    }

    public void logServerRespaldo(String msg){
        agregarLog("[RESPALDO]"+ msg);
    }
    
    public void setControlador(ActionListener controlador) {
        this.controlador = controlador;
    }

    public void mostrar(){
        VistasUtils.enEDT(() -> this.setVisible(true));
    }

    public void cerrar(){
        VistasUtils.enEDT(() -> this.dispose());
    }
}