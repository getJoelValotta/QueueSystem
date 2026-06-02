package puesto;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

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


	public PuestoGUI() {
		setLayout(new BorderLayout(0, 0));
		this.setSize(400,200);
        this.setLocationRelativeTo(null);
		
		this.panelAuxBtn = new JPanel();
		add(this.panelAuxBtn, BorderLayout.CENTER);
		this.panelAuxBtn.setLayout(new GridLayout(0, 2, 0, 0));
		
		this.panelAuxLlamar = new JPanel();
		this.panelAuxBtn.add(this.panelAuxLlamar);
		this.panelAuxLlamar.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		this.btnLlamar = new JButton("Llamar siguiente");
		this.panelAuxLlamar.add(this.btnLlamar);
		this.btnLlamar.setActionCommand(LLAMAR);
		
		this.panelAuxReLlamar = new JPanel();
		this.panelAuxBtn.add(this.panelAuxReLlamar);
		
		this.btnRenotificar = new JButton("Re-notificar");
		this.panelAuxReLlamar.add(this.btnRenotificar);
		this.btnRenotificar.setActionCommand(RENOTIFICAR);

		
		this.panelAuxLblLista = new JPanel();
		add(this.panelAuxLblLista, BorderLayout.SOUTH);
		
		this.lblCantClientesEspera = new JLabel("");
		this.lblCantClientesEspera.setFont(new Font("Segoe UI Variable", Font.PLAIN, 21));
		this.panelAuxLblLista.add(this.lblCantClientesEspera);

		this.panelAuxNorte = new JPanel();
		add(this.panelAuxNorte, BorderLayout.NORTH);
		this.panelAuxNorte.setLayout(new BorderLayout(0, 0));

		this.lblClienteActual = new JLabel("");
		this.lblClienteActual.setFont(new Font("Segoe UI Variable", Font.PLAIN, 21));
		this.panelAuxNorte.add(this.lblClienteActual, BorderLayout.CENTER);
		
		this.lblNumPuesto = new JLabel("");
		this.lblNumPuesto.setFont(new Font("Tahoma", Font.BOLD, 20));
		this.panelAuxNorte.add(this.lblNumPuesto, BorderLayout.WEST);


	}

    public void mostrar(){
        VistasUtils.enEDT(() -> this.setVisible(true));
    }

    public void cerrar(){
        VistasUtils.enEDT(() -> this.dispose());
    }
	
	public void setCantClientes(int clientes) {
        VistasUtils.enEDT(() -> this.lblCantClientesEspera.setText("Hay "+ clientes +" en cola"));
	}
	
	public void inhabilitarBtn() { //para cuando no haya clientes en cola
		this.btnLlamar.setEnabled(false);
		this.btnRenotificar.setEnabled(false);
	}

	public void cambiaTextRenotificarAabandonado(){
		this.btnRenotificar.setText("Abandonado");
	}

	public void cambiaTextAbandonadoARenotificar(){
		this.btnRenotificar.setText("Re-notificar");
	}

	public void inhabilitaRenotificar(){
		this.btnRenotificar.setEnabled(false);
	}

	public void habilitaRenotificar(){
		this.btnRenotificar.setEnabled(true);
	}

	public void setActionListener(ActionListener controlador) {
        btnLlamar.addActionListener(controlador);
		btnRenotificar.addActionListener(controlador);
    }

	public void setClienteActual(long dni) {
		this.lblClienteActual.setText("Cliente actual: "+ dni);
	
	}

	public void limpiarClienteActual(){
		this.lblClienteActual.setText("");
	}
	
	public void setNumPuesto(int puesto) {
		this.lblNumPuesto.setText("Puesto: "+puesto);

	}

    public static void main(String[] args){
    }

}
