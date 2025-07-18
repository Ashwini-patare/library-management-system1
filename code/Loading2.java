import javax.swing.*;

public class Loading2 extends javax.swing.JFrame {

    private JProgressBar jProgressBar1;
    private JLabel jLabel;
    
    public Loading2() {
        initComponents();
        
        Thread t = new Thread(new Runnable() {
            public void run() {
                for (int i = 0; i <= 100; i++) {
                    try {
                        final int progress = i;
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                jProgressBar1.setValue(progress);
                                if (progress == 25) {
                                    jLabel.setText("Connecting Database....");
                                } else if (progress == 50) {
                                    jLabel.setText("Loading Modules.....");
                                } else if (progress == 95) {
                                    jLabel.setText("Launching Application....");
                                } else if (progress == 100) {
                                    new SignIn().setVisible(true);
                                    dispose();
                                }
                            }
                        });
                        Thread.sleep(50);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        t.start();
    }

    private void initComponents() {
        jProgressBar1 = new JProgressBar();
        jLabel = new JLabel();
        JPanel jPanel1 = new JPanel();
        JLabel jLabel1 = new JLabel();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setLayout(null);

        jPanel1.setLayout(null);

        jLabel1.setIcon(new ImageIcon(getClass().getResource("/img/Picsart_23-10-30_17-47-04-022.jpg")));
        jLabel1.setBounds(10, 10, 630, 400);
        jPanel1.add(jLabel1);

        jProgressBar1.setBounds(0, 440, 650, 16);
        jProgressBar1.setStringPainted(true);
        jPanel1.add(jProgressBar1);

        jLabel.setBounds(400, 420, 247, 22);
        jPanel1.add(jLabel);

        jPanel1.setBounds(0, 0, 650, 460);
        add(jPanel1);

        setSize(650, 460);
        setLocationRelativeTo(null);
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Loading2().setVisible(true);
            }
        });
    }
}
