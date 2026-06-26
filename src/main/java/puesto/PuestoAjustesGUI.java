package puesto;

import java.awt.BorderLayout;
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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import com.formdev.flatlaf.FlatLightLaf;

public class PuestoAjustesGUI extends JDialog {
    public static final String CHACHA20 = "CHACHA20", AES = "AES", 
        XML = "#XML#", JSON = "#JSON#", TXT = "#TXT#", ENVIAR_CLAVE = "#ENVIAR_CLAVE_PUESTO#";

    private JRadioButton rbchacha20;
    private JRadioButton rbaes;
    private ButtonGroup groupEncriptacion;
    private JRadioButton rbEncriptacionActivo;
    
    private JTextField campoClaveEncriptacion;
    private JButton btnEnviarClave;
    
    private JRadioButton rbXml;
    private JRadioButton rbJson;
    private JRadioButton rbTextoPlano;
    private ButtonGroup groupPersistencia;
    private JRadioButton rbPersistenciaActivo;
    
    private ActionListener controlador;

    public PuestoAjustesGUI(PuestoGUI parent) {
        super(parent, "Ajustes del Puesto", true);
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            System.err.println("No se pudo inicializar FlatLightLaf");
        }

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(new Dimension(400, 450));
        setLocationRelativeTo(parent);
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

        // --- MÉTODO DE PERSISTENCIA ---
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
        rbTextoPlano.setSelected(true);
        rbPersistenciaActivo = rbTextoPlano;
        
        rbXml.setActionCommand(XML);
        rbJson.setActionCommand(JSON);
        rbTextoPlano.setActionCommand(TXT);

        gbc.gridy = fila++; panelPrincipal.add(rbXml, gbc);
        gbc.gridy = fila++; panelPrincipal.add(rbJson, gbc);
        gbc.gridy = fila++; panelPrincipal.add(rbTextoPlano, gbc);

        gbc.gridy = fila++;
        panelPrincipal.add(new JSeparator(), gbc);

        // --- MÉTODO DE ENCRIPTACIÓN ---
        gbc.gridy = fila++;
        JLabel lblTituloEncriptacion = new JLabel("Método de Encriptación");
        lblTituloEncriptacion.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panelPrincipal.add(lblTituloEncriptacion, gbc);

        rbchacha20 = new JRadioButton("CHACHA20");
        rbaes = new JRadioButton("AES");
        groupEncriptacion = new ButtonGroup();
        groupEncriptacion.add(rbchacha20);
        groupEncriptacion.add(rbaes);
        rbaes.setSelected(true);
        rbEncriptacionActivo = rbaes;
        
        rbchacha20.setActionCommand(CHACHA20);
        rbaes.setActionCommand(AES);

        gbc.gridy = fila++; panelPrincipal.add(rbchacha20, gbc);
        gbc.gridy = fila++; panelPrincipal.add(rbaes, gbc);

        // --- CLAVE DE ENCRIPTACIÓN ---
        gbc.gridy = fila++;
        gbc.insets = new Insets(10, 5, 2, 5);
        JLabel lblClave = new JLabel("Clave de Encriptación:");
        lblClave.setFont(new Font("Segoe UI", Font.BOLD, 12));
        panelPrincipal.add(lblClave, gbc);

        JPanel panelClave = new JPanel(new BorderLayout(5, 0));
        campoClaveEncriptacion = new JTextField();
        campoClaveEncriptacion.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        campoClaveEncriptacion.setPreferredSize(new Dimension(0, 32));

        btnEnviarClave = new JButton("Enviar");
        btnEnviarClave.setPreferredSize(new Dimension(80, 32));
        btnEnviarClave.setActionCommand(ENVIAR_CLAVE);

        panelClave.add(campoClaveEncriptacion, BorderLayout.CENTER);
        panelClave.add(btnEnviarClave, BorderLayout.EAST);
        
        gbc.gridy = fila++;
        gbc.insets = new Insets(2, 5, 6, 5);
        panelPrincipal.add(panelClave, gbc);

        add(panelPrincipal, BorderLayout.CENTER);
        
        configurarListenersCambios();
        configurarBotonEnviar();
    }

    private void configurarBotonEnviar() {
        btnEnviarClave.addActionListener(e -> {
            String clave = getClaveEncriptacion();
            
            if (clave.isEmpty()) {
                mostrarMensajeError("La clave no puede estar vacía");
                return;
            }

            boolean confirmar = mostrarDialogoConfirmacion("¿Enviar clave de encriptación?");

            if (confirmar && controlador != null) {
                controlador.actionPerformed(
                    new ActionEvent(btnEnviarClave, ActionEvent.ACTION_PERFORMED, btnEnviarClave.getActionCommand()));
            }
        });
    }

    private void configurarListenersCambios() {
        ActionListener persistenciaListener = e -> {
            JRadioButton source = (JRadioButton) e.getSource();
            
            if (source == rbPersistenciaActivo) {
                return;
            }

            boolean confirmar = mostrarDialogoConfirmacion("¿Cambiar método de persistencia a " + source.getText() + "?");

            if (confirmar) {
                rbPersistenciaActivo = source;
                if (controlador != null) {
                    controlador.actionPerformed(
                        new ActionEvent(source, ActionEvent.ACTION_PERFORMED, source.getActionCommand()));
                }
            } else {
                rbPersistenciaActivo.setSelected(true);
            }
        };

        rbXml.addActionListener(persistenciaListener);
        rbJson.addActionListener(persistenciaListener);
        rbTextoPlano.addActionListener(persistenciaListener);

        ActionListener encriptacionListener = e -> {
            JRadioButton source = (JRadioButton) e.getSource();
            
            if (source == rbEncriptacionActivo) {
                return;
            }

            boolean confirmar = mostrarDialogoConfirmacion("¿Cambiar método de encriptación a " + source.getText() + "?");

            if (confirmar) {
                rbEncriptacionActivo = source;
                if (controlador != null) {
                    controlador.actionPerformed(
                        new ActionEvent(source, ActionEvent.ACTION_PERFORMED, source.getActionCommand()));
                }
            } else {
                rbEncriptacionActivo.setSelected(true);
            }
        };

        rbchacha20.addActionListener(encriptacionListener);
        rbaes.addActionListener(encriptacionListener);
    }

    private boolean mostrarDialogoConfirmacion(String mensaje) {
        JDialog dialog = new JDialog(this, "Confirmar Cambio", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setResizable(false);

        JLabel lblMensaje = new JLabel(mensaje, SwingConstants.CENTER);
        lblMensaje.setBorder(new EmptyBorder(15, 15, 10, 15));
        dialog.add(lblMensaje, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton btnCambiar = new JButton("Cambiar");
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

    private void mostrarMensajeError(String mensaje) {
        JDialog dialog = new JDialog(this, "Error", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setResizable(false);

        JLabel lblMensaje = new JLabel(mensaje, SwingConstants.CENTER);
        lblMensaje.setBorder(new EmptyBorder(15, 15, 10, 15));
        lblMensaje.setForeground(java.awt.Color.RED);
        dialog.add(lblMensaje, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton btnOk = new JButton("OK");
        btnOk.addActionListener(e -> dialog.dispose());
        panelBotones.add(btnOk);
        dialog.add(panelBotones, BorderLayout.SOUTH);

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    public String getClaveEncriptacion() {
        return campoClaveEncriptacion.getText().trim();
    }


    public void setClaveEncriptacion(String clave) {
        campoClaveEncriptacion.setText(clave);
    }

    public void setPersistencia(String modo) {
        if (modo == null) return;
        switch (modo.toLowerCase()) {
            case XML:
                rbXml.setSelected(true);
                rbPersistenciaActivo = rbXml;
                break;
            case JSON:
                rbJson.setSelected(true);
                rbPersistenciaActivo = rbJson;
                break;
            case "txt":
            case TXT:
                rbTextoPlano.setSelected(true);
                rbPersistenciaActivo = rbTextoPlano;
                break;
        }
    }

    public void setEncriptacion(String modo) {
        if (modo == null) return;
        switch (modo.toUpperCase()) {
            case "CHACHA20":
                rbchacha20.setSelected(true);
                rbEncriptacionActivo = rbchacha20;
                break;
            case "AES":
                rbaes.setSelected(true);
                rbEncriptacionActivo = rbaes;
                break;
        }
    }

    public void setActionListener(ActionListener controlador) {
        this.controlador = controlador;
    }


    public void setConfiguracionActual(String metodoEncriptacion, String metodoPersistencia) {
        System.out.println("estoy configurando el metodo de encrip/persist");
        if (PuestoAjustesGUI.AES.equals(metodoEncriptacion)) {
            rbaes.setSelected(true);
            setEncriptacion(metodoEncriptacion);
        } else if (PuestoAjustesGUI.CHACHA20.equals(metodoEncriptacion)) {
            rbchacha20.setSelected(true);
            setEncriptacion(metodoEncriptacion);
        }

        if (PuestoAjustesGUI.TXT.equals(metodoPersistencia)) {
            rbTextoPlano.setSelected(true);
            setPersistencia(metodoPersistencia);
        } else if (PuestoAjustesGUI.XML.equals(metodoPersistencia)) {
            rbXml.setSelected(true);
            setPersistencia(metodoPersistencia);
        } else if (PuestoAjustesGUI.JSON.equals(metodoPersistencia)) {
            rbJson.setSelected(true);
            setPersistencia(metodoPersistencia);
        }
    }
}