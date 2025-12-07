package ui;

import main.Main;
import models.Book;
import models.BorrowRecord;
import models.User;
import utils.FileHandler;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ReturnPage extends JPanel {

    private Main mainApp;
    private JTextField bookIdField, bookTitleField, borrowDateField, dueDateField, returnDateField, fineField;

    // stores the borrow record after user finds it
    private BorrowRecord selectedRecord = null;

    private Image backgroundImage;

    //CONSTRUCTOR
    public ReturnPage(Main mainApp) {
        this.mainApp = mainApp;
        backgroundImage = new ImageIcon(getClass().getResource("/bgimages/borrow_returnbg.png")).getImage();
        initComponents();
    }

    // this method is used to draw things on the panel, like our background image.
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }


    private void initComponents() {
        // we use GridBagLayout so we can position and align all the form fields neatly.
        setLayout(new GridBagLayout());

        // MAIN CARD PANEL
        JPanel cardPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(245, 245, 245));
                g2d.fillRect(0, 0, getWidth() - 1, getHeight() - 1);
                g2d.setColor(new Color(200, 200, 200));
                g2d.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
                g2d.dispose();
            }
        };
        cardPanel.setOpaque(false);
        cardPanel.setPreferredSize(new Dimension(850, 630)); 
        cardPanel.setLayout(new BorderLayout());

        // TITLE
        JLabel titleLabel = new JLabel("Return Book", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Verdana", Font.BOLD, 40));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(45, 0, 20, 0));
        cardPanel.add(titleLabel, BorderLayout.NORTH);

        // FORM PANEL
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 25, 5));

        // we use GridBagConstraints to control the position and size of each field.
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 15, 12, 15); 
        gbc.anchor = GridBagConstraints.EAST; 
        gbc.weightx = 0; 
        

        // BOOK ID INPUT + FIND BUTTON - user enters book ID to search for borrowed record
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblBookId = new JLabel("Enter Book ID to Return:");
        lblBookId.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        formPanel.add(lblBookId, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL; 
        bookIdField = new JTextField();
        bookIdField.setPreferredSize(new Dimension(270, 35)); 
        bookIdField.setBorder(BorderFactory.createLineBorder(new Color(150, 150, 150)));
        bookIdField.setFont(new Font("Segoe UI", Font.PLAIN, 16)); 
        bookIdField.setBackground(Color.WHITE);
        formPanel.add(bookIdField, gbc);

        gbc.gridx = 2;
        gbc.fill = GridBagConstraints.NONE; 
        JButton findButton = createFindButton();
        formPanel.add(findButton, gbc);

        // BOOK TITLE
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        JLabel lblTitle = new JLabel("Book Title:");
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        formPanel.add(lblTitle, gbc);

        gbc.gridx = 1; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        bookTitleField = createNonEditableField(350, 35); 
        formPanel.add(bookTitleField, gbc);
        gbc.gridwidth = 1;

        // BORROW DATE
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        JLabel lblBorrowDate = new JLabel("Borrow Date:");
        lblBorrowDate.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        formPanel.add(lblBorrowDate, gbc);

        gbc.gridx = 1; gbc.gridwidth = 2; 
        gbc.fill = GridBagConstraints.HORIZONTAL;
        borrowDateField = createNonEditableField(350, 35); 
        formPanel.add(borrowDateField, gbc);
        gbc.gridwidth = 1;

        // DUE DATE
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        JLabel lblDueDate = new JLabel("Due Date:");
        lblDueDate.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        formPanel.add(lblDueDate, gbc);

        gbc.gridx = 1; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        dueDateField = createNonEditableField(350, 35); 
        formPanel.add(dueDateField, gbc);
        gbc.gridwidth = 1;

        // RETURN DATE (TODAY)
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.fill = GridBagConstraints.NONE;
        JLabel lblReturnDate = new JLabel("Return Date (Today):");
        lblReturnDate.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        formPanel.add(lblReturnDate, gbc);

        gbc.gridx = 1; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        returnDateField = createNonEditableField(350, 35); 
        formPanel.add(returnDateField, gbc);
        gbc.gridwidth = 1;

        // FINE INCURRED
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.fill = GridBagConstraints.NONE;
        JLabel lblFine = new JLabel("Fine Incurred:");
        lblFine.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        formPanel.add(lblFine, gbc);

        gbc.gridx = 1; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        fineField = createNonEditableField(350, 35); 
        formPanel.add(fineField, gbc);

        cardPanel.add(formPanel, BorderLayout.CENTER);

        // BUTTONS
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 40, 0)); 

        JButton confirmButton = createActionButton("CONFIRM RETURN", new Color(0, 128, 0), new Color(0, 100, 0));
        JButton homeButton = createActionButton("BACK TO HOME", new Color(0, 174, 239), new Color(0, 140, 190));

        buttonPanel.add(confirmButton);
        buttonPanel.add(homeButton);

        cardPanel.add(buttonPanel, BorderLayout.SOUTH);

        // add card to center
        GridBagConstraints mainGbc = new GridBagConstraints();
        mainGbc.anchor = GridBagConstraints.CENTER;
        add(cardPanel, mainGbc);

        // attach button listeners
        findButton.addActionListener(e -> findBorrowedBook());
        confirmButton.addActionListener(e -> confirmReturn());
        homeButton.addActionListener(e -> mainApp.switchToPage(Main.HOME_PAGE));
    }

    // create a read-only text field for displaying data
    private JTextField createNonEditableField(int width, int height) {
        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(width, height));
        field.setEditable(false);
        field.setBackground(Color.WHITE);
        field.setBorder(BorderFactory.createLineBorder(new Color(150, 150, 150)));
        field.setFont(new Font("Arial", Font.PLAIN, 16)); 
        return field;
    }

    // create styled Find button with hover effect
    private JButton createFindButton() {
        JButton btn = new JButton("FIND BORROW BOOK");
        btn.setPreferredSize(new Dimension(170, 35)); 
        btn.setBackground(new Color(0, 31, 84));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 13)); 
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder());
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        Color hover = new Color(0, 20, 60);
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(hover);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(new Color(0, 31, 84));
            }
        });

        return btn;
    }

    // create reusable button factory with custom colors
    private JButton createActionButton(String text, Color bg, Color hoverBg) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(200, 45)); 
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 15)); 
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder());
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(hoverBg);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(bg);
            }
        });

        return btn;
    }

    // clear all fields when page loads
    @Override
    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag);
        if (aFlag) {
            clearFields();
        }
    }

    // search for borrowed book by ID and display borrow details
    private void findBorrowedBook() {
        // validate that user entered a book ID
        String bookId = bookIdField.getText().trim();
        if (bookId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a Book ID.", "Input Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        selectedRecord = null;
        Book targetBook = null;

        // search through all records to find a matching borrowed book
        for (BorrowRecord rec : mainApp.getAllRecords()) {
            if (rec.getBookId().equalsIgnoreCase(bookId) && rec.getStatus().equals("Borrowed")) {
                selectedRecord = rec;
                break;
            }
        }

        // if no active borrow record found, show error
        if (selectedRecord == null) {
            JOptionPane.showMessageDialog(this, "No active borrow record found for this Book ID.", "Not Borrowed", JOptionPane.ERROR_MESSAGE);
            clearBookFields();
            return;
        }

        // get the book object to display its title
        targetBook = mainApp.getAllBooks().stream()
                .filter(b -> b.getBookId().equalsIgnoreCase(bookId))
                .findFirst()
                .orElse(null);

        // validate book exists in system
        if (targetBook == null) {
            JOptionPane.showMessageDialog(this, "Book not found in system.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // display book and borrow details
        bookTitleField.setText(targetBook.getTitle());
        borrowDateField.setText(selectedRecord.getBorrowDate().toString());
        dueDateField.setText(selectedRecord.getDueDate().toString());
        returnDateField.setText(LocalDate.now().toString());

        // calculate how many days late the book is and compute fine (₱20 per day)
        long daysOverdue = ChronoUnit.DAYS.between(selectedRecord.getDueDate(), LocalDate.now());
        double fine = daysOverdue > 0 ? daysOverdue * 20.0 : 0.0; 
        fineField.setText(String.format("₱%.2f", fine));
    }

    // process the book return and update records in database
    private void confirmReturn() {
        // make sure a borrow record was found first
        if (selectedRecord == null) {
            JOptionPane.showMessageDialog(this, "Please find a borrowed book first.", "No Record", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // update the record with return date and status
        selectedRecord.setReturnDate(LocalDate.now());
        selectedRecord.setStatus("Returned");
        long daysOverdue = ChronoUnit.DAYS.between(selectedRecord.getDueDate(), LocalDate.now());
        double fine = daysOverdue > 0 ? daysOverdue * 20.0 : 0.0;
        String remarks = fine > 0 ? String.format("Late: ₱%.2f", fine) : "-";
        selectedRecord.setRemarks(remarks);

        // get the book object to mark it as available again
        Book book = mainApp.getAllBooks().stream()
                .filter(b -> b.getBookId().equalsIgnoreCase(selectedRecord.getBookId()))
                .findFirst()
                .orElse(null);
        if (book != null) {
            book.markAvailable();
        }

        // save changes to the database files
        FileHandler.writeBooks(Main.BOOKS_FILE, mainApp.getAllBooks());
        FileHandler.writeBorrowRecords(Main.RECORDS_FILE, mainApp.getAllRecords());

        JOptionPane.showMessageDialog(this,
                "<html><b>Book Returned Successfully!</b><br>" +
                "Fine: " + (fine > 0 ? String.format("₱%.2f", fine) : "None") + "</html>",
                "Return Confirmed", JOptionPane.INFORMATION_MESSAGE);

        // clear all fields after successful return
        clearFields();
    }

    // reset all fields to empty
    private void clearFields() {
        bookIdField.setText("");
        bookTitleField.setText("");
        borrowDateField.setText("");
        dueDateField.setText("");
        returnDateField.setText("");
        fineField.setText("");
        selectedRecord = null;
    }

    // reset all book display fields to empty
    private void clearBookFields() {
        bookTitleField.setText("");
        borrowDateField.setText("");
        dueDateField.setText("");
        returnDateField.setText("");
        fineField.setText("");
    }
}