import java.awt.*;
import javax.swing.*;

public class mainWindow extends JFrame {
  
  public mainWindow() {
    super("Train Simulator");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    setExtendedState(JFrame.MAXIMIZED_BOTH); 
    setUndecorated(false); 
    
    setVisible(true);
  }
  
  public static void main(String[] args) {
    new mainWindow();
  }
}
