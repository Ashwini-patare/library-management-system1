import java.sql.*;
import javax.swing.*;

public class SignIn extends JFrame {

    private JTextField txtemail;
    private JPasswordField txtpassword;
    private JButton btnlogin;
    private JLabel lblBackground;

    public SignIn() {
        initComponents();
        setLocationRelativeTo(null); // Center the window
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Sign In");
        setSize(400, 300);
        setLayout(null); // Use absolute positioning

        // Load background image
        ImageIcon backgroundImage = new ImageIcon(getClass().getResource("/img/All Page Backgraound.jpg"));
        lblBackground = new JLabel(backgroundImage);
        lblBackground.setBounds(0, 0, 400, 300);
        add(lblBackground);

        // Labels and fields
        JLabel lblEmail = new JLabel("User ID:");
        lblEmail.setBounds(50, 50, 100, 25);
        lblBackground.add(lblEmail); // Add to background label

        txtemail = new JTextField();
        txtemail.setBounds(150, 50, 200, 25);
        lblBackground.add(txtemail);

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setBounds(50, 100, 100, 25);
        lblBackground.add(lblPassword);

        txtpassword = new JPasswordField();
        txtpassword.setBounds(150, 100, 200, 25);
        lblBackground.add(txtpassword);

        btnlogin = new JButton("Login");
        btnlogin.setBounds(150, 150, 100, 30);
        lblBackground.add(btnlogin);

        btnlogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnloginActionPerformed();
            }
        });

        setVisible(true);
    }

    private void btnloginActionPerformed() {
        String userID = txtemail.getText();
        String password = new String(txtpassword.getPassword());

        if (userID.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "User ID and Password cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            Class.forName("org.postgresql.Driver"); // Load PostgreSQL driver
            con = DriverManager.getConnection("jdbc:postgresql://localhost/library", "postgres", "");

            String sql = "SELECT * FROM login WHERE userid = ? AND password = ?";
            pst = con.prepareStatement(sql);
            pst.setString(1, userID);
            pst.setString(2, password);

            rs = pst.executeQuery();
            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Login Successful!");
                new home().setVisible(true);
                dispose(); // Close login window
            } else {
                JOptionPane.showMessageDialog(this, "Invalid User ID or Password", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }

        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "JDBC Driver Not Found", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Connection Failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (rs != null) rs.close();
                if (pst != null) pst.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new SignIn();
    }
}
