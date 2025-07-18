import javax.swing.*;
import java.awt.*;

public class Loading1 extends JFrame {

    public Loading1() {
        initComponents();

        // Run the loading screen thread
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    new Loading2().setVisible(true);
                    dispose(); // Close current window after opening Loading2
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        });
        t.start();
    }

    private void initComponents() {
        jLabel1 = new JLabel();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        getContentPane().setLayout(new BorderLayout());
        ImageIcon icon = new ImageIcon("img/x.jpg"); 
        jLabel1.setIcon(icon);

        getContentPane().add(jLabel1, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null); // Center the window
    }

    public static void main(String args[]) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // Run the UI on Event Dispatch Thread
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Loading1().setVisible(true);
            }
        });
    }

    // Variables declaration
    private JLabel jLabel1;
}
