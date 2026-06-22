package shared.conexion_server;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import com.formdev.flatlaf.FlatLightLaf;

import shared.VistasUtils;

public class ConexionGUI extends JFrame {

    // Constante exacta del archivo original preserved para acoplamiento estricto
    public static final String CONECTAR = "#CONECTAR#";

    // Componentes originales requeridos por el sistema
    private JTextField campoIP;
    private JTextField campoPuerto;
    private JTextArea areaLog;
    private JButton btnConectar;

    // NUEVO COMPONENTE: Campo para la clave de encriptación solicitado
    private JTextField campoClaveEncriptacion;

    public ConexionGUI() {
        // Inicializar FlatLightLaf de manera segura para mantener consistencia visual
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            System.err.println("No se pudo aplicar FlatLightLaf en la conexión");
        }

        setTitle("Conectar al servidor");
        setSize(440, 440); // Incrementamos la altura para acomodar el nuevo campo estéticamente
        setMinimumSize(new Dimension(400, 380));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

        inicializarComponentes();
    }

    private void inicializarComponentes() {
        // Panel principal usando GridBagLayout para control total de redimensionamiento
        JPanel panelPrincipal = new JPanel(new GridBagLayout());
        panelPrincipal.setBorder(new EmptyBorder(12, 14, 12, 14));
        setContentPane(panelPrincipal);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        int fila = 0;

        // ── 1. TÍTULO: Mucho más chico en la esquina superior izquierda ──
        JLabel lblTitulo = new JLabel("Establecer Conexión");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTitulo.setForeground(UIManager.getColor("Label.disabledForeground")); // Color sutil elegante
        gbc.gridy = fila++;
        gbc.insets = new Insets(0, 0, 10, 0);
        panelPrincipal.add(lblTitulo, gbc);

        // ── 2. NUEVO CAMPO: Clave de Encriptación (Sobre IP/Puerto, alineado a la
        // izquierda) ──
        JLabel lblClave = new JLabel("Clave de Encriptación:");
        lblClave.setFont(new Font("Segoe UI", Font.BOLD, 12));
        gbc.gridy = fila++;
        gbc.insets = new Insets(4, 0, 2, 0);
        panelPrincipal.add(lblClave, gbc);

        campoClaveEncriptacion = new JTextField();
        campoClaveEncriptacion.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        campoClaveEncriptacion.setPreferredSize(new Dimension(0, 30));
        gbc.gridy = fila++;
        gbc.insets = new Insets(0, 0, 10, 0);
        panelPrincipal.add(campoClaveEncriptacion, gbc);

        // ── 3. FILA: IP y Puerto (Mantienen su posición horizontal original) ──
        JPanel panelIpPuerto = new JPanel(new GridBagLayout());
        GridBagConstraints subGbc = new GridBagConstraints();
        subGbc.anchor = GridBagConstraints.WEST;

        JLabel lblIP = new JLabel("IP: ");
        lblIP.setFont(new Font("Segoe UI", Font.BOLD, 12));
        campoIP = new JTextField("localhost");
        campoIP.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        campoIP.setPreferredSize(new Dimension(150, 30));

        JLabel lblPuerto = new JLabel(" Puerto: ");
        lblPuerto.setFont(new Font("Segoe UI", Font.BOLD, 12));
        campoPuerto = new JTextField("1337");
        campoPuerto.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        campoPuerto.setPreferredSize(new Dimension(70, 30));

        subGbc.gridx = 0;
        panelIpPuerto.add(lblIP, subGbc);
        subGbc.gridx = 1;
        panelIpPuerto.add(campoIP, subGbc);
        subGbc.gridx = 2;
        panelIpPuerto.add(lblPuerto, subGbc);
        subGbc.gridx = 3;
        subGbc.fill = GridBagConstraints.HORIZONTAL;
        subGbc.weightx = 1.0;
        panelIpPuerto.add(campoPuerto, subGbc);

        gbc.gridy = fila++;
        gbc.insets = new Insets(4, 0, 12, 0);
        panelPrincipal.add(panelIpPuerto, gbc);

        // ── 4. CENTRO: Área de consola pura (Sin títulos de sección) ──
        areaLog = new JTextArea();
        areaLog.setEditable(false);
        areaLog.setLineWrap(true);
        areaLog.setWrapStyleWord(true);
        areaLog.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JScrollPane scroll = new JScrollPane(areaLog);
        scroll.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));

        // Configuramos la consola para que absorba todo el estiramiento vertical de la
        // ventana
        gbc.gridy = fila++;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(4, 0, 10, 0);
        panelPrincipal.add(scroll, gbc);

        // ── 5. SUR: Botón Conectar ──
        btnConectar = new JButton("Conectar");
        btnConectar.setActionCommand(CONECTAR);
        btnConectar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnConectar.setPreferredSize(new Dimension(0, 36));
        btnConectar.putClientProperty("JButton.buttonType", "roundRect"); // Borde redondeado moderno de FlatLaf

        gbc.gridy = fila++;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(4, 0, 0, 0);
        panelPrincipal.add(btnConectar, gbc);
    }

    // --- METODOS DE API PÚBLICA (Compatibilidad Absoluta Case-Sensitive) ---

    public void mostrar() {
        VistasUtils.enEDT(() -> this.setVisible(true));
    }

    public void cerrar() {
        VistasUtils.enEDT(() -> this.dispose());
    }

    public String getIP() {
        return campoIP.getText().trim();
    }

    public String getPuerto() {
        return campoPuerto.getText().trim();
    }

    /**
     * NUEVO MÉTODO: Expone la clave de encriptación ingresada al controlador.
     */
    public String getClaveEncriptacion() {
        return campoClaveEncriptacion.getText().trim();
    }

    public void appendLog(String mensaje) {
        VistasUtils.enEDT(() -> {
            areaLog.append(mensaje + "\n");
            areaLog.setCaretPosition(areaLog.getDocument().getLength());
        });
    }

    public void appendLogError(String mensaje) {
        VistasUtils.enEDT(() -> {
            areaLog.append("[ERROR] " + mensaje + "\n");
            areaLog.setCaretPosition(areaLog.getDocument().getLength());
        });
    }

    public void setActionListener(ActionListener listener) {
        // Limpieza preventiva contra listeners duplicados
        for (ActionListener al : btnConectar.getActionListeners()) {
            btnConectar.removeActionListener(al);
        }
        btnConectar.addActionListener(listener);
    }

    public void setBtnConectarEnabled(boolean enabled) {
        SwingUtilities.invokeLater(() -> btnConectar.setEnabled(enabled));
    }

    public void transicionVista(JFrame vista) {
        SwingUtilities.invokeLater(() -> {
            vista.setVisible(true);
            this.dispose();
        });
    }

    public void ejecutarNoBloqueante(Runnable tarea) {
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                tarea.run();
                return null;
            }
        }.execute();
    }

    public void setClaveEncriptacion(String clave) {
        VistasUtils.enEDT(() -> campoClaveEncriptacion.setText(clave));
    }
}