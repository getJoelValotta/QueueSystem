package totem;
 
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionListener;
 
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import shared.VistasUtils;
 
public class TotemGUI extends JFrame {
 
    public static final String REGISTRAR = "#REGISTRAR#";
 
    private JPanel panelAuxCampoDNI;
    private JPanel panelAuxTxtDNI;
    private JPanel panelAuxBtnRegistrar;
    private JTextField campoDNI;
    private JTextPane textDNI;
    private JButton btnRegistrar;
    private JPanel panelAuxTxtGuia;
    private JLabel labelGuia;
 
    public TotemGUI() {
        setTitle("Tótem - Autogestión de Turnos");
        setLayout(new BorderLayout());
        setSize(450, 320);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
 
        // Padding general
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(20, 30, 20, 30));
 
        // ── Norte: Label guía ──
        this.panelAuxTxtGuia = new JPanel(new BorderLayout());
        this.labelGuia = new JLabel("Ingrese su DNI para obtener un turno");
        this.labelGuia.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 16));
        this.labelGuia.setHorizontalAlignment(JLabel.CENTER);
        this.panelAuxTxtGuia.add(this.labelGuia, BorderLayout.CENTER);
        add(this.panelAuxTxtGuia, BorderLayout.NORTH);
 
        // ── Centro: Campo DNI (Distribución Vertical) ──
        this.panelAuxCampoDNI = new JPanel();
        this.panelAuxCampoDNI.setLayout(new javax.swing.BoxLayout(this.panelAuxCampoDNI, javax.swing.BoxLayout.Y_AXIS));
        this.panelAuxCampoDNI.setBorder(new EmptyBorder(25, 0, 25, 0));
 
        this.panelAuxTxtDNI = new JPanel(); // Mantenemos el panel original por compatibilidad
        this.textDNI = new JTextPane();
        this.textDNI.setText("DNI");
        this.textDNI.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 16));
        this.textDNI.setForeground(new java.awt.Color(80, 80, 80));
        this.textDNI.setEditable(false);
        this.textDNI.setBackground(null);
        this.textDNI.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
 
        this.campoDNI = new JTextField();
        this.campoDNI.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 40));
        this.campoDNI.setHorizontalAlignment(JTextField.CENTER);
        this.campoDNI.setMaximumSize(new java.awt.Dimension(300, 65));
        this.campoDNI.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
        // Hint nativo de FlatLaf
        this.campoDNI.putClientProperty("JTextField.placeholderText", "Ej: 12345678");

        this.panelAuxCampoDNI.add(this.textDNI);
        this.panelAuxCampoDNI.add(javax.swing.Box.createVerticalStrut(5));
        this.panelAuxCampoDNI.add(this.campoDNI);
        add(this.panelAuxCampoDNI, BorderLayout.CENTER);
 
        // ── Validación de caracteres: solo dígitos, máximo 8 ──
        ((AbstractDocument) this.campoDNI.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string == null) return;
                if (string.matches("\\d+") && fb.getDocument().getLength() + string.length() <= 8) {
                    super.insertString(fb, offset, string, attr);
                }
            }
            @Override
            public void replace(FilterBypass fb, int offset, int length, String string, AttributeSet attr) throws BadLocationException {
                if (string == null) return;
                if (string.matches("\\d+") && fb.getDocument().getLength() - length + string.length() <= 8) {
                    super.replace(fb, offset, length, string, attr);
                }
            }
        });
        
        // ── Habilitar / deshabilitar botón ──
        this.campoDNI.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e)  { actualizarBoton(); }
            @Override public void removeUpdate(DocumentEvent e)  { actualizarBoton(); }
            @Override public void changedUpdate(DocumentEvent e) { actualizarBoton(); }
            private void actualizarBoton() {
                int largo = campoDNI.getText().trim().length();
                btnRegistrar.setEnabled(largo == 7 || largo == 8);
            }
        });
 
        // ── Sur: botón Registrar ──
        this.panelAuxBtnRegistrar = new JPanel(new BorderLayout());
        this.btnRegistrar = new JButton("Registrar Turno");
        this.btnRegistrar.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 16));
        this.btnRegistrar.setPreferredSize(new java.awt.Dimension(0, 45));
        this.btnRegistrar.setActionCommand(REGISTRAR);
        this.btnRegistrar.setEnabled(false);
        this.btnRegistrar.putClientProperty("JButton.buttonType", "roundRect");
        this.panelAuxBtnRegistrar.add(this.btnRegistrar, BorderLayout.CENTER);
        
        add(this.panelAuxBtnRegistrar, BorderLayout.SOUTH);
    }
 
    public void mostrar(){
        VistasUtils.enEDT(() -> this.setVisible(true));
    }

    public void cerrar(){
        VistasUtils.enEDT(() -> this.dispose());
    }

    // ── Métodos de estado del label guía ──
 
    public void setGuiaInvis() {
        new Thread(() -> {
            try {
              Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            SwingUtilities.invokeLater(() -> this.labelGuia.setVisible(false));
        }).start();
    }
 
    public void setGuiaError(String guia) {
        VistasUtils.enEDT(() -> {
            this.labelGuia.setVisible(true);
            this.labelGuia.setText(guia);
            this.labelGuia.setForeground(Color.RED);
            setGuiaInvis();
        });
    }
 
    public void setGuiaExito(String guia) {
        VistasUtils.enEDT(() -> {
            this.labelGuia.setVisible(true);
            this.labelGuia.setText(guia);
            this.labelGuia.setForeground(new Color(0, 150, 80));
            setGuiaInvis();
        });
    }
 
    // ── Listener del botón ──
 
    public void setActionListener(ActionListener controlador) {
        this.btnRegistrar.addActionListener(controlador);
    }
 
    // ── Getters / setters del campo ──
 
    public String getDNI() {
        return this.campoDNI.getText().trim();
    }
 
    public void limpiaDNI() {
        this.campoDNI.setText("");
    }
 
}
 