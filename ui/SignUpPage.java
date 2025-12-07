package ui;

// this import use the main class to switch pages
import main.Main;

import models.Member;
import utils.FileHandler;
import utils.IDGenerator;

import javax.swing.*;
import java.awt.*;

public class SignUpPage extends JPanel {

    private Main mainApp;

    private JTextField nameField, usernameField;                        
    private JPasswordField passwordField, confirmPasswordField; 

    private Image backgroundImage;              

    private JPanel signupFormPanel;
    private JLabel titleLabel, nameLabel, unLabel, pwLabel, cpLabel, loginLink;
    private JButton signupButton;

    public SignUpPage(Main mainApp) {
        this.mainApp = mainApp;

        // BG IMAGE
        // we load the background image from the resource folder in the project.
        backgroundImage = new ImageIcon(
                getClass().getResource("/bgimages/login_pagebg.png")
        ).getImage();

        // this method adds all the labels and buttons to the page.
        initComponents();
    }

    // this method is special and is used to draw things on the panel, like our background image.
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // draw scaled background
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }

    // this is where we create and set up all the parts of the page (buttons, text).
    private void initComponents() {

        // we use 'null' layout so we can set the exact position of every item using setBounds.
        setLayout(null);
        // make transparent to see background.
        setOpaque(false);

        // CREATE THE WHITE LOGIN PANEL
        // this is the white box in the middle where the user types their info.
        signupFormPanel = new JPanel();
        signupFormPanel.setLayout(null);
        signupFormPanel.setBackground(Color.WHITE);
        signupFormPanel.setBounds(780, 120, 630, 590);

        // ADD COMPONENTS TO THE WHITE PANEL
        // TITLE: Sign Up Title
        titleLabel = new JLabel("Sign up", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI Black", Font.BOLD, 40));
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setBounds(0, 90, 630, 60);
        signupFormPanel.add(titleLabel);

        // TITLE: Name Label
        nameLabel = new JLabel("Name:", SwingConstants.LEFT);
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        nameLabel.setForeground(Color.BLACK);
        nameLabel.setBounds(135, 170, 630, 60);
        signupFormPanel.add(nameLabel);

        // the text field where the user enters the name.
        nameField = new JTextField();
        nameField.setBounds(135, 220, 170, 30);
        signupFormPanel.add(nameField);

        // TITLE: Username Label
        unLabel = new JLabel("User Name:", SwingConstants.LEFT);
        unLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        unLabel.setForeground(Color.BLACK);
        unLabel.setBounds(350, 170, 630, 60);
        signupFormPanel.add(unLabel);

        // the text field where the user enters the username.
        usernameField = new JTextField();
        usernameField.setBounds(350, 220, 170, 30);
        signupFormPanel.add(usernameField);

        // TITLE: Password Label
        pwLabel = new JLabel("Password:", SwingConstants.LEFT);
        pwLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        pwLabel.setForeground(Color.BLACK);
        pwLabel.setBounds(135, 270, 630, 60);
        signupFormPanel.add(pwLabel);

        // the text field where the user enters the password (hides text).
        passwordField = new JPasswordField();
        passwordField.setBounds(135, 320, 170, 30);
        signupFormPanel.add(passwordField);

        // TITLE: Confirm Password Label
        cpLabel = new JLabel("Confirm Password:", SwingConstants.LEFT);
        cpLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        cpLabel.setForeground(Color.BLACK);
        cpLabel.setBounds(350, 270, 630, 60);
        signupFormPanel.add(cpLabel);

        // the text field where the user confirms the password (hides text).
        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setBounds(350, 320, 170, 30);
        signupFormPanel.add(confirmPasswordField);

        // TITLE: Create Account Button
        signupButton = new JButton("Create Account");
        signupButton.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        signupButton.setForeground(Color.WHITE);
        signupButton.setBackground(Color.BLACK);
        signupButton.setBounds(240, 440, 150, 35);
        signupFormPanel.add(signupButton);

        // TITLE: Login Link
        loginLink = new JLabel("Log in");
        loginLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginLink.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        loginLink.setForeground(Color.decode("#18417f"));
        loginLink.setBounds(295, 480, 200, 30);
        signupFormPanel.add(loginLink);

        add(signupFormPanel);

        // ACTION LISTENERS FOR BUTTONS AND LINKS

        // when the Create Account button is clicked, we run the handleSignUp method.
        signupButton.addActionListener(e -> handleSignUp());

        // when the Log in link is clicked, we clear the boxes and switch to the Login page.
        loginLink.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                clearFields();
                mainApp.switchToPage(Main.LOGIN_PAGE);
            }
        });
    }

    // this method checks if the sign-up information is correct and then creates the new account.
    private void handleSignUp() {
        String name = nameField.getText();
        String username = usernameField.getText();
        String pass1 = new String(passwordField.getPassword());
        String pass2 = new String(confirmPasswordField.getPassword());

        // Validation
        // Check if any of the boxes are empty.
        if (name.isEmpty() || username.isEmpty() || pass1.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // check if the two password boxes match.
        if (!pass1.equals(pass2)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // check if username already exists
        // Loop through all existing users to make sure the chosen username is not already taken.
        for (models.User user : mainApp.getAllUsers()) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                JOptionPane.showMessageDialog(this, "Username '" + username + "' is already taken.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // Generate ID and Create Member
        // create a new unique Library ID for the new member (like M001, M002, etc.).
        String newId = IDGenerator.generateNextMemberId(mainApp.getAllUsers());

        // Create new Member
        // make a new Member object using the generated ID and the information they typed.
        Member newMember = new Member(newId, name, username, pass1);

        // Add to in-memory list
        // add the new member to the list of users that the application is currently using.
        mainApp.getAllUsers().add(newMember);

        // Append to file
        // save the new member's information permanently to the user data file.
        FileHandler.appendMember(Main.MEMBERS_FILE, newMember);

        // Show success and go to Login
        // show a message that the account was created successfully and tell them their Library ID.
        JOptionPane.showMessageDialog(
                this,
                "Registration successful!\nYour new Library ID is: " + newId + "\nPlease log in.",
                "Success",
                JOptionPane.INFORMATION_MESSAGE
        );

        clearFields();
        mainApp.switchToPage(Main.LOGIN_PAGE);
    }

    // this method clears the text from all the input boxes after registration or when leaving the page.
    private void clearFields() {
        nameField.setText("");
        usernameField.setText("");
        passwordField.setText("");
        confirmPasswordField.setText("");
    }
}
