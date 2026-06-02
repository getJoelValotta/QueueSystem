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
        setTitle("Tótem");
        setLayout(new BorderLayout(0, 0));
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
 
        // padding general
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(8, 8, 8, 8));
 
        // ── Norte: label guía ──
        this.panelAuxTxtGuia = new JPanel();
        add(this.panelAuxTxtGuia, BorderLayout.NORTH);
 
        this.labelGuia = new JLabel(" ");
        this.panelAuxTxtGuia.add(this.labelGuia);
 
        // ── Centro: campo DNI ──
        this.panelAuxCampoDNI = new JPanel();
        add(this.panelAuxCampoDNI, BorderLayout.CENTER);
 
        this.panelAuxTxtDNI = new JPanel();
        this.panelAuxCampoDNI.add(this.panelAuxTxtDNI);
 
        this.textDNI = new JTextPane();
        this.textDNI.setText("DNI");
        this.textDNI.setEditable(false);
        this.textDNI.setBackground(null);
        this.panelAuxTxtDNI.add(this.textDNI);
 
        this.campoDNI = new JTextField();
        this.campoDNI.setColumns(10);
        this.panelAuxCampoDNI.add(this.campoDNI);
 
        // ── Validación de caracteres: solo dígitos, máximo 8 ──
        ((AbstractDocument) this.campoDNI.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                    throws BadLocationException {
                if (string == null) return;
                if (string.matches("\\d+") && fb.getDocument().getLength() + string.length() <= 8) {
                    super.insertString(fb, offset, string, attr);
                }
            }
 
            @Override
            public void replace(FilterBypass fb, int offset, int length, String string, AttributeSet attr)
                    throws BadLocationException {
                if (string == null) return;
                if (string.matches("\\d+") && fb.getDocument().getLength() - length + string.length() <= 8) {
                    super.replace(fb, offset, length, string, attr);
                }
            }
        });
 

        
        // ── Habilitar / deshabilitar botón según longitud (7 u 8 dígitos) ──
        this.campoDNI.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e)  { actualizarBoton(); }
            @Override public void removeUpdate(DocumentEvent e)  { actualizarBoton(); }
            @Override public void changedUpdate(DocumentEvent e) { actualizarBoton(); }
 
            private void actualizarBoton() {
                int largo = campoDNI.getText().trim().length();
                btnRegistrar.setEnabled(largo == 7 || largo == 8);
            }
        });
 
        // ── Sur: botón Registrar (deshabilitado por defecto) ──
        this.panelAuxBtnRegistrar = new JPanel();
        add(this.panelAuxBtnRegistrar, BorderLayout.SOUTH);
 
        this.btnRegistrar = new JButton("Registrar");
        this.btnRegistrar.setActionCommand(REGISTRAR);
        this.btnRegistrar.setEnabled(false);
        this.panelAuxBtnRegistrar.add(this.btnRegistrar);
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
 