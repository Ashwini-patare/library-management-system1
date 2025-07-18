import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class IssueBook extends JFrame {
    private PreparedStatement pst;
    private ResultSet rs;
    private Connection c;

    private JTextField txtissuedate, txtbookname, txtduedate, txtstudentid, txtid;
    private JButton btnIssue, btnSearch, btnClose;

    public IssueBook() {
        initComponents();
        c = ConnectToDB();
        SimpleDateFormat dat = new SimpleDateFormat("yyyy-MM-dd"); // Format for SQL DATE
        txtissuedate.setText(dat.format(new Date()));
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Issue Book");
        setSize(500, 350);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblId = new JLabel("Book ID:");
        JLabel lblBookName = new JLabel("Book Name:");
        JLabel lblIssueDate = new JLabel("Issue Date:");
        JLabel lblDueDate = new JLabel("Due Date:");
        JLabel lblStudentId = new JLabel("Student ID:");

        txtid = new JTextField(15);
        txtbookname = new JTextField(15);
        txtbookname.setEditable(false);
        txtissuedate = new JTextField(15);
        txtissuedate.setEditable(false);
        txtduedate = new JTextField(15);
        txtduedate.setEditable(false);
        txtstudentid = new JTextField(15);

        btnIssue = new JButton("Issue Book");
        btnSearch = new JButton("Search");
        btnClose = new JButton("Close");

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(lblId, gbc);
        gbc.gridx = 1;
        panel.add(txtid, gbc);
        gbc.gridx = 2;
        panel.add(btnSearch, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(lblBookName, gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(txtbookname, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        panel.add(lblIssueDate, gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(txtissuedate, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        panel.add(lblDueDate, gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(txtduedate, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        panel.add(lblStudentId, gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(txtstudentid, gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        panel.add(btnIssue, gbc);

        gbc.gridy = 6;
        panel.add(btnClose, gbc);

        add(panel, BorderLayout.CENTER);

        btnSearch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                searchBook();
            }
        });

        btnIssue.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                issueBook();
            }
        });

        btnClose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                dispose();
            }
        });
    }

    private void searchBook() {
        if (txtid.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter Book ID first.");
            return;
        }

        try {
            pst = c.prepareStatement("SELECT name, due, status FROM book WHERE id = ?");
            pst.setInt(1, Integer.parseInt(txtid.getText()));
            rs = pst.executeQuery();

            if (rs.next()) {
                txtbookname.setText(rs.getString("name"));
                Date dueDate = rs.getDate("due");

                if (dueDate != null) {
                    txtduedate.setText(new SimpleDateFormat("yyyy-MM-dd").format(dueDate));
                } else {
                    txtduedate.setText("");
                }

                if ("Issued".equalsIgnoreCase(rs.getString("status"))) {
                    JOptionPane.showMessageDialog(this, "Warning: This book is already issued.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "No record found for this Book ID.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pst != null) pst.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void issueBook() {
        if (txtid.getText().equals("") || txtstudentid.getText().equals("") || txtbookname.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Please fill all fields before issuing.");
            return;
        }

        if (txtduedate.getText().trim().isEmpty()) {
            txtduedate.setText(calculateDueDate(txtissuedate.getText()));
        }

        try {
            pst = c.prepareStatement("UPDATE book SET status = 'Issued', issue = ?, due = ?, studentid = ? WHERE id = ?");
            
            pst.setDate(1, java.sql.Date.valueOf(txtissuedate.getText())); // Convert String to SQL Date
            pst.setDate(2, java.sql.Date.valueOf(txtduedate.getText())); // Convert String to SQL Date
            pst.setInt(3, Integer.parseInt(txtstudentid.getText()));
            pst.setInt(4, Integer.parseInt(txtid.getText()));

            int updated = pst.executeUpdate();
            if (updated > 0) {
                JOptionPane.showMessageDialog(this, "Book Issued Successfully!");
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Book ID not found or already issued.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (pst != null) pst.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private String calculateDueDate(String issueDateStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date issueDate = sdf.parse(issueDateStr);
            Calendar cal = Calendar.getInstance();
            cal.setTime(issueDate);
            cal.add(Calendar.DAY_OF_MONTH, 15);
            return sdf.format(cal.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private void clearFields() {
        txtbookname.setText("");
        txtduedate.setText("");
        txtissuedate.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        txtstudentid.setText("");
        txtid.setText("");
    }

    public static Connection ConnectToDB() {
        Connection conn = null;
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/library", "postgres", "");
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return conn;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new IssueBook().setVisible(true);
            }
        });
    }
}
