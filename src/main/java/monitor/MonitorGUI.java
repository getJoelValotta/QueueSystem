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
    private static final int MAX_HISTORIAL = 5;

    // Colores modernos para consistencia visual con FlatLaf
    private final Color COLOR_ACCENT = new Color(0, 102, 204); // Azul corporativo elegante
    private final Color COLOR_TEXT_MUTED = new Color(120, 120, 120); // Gris para historial

    public MonitorGUI() {
        // Inicializar el Look & Feel de manera segura
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            System.err.println("No se pudo aplicar FlatLightLaf en el Monitor");
        }

        setTitle("Pantalla de Turnos - Sala de Espera");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 650);
        setMinimumSize(new Dimension(450, 550));
        setLocationRelativeTo(null);

        inicializarComponentes();
    }

    private void inicializarComponentes() {
        // Panel principal con márgenes limpios
        JPanel panelContenedor = new JPanel(new GridBagLayout());
        panelContenedor.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;

        lblTurnos = new JLabel[MAX_HISTORIAL];
        lblPuestos = new JLabel[MAX_HISTORIAL];
        panelesFila = new JPanel[MAX_HISTORIAL * 2]; // Preservamos tamaño original para compatibilidad

        int panelIndex = 0;

        for (int i = 0; i < MAX_HISTORIAL; i++) {
            gbc.gridy = i * 2; // Multiplicamos para dejar espacio si ponemos separadores
            gbc.weighty = (i == 0) ? 0.1 : (i == 1 ? 0.3 : 0.15); // La fila 1 (llamado activo) es más alta

            // --- PANEL IZQUIERDO: PUESTO ---
            JPanel panelPuesto = new JPanel(new GridBagLayout());
            lblPuestos[i] = new JLabel(i == 0 ? "PUESTO" : "-", SwingConstants.CENTER);
            
            // --- PANEL DERECHO: TURNO ---
            JPanel panelTurno = new JPanel(new GridBagLayout());
            lblTurnos[i] = new JLabel(i == 0 ? "TURNO" : "-", SwingConstants.CENTER);

            // --- APLICACIÓN DE JERARQUÍA VISUAL (ESTILOS POR FILA) ---
            if (i == 0) {
                // Fila de Encabezados
                lblPuestos[i].setFont(new Font("Segoe UI", Font.BOLD, 16));
                lblTurnos[i].setFont(new Font("Segoe UI", Font.BOLD, 16));
                lblPuestos[i].setForeground(COLOR_ACCENT);
                lblTurnos[i].setForeground(COLOR_ACCENT);
                panelPuesto.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, COLOR_ACCENT));
                panelTurno.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, COLOR_ACCENT));
            } else if (i == 1) {
                // FILA DESTACADA: Último turno llamado (Mas grande y vistoso)
                lblPuestos[i].setFont(new Font("Segoe UI", Font.BOLD, 26));
                lblTurnos[i].setFont(new Font("Segoe UI", Font.BOLD, 36));
                panelPuesto.setBackground(UIManager.getColor("Component.background"));
                panelTurno.setBackground(UIManager.getColor("Component.background"));
                // Sutil borde para dar efecto de "Tarjeta o Card" destacado
                panelPuesto.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 0, Color.LIGHT_GRAY));
                panelTurno.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 1, Color.LIGHT_GRAY));
            } else {
                // FILAS DE HISTORIAL: Turnos anteriores (Más pequeños y opacos)
                lblPuestos[i].setFont(new Font("Segoe UI", Font.PLAIN, 18));
                lblTurnos[i].setFont(new Font("Segoe UI", Font.PLAIN, 20));
                lblPuestos[i].setForeground(COLOR_TEXT_MUTED);
                lblTurnos[i].setForeground(COLOR_TEXT_MUTED);
                
                // Zebra striping para facilitar la lectura del historial
                Color fondoZebra = (i % 2 == 0) ? UIManager.getColor("Table.alternateRowBackground") : panelContenedor.getBackground();
                panelPuesto.setBackground(fondoZebra);
                panelTurno.setBackground(fondoZebra);
            }

            // Añadir JLabels a sus respectivos subpaneles
            panelPuesto.add(lblPuestos[i]);
            panelTurno.add(lblTurnos[i]);

            // Guardar en la estructura original para compatibilidad con el resto del sistema
            panelesFila[panelIndex++] = panelPuesto;
            panelesFila[panelIndex++] = panelTurno;

            // Añadir al panel contenedor principal
            gbc.insets = new Insets(2, 0, 2, 0);
            gbc.gridx = 0; gbc.weightx = 0.4;
            panelContenedor.add(panelPuesto, gbc);
            
            gbc.gridx = 1; gbc.weightx = 0.6;
            panelContenedor.add(panelTurno, gbc);
            
            // Colocar un separador estético debajo de la fila destacado para marcar corte con el historial
            if (i == 1) {
                gbc.gridy = (i * 2) + 1;
                gbc.gridx = 0; gbc.gridwidth = 2;
                gbc.weighty = 0.0;
                gbc.insets = new Insets(8, 0, 8, 0);
                panelContenedor.add(new JSeparator(), gbc);
                gbc.gridwidth = 1; // Restaurar ancho
            }
        }

        this.add(panelContenedor);
    }

    public void registrarLlamado(String dni, String puesto) {
        int posicionExistente = -1;
        int i = 1;
        int limite;

        // 1. Buscar si el DNI ya está en pantalla (Lógica original preservada)
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

        // Disparar animación moderna sin romper las dimensiones de la tabla
        ejecutarAnimacionPulso(lblTurnos[1], lblPuestos[1]);
    }

    /**
     * Animación optimizada de pulso. Alerta visualmente combinando tamaño 
     * y color de alerta intermitente sin causar desajustes de Layout.
     */
    private void ejecutarAnimacionPulso(JLabel labelDni, JLabel labelPuesto) {
        Font fuenteOriginal = new Font("Segoe UI", Font.BOLD, 36); 
        Font fuentePuestoOriginal = new Font("Segoe UI", Font.BOLD, 26);
        
        Font fuenteGrande = new Font("Segoe UI", Font.BOLD, 42);   
        Font fuentePuestoGrande = new Font("Segoe UI", Font.BOLD, 30);   
        
        Color colorOriginal = UIManager.getColor("Label.foreground");
        Color colorAlerta = COLOR_ACCENT;
        
        Timer timer = new Timer(250, null); 
        timer.addActionListener(new ActionListener() {
            int contador = 0;
            @Override
            public void actionPerformed(ActionEvent e) {
                if (contador % 2 == 0) {
                    labelDni.setFont(fuenteGrande);
                    labelPuesto.setFont(fuentePuestoGrande);
                    labelDni.setForeground(colorAlerta);
                    labelPuesto.setForeground(colorAlerta);
                } else {
                    labelDni.setFont(fuenteOriginal);
                    labelPuesto.setFont(fuentePuestoOriginal);
                    labelDni.setForeground(colorOriginal);
                    labelPuesto.setForeground(colorOriginal);
                }
                
                contador++;
                if (contador >= 8) { // 4 parpadeos completos de atención
                    timer.stop();
                    labelDni.setFont(fuenteOriginal); 
                    labelPuesto.setFont(fuentePuestoOriginal);
                    labelDni.setForeground(colorOriginal);
                    labelPuesto.setForeground(colorOriginal);
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