package totem;
 
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
 
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import com.formdev.flatlaf.FlatLightLaf;

import shared.VistasUtils;
 
public class TotemGUI extends JFrame {
 
    // Constante exacta requerida por el Controlador
    public static final String REGISTRAR = "#REGISTRAR#";
 
    // Atributos originales preservados para mantener compatibilidad absoluta
    private JPanel panelAuxCampoDNI;
    private JPanel panelAuxTxtDNI;
    private JPanel panelAuxBtnRegistrar;
    private JTextField campoDNI;
    private JTextPane textDNI;
    private JButton btnRegistrar;
    private JPanel panelAuxTxtGuia;
    private JLabel labelGuia;
 
    // Texto por defecto solicitado
    private final String TEXTO_DEFAULT = "Ingrese su documento para registrarse";

    // Componentes de control interno para el Cartel Guía
    private Timer timerGuia;
    private Color colorOriginalLabel;

    public TotemGUI() {
        // Inicializar FlatLightLaf de manera segura
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            System.err.println("No se pudo aplicar FlatLightLaf en el Tótem");
        }

        setTitle("Tótem - Autogestión de Turnos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 450);
        setMinimumSize(new Dimension(400, 400));
        setLocationRelativeTo(null);
        setResizable(true);
 
        inicializarComponentes();
        inicializarTimer();
    }
 
    private void inicializarComponentes() {
        // Layout principal del Frame en GridBagLayout para flotar el contenido en el centro absoluto
        this.setLayout(new GridBagLayout());
        
        // Instanciación de todos los objetos originales para prevenir NullPointerExceptions externos
        panelAuxCampoDNI = new JPanel(new GridBagLayout());
        panelAuxTxtDNI = new JPanel();
        panelAuxBtnRegistrar = new JPanel();
        panelAuxTxtGuia = new JPanel();
        textDNI = new JTextPane(); // Oculto de la vista por diseño limpio

        // 1. Cartel de guía con el texto por defecto solicitado
        labelGuia = new JLabel(TEXTO_DEFAULT, SwingConstants.CENTER);
        labelGuia.setFont(new Font("Segoe UI", Font.BOLD, 16));
        colorOriginalLabel = labelGuia.getForeground(); // <-- GUARDAMOS EL COLOR ORIGINAL AQUÍ
        
        // 2. Barra de ingreso (Sin ejemplos ni hints)
        campoDNI = new JTextField();
        campoDNI.setFont(new Font("Segoe UI", Font.BOLD, 36));
        campoDNI.setHorizontalAlignment(JTextField.CENTER);
        campoDNI.setPreferredSize(new Dimension(280, 55));
 
        // 3. Botón de Registro situado debajo
        btnRegistrar = new JButton("Registrar Turno");
        btnRegistrar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnRegistrar.setPreferredSize(new Dimension(280, 45));
        btnRegistrar.setActionCommand(REGISTRAR);
        btnRegistrar.setEnabled(false);
        btnRegistrar.putClientProperty("JButton.buttonType", "roundRect");

        // --- CONSTRUCCIÓN DEL PANEL CENTRAL (COMPONENTE RÍGIDO CENTRADO) ---
        GridBagConstraints gbcInterno = new GridBagConstraints();
        gbcInterno.gridx = 0;
        gbcInterno.fill = GridBagConstraints.HORIZONTAL;
        gbcInterno.insets = new Insets(15, 0, 15, 0); // Separación vertical limpia
        gbcInterno.anchor = GridBagConstraints.CENTER;

        gbcInterno.gridy = 0;
        panelAuxCampoDNI.add(labelGuia, gbcInterno);

        gbcInterno.gridy = 1;
        panelAuxCampoDNI.add(campoDNI, gbcInterno);

        gbcInterno.gridy = 2;
        panelAuxCampoDNI.add(btnRegistrar, gbcInterno);

        // Agregamos el contenedor principal al Frame
        this.add(panelAuxCampoDNI, new GridBagConstraints());

        // ── Validación de caracteres: permite vaciado (string.isEmpty()) ──
        ((AbstractDocument) this.campoDNI.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string == null) return;
                if ((string.isEmpty() || string.matches("\\d+")) && fb.getDocument().getLength() + string.length() <= 8) {
                    super.insertString(fb, offset, string, attr);
                }
            }
            @Override
            public void replace(FilterBypass fb, int offset, int length, String string, AttributeSet attr) throws BadLocationException {
                if (string == null) return;
                if ((string.isEmpty() || string.matches("\\d+")) && fb.getDocument().getLength() - length + string.length() <= 8) {
                    super.replace(fb, offset, length, string, attr);
                }
            }
        });
        
        // ── Habilitar / deshabilitar botón original ──
        this.campoDNI.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e)  { actualizarBoton(); }
            @Override public void removeUpdate(DocumentEvent e)  { actualizarBoton(); }
            @Override public void changedUpdate(DocumentEvent e) { actualizarBoton(); }
            private void actualizarBoton() {
                int largo = campoDNI.getText().trim().length();
                btnRegistrar.setEnabled(largo == 7 || largo == 8);
            }
        });
    }

    /**
     * Inicializa el Timer de Swing de forma segura.
     */
    private void inicializarTimer() {
        timerGuia = new Timer(20000, e -> {
            labelGuia.setText(TEXTO_DEFAULT);
            labelGuia.setForeground(colorOriginalLabel); // Restaura el color exacto guardado
        });
        timerGuia.setRepeats(false); // Una única ejecución por disparo
    }
 
    // --- MÉTODOS DE CONTROL DE CICLO DE VIDA ---

    public void mostrar(){
        VistasUtils.enEDT(() -> this.setVisible(true));
    }

    public void cerrar(){
        VistasUtils.enEDT(() -> this.dispose());
    }

    // ── Métodos de estado del label guía controlados mediante el Timer unificado ──
 
    public void setGuiaError(String guia) {
        VistasUtils.enEDT(() -> {
            this.labelGuia.setText(guia);
            this.labelGuia.setForeground(Color.RED);
            
            // CORRECCIÓN SINCRO: Modificamos tanto delay como initialDelay y reiniciamos
            timerGuia.stop();
            timerGuia.setInitialDelay(5000); // 20 segundos exactos solicitados
            timerGuia.setDelay(5000);
            timerGuia.restart(); // Mata cualquier ejecución previa y arranca de cero
        });
    }
 
    public void setGuiaExito(String guia) {
        VistasUtils.enEDT(() -> {
            this.labelGuia.setText(guia);
            this.labelGuia.setForeground(new Color(0, 150, 80)); // Verde éxito
            
            // Éxitos visuales cortos (5 segundos) para no entorpecer el uso continuo del Tótem
            timerGuia.stop();
            timerGuia.setInitialDelay(5000); 
            timerGuia.setDelay(5000);
            timerGuia.restart();
        });
    }
 
    // ── Inyección del Listener del Botón (Envoltorio Seguro de Limpieza) ──
 
    public void setActionListener(ActionListener controlador) {
        for (ActionListener al : btnRegistrar.getActionListeners()) {
            btnRegistrar.removeActionListener(al);
        }

        btnRegistrar.addActionListener(e -> {
            if (controlador != null) {
                controlador.actionPerformed(e);
            }
            limpiaDNI();
        });
    }
 
    // ── Getters / Setters con nomenclaturas Case-Sensitive exactas ──
 
    public String getDNI() {
        return this.campoDNI.getText().trim();
    }
 
    public void limpiaDNI() {
        VistasUtils.enEDT(() -> {
            this.campoDNI.setText("");
            this.campoDNI.requestFocus();
        });
    }
}