package ui;

// APP
import main.Main;
import models.Book;

// UI 
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;

public class CategoriesPage extends JPanel {
    // REFERENCE TO MAIN APP
    private Main mainApp;
    private DefaultTableModel tableModel;
    private JTable bookTable;
    private JTextField searchField;
    private TableRowSorter<DefaultTableModel> sorter;

    // UI CONSTANTS (Borrowed from AdminPage for consistency)
    private static final Color COLOR_DASH_BG = Color.WHITE;
    private static final Color COLOR_HEADING = new Color(0, 32, 77);
    private static final Color COLOR_BUTTON = new Color(13, 110, 204);
    private static final Color COLOR_BUTTON_HOVER = new Color(20, 130, 220);
    private static final Color COLOR_NAV_BG = new Color(0, 32, 77); // Used for table header
    
    // FONTS
    private static final Font FONT_DASH_TITLE = new Font("Verdana", Font.BOLD, 32);
    private static final Font FONT_BUTTON = new Font("Verdana", Font.BOLD, 16);
    private static final Font FONT_TABLE = new Font("Verdana", Font.PLAIN, 16);
    private static final Font FONT_TABLE_HEADER = new Font("Verdana", Font.BOLD, 20);
    private static final Font FONT_SEARCH_FIELD = new Font("Verdana", Font.PLAIN, 17);


    public CategoriesPage(Main mainApp) {
        this.mainApp = mainApp;
        initComponents();
        loadBookData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(25, 25)); // Larger gaps
        setBackground(COLOR_DASH_BG);
        setBorder(new EmptyBorder(25, 25, 25, 25)); // Padding

       // 1. TOP PANEL (Header with Title, Search, Back Button)
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // 2. CENTER PANEL (Book Table)
        String[] columnNames = {"Book ID", "Title", "Author", "Genre", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        bookTable = new JTable(tableModel);
        styleTable(bookTable); // Apply custom styling
        
        sorter = new TableRowSorter<>(tableModel);
        bookTable.setRowSorter(sorter);

        JScrollPane scrollPane = new JScrollPane(bookTable);
        // DEFAULT BORDER STYLES
        scrollPane.setBackground(COLOR_DASH_BG); 
        add(scrollPane, BorderLayout.CENTER);

        // 3. BOTTOM PANEL (Details Button)
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setOpaque(false);
        JButton detailsButton = createStyledButton("View Selected Book Details");
        bottomPanel.add(detailsButton);
        add(bottomPanel, BorderLayout.SOUTH);

        // SEARCH LISTENERS
        searchField.addActionListener(e -> performSearch());

        detailsButton.addActionListener(e -> showBookDetails());
    }


     // Creates the styled header with Title, Search, and Back Button.
     
    private JPanel createHeaderPanel() {
        JPanel mainHeader = new JPanel();
        mainHeader.setLayout(new BoxLayout(mainHeader, BoxLayout.Y_AXIS));
        mainHeader.setOpaque(false);

        // TITLE AND BACK BUTTON ROW
        JPanel titleRow = new JPanel(new BorderLayout());
        titleRow.setOpaque(false);
        
        JLabel titleLabel = new JLabel("BOOK CATALOG");
        titleLabel.setFont(FONT_DASH_TITLE);
        titleLabel.setForeground(COLOR_HEADING);
        titleRow.add(titleLabel, BorderLayout.WEST);
        
        JButton backButton = createStyledButton("Back to Home");
        backButton.addActionListener(e -> mainApp.switchToPage(Main.HOME_PAGE));
        titleRow.add(backButton, BorderLayout.EAST);
        
        mainHeader.add(titleRow);
        mainHeader.add(Box.createVerticalStrut(15)); // Space between title and search

        // SEARCH BAR
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        searchPanel.setOpaque(false);
        
        JLabel searchLabel = new JLabel("Search (Title, Author, or Genre):");
        searchLabel.setFont(FONT_SEARCH_FIELD);
        searchLabel.setForeground(COLOR_HEADING);
        searchPanel.add(searchLabel);
        
        searchField = new JTextField(30);
        searchField.setFont(FONT_SEARCH_FIELD);
        searchField.setPreferredSize(new Dimension(300, 35));
        searchField.putClientProperty("JComponent.roundRect", true); // For rounded corners (if supported)
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_HEADING.darker(), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        
        // Add a KeyListener to filter as the user types
        searchField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                performSearch();
            }
        });

        searchPanel.add(searchField);
        
        mainHeader.add(searchPanel);
        
        return mainHeader;
    }

    
     //Helper method to create a styled button (reused from AdminPage)
    
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(FONT_BUTTON);
        button.setBackground(COLOR_BUTTON);
        button.setForeground(Color.WHITE);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);
        button.setMargin(new Insets(10, 15, 10, 15)); // Padding

        // HOVER EFFECT
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(COLOR_BUTTON_HOVER);
            }
            public void mouseExited(MouseEvent evt) {
                button.setBackground(COLOR_BUTTON);
            }
        });
        return button;
    }

    
     //FONT AND COLOR STYLING FOR TABLE
     
    private void styleTable(JTable table) {
        table.setFont(FONT_TABLE);
        table.setRowHeight(30); 
        table.setGridColor(new Color(220, 220, 220));
        table.setSelectionBackground(new Color(200, 220, 255)); // light blue selection
        table.setFillsViewportHeight(true);
        
        JTableHeader header = table.getTableHeader();
        header.setFont(FONT_TABLE_HEADER);
        header.setBackground(COLOR_NAV_BG); // dark blue header
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 40)); 
        header.setReorderingAllowed(false); // prevent column reordering
    }


    //refreshes the table with all book data from mainApp
    private void loadBookData() {
        tableModel.setRowCount(0); 
        
        ArrayList<Book> books = mainApp.getAllBooks();
        Collections.sort(books); 
        
        for (Book book : books) {
            tableModel.addRow(new Object[]{
                book.getBookId(),
                book.getTitle(),
                book.getAuthor(),
                book.getGenre(),
                book.getStatus()
            });
        }
    }

    
    //filters the table based on the search query.
     
    private void performSearch() {
        String query = searchField.getText();
        if (query.trim().isEmpty()) {
            sorter.setRowFilter(null); 
        } else {
            // create a filter that checks all columns case-insensitively
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + query));
        }
    }

    /**
     * shows a popup with details of the selected book.
     */
    private void showBookDetails() {
        int selectedRow = bookTable.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Please select a book from the table first.", 
                "No Book Selected", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // convert view row index to model row index (essential for sorting/filtering)
        int modelRow = bookTable.convertRowIndexToModel(selectedRow);
        
        // get data from the table model
        String bookId = (String) tableModel.getValueAt(modelRow, 0);
        String title = (String) tableModel.getValueAt(modelRow, 1);
        String author = (String) tableModel.getValueAt(modelRow, 2);
        String genre = (String) tableModel.getValueAt(modelRow, 3);
        String status = (String) tableModel.getValueAt(modelRow, 4);

        String message = String.format(
            "Book ID:  %s\n" +
            "Title:    %s\n" +
            "Author:   %s\n" +
            "Genre:    %s\n" +
            "Status:   %s",
            bookId, title, author, genre, status
        );
        
        JOptionPane.showMessageDialog(this, message, "Book Details: " + title, JOptionPane.INFORMATION_MESSAGE);
    }
    
    
    //Override setVisible to refresh data every time the page is shown.
     
    @Override
    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag);
        if (aFlag) {
            loadBookData(); // refresh data
            searchField.setText(""); // clear search
            sorter.setRowFilter(null);
        }
    }
}   