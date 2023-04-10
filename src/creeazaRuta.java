import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class creeazaRuta extends JFrame{

	 private JPanel buttonPanel, drawingPanel;
	 private JTextField startXField, startYField, endXField, endYField;
	 private JButton drawButton, saveButton;
	 private ArrayList<coordonate_linie> ruteSalvate;
	 private JList<coordonate_linie> ruteList;
	 private DefaultListModel<coordonate_linie> ruteListModel;
	 
	 
	 public creeazaRuta() {
		    super("Train Simulator");
		    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		    
		    // Panoul cu butoane
		    buttonPanel = new JPanel();
		    buttonPanel.setLayout(new GridLayout(50, 2, 5, 5));
		    startXField = new JTextField();
		    startYField = new JTextField();
		    endXField = new JTextField();
		    endYField = new JTextField();
		    drawButton = new JButton("Desenează");
		    saveButton = new JButton("Salvează");
		    buttonPanel.add(new JLabel("X start:"));
		    buttonPanel.add(startXField);
		    buttonPanel.add(new JLabel("Y start:"));
		    buttonPanel.add(startYField);
		    buttonPanel.add(new JLabel("X sfarsit:"));
		    buttonPanel.add(endXField);
		    buttonPanel.add(new JLabel("Y sfarsit:"));
		    buttonPanel.add(endYField);
		    buttonPanel.add(drawButton);
		    buttonPanel.add(saveButton);
		    getContentPane().add(buttonPanel, BorderLayout.WEST);
		    setVisible(true);
	 }
	 
	 public static void main(String[] args) {
		    new creeazaRuta();
		  }
}
