import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class creeazaRuta extends JFrame implements WindowListener {
	private mainWindow prevWindow;
	private ArrayList<coordonate_linie> ruteSalvate;
	private JList<coordonate_linie> ruteList;
	private DefaultListModel<coordonate_linie> ruteListModel;
	private JPanel drawingPanel;
	private int idRuta = -1;

	public creeazaRuta(mainWindow prevWindow) {

		super("Adaptează o rută");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		addWindowListener(this);

		this.prevWindow = prevWindow;
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
		// obține dimensiunea ecranului
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
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
		
		// setează dimensiunea și poziția ferestrei
		setBounds(0, 0, screenSize.width, screenSize.height);
		setLocationRelativeTo(null); // poziționează fereastra în centrul ecranului

		// crează o bară de meniu
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		// crează un meniu cu două butoane
		JMenu menu = new JMenu("Meniu rute");
		menuBar.add(menu);

		JMenuItem item1 = new JMenuItem("Încarcă rută");
		item1.addActionListener(new ActionListener() {
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
		menu.add(item1);

		JMenuItem item2 = new JMenuItem("Resetează canvas");
		item2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ruteListModel.clear();
				ruteSalvate.clear();
				drawingPanel.repaint();
			}
		});
		menu.add(item2);
		JMenuItem item3 = new JMenuItem("Salvează rută");
		item2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					File newRouteFile = new File(
							"C:\\Users\\andre\\eclipse-workspace\\ControlulAutonomAlTrenurilor\\src\\EditedRoute"
									+ idRuta + ".txt");
					FileWriter writerNewFile = new FileWriter(newRouteFile);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		menu.add(item3);

		setVisible(true);
	}

	public static void main(String[] args) {
		mainWindow parent = new mainWindow();
		parent.setVisible(true);
		creeazaRuta rute = new creeazaRuta(parent);
		parent.setVisible(false);
		rute.addWindowListener(new WindowAdapter() {
			public void windowClosed(WindowEvent e) {
				parent.setVisible(true);
			}
		});
		rute.setVisible(true);
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}

	@Override
	public void windowClosing(WindowEvent e) {
	}

	@Override
	public void windowClosed(WindowEvent e) {
		prevWindow.setVisible(true);
		dispose();
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
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
