package ui;

import main.Main;
import models.Book;
import models.BorrowRecord;
import models.Member;
import models.User;
import utils.FileHandler;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class BorrowPage extends JPanel {

    private Main mainApp;
    
    private JTextField memberIdField, memberNameField, bookIdField, bookTitleField, authorField, genreField;

    private Book selectedBook = null;
    private Image backgroundImage;

    // CONSTRUCTOR
    public BorrowPage(Main mainApp) {
        this.mainApp = mainApp;
        backgroundImage = new ImageIcon(getClass().getResource("/bgimages/borrow_returnbg.png")).getImage();
        initComponents();
    }

    // this method is special and is used to draw things on the panel, like our background image.
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }

    // this is where we create and set up all the parts of the page (buttons, text).
    private void initComponents() {
        setLayout(new GridBagLayout());

        // MAIN CARD PANEL (sharp corners)
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

        // TITLE LABEL
        JLabel titleLabel = new JLabel("Borrow Book", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI Black", Font.BOLD, 40));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(45, 0, 20, 0));
        cardPanel.add(titleLabel, BorderLayout.NORTH);

        // FORM PANEL
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 25, 5));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.anchor = GridBagConstraints.EAST; // align labels to the right
        gbc.weightx = 0; // labels don't take extra horizontal space

        // MEMBER ID
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblMemberId = new JLabel("Member ID:");
        lblMemberId.setFont(new Font("Segoe UI", Font.PLAIN, 16)); 
        formPanel.add(lblMemberId, gbc);

        gbc.gridx = 1; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL; // make field stretch
        // wider field
        memberIdField = createNonEditableField(350, 35);
        formPanel.add(memberIdField, gbc);
        gbc.gridwidth = 1;

        // MEMBER NAME (first)
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE; // reset fill for label
        JLabel lblMemberName1 = new JLabel("Member Name:");
        lblMemberName1.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        formPanel.add(lblMemberName1, gbc);

        gbc.gridx = 1; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        // wider field
        memberNameField = createNonEditableField(350, 35);
        formPanel.add(memberNameField, gbc);
        gbc.gridwidth = 1;

        // Enter Book ID + FETCH
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        JLabel lblBookId = new JLabel("Enter Book ID:");
        lblBookId.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        formPanel.add(lblBookId, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL; 
        bookIdField = new JTextField();
        // wider book ID field
        bookIdField.setPreferredSize(new Dimension(270, 35));
        bookIdField.setBorder(BorderFactory.createLineBorder(new Color(150, 150, 150)));
        bookIdField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        bookIdField.setBackground(Color.WHITE);
        formPanel.add(bookIdField, gbc);

        gbc.gridx = 2;
        gbc.fill = GridBagConstraints.NONE; // reset fill for button
        JButton fetchButton = createFetchButton();
        formPanel.add(fetchButton, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 3;
        formPanel.add(Box.createVerticalStrut(15), gbc); // add a small gap
        gbc.gridwidth = 1; // reset grid width
        //END GAP

        // BOOK TITLE
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.fill = GridBagConstraints.NONE;
        JLabel lblTitle = new JLabel("Book Title:");
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        formPanel.add(lblTitle, gbc);

        gbc.gridx = 1; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // wider field
        bookTitleField = createNonEditableField(350, 35);
        formPanel.add(bookTitleField, gbc);
        gbc.gridwidth = 1;

        // AUTHOR
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.fill = GridBagConstraints.NONE;
        JLabel lblAuthor = new JLabel("Author:");
        lblAuthor.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        formPanel.add(lblAuthor, gbc);

        gbc.gridx = 1; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // wider field
        authorField = createNonEditableField(350, 35);
        formPanel.add(authorField, gbc);
        gbc.gridwidth = 1;

        // GENRE
        gbc.gridx = 0; gbc.gridy = 6;
        gbc.fill = GridBagConstraints.NONE;
        JLabel lblGenre = new JLabel("Genre:");
        lblGenre.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        formPanel.add(lblGenre, gbc);

        gbc.gridx = 1; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // wider field
        genreField = createNonEditableField(350, 35);
        formPanel.add(genreField, gbc);

        cardPanel.add(formPanel, BorderLayout.CENTER);

        // BOTTOM BUTTONS
        // increased space below the form
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 30, 0));

        JButton borrowButton = createActionButton("BORROW", new Color(0, 128, 0), new Color(0, 100, 0));
        JButton clearButton = createActionButton("CLEAR", new Color(220, 20, 60), new Color(180, 0, 40));
        JButton homeButton = createActionButton("BACK TO HOME", new Color(0, 174, 239), new Color(0, 140, 190));

        buttonPanel.add(borrowButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(homeButton);

        cardPanel.add(buttonPanel, BorderLayout.SOUTH);

        // add card to center
        GridBagConstraints mainGbc = new GridBagConstraints();
        mainGbc.anchor = GridBagConstraints.CENTER;
        add(cardPanel, mainGbc);

        // ACTION LISTENERS
        fetchButton.addActionListener(e -> fetchBookDetails());
        borrowButton.addActionListener(e -> performBorrow());
        clearButton.addActionListener(e -> clearBookFields());
        homeButton.addActionListener(e -> mainApp.switchToPage(Main.HOME_PAGE));
    }

    // create a read-only text field for displaying info
    private JTextField createNonEditableField(int width, int height) {
        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(width, height));
        field.setEditable(false);
        field.setBackground(Color.WHITE);
        field.setBorder(BorderFactory.createLineBorder(new Color(150, 150, 150)));
        field.setFont(new Font("Arial", Font.PLAIN, 16));
        return field;
    }

    // create styled fetch button with hover effect
    private JButton createFetchButton() {
        JButton btn = new JButton("FETCH");
        btn.setPreferredSize(new Dimension(90, 35));
        btn.setBackground(new Color(0, 31, 84));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
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

    // create reusable action buttons with custom colors
    private JButton createActionButton(String text, Color bg, Color hoverBg) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(150, 45));
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

    // auto-fill member details when page is shown
    @Override
    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag);
        if (aFlag) {
            clearBookFields();
            User user = mainApp.getCurrentUser();
            if (user instanceof Member member) {
                memberIdField.setText(member.getLibraryId());
                memberNameField.setText(member.getFullName());
            }
        }
    }

    // search for book by ID and display details
    private void fetchBookDetails() {
        String bookId = bookIdField.getText().trim();
        if (bookId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a Book ID.", "Input Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        selectedBook = mainApp.getAllBooks().stream()
                .filter(b -> b.getBookId().equalsIgnoreCase(bookId))
                .findFirst()
                .orElse(null);

        // make sure a book was selected first
        if (selectedBook == null) {
            JOptionPane.showMessageDialog(this, "Book not found.", "Not Found", JOptionPane.ERROR_MESSAGE);
            clearBookFields();
            return;
        }

        if (!selectedBook.isAvailable()) {
            JOptionPane.showMessageDialog(this, "This book is currently borrowed.", "Unavailable", JOptionPane.WARNING_MESSAGE);
            clearBookFields();
            return;
        }

        bookTitleField.setText(selectedBook.getTitle());
        authorField.setText(selectedBook.getAuthor());
        genreField.setText(selectedBook.getGenre());
    }

    // process the borrow action and save to database
    private void performBorrow() {
        if (selectedBook == null) {
            JOptionPane.showMessageDialog(this, "Please fetch an available book first.", "No Book Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // only members can borrow books (not librarian and guests)
        User user = mainApp.getCurrentUser();
        if (!(user instanceof Member member)) {
            JOptionPane.showMessageDialog(this, "Only members can borrow books.", "Access Denied", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // mark the book as borrowed
        selectedBook.markBorrowed();
        LocalDate borrowDate = LocalDate.now();
        LocalDate dueDate = borrowDate.plusDays(7);

        // create a borrow record with transaction details
        BorrowRecord record = new BorrowRecord(
                member.getLibraryId(),
                member.getFullName(),
                selectedBook.getBookId(),
                selectedBook.getTitle(),
                borrowDate,
                dueDate,
                null,
                "Borrowed",
                "-"
        );

        // save changes to the database files
        mainApp.getAllRecords().add(record);
        FileHandler.writeBooks(Main.BOOKS_FILE, mainApp.getAllBooks());
        FileHandler.writeBorrowRecords(Main.RECORDS_FILE, mainApp.getAllRecords());

        JOptionPane.showMessageDialog(this,
                "Book Borrowed Successfully!\nDue Date: " + dueDate + "\nPlease return the book before the due date.",
                "Success", JOptionPane.INFORMATION_MESSAGE);

        clearBookFields();
    }
    
    // reset all book fields to empty
    private void clearBookFields() {
        bookIdField.setText("");
        bookTitleField.setText("");
        authorField.setText("");
        genreField.setText("");
        selectedBook = null;
    }
}