import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Container;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReturnBook extends JFrame {
    Connection c;
    PreparedStatement pst;
    ResultSet rs;

    private JButton btnReturn, btnClose, btnSearch, btnSubmitFine;
    private JTextField txtBookID, txtBookName, txtDueDate, txtIssueDate, txtStudentID, txtFine;
    private JLabel lblTitle, lblBookID, lblBookName, lblIssueDate, lblDueDate, lblStudentID, lblFine;

    public ReturnBook() {
        initComponents();
        connect(); // Fixed database connection error
    }

    private void connect() {
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/library", "postgres", "yourpassword");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ReturnBook.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Database Driver Not Found!", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            Logger.getLogger(ReturnBook.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Database Connection Failed!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initComponents() {
        Container container = getContentPane();
        container.setLayout(null);

        lblTitle = new JLabel("Return Book");
        lblTitle.setBounds(180, 20, 150, 30);
        container.add(lblTitle);

        lblBookID = new JLabel("Book ID");
        lblBookID.setBounds(50, 60, 100, 30);
        container.add(lblBookID);

        txtBookID = new JTextField();
        txtBookID.setBounds(150, 60, 200, 30);
        container.add(txtBookID);

        lblStudentID = new JLabel("Student ID");
        lblStudentID.setBounds(50, 100, 100, 30);
        container.add(lblStudentID);

        txtStudentID = new JTextField();
        txtStudentID.setBounds(150, 100, 200, 30);
        container.add(txtStudentID);

        btnSearch = new JButton("Search");
        btnSearch.setBounds(360, 100, 100, 30);
        btnSearch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                searchBook();
            }
        });
        container.add(btnSearch);

        lblBookName = new JLabel("Book Name");
        lblBookName.setBounds(50, 140, 100, 30);
        container.add(lblBookName);

        txtBookName = new JTextField();
        txtBookName.setBounds(150, 140, 200, 30);
        txtBookName.setEditable(false);
        container.add(txtBookName);

        lblIssueDate = new JLabel("Issue Date");
        lblIssueDate.setBounds(50, 180, 100, 30);
        container.add(lblIssueDate);

        txtIssueDate = new JTextField();
        txtIssueDate.setBounds(150, 180, 200, 30);
        txtIssueDate.setEditable(false);
        container.add(txtIssueDate);

        lblDueDate = new JLabel("Due Date");
        lblDueDate.setBounds(50, 220, 100, 30);
        container.add(lblDueDate);

        txtDueDate = new JTextField();
        txtDueDate.setBounds(150, 220, 200, 30);
        txtDueDate.setEditable(false);
        container.add(txtDueDate);

        lblFine = new JLabel("Fine (₹)");
        lblFine.setBounds(50, 260, 100, 30);
        container.add(lblFine);

        txtFine = new JTextField();
        txtFine.setBounds(150, 260, 200, 30);
        txtFine.setEditable(false);
        container.add(txtFine);

        btnReturn = new JButton("Return Book");
        btnReturn.setBounds(100, 300, 120, 30);
        btnReturn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                returnBook();
            }
        });
        container.add(btnReturn);

        btnSubmitFine = new JButton("Submit Fine");
        btnSubmitFine.setBounds(230, 300, 120, 30);
        btnSubmitFine.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                txtFine.setText("0");
                JOptionPane.showMessageDialog(null, "Fine cleared successfully!");
            }
        });
        container.add(btnSubmitFine);

        btnClose = new JButton("Close");
        btnClose.setBounds(360, 300, 100, 30);
        btnClose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                dispose();
            }
        });
        container.add(btnClose);

        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void searchBook() {
        try {
            String bookID = txtBookID.getText().trim();
            String studentID = txtStudentID.getText().trim();

            if (bookID.isEmpty() || studentID.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter both Book ID and Student ID", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            pst = c.prepareStatement("SELECT * FROM book WHERE id=? AND studentid=?");
            pst.setInt(1, Integer.parseInt(bookID));
            pst.setInt(2, Integer.parseInt(studentID));
            rs = pst.executeQuery();

            if (rs.next()) {
                txtBookName.setText(rs.getString("name"));
                txtIssueDate.setText(rs.getString("issue"));
                txtDueDate.setText(rs.getString("due"));

                String dueDateStr = rs.getString("due");
                txtFine.setText(calculateFine(dueDateStr));
            } else {
                JOptionPane.showMessageDialog(this, "No matching record found!", "Not Found", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReturnBook.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void returnBook() {
        try {
            pst = c.prepareStatement("UPDATE book SET status = 'notIssue', issue = NULL, due = NULL, studentid = NULL WHERE id = ?");
            pst.setInt(1, Integer.parseInt(txtBookID.getText()));

            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(this, "Book returned successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                clear();
            } else {
                JOptionPane.showMessageDialog(this, "Error updating book record", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReturnBook.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String calculateFine(String dueDateStr) {
        try {
            if (dueDateStr == null || dueDateStr.isEmpty()) return "0";

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dueDate = sdf.parse(dueDateStr);
            Date currentDate = new Date();

            if (currentDate.after(dueDate)) {
                long diff = currentDate.getTime() - dueDate.getTime();
                long daysLate = diff / (1000 * 60 * 60 * 24);
                return String.valueOf(daysLate * 5);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "0";
    }

    private void clear() {
        txtBookID.setText("");
        txtBookName.setText("");
        txtDueDate.setText("");
        txtIssueDate.setText("");
        txtStudentID.setText("");
        txtFine.setText("");
    }

    public static void main(String args[]) {
        new ReturnBook().setVisible(true);
    }
}
