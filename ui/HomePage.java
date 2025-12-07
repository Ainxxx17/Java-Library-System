package ui;

import main.Main;
import models.User;
import models.Member;
import models.BorrowRecord;
import models.Book;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.stream.Collectors;

public class HomePage extends JPanel {

    private Main mainApp;

    // UI COMPONENTS
    private JPanel mainPanel; // PANEL FOR BACKGROUND IMAGE
    private JLabel mainTitleLabel;
    private JLabel sectionTitleLabel;
    private JLabel sectionSubTitleLabel;
    private JPanel booksPanel;
    private Image backgroundImage;

    // UI CONSTANTS
    private static final Color COLOR_NAV_BG_TRANSPARENT = new Color(255, 255, 255, 230);
    private static final Color COLOR_TEXT_DARK = new Color(33, 33, 33);
    private static final Color COLOR_CARD_BG = new Color(0, 51, 102, 220);
    private static final Font FONT_TITLE = new Font("Arial", Font.BOLD, 72);
    private static final Font FONT_TAGLINE = new Font("Arial", Font.ITALIC, 22);

    public HomePage(Main mainApp) {
        this.mainApp = mainApp;
        setLayout(new BorderLayout());

        // LOAD BACKGROUND IMAGE
        ImageIcon lpBG = new ImageIcon(getClass().getResource("/bgimages/homebg.png"));

        // SET BACKGROUND IMAGE
        this.backgroundImage = lpBG.getImage();

        // PANEL WITH DYNAMIC NAVBAR
        mainPanel = createMainPanelWithBackground(null);
        add(mainPanel, BorderLayout.CENTER);

        refresh();
    }

    /*

      updates the UI state to match the current user (guest vs. member).
     this handles the conditional rendering of the navbar and card actions.

     */
    public void refresh() {
        User user = mainApp.getCurrentUser();
        boolean isGuest = (user == null) || !(user instanceof Member);

        // REBUILD MAIN PANEL
        if (mainPanel.getParent() == this) {
            remove(mainPanel);
        }
        mainPanel = createMainPanelWithBackground(user);
        add(mainPanel, BorderLayout.CENTER);

        // HEADER
        if (isGuest) {
            mainTitleLabel.setText("F4 LIBRARY");
            mainTitleLabel.setFont(new Font("Segoe UI Black", Font.BOLD, 90));
            sectionTitleLabel.setText("Upcoming Books...");
            sectionSubTitleLabel.setText("BE A MEMBER NOW TO FULLY ACCESS BOOKS!");
            sectionSubTitleLabel.setFont(new Font("Sansserif", Font.PLAIN, 20));
        } else {
            // FOR MEMBERS
            mainTitleLabel.setText("Welcome Back, " + user.getFullName() + "!");
            mainTitleLabel.setFont(new Font("Segoe UI Black", Font.BOLD, 80));
            sectionTitleLabel.setText("Upcoming Books...");
            sectionSubTitleLabel.setText("Explore and Borrow Your Favorites!");
        }

        // UPDATE BOOK CARDS
        booksPanel.removeAll();
        addBookCards(isGuest);

        revalidate();
        repaint();
    }

    // UI CONSTRUCTION METHODS
    // creates the main panel with background and all sections
    private JPanel createMainPanelWithBackground(User user) {
        // MAIN PANEL WITH BACKGROUND
        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;

                if (backgroundImage != null) {
                    g2.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                } else {
                    g2.setColor(new Color(0, 32, 77));
                    g2.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        panel.setOpaque(false);
        panel.setLayout(new BorderLayout());

        // NAVIGATION BAR WRAPPER (NORTH)
        JPanel navbarWrapper = createNavBarWrapper(user);
        panel.add(navbarWrapper, BorderLayout.NORTH);

        // CENTER WRAPPER (Header + Content)
        JPanel centerWrapper = new JPanel();
        centerWrapper.setLayout(new BoxLayout(centerWrapper, BoxLayout.Y_AXIS));
        centerWrapper.setOpaque(false);

        // HEADER SECTION
        centerWrapper.add(createHeaderSection(user));

        // CONTENT SECTION (Book Cards)
        centerWrapper.add(createContentSection());

        panel.add(centerWrapper, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createNavBarWrapper(User user) {
        // NAVIGATION BAR (Rounded corners effect)
        JPanel navbar = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(COLOR_NAV_BG_TRANSPARENT);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        navbar.setOpaque(false);
        navbar.setPreferredSize(new Dimension(0, 55));

        // DYNAMIC NAVBAR BUTTONS
        navbar.add(createNavButton("HOME", Main.HOME_PAGE, false));
        navbar.add(createNavButton("CATEGORY", Main.CATEGORIES_PAGE, false));

        boolean isMember = (user != null && user.canBorrow()); // Check if user is a member

        if (isMember) {
            // Member UI: Borrow and Return Books are visible
            navbar.add(createNavButton("BORROW BOOK", Main.BORROW_PAGE, false));

            // ADDED "YOUR BOOKS" BUTTON
            JButton yourBooksBtn = createNavButton("YOUR BOOKS", null, false);
            yourBooksBtn.addActionListener(e -> showBorrowedBooksDialog((Member) user));
            navbar.add(yourBooksBtn);
            // END OF ADDED BUTTON
            
            navbar.add(createNavButton("RETURN BOOK", Main.RETURN_PAGE, false));
        }

        // SEPARATOR
        JLabel separator = new JLabel("|");
        separator.setFont(new Font("SansSerif", Font.BOLD, 14));
        separator.setForeground(COLOR_TEXT_DARK);
        navbar.add(separator);

        navbar.add(createNavButton("ABOUT US", Main.ABOUT_PAGE, false));

        if (user == null) {
            // GUEST UI:  SIGN UP
            navbar.add(createNavButton("SIGN UP", Main.SIGNUP_PAGE, true));
        } else {
            // MEMBER UI:  LOG OUT
            JButton logoutBtn = createNavButton("LOG OUT", null, true);
            logoutBtn.setHorizontalTextPosition(SwingConstants.LEFT);
            logoutBtn.addActionListener(e -> mainApp.logout());
            navbar.add(logoutBtn);
        }

        // NAVBAR WRAPPER (margin spacing)
        JPanel navbarWrapper = new JPanel(new BorderLayout());
        navbarWrapper.setOpaque(false);
        navbarWrapper.setBorder(BorderFactory.createEmptyBorder(40, 150, 0, 150));
        navbarWrapper.add(navbar, BorderLayout.CENTER);

        return navbarWrapper;
    }
    // HEADER SECTION
    // creates the header section with title and tagline
    private JPanel createHeaderSection(User user) {
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setOpaque(false);

        mainTitleLabel = new JLabel("F4 LIBRARY"); // initial text (updated in refresh)
        mainTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainTitleLabel.setFont(FONT_TITLE);
        mainTitleLabel.setForeground(Color.WHITE);

        JLabel tagline = new JLabel("Discover knowledge, borrow with ease.");
        tagline.setAlignmentX(Component.CENTER_ALIGNMENT);
        tagline.setFont(FONT_TAGLINE);
        tagline.setForeground(new Color(230, 230, 230));

        header.add(Box.createVerticalStrut(80));
        header.add(mainTitleLabel);
        header.add(Box.createVerticalStrut(10));
        header.add(tagline);
        header.add(Box.createVerticalStrut(50));

        return header;
    }
    // CONTENT SECTION
    // creates the content section with book cards
    private JPanel createContentSection() {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);
        content.setBorder(BorderFactory.createEmptyBorder(20, 60, 20, 60));

        JPanel headerRow = new JPanel(new BorderLayout());
        headerRow.setOpaque(false);

        sectionTitleLabel = new JLabel("Upcoming Books..."); // Initial text (updated in refresh)
        sectionTitleLabel.setFont(new Font("Verdana", Font.BOLD, 25));
        sectionTitleLabel.setForeground(Color.decode("#18417f"));
        headerRow.add(sectionTitleLabel, BorderLayout.WEST);

        sectionSubTitleLabel = new JLabel("BE A MEMBER NOW TO FULLY ACCESS BOOKS!"); // Initial text (updated in refresh)
        sectionSubTitleLabel.setFont(new Font("Verdana", Font.BOLD, 25));
        sectionSubTitleLabel.setForeground(Color.decode("#18417f"));
        headerRow.add(sectionSubTitleLabel, BorderLayout.EAST);

        // BOOK CARDS PANEL
        // GridLayout with 4 columns and spacing
        booksPanel = new JPanel(new GridLayout(1, 4, 30, 30));
        booksPanel.setOpaque(false);

        content.add(headerRow);
        content.add(Box.createVerticalStrut(20));
        content.add(booksPanel);

        return content;
    }

    // ADD BOOK CARDS
    // this method adds book cards to the booksPanel based on whether the user is a guest or a member.
    private void addBookCards(boolean isGuest) {
        String[][] books = {
            {"INTRODUCTION TO JAVA", "Educational"},
            {"PRIDE AND PREJUDICE", "Romance"},
            {"DUNE", "Sci-Fi"},
            {"TO KILL A MOCKINGBIRD", "Literature"}
        };

        for (String[] book : books) {
            booksPanel.add(createBookCard(book[0], book[1], isGuest));
        }
    }

    // helper Methods with integrated functionality
    private JButton createNavButton(String text, String pageName, boolean isRightAligned) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setForeground(Color.decode("#18417f"));

        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);

        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // NAVIGATION ACTION
        if (pageName != null) {
            button.addActionListener(e -> mainApp.switchToPage(pageName));
        }

        // HOVER EFFECT (only for left-aligned buttons)
        if (!isRightAligned) {
            button.addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) { button.setBackground(Color.WHITE); }
                @Override public void mouseExited(MouseEvent e) { button.setBackground(Color.decode("#18417f")); }
            });
        }
        return button;
    }

    private JPanel createBookCard(String title, String subtitle, boolean isGuest) {
        // define image size for consistency
        final int IMAGE_WIDTH = 250;
        final int IMAGE_HEIGHT = 250;
        
        Color DARK_BLUE_FOOTER_BG = new Color(0, 51, 102);

        // MAIN CARD PANEL
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(DARK_BLUE_FOOTER_BG, 2));

        // IMAGE PANEL (top Section)
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setPreferredSize(new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT));
        
        String fileName;
        if (title.contains("JAVA")) {
            fileName = "javabook.jpg";
        } else if (title.contains("PRIDE")) {
            fileName = "pride_prejudice_book.jpg";
        } else if (title.contains("DUNE")) {
            fileName = "dunebook.jpg";
        } else if (title.contains("MOCKINGBIRD")) {
            fileName = "mocking_bird_book.jpg";
        } else {
            fileName = null; // no specific file mapping found
        }
        
        JLabel imageContainer = new JLabel();
        imageContainer.setHorizontalAlignment(SwingConstants.CENTER);
        imageContainer.setVerticalAlignment(SwingConstants.CENTER);

        ImageIcon bookImage = null;
        
        String path = "/bgimages/" + fileName;

        if (fileName != null) {
            java.net.URL imgURL = getClass().getResource(path);

            if (imgURL != null) {
                ImageIcon tempIcon = new ImageIcon(imgURL);
                if (tempIcon.getImage() != null) {
                    Image img = tempIcon.getImage().getScaledInstance(
                            IMAGE_WIDTH - 20, IMAGE_HEIGHT - 20, Image.SCALE_SMOOTH);
                    bookImage = new ImageIcon(img);
                }
            }
        }

        if (bookImage != null) {
            imageContainer.setIcon(bookImage);
        } else {
            imageContainer.setText("Photo not available");
            imageContainer.setFont(new Font("Arial", Font.BOLD, 14));
            imageContainer.setForeground(DARK_BLUE_FOOTER_BG.darker());
            imagePanel.setBackground(new Color(230, 230, 230)); // set a neutral background
        }
        
        imagePanel.add(imageContainer, BorderLayout.CENTER);
        card.add(imagePanel, BorderLayout.CENTER);

        // FOOTER PANEL (bottom Section)
        JPanel footer = new JPanel();
        footer.setBackground(DARK_BLUE_FOOTER_BG);
        footer.setLayout(new BoxLayout(footer, BoxLayout.Y_AXIS));
        footer.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
        
        //mock book ID generation
        // random book ID generation for display
        int number = (int)(Math.random() * 990 + 10);

        // Format the number to ensure it has 3 digits
        String formattedId = String.format("%03d", number);

        // create final book ID placeholder
        String bookIdPlaceholder = "B" + formattedId;

        // LABELS (title, subtitle, ID)
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Verdana", Font.BOLD, 15));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // SUBTITLE LABEL
        JLabel subtitleLabel = new JLabel(subtitle, SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Verdana", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(200, 200, 200));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // ID LABEL
        JLabel idLabel = new JLabel("ID: " + bookIdPlaceholder, SwingConstants.CENTER);
        idLabel.setFont(new Font("Verdana", Font.ITALIC, 14));
        idLabel.setForeground(new Color(150, 150, 150));
        idLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        footer.add(titleLabel);
        footer.add(subtitleLabel);
        footer.add(idLabel);
        
        // BUTTONS PANEL (conditional actions)
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        statusPanel.setOpaque(false);
        
        if (isGuest) {
            JButton detailsBtn = createActionButton("DETAILS", Color.RED.darker());
            detailsBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Please log in or sign up to view full details and borrow.", "Access Denied", JOptionPane.WARNING_MESSAGE));
            statusPanel.add(detailsBtn);
        } else {
            JButton previewBtn = createActionButton("PREVIEW", Color.decode("#18417f"));
            previewBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Soon!"));
            statusPanel.add(previewBtn);
        }

        footer.add(Box.createVerticalStrut(5));
        footer.add(statusPanel);

        card.add(footer, BorderLayout.SOUTH);

        return card;
    }
    
    // helper method to create action buttons
    private JButton createActionButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("SansSerif", Font.BOLD, 15));
        button.setPreferredSize(new Dimension(120, 25));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        return button;
    }
    
    // MEMBERS' BORROWED BOOKS DIALOG
    // shows a dialog with the list of currently borrowed books for the member
    private void showBorrowedBooksDialog(Member member) {
        List<BorrowRecord> memberRecords = mainApp.getAllRecords().stream()
                .filter(rec -> rec.getMemberId().equals(member.getLibraryId()) && rec.getStatus().equals("Borrowed"))
                .collect(Collectors.toList());

        String[] columnNames = {"Book ID", "Title", "Author", "Genre", "Due Date"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        for (BorrowRecord record : memberRecords) {
            Book book = mainApp.getAllBooks().stream()
                    .filter(b -> b.getBookId().equals(record.getBookId()))
                    .findFirst().orElse(null);

            if (book != null) {
                Object[] rowData = {
                    record.getBookId(),
                    book.getTitle(),
                    book.getAuthor(),
                    book.getGenre(),
                    record.getDueDate().toString()
                };
                tableModel.addRow(rowData);
            }
        }
        
        // CREATE TABLE
        JTable table = new JTable(tableModel);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(800, 300));
        
        // conditional message for no borrowed books
        if (memberRecords.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "You currently have no books borrowed.", 
                "Your Books", 
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, 
                scrollPane, 
                "Your Currently Borrowed Books", 
                JOptionPane.PLAIN_MESSAGE);
        }
    }
    
    }