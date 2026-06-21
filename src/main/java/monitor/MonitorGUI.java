package monitor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import com.formdev.flatlaf.FlatLightLaf;
import shared.VistasUtils;

public class MonitorGUI extends JFrame {

    private JLabel[] lblTurnos;
    private JLabel[] lblPuestos;
    private JPanel[] panelesFila;
    
    // ESPECIFICACIÓN 1: 6 filas en total (1 Encabezado + 5 llamados concurrentes en pantalla)
    private static final int MAX_HISTORIAL = 6;

    // Colores corporativos consistentes con FlatLaf
    private final Color COLOR_ACCENT = new Color(0, 102, 204); // Azul elegante
    private final Color COLOR_TEXT_MUTED = new Color(120, 120, 120); // Gris historial

    public MonitorGUI() {
        // Inicializar Look & Feel de manera segura
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            System.err.println("No se pudo aplicar FlatLightLaf en el Monitor");
        }

        setTitle("Pantalla de Turnos - Sala de Espera");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 720); // Ajustamos la altura para acomodar estéticamente el 5to llamado
        setMinimumSize(new Dimension(450, 600));
        setLocationRelativeTo(null);

        inicializarComponentes();
    }

    private void inicializarComponentes() {
        JPanel panelContenedor = new JPanel(new GridBagLayout());
        panelContenedor.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;

        lblTurnos = new JLabel[MAX_HISTORIAL];
        lblPuestos = new JLabel[MAX_HISTORIAL];
        panelesFila = new JPanel[MAX_HISTORIAL * 2]; // Preservamos tamaño del vector original

        int panelIndex = 0;

        for (int i = 0; i < MAX_HISTORIAL; i++) {
            gbc.gridy = i * 2;
            // Distribución proporcional de alturas en pantalla
            gbc.weighty = (i == 0) ? 0.05 : (i == 1 ? 0.25 : 0.14);

            JPanel panelPuesto = new JPanel(new GridBagLayout());
            lblPuestos[i] = new JLabel(i == 0 ? "PUESTO" : "-", SwingConstants.CENTER);
            
            JPanel panelTurno = new JPanel(new GridBagLayout());
            lblTurnos[i] = new JLabel(i == 0 ? "TURNO" : "-", SwingConstants.CENTER);

            // ── APLICACIÓN DE ESTILOS Y JERARQUÍAS VISUALES ──
            if (i == 0) {
                // Fila 0: Encabezados estáticos
                lblPuestos[i].setFont(new Font("Segoe UI", Font.BOLD, 15));
                lblTurnos[i].setFont(new Font("Segoe UI", Font.BOLD, 15));
                lblPuestos[i].setForeground(COLOR_ACCENT);
                lblTurnos[i].setForeground(COLOR_ACCENT);
                panelPuesto.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, COLOR_ACCENT));
                panelTurno.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, COLOR_ACCENT));
            } else if (i == 1) {
                // Fila 1: ULTIMO LLAMADO (Cajas rígidas para evitar el tembleque)
                lblPuestos[i].setFont(new Font("Segoe UI", Font.BOLD, 26));
                lblTurnos[i].setFont(new Font("Segoe UI", Font.BOLD, 36));
                
                panelPuesto.setBackground(UIManager.getColor("Component.background"));
                panelTurno.setBackground(UIManager.getColor("Component.background"));
                
                panelPuesto.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 0, Color.LIGHT_GRAY));
                panelTurno.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 1, Color.LIGHT_GRAY));
                
                // ESPECIFICACIÓN 2: Dimensiones fijas para que el pulso no mueva el entorno
                panelPuesto.setPreferredSize(new Dimension(160, 90));
                panelTurno.setPreferredSize(new Dimension(240, 90));
            } else {
                // Filas 2 a 5: Historial de llamados anteriores
                lblPuestos[i].setFont(new Font("Segoe UI", Font.PLAIN, 18));
                lblTurnos[i].setFont(new Font("Segoe UI", Font.PLAIN, 20));
                lblPuestos[i].setForeground(COLOR_TEXT_MUTED);
                lblTurnos[i].setForeground(COLOR_TEXT_MUTED);
                
                // Zebra Striping para alta legibilidad
                Color fondoZebra = (i % 2 == 0) ? UIManager.getColor("Table.alternateRowBackground") : panelContenedor.getBackground();
                panelPuesto.setBackground(fondoZebra);
                panelTurno.setBackground(fondoZebra);
            }

            panelPuesto.add(lblPuestos[i]);
            panelTurno.add(lblTurnos[i]);

            panelesFila[panelIndex++] = panelPuesto;
            panelesFila[panelIndex++] = panelTurno;

            // Inserción en el Layout
            gbc.insets = new Insets(2, 0, 2, 0);
            gbc.gridx = 0; gbc.weightx = 0.4;
            panelContenedor.add(panelPuesto, gbc);
            
            gbc.gridx = 1; gbc.weightx = 0.6;
            panelContenedor.add(panelTurno, gbc);
            
            // Separador estético debajo del llamado principal
            if (i == 1) {
                gbc.gridy = (i * 2) + 1;
                gbc.gridx = 0; gbc.gridwidth = 2;
                gbc.weighty = 0.0;
                gbc.insets = new Insets(6, 0, 6, 0);
                panelContenedor.add(new JSeparator(), gbc);
                gbc.gridwidth = 1; // Restaurar ancho unitario
            }
        }

        this.add(panelContenedor);
    }

    public void registrarLlamado(String dni, String puesto) {
        int posicionExistente = -1;
        int i = 1;
        int limite;

        // 1. Buscar si el DNI ya está en pantalla (Lógica original de corrimientos intacta)
        while (i < MAX_HISTORIAL && posicionExistente == -1) {
            if (lblTurnos[i].getText().equals(dni)) {
                posicionExistente = i; 
            }
            i++;
        }

        limite = (posicionExistente != -1) ? posicionExistente : MAX_HISTORIAL - 1;

        for (i = limite; i > 0; i--) {
            lblTurnos[i].setText(lblTurnos[i - 1].getText());
            lblPuestos[i].setText(lblPuestos[i - 1].getText());
        }

        lblTurnos[1].setText(dni);
        lblPuestos[1].setText(puesto);

        // Disparar animación aislada y segura
        ejecutarAnimacionPulso(lblTurnos[1]);
    }

    /**
     * ESPECIFICACIÓN 2: Animación corregida. Aplica ÚNICAMENTE a la información 
     * del turno (labelDni) y frena el reajuste del layout exterior.
     */
    private void ejecutarAnimacionPulso(JLabel labelDni) {
        Font fuenteOriginal = new Font("Segoe UI", Font.BOLD, 36); 
        Font fuenteGrande = new Font("Segoe UI", Font.BOLD, 42);   
        
        Color colorOriginal = UIManager.getColor("Label.foreground");
        Color colorAlerta = COLOR_ACCENT;
        
        Timer timer = new Timer(220, null); 
        timer.addActionListener(new ActionListener() {
            int contador = 0;
            @Override
            public void actionPerformed(ActionEvent e) {
                if (contador % 2 == 0) {
                    labelDni.setFont(fuenteGrande);
                    labelDni.setForeground(colorAlerta);
                } else {
                    labelDni.setFont(fuenteOriginal);
                    labelDni.setForeground(colorOriginal);
                }
                
                contador++;
                if (contador >= 8) { // 4 destellos de atención
                    timer.stop();
                    labelDni.setFont(fuenteOriginal); 
                    labelDni.setForeground(colorOriginal);
                }
            }
        });
        timer.start();
    }

    public void mostrar(){
        VistasUtils.enEDT(() -> this.setVisible(true));
    }

    public void cerrar(){
        VistasUtils.enEDT(() -> this.dispose());
    }
}