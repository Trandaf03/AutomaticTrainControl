import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Ruta extends JFrame implements WindowListener {

    private JPanel buttonPanel, drawingPanel;
    private JTextField startXField, startYField, endXField, endYField;
    private JButton drawButton, saveButton, resetButton, loadButton;
    private ArrayList<coordonate_linie> ruteSalvate;
    private JList<coordonate_linie> ruteList;
    private DefaultListModel<coordonate_linie> ruteListModel;
    private mainWindow prevWindow;
    private Point startPoint, endPoint; // Store the starting and ending points of the line

    public Ruta(mainWindow prevWindow) {
        super("Creează o rută nouă");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        addWindowListener(this);

        this.prevWindow = prevWindow;

		// Panoul cu butoane
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(50, 1, 5, 5));
		startXField = new JTextField();
		startYField = new JTextField();
		endXField = new JTextField();
		endYField = new JTextField();
		drawButton = new JButton("Desenează");
		saveButton = new JButton("Salvează");
		resetButton = new JButton("Resetează");
		loadButton = new JButton("Încarcă");
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
		buttonPanel.add(resetButton);
		buttonPanel.add(loadButton);
		getContentPane().add(buttonPanel, BorderLayout.WEST);

		// Listener pentru butonul de reset
		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Sterge toate coordonatele și resetați panoul de desenare
				ruteListModel.clear();
				ruteSalvate.clear();
				drawingPanel.repaint();
			}
		});

		// Panoul de desenare
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

		// Lista cu rute salvate
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

		JPanel listPanel = new JPanel();
		listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.PAGE_AXIS));
		listPanel.add(new JLabel("Lista cu rute salvate:"));
		listPanel.add(ruteList);

		JScrollPane scrollPane = new JScrollPane(listPanel);
		scrollPane.setPreferredSize(new Dimension(200, 0));
		getContentPane().add(scrollPane, BorderLayout.EAST);

		JPanel buttonWrapper = new JPanel();
		buttonWrapper.setLayout(new BoxLayout(buttonWrapper, BoxLayout.PAGE_AXIS));
		buttonWrapper.add(new JLabel());
		buttonWrapper.add(buttonPanel);
		getContentPane().add(buttonWrapper, BorderLayout.WEST);

		drawingPanel.setPreferredSize(new Dimension(500, 0));
		drawingPanel.setBackground(Color.WHITE);
		JPanel drawingWrapper = new JPanel();
		drawingWrapper.setLayout(new BoxLayout(drawingWrapper, BoxLayout.PAGE_AXIS));
		drawingWrapper.add(new JLabel("Zona de desenat:"));
		drawingWrapper.add(drawingPanel);
		getContentPane().add(drawingWrapper, BorderLayout.CENTER);

		
		drawingPanel.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                startPoint = e.getPoint();
            }

            public void mouseReleased(MouseEvent e) {
                endPoint = e.getPoint();
                coordonate_linie rutaNoua = new coordonate_linie(startPoint.x, startPoint.y, endPoint.x, endPoint.y);
                ruteSalvate.add(rutaNoua);
                ruteListModel.addElement(rutaNoua);
                redrawLines(ruteListModel, ruteSalvate, drawingPanel);
            }
        });

        // Mouse motion listener for drawing temporary lines
        drawingPanel.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                endPoint = e.getPoint();
                redrawLines(ruteListModel, ruteSalvate, drawingPanel);
                Graphics g = drawingPanel.getGraphics();
                g.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y);
                g.dispose();
            }
        });

        // Rest of the code...
    
		
		// Listener pentru butonul de desenare
		drawButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int startX = Integer.parseInt(startXField.getText());
				int startY = Integer.parseInt(startYField.getText());
				int endX = Integer.parseInt(endXField.getText());
				int endY = Integer.parseInt(endYField.getText());

				Graphics g = drawingPanel.getGraphics();
				g.drawLine(startX, startY, endX, endY);
				g.dispose();

				coordonate_linie rutaNoua = new coordonate_linie(startX, startY, endX, endY);
				ruteSalvate.add(rutaNoua);
				ruteListModel.addElement(rutaNoua);

				startXField.setText(String.valueOf(endX));
				startYField.setText(String.valueOf(endY));
				endXField.setText("");
				endYField.setText("");
			}
		});

		// Listener pentru butonul de salvare a rutei
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					// Create a File object with the path to the input file
					File inputFile = new File(
							"C:\\Users\\andre\\eclipse-workspace\\ControlulAutonomAlTrenurilor\\src\\Route_names.txt");

					// Create a Scanner object to read the input file
					Scanner scanner = new Scanner(inputFile);

					int maxRouteNumber = 0;
					String lastRouteLine = null;
					StringBuilder fileContent = new StringBuilder();

					// Read each line from the input file and find the maximum route number and the
					// last route line
					while (scanner.hasNextLine()) {
						String line = scanner.nextLine();
						if (line.matches("Route\\d+")) {
							int routeNumber = Integer.parseInt(line.substring(5));
							if (routeNumber > maxRouteNumber) {
								maxRouteNumber = routeNumber;
							}
							lastRouteLine = line;
						}
						fileContent.append(line).append("\n");
					}

					// Increment the maximum route number
					int newRouteNumber = maxRouteNumber + 1;

					if (ruteSalvate.size() > 0) {
						// Create a new file for the new route
						File newRouteFile = new File(
								"C:\\Users\\andre\\eclipse-workspace\\ControlulAutonomAlTrenurilor\\src\\Route"
										+ newRouteNumber + ".txt");
						FileWriter writerNewFile = new FileWriter(newRouteFile);

						// Write the modified route lines to the new file
						for (coordonate_linie ptDesen : ruteSalvate) {
							writerNewFile.write(ptDesen.getX1() + " " + ptDesen.getY1() + " " + ptDesen.getX2() + " "
									+ ptDesen.getY2() + "\n");
						}

						// Write the original text back to the input file
						FileWriter writer = new FileWriter(inputFile);
						writer.write(fileContent.toString());

						// Write the last route line to the input file
						writer.write("Route" + newRouteNumber + "\n");

						// Close the writers
						writer.close();
						writerNewFile.close();

						// Show a message dialog indicating success
						JOptionPane.showMessageDialog(null, "Ruta a fost salvata cu succes.", "Succes",
								JOptionPane.INFORMATION_MESSAGE);
					} else {
						// Show an error message dialog if no routes have been saved
						JOptionPane.showMessageDialog(null, "Nu ai pus nici-o ruta ca sa o salvezi", "Eroare",
								JOptionPane.ERROR_MESSAGE);
					}

					// Close the scanner
					scanner.close();

				} catch (IOException e1) {
					// If there's an error, handle the exception here
					e1.printStackTrace();
				}
			}
		});

		loadButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
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

		setVisible(true);
	}

	public static void main(String[] args) {
		mainWindow parent = new mainWindow();
		parent.setVisible(true);
		Ruta rute = new Ruta(parent);
		parent.setVisible(false);
		rute.addWindowListener(new WindowAdapter() {
			public void windowClosed(WindowEvent e) {
				parent.setVisible(true);
			}
		});
		rute.setVisible(true);
	}

	public void redrawLines(DefaultListModel ruteListModel, ArrayList<coordonate_linie> ruteSalvate, JPanel drawingPanel) {
	    Graphics2D g2d = (Graphics2D) drawingPanel.getGraphics();
	    g2d.clearRect(0, 0, drawingPanel.getWidth(), drawingPanel.getHeight());
	    
	    // Set the stroke width
	    float strokeWidth = 3.0f;  // Adjust this value to make the lines thicker
	    g2d.setStroke(new BasicStroke(strokeWidth));
	    
	    for (coordonate_linie ptDesen : ruteSalvate) {
	        g2d.drawLine(ptDesen.getX1(), ptDesen.getY1(), ptDesen.getX2(), ptDesen.getY2());
	    }
	    g2d.dispose();
	}


	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosed(WindowEvent e) {
		prevWindow.setVisible(true);
		dispose();
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

}
