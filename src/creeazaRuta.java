import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

public class creeazaRuta extends JFrame implements WindowListener {
	private mainWindow prevWindow;
    public creeazaRuta(mainWindow prevWindow) {
    	
    	   super("Adaptează o rută");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        addWindowListener(this);

        this.prevWindow = prevWindow;
     

        // obține dimensiunea ecranului
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        // setează dimensiunea și poziția ferestrei
        setBounds(0, 0, screenSize.width, screenSize.height);
        setLocationRelativeTo(null); // poziționează fereastra în centrul ecranului
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
