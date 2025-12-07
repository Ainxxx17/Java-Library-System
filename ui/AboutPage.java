package ui;

import main.Main;

import javax.swing.*;
import java.awt.*;

public class AboutPage extends JPanel {

    // this will hold the main app so we can change pages
    private Main mainApp;

    // this will hold the background picture
    private Image backgroundImage;

    //TITLE: Header Text
    private JLabel teamTitle;

    // IAN
    private JLabel member1Name, member1Pos, member1Sec;

    // CHARLZE
    private JLabel member2Name, member2Pos, member2Sec;

    // REIMAR
    private JLabel member3Name, member3Pos, member3Sec;

    // KIEL
    private JLabel member4Name, member4Pos,  member4Sec;

    // Back Button
    private JButton backButton;

    //TITLE: Constructor for About Page
    public AboutPage(Main mainApp) {       
        this.mainApp = mainApp;
        
        // we load the image from the resources folder
        backgroundImage = new ImageIcon(
            getClass().getResource("/bgimages/about_usbg.png")
        ).getImage();

        initComponents();
    }

    //TITLE: Draw Background Image on Panel
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // we draw the image stretched to fit the panel size
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }

    //TITLE: Create and Add All Labels and Buttons
    private void initComponents() {

        // we use null layout so we can place things manually
        setLayout(null);

        //TITLE: Header Text
        teamTitle = new JLabel("Our Team");
        teamTitle.setFont(new Font("Segoe UI Black", Font.BOLD, 60));
        teamTitle.setForeground(Color.WHITE);
        teamTitle.setBounds(100, 120, 1350, 100);
        add(teamTitle);

        //TITLE: Team Members
        // IAN
        member1Name = new JLabel("Ian Cabalquinto", SwingConstants.CENTER);
        member1Name.setFont(new Font("Segoe UI", Font.BOLD, 30));
        member1Name.setForeground(Color.BLACK);
        member1Name.setBounds(60, 600, 350, 40);
        add(member1Name);

        member1Pos = new JLabel("LEADER", SwingConstants.CENTER);
        member1Pos.setFont(new Font("Segoe UI", Font.BOLD, 25));
        member1Pos.setForeground(Color.decode("#18417f"));
        member1Pos.setBounds(60, 580, 350, 150);
        add(member1Pos);

        member1Sec = new JLabel("BSIT 2A 1", SwingConstants.CENTER);
        member1Sec.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        member1Sec.setForeground(Color.decode("#18417f"));
        member1Sec.setBounds(60, 605, 350, 150);
        add(member1Sec);

        // CHARLZE
        member2Name = new JLabel("Charlze Boneo", SwingConstants.CENTER);
        member2Name.setFont(new Font("Segoe UI", Font.BOLD, 30));
        member2Name.setForeground(Color.BLACK);
        member2Name.setBounds(420, 600, 350, 40);
        add(member2Name);

        member2Pos = new JLabel("MEMBER", SwingConstants.CENTER);
        member2Pos.setFont(new Font("Segoe UI", Font.BOLD, 25));
        member2Pos.setForeground(Color.decode("#18417f"));
        member2Pos.setBounds(420, 580, 350, 150);
        add(member2Pos);

        member2Sec = new JLabel("BSIT 2A 1", SwingConstants.CENTER);
        member2Sec.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        member2Sec.setForeground(Color.decode("#18417f"));
        member2Sec.setBounds(420, 605, 350, 150);
        add(member2Sec);

        // REIMAR
        member3Name = new JLabel("Reimar Gutierrez", SwingConstants.CENTER);
        member3Name.setFont(new Font("Segoe UI", Font.BOLD, 30));
        member3Name.setForeground(Color.BLACK);
        member3Name.setBounds(780, 600, 350, 40);
        add(member3Name);

        member3Pos = new JLabel("MEMBER", SwingConstants.CENTER);
        member3Pos.setFont(new Font("Segoe UI", Font.BOLD, 25));
        member3Pos.setForeground(Color.decode("#18417f"));
        member3Pos.setBounds(780, 580, 350, 150);
        add(member3Pos);

        member3Sec = new JLabel("BSIT 2A 1", SwingConstants.CENTER);
        member3Sec.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        member3Sec.setForeground(Color.decode("#18417f"));
        member3Sec.setBounds(780, 605, 350, 150);
        add(member3Sec);

        // KIEL
        member4Name = new JLabel("Ezekiel Manalo", SwingConstants.CENTER);
        member4Name.setFont(new Font("Segoe UI", Font.BOLD, 30));
        member4Name.setForeground(Color.BLACK);
        member4Name.setBounds(1140, 600, 350, 40);
        add(member4Name);

        member4Pos = new JLabel("MEMBER", SwingConstants.CENTER);
        member4Pos.setFont(new Font("Segoe UI", Font.BOLD, 25));
        member4Pos.setForeground(Color.decode("#18417f"));
        member4Pos.setBounds(1140, 580, 350, 150);
        add(member4Pos);

        member4Sec = new JLabel("BSIT 2A 1", SwingConstants.CENTER);
        member4Sec.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        member4Sec.setForeground(Color.decode("#18417f"));
        member4Sec.setBounds(1140, 605, 350, 150);
        add(member4Sec);

        //TITLE: Back to Home Button
        backButton = new JButton("Back to Home");
        backButton.setFont(new Font("Verdana", Font.BOLD, 20));
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(Color.decode("#18417f"));
        backButton.setBounds(1200, 780, 225, 40);
        add(backButton);

        backButton.addActionListener(e -> mainApp.switchToPage(Main.HOME_PAGE));
    }
}
