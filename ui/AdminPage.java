package ui;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.RowFilter;
import javax.swing.table.TableRowSorter;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import main.Main;
import models.Admin;
import models.Book;
import models.BorrowRecord;
import models.Member;
import models.User;
import utils.FileHandler;
import utils.IDGenerator;

public class AdminPage extends JPanel {

    private Main mainApp;

    // UI COMPONENTS
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private JPanel linksPanel;

    // TABLE MODELS
    private DefaultTableModel booksModel;
    private DefaultTableModel membersModel;
    private DefaultTableModel recordsModel;
    private DefaultTableModel auditLogModel;

    // TABLES
    private JTable booksTable;
    private JTable membersTable;
    private JTable recordsTable;
    private JTable auditLogTable;

    // DASHBOARD LABELS
    private JLabel totalBooksLabel;
    private JLabel totalMembersLabel;
    private JLabel borrowedBooksLabel;

    // UI CONSTANTS
    private static final Color COLOR_NAV_BG = new Color(0, 32, 77);
    private static final Color COLOR_NAV_ACTIVE = new Color(0, 51, 128);
    private static final Color COLOR_DASH_BG = Color.WHITE;
    private static final Color COLOR_CARD_DARK_BLUE = new Color(0, 46, 110);
    private static final Color COLOR_CARD_MEDIUM_BLUE = new Color(13, 110, 204);
    private static final Color COLOR_CARD_CYAN = new Color(14, 166, 178);
    private static final Color COLOR_TEXT_WHITE = Color.WHITE;
    private static final Color COLOR_TEXT_GRAY = Color.LIGHT_GRAY;
    private static final Color COLOR_STATUS_GREEN = new Color(50, 205, 50);
    private static final Color COLOR_HEADING = new Color(0, 32, 77);
    private static final Color COLOR_BUTTON_HOVER = new Color(20, 130, 220);
    private static final Color COLOR_BUTTON = new Color(13, 110, 204);

    // FONTS
    private static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 26);
    private static final Font FONT_USER = new Font("Segoe UI", Font.BOLD, 20);
    private static final Font FONT_USER_ROLE = new Font("Segoe UI", Font.PLAIN, 16);
    private static final Font FONT_NAV = new Font("Segoe UI", Font.BOLD, 20);
    private static final Font FONT_DASH_TITLE = new Font("Segoe UI", Font.BOLD, 32);
    private static final Font FONT_DASH_NUMBER = new Font("Segoe UI", Font.BOLD, 72);
    private static final Font FONT_DASH_LABEL = new Font("Segoe UI", Font.BOLD, 18);
    private static final Font FONT_CALENDAR_DATE = new Font("Segoe UI", Font.BOLD, 36);
    private static final Font FONT_TABLE = new Font("Segoe UI", Font.PLAIN, 16);
    private static final Font FONT_TABLE_HEADER = new Font("Segoe UI", Font.BOLD, 18);
    private static final Font FONT_BUTTON = new Font("Segoe UI", Font.BOLD, 16);
    private static final Font FONT_FORM_LABEL = new Font("Segoe UI", Font.PLAIN, 18);
    private static final Font FONT_ACCOUNT_LABEL = new Font("Segoe UI", Font.PLAIN, 20);

    // CardLayout Page Names
    private static final String PAGE_DASHBOARD = "DASHBOARD";
    private static final String PAGE_BOOKS = "BOOKS";
    private static final String PAGE_MEMBERS = "MEMBERS";
    private static final String PAGE_BORROW_LOGS = "BORROW_LOGS";
    private static final String PAGE_AUDIT_LOG = "AUDIT_LOG";
    private static final String PAGE_ACCOUNT = "ACCOUNT";

    public AdminPage(Main mainApp) {
        this.mainApp = mainApp;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        add(createNavPanel(), BorderLayout.WEST);
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(COLOR_DASH_BG);

        contentPanel.add(createContentPage("DASHBOARD", createDashboardCardsPanel()), PAGE_DASHBOARD);
        contentPanel.add(createContentPage("BOOK MANAGEMENT", createBooksPanelContent()), PAGE_BOOKS);
        contentPanel.add(createContentPage("MEMBER RECORDS", membersPanelContent()), PAGE_MEMBERS);
        contentPanel.add(createContentPage("BORROW/RETURN LOGS", createRecordsPanelContent()), PAGE_BORROW_LOGS);
        contentPanel.add(createContentPage("SYSTEM LOG HISTORY", createAuditLogPanelContent()), PAGE_AUDIT_LOG);
        contentPanel.add(createContentPage("ACCOUNT", createAccountPanelContent()), PAGE_ACCOUNT);

        add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel createContentPage(String title, JComponent content) {
        JPanel page = new JPanel(new BorderLayout(10, 20));
        page.setBackground(COLOR_DASH_BG);
        page.setBorder(new EmptyBorder(20, 25, 20, 25));

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        headerPanel.setOpaque(false);
        
        // MODIFIED: Removed file name argument
        JLabel arrowIcon = new JLabel(createIcon(30, SimpleIcon.ARROW_RIGHT));

        if (!title.equals("DASHBOARD")) {
            arrowIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));
            arrowIcon.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    cardLayout.show(contentPanel, PAGE_DASHBOARD);
                    JPanel navLink = findNavLink(PAGE_DASHBOARD);
                    if (navLink != null) {
                        updateActiveNav(navLink);
                    }
                }
            });
        } else {
            arrowIcon.setVisible(false);
        }

        headerPanel.add(arrowIcon);
        headerPanel.add(Box.createHorizontalStrut(10));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(FONT_DASH_TITLE);
        titleLabel.setForeground(COLOR_HEADING);
        headerPanel.add(titleLabel);

        page.add(headerPanel, BorderLayout.NORTH);
        page.add(content, BorderLayout.CENTER);

        return page;
    }

    private JPanel createNavPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(COLOR_NAV_BG);
        panel.setPreferredSize(new Dimension(280, 0));
        panel.setBorder(new EmptyBorder(15, 10, 15, 10));

        JLabel title = new JLabel("F4 LIBRARY");
        title.setFont(FONT_TITLE);
        title.setForeground(COLOR_TEXT_WHITE);
        title.setBorder(new EmptyBorder(10, 15, 20, 15));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(title);

        panel.add(createUserInfoPanel());
        panel.add(Box.createVerticalStrut(20));

        JSeparator separator = new JSeparator();
        separator.setBackground(COLOR_NAV_ACTIVE);
        separator.setForeground(COLOR_NAV_ACTIVE);
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2));
        panel.add(separator);
        panel.add(Box.createVerticalStrut(20));

        linksPanel = new JPanel();
        linksPanel.setLayout(new BoxLayout(linksPanel, BoxLayout.Y_AXIS));
        linksPanel.setOpaque(false);
        linksPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // MODIFIED: Removed file name arguments
        addNavLink(PAGE_DASHBOARD, "Dashboard", createIcon(28, SimpleIcon.DASHBOARD), true);
        addNavLink(PAGE_BOOKS, "Books", createIcon(28, SimpleIcon.BOOK_STACK), false);
        addNavLink(PAGE_MEMBERS, "Members", createIcon(28, SimpleIcon.PEOPLE), false);
        addNavLink(PAGE_BORROW_LOGS, "Records", createIcon(28, SimpleIcon.RECORDS), false);
        addNavLink(PAGE_ACCOUNT, "Account", createIcon(28, SimpleIcon.PERSON_NAV), false);

        panel.add(linksPanel);
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    private void addNavLink(String pageName, String title, Icon icon, boolean isActive) {
        JPanel linkPanel = createNavLink(title, icon, isActive);
        linkPanel.setName(pageName);
        linkPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(contentPanel, pageName);
                updateActiveNav(linkPanel);
            }
        });
        linksPanel.add(linkPanel);
    }

    private void updateActiveNav(JPanel activeLink) {
        for (Component comp : linksPanel.getComponents()) {
            if (comp instanceof JPanel) {
                comp.setBackground(COLOR_NAV_BG);
            }
        }
        activeLink.setBackground(COLOR_NAV_ACTIVE);
    }

    private JPanel findNavLink(String pageName) {
        for (Component comp : linksPanel.getComponents()) {
            if (comp instanceof JPanel && pageName.equals(comp.getName())) {
                return (JPanel) comp;
            }
        }
        return null;
    }

    private JPanel createUserInfoPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 0));
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setBorder(new EmptyBorder(0, 15, 0, 10));

        // MODIFIED: Removed file name argument
        JLabel iconLabel = new JLabel(createIcon(50, SimpleIcon.PERSON));
        panel.add(iconLabel, BorderLayout.WEST);
        
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        User currentUser = mainApp.getCurrentUser();
        String username = (currentUser != null) ? currentUser.getUsername() : "Admin";
        String role = (currentUser != null) ? (currentUser instanceof Member ? "Member" : "Admin") : "Librarian";
        
        JLabel usernameLabel = new JLabel(username);
        usernameLabel.setFont(FONT_USER);
        usernameLabel.setForeground(COLOR_TEXT_WHITE);
        textPanel.add(usernameLabel);

        JLabel roleLabel = new JLabel(role);
        roleLabel.setFont(FONT_USER_ROLE);
        roleLabel.setForeground(COLOR_TEXT_GRAY);
        textPanel.add(roleLabel);

        panel.add(textPanel, BorderLayout.CENTER);
        JPanel statusDot = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(COLOR_STATUS_GREEN);
                g2d.fillOval(0, 0, 12, 12);
            }
        };
        statusDot.setOpaque(false);
        statusDot.setPreferredSize(new Dimension(12, 12));
        JPanel statusWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 8));
        statusWrapper.setOpaque(false);
        statusWrapper.add(statusDot);
        panel.add(statusWrapper, BorderLayout.EAST);

        return panel;
    }

    private JPanel createNavLink(String text, Icon icon, boolean isActive) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 12));
        panel.setBackground(isActive ? COLOR_NAV_ACTIVE : COLOR_NAV_BG);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 65));
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setBorder(new EmptyBorder(0, 15, 0, 0));
        
        JLabel iconLabel = new JLabel(icon);
        panel.add(iconLabel);

        JLabel textLabel = new JLabel(text);
        textLabel.setFont(FONT_NAV);
        textLabel.setForeground(COLOR_TEXT_WHITE);
        panel.add(textLabel);

        return panel;
    }

    private JPanel createDashboardCardsPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(25, 25));
        mainPanel.setOpaque(false);

        JPanel leftGrid = new JPanel(new GridLayout(2, 2, 25, 25));
        leftGrid.setOpaque(false);

        totalBooksLabel = new JLabel("0");
        totalMembersLabel = new JLabel("0");
        borrowedBooksLabel = new JLabel("0");

        // MODIFIED: Removed file name arguments
        JPanel card1 = createDashboardStatCard(COLOR_CARD_DARK_BLUE, createIcon(90, SimpleIcon.BOOK), totalBooksLabel, "Total Books");
        JPanel card2 = createDashboardStatCard(COLOR_CARD_MEDIUM_BLUE, createIcon(90, SimpleIcon.PEOPLE_LARGE), totalMembersLabel, "Total Members");
        JPanel card3 = createDashboardStatCard(COLOR_CARD_MEDIUM_BLUE, createIcon(90, SimpleIcon.ISSUE_BOOK), borrowedBooksLabel, "Borrowed Books");
        JPanel card4 = createDashboardActionCard(COLOR_CARD_MEDIUM_BLUE, createIcon(90, SimpleIcon.OPEN_BOOK), "All Record");

        addCardClickListener(card1, PAGE_BOOKS);
        addCardClickListener(card2, PAGE_MEMBERS);
        addCardClickListener(card3, PAGE_BORROW_LOGS);
        addCardClickListener(card4, PAGE_BORROW_LOGS);

        leftGrid.add(card1);
        leftGrid.add(card2);
        leftGrid.add(card3);
        leftGrid.add(card4);

        JPanel calendarCard = createCalendarCard();
        calendarCard.setPreferredSize(new Dimension(350, 0));

        mainPanel.add(leftGrid, BorderLayout.CENTER);
        mainPanel.add(calendarCard, BorderLayout.EAST);

        return mainPanel;
    }

    private JPanel createCalendarCard() {
        JPanel card = new JPanel(new BorderLayout());
        card.setOpaque(false);

        JPanel topPanel = new JPanel(new GridBagLayout());
        topPanel.setBackground(COLOR_CARD_CYAN);
        topPanel.setPreferredSize(new Dimension(0, 180));

        JPanel contentWrapper = new JPanel();
        contentWrapper.setLayout(new BoxLayout(contentWrapper, BoxLayout.Y_AXIS));
        contentWrapper.setOpaque(false);
        
        // MODIFIED: Removed file name argument
        JLabel iconLabel = new JLabel(createIcon(180, SimpleIcon.CALENDAR));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        JLabel dateLabel = new JLabel(date);
        dateLabel.setFont(FONT_CALENDAR_DATE);
        dateLabel.setForeground(Color.WHITE);
        dateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        contentWrapper.add(Box.createVerticalStrut(20));
        contentWrapper.add(iconLabel);
        contentWrapper.add(Box.createVerticalStrut(20));
        contentWrapper.add(dateLabel);
        contentWrapper.add(Box.createVerticalStrut(20));

        topPanel.add(contentWrapper);
        card.add(topPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new GridBagLayout());
        bottomPanel.setBackground(COLOR_CARD_DARK_BLUE);
        bottomPanel.setPreferredSize(new Dimension(0, 80));
        JLabel textLabel = new JLabel("Date Today");
        textLabel.setFont(FONT_DASH_LABEL);
        textLabel.setForeground(Color.WHITE);
        bottomPanel.add(textLabel);

        card.add(bottomPanel, BorderLayout.SOUTH);
        return card;
    }

    private JPanel createDashboardStatCard(Color color, Icon icon, JLabel numberLabel, String labelText) {
        JPanel card = new JPanel(new BorderLayout(20, 0));
        card.setBackground(color);
        card.setBorder(new EmptyBorder(25, 30, 25, 30));

        numberLabel.setFont(FONT_DASH_NUMBER);
        numberLabel.setForeground(Color.WHITE);
        card.add(numberLabel, BorderLayout.WEST);

        JPanel eastPanel = new JPanel();
        eastPanel.setOpaque(false);
        eastPanel.setLayout(new BoxLayout(eastPanel, BoxLayout.Y_AXIS));
        JLabel iconLbl = new JLabel(icon);
        iconLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel textLabel = new JLabel(labelText);
        textLabel.setFont(FONT_DASH_LABEL);
        textLabel.setForeground(Color.WHITE);
        textLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        eastPanel.add(iconLbl);
        eastPanel.add(Box.createVerticalStrut(10));
        eastPanel.add(textLabel);

        card.add(eastPanel, BorderLayout.EAST);
        return card;
    }

    private JPanel createDashboardActionCard(Color topColor, Icon icon, String labelText) {
        JPanel card = new JPanel(new BorderLayout());
        card.setOpaque(false);

        JPanel topPanel = new JPanel(new GridBagLayout());
        topPanel.setBackground(topColor);
        topPanel.setPreferredSize(new Dimension(0, 180));
        JLabel iconLabel = new JLabel(icon);
        topPanel.add(iconLabel);
        JPanel bottomPanel = new JPanel(new GridBagLayout());
        bottomPanel.setBackground(COLOR_CARD_DARK_BLUE);
        bottomPanel.setPreferredSize(new Dimension(0, 80));

        JLabel textLabel = new JLabel(labelText);
        textLabel.setFont(FONT_DASH_LABEL);
        textLabel.setForeground(Color.WHITE);
        bottomPanel.add(textLabel);

        card.add(topPanel, BorderLayout.CENTER);
        card.add(bottomPanel, BorderLayout.SOUTH);

        return card;
    }

    private void addCardClickListener(JPanel card, String pageName) {
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(contentPanel, pageName);
                JPanel navLink = findNavLink(pageName);
                if (navLink != null) {
                    updateActiveNav(navLink);
                }
            }
        });
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(FONT_BUTTON);
        button.setBackground(COLOR_BUTTON);
        button.setForeground(Color.WHITE);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);
        button.setMargin(new Insets(10, 15, 10, 15));
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

    private void styleTable(JTable table) {
        table.setFont(FONT_TABLE);
        table.setRowHeight(30);
        table.setGridColor(new Color(220, 220, 220));

        JTableHeader header = table.getTableHeader();
        header.setFont(FONT_TABLE_HEADER);
        header.setBackground(COLOR_NAV_BG);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 40));
    }

    private JPanel createBooksPanelContent() {
        JPanel panel = new JPanel(new BorderLayout(10, 15));
        panel.setOpaque(false);

        String[] bookCols = {"ID", "Title", "Author", "Genre", "Status"};
        booksModel = new DefaultTableModel(bookCols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };


        
        booksTable = new JTable(booksModel);
        styleTable(booksTable);
        panel.add(new JScrollPane(booksTable), BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        buttonPanel.setOpaque(false);
        JButton addButton = createStyledButton("Add Book");
        JButton editButton = createStyledButton("Edit Selected");
        JButton deleteButton = createStyledButton("Delete Selected");
        JButton saveButton = createStyledButton("Save All Changes to File");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(saveButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);
        addButton.addActionListener(e -> addBook());
        editButton.addActionListener(e -> editBook());
        deleteButton.addActionListener(e -> deleteBook());
        saveButton.addActionListener(e -> saveBooks());

        return panel;
    }

    private JPanel membersPanelContent() {
        JPanel panel = new JPanel(new BorderLayout(10, 15));
        panel.setOpaque(false);

        String[] memberCols = {"ID", "Full Name", "Username", "Role"};
        membersModel = new DefaultTableModel(memberCols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false;
            }
        };
        membersTable = new JTable(membersModel);
        styleTable(membersTable);

        membersModel.setRowCount(0);
        for (User user : mainApp.getAllUsers()) {
            String role = (user instanceof Member) ? "Member" : "Admin";
            membersModel.addRow(new Object[]{
                user.getLibraryId(), user.getFullName(), user.getUsername(), role
            });
        }

        panel.add(new JScrollPane(membersTable), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        buttonPanel.setOpaque(false);
        JButton addButton = createStyledButton("Add Member");
        JButton editButton = createStyledButton("Edit Selected");
        JButton deleteButton = createStyledButton("Delete Selected");
        JButton saveButton = createStyledButton("Save All Changes to File");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(saveButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);
        // CREATE (ADD) BUTTON
        addButton.addActionListener(e -> {
            JTextField fullNameField = new JTextField(15);
            fullNameField.setFont(FONT_FORM_LABEL);
            JTextField usernameField = new JTextField(15);
            usernameField.setFont(FONT_FORM_LABEL);
            JTextField passwordField = new JTextField(15);
            passwordField.setFont(FONT_FORM_LABEL);
            
            String[] roles = {"Member", "Admin"};
            JComboBox<String> roleCombo = new JComboBox<>(roles);
            roleCombo.setFont(FONT_FORM_LABEL);
            roleCombo.setSelectedItem("Member");

            JPanel addPanel = new JPanel(new GridLayout(0, 2, 10, 10));
            addPanel.add(createDialogLabel("Full Name:"));
            addPanel.add(fullNameField);
            addPanel.add(createDialogLabel("Username:"));
            addPanel.add(usernameField);
            addPanel.add(createDialogLabel("Password:"));
            addPanel.add(passwordField);
            addPanel.add(createDialogLabel("Role:"));
            addPanel.add(roleCombo);

            int result = JOptionPane.showConfirmDialog(AdminPage.this, addPanel, "Add New Member",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                String fullName = fullNameField.getText();
                String username = usernameField.getText();
                String password = passwordField.getText();
                String role = (String) roleCombo.getSelectedItem();
                if (fullName.isEmpty() || username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(AdminPage.this, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                for (User user : mainApp.getAllUsers()) {
                    if (user.getUsername().equalsIgnoreCase(username)) {
                        JOptionPane.showMessageDialog(AdminPage.this, "Username already exists.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                String id = IDGenerator.generateNextMemberId(mainApp.getAllUsers());
                User newMember;

                if ("Admin".equals(role)) {
                    newMember = new Admin(id, fullName, username, password);
                } else {
                    newMember = new Member(id, fullName, username, password);
                }

                mainApp.getAllUsers().add(newMember);

                membersModel.setRowCount(0);
                for (User user : mainApp.getAllUsers()) {
                    String r = (user instanceof Member) ? "Member" : "Admin";
                    membersModel.addRow(new Object[]{
                        user.getLibraryId(), user.getFullName(), user.getUsername(), r
                    });
                }
                refreshData();
            }
        });

        // UPDATE (EDIT) BUTTON
        editButton.addActionListener(e -> {
            int selectedRow = membersTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(AdminPage.this, "Please select a member to edit.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String memberId = (String) membersModel.getValueAt(selectedRow, 0);
            User userToEdit = mainApp.getAllUsers().stream()
                .filter(u -> u.getLibraryId().equals(memberId))
                .findFirst().orElse(null);

            if (userToEdit == null) return;

            JTextField fullNameField = new JTextField(userToEdit.getFullName(), 15);
            fullNameField.setFont(FONT_FORM_LABEL);
            JTextField usernameField = new JTextField(userToEdit.getUsername(), 15);
            usernameField.setFont(FONT_FORM_LABEL);
            JTextField passwordField = new JTextField(userToEdit.getPassword(), 15);
            passwordField.setFont(FONT_FORM_LABEL);

            String[] roles = {"Member", "Admin"};
            JComboBox<String> roleCombo = new JComboBox<>(roles);
            roleCombo.setFont(FONT_FORM_LABEL);
            String currentRole = (userToEdit instanceof Member) ? "Member" : "Admin";
            roleCombo.setSelectedItem(currentRole);

            JPanel editPanel = new JPanel(new GridLayout(0, 2, 10, 10));
            editPanel.add(createDialogLabel("Full Name:"));
            editPanel.add(fullNameField);
            editPanel.add(createDialogLabel("Username:"));
            editPanel.add(usernameField);
            editPanel.add(createDialogLabel("Password:"));
            editPanel.add(passwordField);
            editPanel.add(createDialogLabel("Role:"));
            editPanel.add(roleCombo);

            int result = JOptionPane.showConfirmDialog(AdminPage.this, editPanel, "Edit Member: " + memberId,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                String newFullName = fullNameField.getText();
                String newUsername = usernameField.getText();
                String newPassword = passwordField.getText();
                String newRole = (String) roleCombo.getSelectedItem();
                if (newFullName.isEmpty() || newUsername.isEmpty() || newPassword.isEmpty()) {
                    JOptionPane.showMessageDialog(AdminPage.this, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                for (User user : mainApp.getAllUsers()) {
                    if (!user.getLibraryId().equals(memberId) && user.getUsername().equalsIgnoreCase(newUsername)) {
                        JOptionPane.showMessageDialog(AdminPage.this, "Username already exists.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                userToEdit.setFullName(newFullName);
                userToEdit.setUsername(newUsername);
                userToEdit.setPassword(newPassword);

                if (!currentRole.equals(newRole)) {
                    mainApp.getAllUsers().remove(userToEdit);
                    User updatedUser;
                    if ("Admin".equals(newRole)) {
                        updatedUser = new Admin(memberId, newFullName, newUsername, newPassword);
                    } else {
                        updatedUser = new Member(memberId, newFullName, newUsername, newPassword);
                    }
                    mainApp.getAllUsers().add(updatedUser);
                }

                membersModel.setRowCount(0);
                for (User user : mainApp.getAllUsers()) {
                    String r = (user instanceof Member) ? "Member" : "Admin";
                    membersModel.addRow(new Object[]{
                        user.getLibraryId(), user.getFullName(), user.getUsername(), r
                    });
                }
                refreshData();
            }
        });

        // DELETE BUTTON
        deleteButton.addActionListener(e -> {
            int selectedRow = membersTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(AdminPage.this, "Please select a member to delete.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
           }

            String memberId = (String) membersModel.getValueAt(selectedRow, 0);
            String memberName = (String) membersModel.getValueAt(selectedRow, 1);

            JLabel message = new JLabel("<html>Are you sure you want to delete this member?<br>ID: " + memberId + "<br>Name: " + memberName + "</html>");
            message.setFont(FONT_FORM_LABEL);

            int confirm = JOptionPane.showConfirmDialog(AdminPage.this,
                               message,
                "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                mainApp.getAllUsers().removeIf(user -> user.getLibraryId().equals(memberId));
                membersModel.setRowCount(0);
                for (User user : mainApp.getAllUsers()) {
                    String r = (user instanceof Member) ? "Member" : "Admin";
                    membersModel.addRow(new Object[]{
                        user.getLibraryId(), user.getFullName(), user.getUsername(), r
                    });
                }
                refreshData();
            }
        });

        // SAVE BUTTON
        saveButton.addActionListener(e -> {
            FileHandler.writeUsers(Main.MEMBERS_FILE, mainApp.getAllUsers());

            JLabel message = new JLabel("Member list saved to " + Main.MEMBERS_FILE);
            message.setFont(FONT_FORM_LABEL);
            JOptionPane.showMessageDialog(AdminPage.this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
        });
        return panel;
    }

    private JPanel createRecordsPanelContent() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setOpaque(false);

        String[] recordCols = {"Member ID", "Book ID", "Book Title", "Borrow Date", "Due Date", "Return Date", "Status", "Remarks"};
        recordsModel = new DefaultTableModel(recordCols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false;
            }
        };
        recordsTable = new JTable(recordsModel);
        styleTable(recordsTable);
        panel.add(new JScrollPane(recordsTable), BorderLayout.CENTER);

        return panel;
    }

    private JPanel createAuditLogPanelContent() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setOpaque(false);

        String[] logCols = {"Timestamp", "User", "Action", "Details"};
        auditLogModel = new DefaultTableModel(logCols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false;
            }
        };
        auditLogTable = new JTable(auditLogModel);
        styleTable(auditLogTable);
        panel.add(new JScrollPane(auditLogTable), BorderLayout.CENTER);

        return panel;
    }

    private JPanel createAccountPanelContent() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(20, 0, 20, 0));

        User currentUser = mainApp.getCurrentUser();
        String username = (currentUser != null) ? currentUser.getUsername() : "Admin";
        String role = (currentUser != null) ? (currentUser instanceof Member ? "Member" : "Librarian") : "Librarian";

        panel.add(Box.createVerticalStrut(20));
        JLabel userLabel = new JLabel("Username: " + username);
        userLabel.setFont(FONT_ACCOUNT_LABEL);
        userLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(userLabel);

        panel.add(Box.createVerticalStrut(10));
        JLabel roleLabel = new JLabel("Role: " + role);
        roleLabel.setFont(FONT_ACCOUNT_LABEL);
        roleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);   
        panel.add(roleLabel);

        panel.add(Box.createVerticalStrut(30));
        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2));
        separator.setAlignmentX(Component.LEFT_ALIGNMENT);   
        panel.add(separator);

        panel.add(Box.createVerticalStrut(30));
        JButton logoutButton = createStyledButton("Log Out");
        logoutButton.addActionListener(e -> mainApp.logout());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(logoutButton);
        buttonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT); 

        panel.add(buttonPanel);

        panel.add(Box.createVerticalGlue());

        return panel;
    }

    /**
     * Creates a scalable icon using SimpleIcon drawing logic.
     * All file dependency logic has been removed.
     *
     * @param size         The desired width and height of the icon.
     * @param type         The SimpleIcon type (e.g., SimpleIcon.BOOK).
     * @return An Icon object.
     */
    private Icon createIcon(int size, int type) {
        // REPLACED: Directly returns the drawn icon without checking for files
        return new SimpleIcon(type, size);
    }

    public void refreshData() {
        if (booksModel != null) loadBooks();
        if (membersModel != null) loadMembers();
        if (recordsModel != null) loadRecords();
        if (auditLogModel != null) loadAuditLogs();
        if (totalBooksLabel != null) {
            totalBooksLabel.setText(String.valueOf(mainApp.getAllBooks().size()));
        }
        if (totalMembersLabel != null) {
            totalMembersLabel.setText(String.valueOf(mainApp.getAllUsers().size()));
        }
        if (borrowedBooksLabel != null) {
            long borrowedCount = mainApp.getAllRecords().stream()
                .filter(record -> "Borrowed".equalsIgnoreCase(record.getStatus()))
                .count();
            borrowedBooksLabel.setText(String.valueOf(borrowedCount));
        }
    }

    private void loadBooks() {
        booksModel.setRowCount(0);
        for (Book book : mainApp.getAllBooks()) {
            booksModel.addRow(new Object[]{
                book.getBookId(), book.getTitle(), book.getAuthor(), book.getGenre(), book.getStatus()
            });
        }
    }

    private void loadMembers() {
        membersModel.setRowCount(0);
        for (User user : mainApp.getAllUsers()) {
            String role = (user instanceof Member) ? "Member" : "Admin";
            membersModel.addRow(new Object[]{
                user.getLibraryId(), user.getFullName(), user.getUsername(), role
            });
        }
    }

    private void loadRecords() {
        recordsModel.setRowCount(0);
        for (BorrowRecord r : mainApp.getAllRecords()) {
            recordsModel.addRow(new Object[]{
                r.getMemberId(), r.getBookId(), r.getBookTitle(),
                r.getBorrowDate(), r.getDueDate(),
                (r.getReturnDate() == null ? "-" : r.getReturnDate()),
                r.getStatus(), r.getRemarks()
            });
        }
    }
    
    // if empty, show dummy data 
    private void loadAuditLogs() {
        auditLogModel.setRowCount(0);
        auditLogModel.addRow(new Object[]{"2025-10-28 10:30:01", "Admin101", "Login", "Successful login"});
        auditLogModel.addRow(new Object[]{"2025-10-28 10:31:05", "Admin101", "Create Book", "ID: B001, Title: Java Basics"});
    }

    private JLabel createDialogLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_FORM_LABEL);
        return label;
    }

    private void addBook() {
        JTextField titleField = new JTextField(15);
        titleField.setFont(FONT_FORM_LABEL);
        JTextField authorField = new JTextField(15);
        authorField.setFont(FONT_FORM_LABEL);
        JTextField genreField = new JTextField(15);
        genreField.setFont(FONT_FORM_LABEL);
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.add(createDialogLabel("Title:"));
        panel.add(titleField);
        panel.add(createDialogLabel("Author:"));
        panel.add(authorField);
        panel.add(createDialogLabel("Genre:"));
        panel.add(genreField);
        int result = JOptionPane.showConfirmDialog(this, panel, "Add New Book",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String title = titleField.getText();
            String author = authorField.getText();
            String genre = genreField.getText();

            if (title.isEmpty() || author.isEmpty() || genre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String id = IDGenerator.generateNextBookId(mainApp.getAllBooks());
            Book newBook = new Book(id, title, author, genre, "Available");
            mainApp.getAllBooks().add(newBook);

            loadBooks();
            refreshData();
        }
    }

    private void editBook() {
        int selectedRow = booksTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book to edit.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String bookId = (String) booksModel.getValueAt(selectedRow, 0);
        Book bookToEdit = mainApp.getAllBooks().stream()
            .filter(b -> b.getBookId().equals(bookId))
            .findFirst().orElse(null);
        if (bookToEdit == null) return;

        JTextField titleField = new JTextField(bookToEdit.getTitle(), 15);
        titleField.setFont(FONT_FORM_LABEL);
        JTextField authorField = new JTextField(bookToEdit.getAuthor(), 15);
        authorField.setFont(FONT_FORM_LABEL);
        JTextField genreField = new JTextField(bookToEdit.getGenre(), 15);
        genreField.setFont(FONT_FORM_LABEL);

        String[] statuses = {"Available", "Borrowed"};
        JComboBox<String> statusCombo = new JComboBox<>(statuses);
        statusCombo.setFont(FONT_FORM_LABEL);
        statusCombo.setSelectedItem(bookToEdit.getStatus());
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.add(createDialogLabel("Title:"));
        panel.add(titleField);
        panel.add(createDialogLabel("Author:"));
        panel.add(authorField);
        panel.add(createDialogLabel("Genre:"));
        panel.add(genreField);
        panel.add(createDialogLabel("Status:"));
        panel.add(statusCombo);
        int result = JOptionPane.showConfirmDialog(this, panel, "Edit Book: " + bookId,
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            bookToEdit.setTitle(titleField.getText());
            bookToEdit.setAuthor(authorField.getText());
            bookToEdit.setGenre(genreField.getText());
            bookToEdit.setStatus((String) statusCombo.getSelectedItem());

            loadBooks();
            refreshData();
        }
    }

    private void deleteBook() {
        int selectedRow = booksTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book to delete.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String bookId = (String) booksModel.getValueAt(selectedRow, 0);
        String bookTitle = (String) booksModel.getValueAt(selectedRow, 1);

        JLabel message = new JLabel("<html>Are you sure you want to delete this book?<br>ID: " + bookId + "<br>Title: " + bookTitle + "</html>");
        message.setFont(FONT_FORM_LABEL);

        int confirm = JOptionPane.showConfirmDialog(this,
            message,
            "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            mainApp.getAllBooks().removeIf(book -> book.getBookId().equals(bookId));
            loadBooks();
            refreshData();
        }
    }

    private void saveBooks() {
        FileHandler.writeBooks(Main.BOOKS_FILE, mainApp.getAllBooks());
        JLabel message = new JLabel("Book list saved to " + Main.BOOKS_FILE);
        message.setFont(FONT_FORM_LABEL);
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
}
 
/**
 * SimpleIcon class using Graphics2D to draw icons programmatically.
 */
class SimpleIcon implements Icon {
    public static final int PERSON = 1;
    public static final int BOOK = 2;
    public static final int PEOPLE = 3;
    public static final int RECORDS = 4;
    public static final int DASHBOARD = 5;
    public static final int ARROW_RIGHT = 6;
    public static final int ISSUE_BOOK = 7;
    public static final int CALENDAR = 8;
    public static final int BOOK_STACK = 9;
    public static final int PERSON_NAV = 10;
    public static final int PEOPLE_LARGE = 12;
    public static final int OPEN_BOOK = 13;

    private int type;
    private int size;
    private Color color = Color.WHITE;
    
    public SimpleIcon(int type, int size) {
        this.type = type;
        this.size = size;
        if (type == ARROW_RIGHT) {
            // New specified color for fallback: #18417f
            this.color = new Color(24, 65, 127);
        }
    }

    public SimpleIcon(int type) {
        this(type, 24);
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(color);

        float strokeWidth = Math.max(2f, size / 12f);
        g2.setStroke(new BasicStroke(strokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        int p = (int)(size * 0.1);
        int s = (int)(size * 0.8);
        x += p;
        y += p;
        switch (type) {
            // person icon
            case PERSON:
                g2.fillOval(x + s/4, y, s/2, s/2);
                g2.fillArc(x, y + s/2, s, s, 190, 160);
                break;
            case PERSON_NAV:
                g2.drawOval(x + s/4, y, s/2, s/2);
                g2.drawArc(x, y + s/2, s, s, 190, 160);
                break;

            // book icon
            case BOOK:
                g2.setColor(new Color(255, 255, 255, 50));
                g2.fillRect(x + s/8, y, s*7/8, s);
                g2.setColor(new Color(255, 255, 255, 80));
                g2.fillRect(x, y, s/8, s);
                g2.setColor(color);
                g2.drawRect(x + s/8, y, s*7/8, s);
                g2.drawRect(x, y, s/8, s);
                g2.drawLine(x + s/8, y + s/6, x + s*7/8, y + s/6);
                break;
            case BOOK_STACK:
                g2.drawRect(x, y, s, s/3);
                g2.drawRect(x, y + s/3 + p/2, s, s/3);
                g2.drawRect(x, y + s*2/3 + p, s, s/3);
                break;
            // people icon
            case PEOPLE:
                g2.drawOval(x + s/3, y, s/3, s/3);
                g2.drawArc(x + s/6, y + s/3, s*2/3, s*2/3, 190, 160);
                g2.drawOval(x, y + s/4, s/3, s/3);
                g2.drawArc(x - s/6, y + s*7/12, s*2/3, s*2/3, 190, 160);
                break;
            case PEOPLE_LARGE:
                g2.setColor(new Color(255, 255, 255, 150));
                g2.fillOval(x, y + s/8, s/2, s/2);
                g2.fillArc(x - s/4, y + s*3/8, s, s/2, 200, 140);
                g2.setColor(color);
                g2.fillOval(x + s/4, y + s/4, s/2, s/2);
                g2.fillArc(x, y + s/2, s, s/2, 200, 140);
                break;
            case RECORDS:
                g2.drawRect(x, y, s, s);
                g2.drawLine(x + s/4, y + s/4, x + s*3/4, y + s/4);
                g2.drawLine(x + s/4, y + s/2, x + s*3/4, y + s/2);
                g2.drawLine(x + s/4, y + s*3/4, x + s*3/4, y + s*3/4);
                break;
            case DASHBOARD:
                g2.setStroke(new BasicStroke(1));
                g2.fillRect(x, y, s/2-p/2, s/2-p/2);
                g2.fillRect(x + s/2+p/2, y, s/2-p/2, s/2-p/2);
                g2.fillRect(x, y + s/2+p/2, s/2-p/2, s/2-p/2);
                g2.fillRect(x + s/2+p/2, y + s/2+p/2, s/2-p/2, s/2-p/2);
                break;
            case ARROW_RIGHT:
                g2.setStroke(new BasicStroke(size / 6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawLine(x + s/4, y, x + s*3/4, y + s/2);
                g2.drawLine(x + s/4, y + s, x + s*3/4, y + s/2);
                break;
            case ISSUE_BOOK:
                g2.setColor(new Color(255, 255, 255, 80));
                g2.fillRect(x + s/4, y, s*3/4, s/2);
                g2.fillRect(x, y + s*2/3, s, s/3);
                g2.setColor(color);
                g2.drawRect(x + s/4, y, s*3/4, s/2);
                g2.drawLine(x + s/4, y + s/4, x + s, y + s/4);
                g2.drawRect(x, y + s*2/3, s, s/3);
                g2.drawLine(x + s/4, y + s*2/3, x + s/4, y + s);
                g2.drawLine(x + s/2, y + s*2/3, x + s/2, y + s);
                break;
            // calendar icon
            case CALENDAR:
                g2.setColor(new Color(255, 255, 255, 80));
                g2.fillRect(x, y + s/8, s, s*7/8);
                g2.setColor(color);
                g2.drawRect(x, y + s/8, s, s*7/8);
                g2.drawLine(x, y + s/3, x+s, y + s/3);
                g2.setStroke(new BasicStroke(strokeWidth));
                g2.drawOval(x + s/6, y, s/6, s/4);
                g2.drawOval(x + s/2 - s/12, y, s/6, s/4);
                g2.drawOval(x + s*5/6 - s/6, y, s/6, s/4);
                int dotSize = Math.max(2, s / 12);
                g2.fillOval(x + s/6, y + s/2, dotSize, dotSize);
                g2.fillOval(x + s/2 - dotSize/2, y + s/2, dotSize, dotSize);
                g2.fillOval(x + s*5/6 - dotSize, y + s/2, dotSize, dotSize);
                g2.fillOval(x + s/6, y + s*3/4, dotSize, dotSize);
                g2.fillOval(x + s/2 - dotSize/2, y + s*3/4, dotSize, dotSize);
                g2.fillOval(x + s*5/6 - dotSize, y + s*3/4, dotSize, dotSize);
                break;
            case OPEN_BOOK:
                g2.setColor(new Color(255, 255, 255, 80));
                g2.fillArc(x, y + s/8, s, s*7/8, 0, 180);
                g2.setColor(color);
                g2.drawArc(x, y + s/8, s, s*7/8, 0, 180);
                g2.drawLine(x + s/2, y + s/8, x + s/2, y + s);
                g2.drawLine(x + s/4, y + s/3, x + s/2 - p, y + s/3);
                g2.drawLine(x + s/4, y + s/2, x + s/2 - p, y + s/2);
                g2.drawLine(x + s/4, y + s*2/3, x + s/2 - p, y + s*2/3);
                g2.drawLine(x + s/2 + p, y + s/3, x + s*3/4, y + s/3);
                g2.drawLine(x + s/2 + p, y + s/2, x + s*3/4, y + s/2);
                g2.drawLine(x + s/2 + p, y + s*2/3, x + s*3/4, y + s*2/3);
                break;
        }
        g2.dispose();
    }

    @Override
    public int getIconWidth() { 
        return size;
    }

    @Override
    public int getIconHeight() { 
        return size;
    }
}