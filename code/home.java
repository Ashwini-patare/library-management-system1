import javax.swing.*;

public class home extends JFrame {
    private JButton jButton1, jButton2, jButton3, jButton4, jButton5, jButton6;
    private JLabel jLabel2;

    public home() {
        initComponents();
    }

    private void initComponents() {
        jButton1 = new JButton("Logout");
        jButton2 = new JButton("Issue Books");
        jButton3 = new JButton("Return Book");
        jButton4 = new JButton("Add Book");
        jButton5 = new JButton("Student Registration");
        jButton6 = new JButton("Close");
        jLabel2 = new JLabel();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setSize(1370, 770);
        setLocationRelativeTo(null);

        // Load background image from src/img folder
        ImageIcon background = new ImageIcon(getClass().getClassLoader().getResource("img/home page.jpg"));
        jLabel2.setIcon(background);
        jLabel2.setBounds(0, 0, 1370, 770);

        // Logout button
        jButton1.setBounds(1160, 620, 180, 60);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutAction();
            }
        });
        add(jButton1);

        // Issue Books
        jButton2.setBounds(1160, 360, 180, 60);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                new IssueBook().setVisible(true);
            }
        });
        add(jButton2);

        // Return Book
        jButton3.setBounds(1160, 480, 180, 60);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                new ReturnBook().setVisible(true);
            }
        });
        add(jButton3);

        // Add Book
        jButton4.setBounds(1160, 240, 180, 60);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                new AddBook().setVisible(true);
            }
        });
        add(jButton4);

        // Student Registration
        jButton5.setBounds(1160, 80, 180, 60);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                new StudentRegistration().setVisible(true);
            }
        });
        add(jButton5);

        // Close button
        jButton6.setBounds(1320, 0, 50, 50);
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeApplication();
            }
        });
        add(jButton6);

        // Add background label at the end so it stays behind other components
        add(jLabel2);

        setVisible(true);
    }

    private void logoutAction() {
        int yes = JOptionPane.showConfirmDialog(this, "Are you sure you want to log out?", "Logout", JOptionPane.YES_NO_OPTION);
        if (yes == JOptionPane.YES_OPTION) {
            new SignIn().setVisible(true);
            dispose();
        }
    }

    private void closeApplication() {
        int yes = JOptionPane.showConfirmDialog(this, "Are you sure you want to exit?", "Exit", JOptionPane.YES_NO_OPTION);
        if (yes == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new home().setVisible(true);
            }
        });
    }
}
