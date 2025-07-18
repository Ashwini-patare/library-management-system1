import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Date;
import javax.swing.JOptionPane;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddBook extends javax.swing.JFrame {
    public AddBook() {
        initComponents();
    }

    public void clear() {
        txtname.setText("");
        txtauthor.setText("");
        txtstatus.setText("NotIssue");  // Default status
        txtissue.setText("");
        txtdue.setText("");
        txtstudentid.setText("");
    }

    private void initComponents() {
        btnSave = new javax.swing.JButton();
        txtname = new javax.swing.JTextField();
        txtauthor = new javax.swing.JTextField();
        txtstatus = new javax.swing.JTextField();
        txtissue = new javax.swing.JTextField();
        txtdue = new javax.swing.JTextField();
        txtstudentid = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setLayout(null);

        btnSave.setText("Save");
        btnSave.setBounds(330, 400, 110, 30);
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        add(btnSave);

        javax.swing.JLabel lblName = new javax.swing.JLabel("Book Name");
        lblName.setBounds(130, 50, 100, 30);
        add(lblName);
        txtname.setBounds(250, 50, 200, 30);
        add(txtname);

        javax.swing.JLabel lblAuthor = new javax.swing.JLabel("Author");
        lblAuthor.setBounds(130, 100, 100, 30);
        add(lblAuthor);
        txtauthor.setBounds(250, 100, 200, 30);
        add(txtauthor);

        javax.swing.JLabel lblStatus = new javax.swing.JLabel("Status");
        lblStatus.setBounds(130, 150, 100, 30);
        add(lblStatus);
        txtstatus.setBounds(250, 150, 200, 30);
        add(txtstatus);

        javax.swing.JLabel lblIssue = new javax.swing.JLabel("Issue Date (YYYY-MM-DD)");
        lblIssue.setBounds(130, 200, 180, 30);
        add(lblIssue);
        txtissue.setBounds(250, 200, 200, 30);
        add(txtissue);

        javax.swing.JLabel lblDue = new javax.swing.JLabel("Due Date (YYYY-MM-DD)");
        lblDue.setBounds(130, 250, 180, 30);
        add(lblDue);
        txtdue.setBounds(250, 250, 200, 30);
        add(txtdue);

        javax.swing.JLabel lblStudentId = new javax.swing.JLabel("Student ID");
        lblStudentId.setBounds(130, 300, 100, 30);
        add(lblStudentId);
        txtstudentid.setBounds(250, 300, 200, 30);
        add(txtstudentid);

        setSize(600, 500);
        setVisible(true);
    }

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {
        Connection conn = null;
        PreparedStatement pst = null;

        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/library", "postgres", ""); // Change username & password if needed

            if (txtname.getText().equals("")) {
                JOptionPane.showMessageDialog(this, "Please enter Book Name");
                txtname.requestFocus();
                return;
            }

            if (txtauthor.getText().equals("")) {
                JOptionPane.showMessageDialog(this, "Please enter Author Name");
                txtauthor.requestFocus();
                return;
            }

            String sql = "INSERT INTO book (name, author, status, issue, due, studentid) VALUES (?, ?, ?, ?, ?, ?)";
            pst = conn.prepareStatement(sql);
            pst.setString(1, txtname.getText());
            pst.setString(2, txtauthor.getText());
            pst.setString(3, txtstatus.getText().isEmpty() ? "NotIssue" : txtstatus.getText());

            // Handle issue date
            if (txtissue.getText().isEmpty()) {
                pst.setNull(4, java.sql.Types.DATE);
            } else {
                try {
                    pst.setDate(4, Date.valueOf(txtissue.getText()));
                } catch (IllegalArgumentException e) {
                    JOptionPane.showMessageDialog(this, "Invalid issue date format. Use YYYY-MM-DD.");
                    return;
                }
            }

            // Handle due date
            if (txtdue.getText().isEmpty()) {
                pst.setNull(5, java.sql.Types.DATE);
            } else {
                try {
                    pst.setDate(5, Date.valueOf(txtdue.getText()));
                } catch (IllegalArgumentException e) {
                    JOptionPane.showMessageDialog(this, "Invalid due date format. Use YYYY-MM-DD.");
                    return;
                }
            }

            // Handle student ID
            if (txtstudentid.getText().isEmpty()) {
                pst.setNull(6, java.sql.Types.INTEGER);
            } else {
                try {
                    pst.setInt(6, Integer.parseInt(txtstudentid.getText()));
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Invalid Student ID. Enter a valid number.");
                    return;
                }
            }

            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Record Saved Successfully");
            clear();

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AddBook.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Database Driver Not Found");
        } catch (SQLException ex) {
            Logger.getLogger(AddBook.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Error saving data: " + ex.getMessage());
        } finally {
            try {
                if (pst != null) pst.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(AddBook.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void main(String[] args) {
        new AddBook();
    }

    private javax.swing.JButton btnSave;
    private javax.swing.JTextField txtname;
    private javax.swing.JTextField txtauthor;
    private javax.swing.JTextField txtstatus;
    private javax.swing.JTextField txtissue;
    private javax.swing.JTextField txtdue;
    private javax.swing.JTextField txtstudentid;
}
