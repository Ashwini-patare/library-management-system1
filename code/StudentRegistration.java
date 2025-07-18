import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StudentRegistration extends javax.swing.JFrame {
    public StudentRegistration() {
        initComponents();
    }
    
    public void clear() {
        txtname.setText("");
        txtcourse.setText("");
        txtbranch.setText("");
        txtsem.setText("");
    }
    
    private void initComponents() {
        btnsave = new javax.swing.JButton();
        txtname = new javax.swing.JTextField();
        txtcourse = new javax.swing.JTextField();
        txtbranch = new javax.swing.JTextField();
        txtsem = new javax.swing.JTextField();
        
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setLayout(null);
        
        btnsave.setText("Save");
        btnsave.setBounds(330, 570, 110, 30);
        btnsave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnsaveActionPerformed(evt);
            }
        });
        add(btnsave);
        
        JLabel lblName = new JLabel("Student Name");
        lblName.setBounds(130, 170, 100, 30);
        add(lblName);
        txtname.setBounds(250, 170, 200, 30);
        add(txtname);
        
        JLabel lblCourse = new JLabel("Course");
        lblCourse.setBounds(130, 220, 100, 30);
        add(lblCourse);
        txtcourse.setBounds(250, 220, 200, 30);
        add(txtcourse);
        
        JLabel lblBranch = new JLabel("Branch");
        lblBranch.setBounds(130, 270, 100, 30);
        add(lblBranch);
        txtbranch.setBounds(250, 270, 200, 30);
        add(txtbranch);
        
        JLabel lblSem = new JLabel("Semester");
        lblSem.setBounds(130, 320, 100, 30);
        add(lblSem);
        txtsem.setBounds(250, 320, 200, 30);
        add(txtsem);
        
        setSize(600, 700);
        setVisible(true);
    }
    
    private void btnsaveActionPerformed(java.awt.event.ActionEvent evt) {
        Connection conn = null;
        PreparedStatement pst = null;
        
        try {
            // Load PostgreSQL driver
            Class.forName("org.postgresql.Driver");
            // Establish connection
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/library", "postgres", "");
            
            // Validation
            if (txtname.getText().equals("")) {
                JOptionPane.showMessageDialog(this, "Please enter Student Name");
                txtname.requestFocus();
            } else if (txtcourse.getText().equals("")) {
                JOptionPane.showMessageDialog(this, "Please enter Course");
                txtcourse.requestFocus();
            } else if (txtbranch.getText().equals("")) {
                JOptionPane.showMessageDialog(this, "Please enter Branch");
                txtbranch.requestFocus();
            } else if (txtsem.getText().equals("")) {
                JOptionPane.showMessageDialog(this, "Please enter Semester");
                txtsem.requestFocus();
            } else {
                // Insert query for PostgreSQL (without ID, as it is auto-generated)
                pst = conn.prepareStatement("INSERT INTO student (name, course, branch, semester) VALUES (?, ?, ?, ?)");
                pst.setString(1, txtname.getText());
                pst.setString(2, txtcourse.getText());
                pst.setString(3, txtbranch.getText());
                pst.setInt(4, Integer.parseInt(txtsem.getText())); // Convert String to Integer
                
                pst.executeUpdate();
                
                JOptionPane.showMessageDialog(this, "Record Saved Successfully");
                clear();
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(StudentRegistration.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(StudentRegistration.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number for Semester");
        } finally {
            try {
                if (pst != null) pst.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(StudentRegistration.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public static void main(String[] args) {
        new StudentRegistration();
    }
    
    private javax.swing.JButton btnsave;
    private javax.swing.JTextField txtname;
    private javax.swing.JTextField txtcourse;
    private javax.swing.JTextField txtbranch;
    private javax.swing.JTextField txtsem;
}

