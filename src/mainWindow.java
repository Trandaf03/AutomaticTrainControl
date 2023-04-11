import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.*;

public class mainWindow extends JFrame {

	private ArrayList<coordonate_linie> ruteSalvate;
	private JList<coordonate_linie> ruteList;
	private DefaultListModel<coordonate_linie> ruteListModel;
	private JPanel drawingPanel;
	public mainWindow() {
		super("Train Simulator");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setUndecorated(false);

		// Create the menu bar
		JMenuBar menuBar = new JMenuBar();

		// Create the "File" menu
		JMenu fileMenu = new JMenu("Meniu");
		JMenu routeMenu = new JMenu("Rută");
		
		drawingPanel = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				int gridSize = 10; // set the size of the grid
				g.setColor(Color.LIGHT_GRAY);
				for (int x = 0; x < getWidth(); x += gridSize) {
					for (int y = 0; y < getHeight(); y += gridSize) {
						g.drawLine(x, y, x, y + gridSize);
						g.drawLine(x, y, x + gridSize, y);
					}
				}
			}
		};
		getContentPane().add(drawingPanel, BorderLayout.CENTER);
		
		ruteSalvate = new ArrayList<coordonate_linie>();
		ruteListModel = new DefaultListModel<coordonate_linie>();
		ruteList = new JList<coordonate_linie>(ruteListModel);
		ruteList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				// Verificăm dacă utilizatorul a făcut dublu clic pe elementul selectat și
				// eliminăm elementul dacă a făcut-o
				if (evt.getClickCount() == 2) {
					int index = ruteList.getSelectedIndex();
					if (index != -1) {
						ruteListModel.remove(index);
						ruteSalvate.remove(index);
						redrawLines(ruteListModel, ruteSalvate, drawingPanel);
					}
				}
			}
		});

		// Create the "Creaza ruta" menu item and add an action listener
		JMenuItem createRouteMenuItem = new JMenuItem("Creaza ruta");
		createRouteMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Code to create a new route
				Ruta rute = new Ruta(mainWindow.this);
				rute.addWindowListener(new WindowAdapter() {
					public void windowClosed(WindowEvent e) {
						setVisible(true); // Show the previous window
					}
				});
				setVisible(false); // Hide the current window
				rute.setVisible(true); // Show the new window
			}
		});
		fileMenu.add(createRouteMenuItem);

		// Create the "Adapteaza ruta" menu item and add an action listener
		JMenuItem adjustRouteMenuItem = new JMenuItem("Adapteaza ruta");
		adjustRouteMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Code to adjust an existing route
				creeazaRuta rute = new creeazaRuta(mainWindow.this);
				rute.addWindowListener(new WindowAdapter() {
					public void windowClosed(WindowEvent e) {
						setVisible(true); // Show the previous window
					}
				});
				setVisible(false); // Hide the current window
				rute.setVisible(true); // Show the new window
			}
		});
		fileMenu.add(adjustRouteMenuItem);

		// Create the "Incarca ruta adaptata" menu item and add an action listener
		JMenuItem loadAdjustedRouteMenuItem = new JMenuItem("Incarca ruta adaptata");
		loadAdjustedRouteMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Code to load an adjusted route from a file
				ruteListModel.clear();
				ruteSalvate.clear();
				drawingPanel.repaint();
				JFrame fereastraRute = new JFrame("Selectează o rută");
				fereastraRute.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				fereastraRute.setSize(500, 500);
				fereastraRute.setVisible(true);
				ArrayList<JButton> buttonList;
				buttonList = new ArrayList<>();
				try {
					File inputFile = new File(
							"C:\\Users\\andre\\eclipse-workspace\\ControlulAutonomAlTrenurilor\\src\\Route_names.txt");
					Scanner scanner = new Scanner(inputFile);
					int routeNumber = 0;
					while (scanner.hasNextLine()) {
						String line = scanner.nextLine();
						if (line.matches("Route\\d+")) {
							routeNumber = Integer.parseInt(line.substring(5));
						}
					}
					JPanel panel = new JPanel();
					panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
					for (int i = 0; i < routeNumber; i++) {
						int j = i + 1;
						JButton button = new JButton("Route" + j);
						panel.add(button);
						buttonList.add(button);
						button.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								// Code to execute when the button is clicked
								try {
									File newRouteFile = new File(
											"C:\\Users\\andre\\eclipse-workspace\\ControlulAutonomAlTrenurilor\\src\\Route"
													+ j + ".txt");
									Scanner scanner = new Scanner(newRouteFile);
									int x1, x2, y1, y2;
									while (scanner.hasNextLine()) {
										String line = scanner.nextLine();
										String[] values = line.split(" ");
										x1 = Integer.parseInt(values[0]);
										y1 = Integer.parseInt(values[1]);
										x2 = Integer.parseInt(values[2]);
										y2 = Integer.parseInt(values[3]);
										coordonate_linie coord = new coordonate_linie(x1, y1, x2, y2);
										ruteSalvate.add(coord);
										ruteListModel.addElement(coord);
									}
								} catch (IOException e1) {
									// If there's an error, handle the exception here
									e1.printStackTrace();
								}

								redrawLines(ruteListModel, ruteSalvate, drawingPanel);
								fereastraRute.setVisible(false);
							}
						});
					}
					fereastraRute.getContentPane().add(panel);

					fereastraRute.setVisible(true);

				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}

		});
		
		JMenuItem deleteCanvas = new JMenuItem("Resetează desenul");
		deleteCanvas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ruteListModel.clear();
				ruteSalvate.clear();
				drawingPanel.repaint();
			}
		});
		
		
		routeMenu.add(loadAdjustedRouteMenuItem);
		routeMenu.add(deleteCanvas);
		
		setVisible(true);
		
		

	// Add the "File" menu to the menu bar
	menuBar.add(fileMenu);
	menuBar.add(routeMenu);

	// Set the menu bar for the window
	setJMenuBar(menuBar);

	setVisible(true);}

	public static void main(String[] args) {
		new mainWindow();
	}
	
	
	public void redrawLines(DefaultListModel ruteListModel, ArrayList<coordonate_linie> ruteSalvate,
			JPanel drawingPanel) {
		Graphics g = drawingPanel.getGraphics();
		g.clearRect(0, 0, drawingPanel.getWidth(), drawingPanel.getHeight());
		for (coordonate_linie ptDesen : ruteSalvate) {
			g.drawLine(ptDesen.getX1(), ptDesen.getY1(), ptDesen.getX2(), ptDesen.getY2());
		}
		g.dispose();
	}

}
