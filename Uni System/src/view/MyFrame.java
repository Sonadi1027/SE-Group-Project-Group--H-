import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.HashMap;
import java.util.Map;

public class MyFrame extends JFrame implements ActionListener {

    // ---------- form components ----------
    private RoundedTextField usernameField;
    private RoundedPasswordField passwordField;
    private RoundedButton loginButton;
    private JButton signUpButton;
    private JButton forgotPasswordButton;
    private EyeToggleButton eyeToggle;
    private JLabel heading;
    private JLabel roleLabel;
    private JLabel userLabel;
    private JLabel passLabel;
    private JCheckBox keepSignedIn;

    // ---------- role selection ----------
    private JRadioButton studentRadio;
    private JRadioButton lecturerRadio;
    private JRadioButton adminRadio;
    private ButtonGroup roleGroup;

    // ---------- structural panels ----------
    private ImagePanel leftPanel;
    private RoundedPanel card;
    private JLabel capIcon;
    private JLabel systemTitle;
    private JLabel systemSubtitle;
    private JLabel campusName;

    // sizing rules for the responsive layout
    private static final int MIN_WIDTH = 900;
    private static final int MIN_HEIGHT = 650;
    private static final int CARD_MAX_WIDTH = 470;
    private static final int SIDE_MARGIN = 40;
    private static final int CARD_PAD = 36;

    // maroon used across the app - keep this consistent with SignUpFrame
    private static final Color MAROON = new Color(123, 17, 19);
    private static final Color GOLD = new Color(230, 180, 60);

    // colors for the grey card UI
    private static final Color PAGE_BG = new Color(255, 255, 255);
    private static final Color CARD_BG = new Color(240, 240, 240);   // the grey card
    private static final Color CARD_BORDER = new Color(225, 225, 225);
    private static final Color TEXT_DARK = new Color(35, 35, 40);
    private static final Color TEXT_GREY = new Color(110, 116, 122);

    // ---------- TEMPORARY demo data ----------
    // Replace this whole map + the checkLogin() method with a real
    // DBConnector.authenticate(username, password) call that returns
    // the role stored in your `users` table. This is only here so the
    // role-matching logic below has something to run against.
    private static final Map<String, String[]> DEMO_USERS = new HashMap<>();
    static {
        // username -> { password, role }
        DEMO_USERS.put("student@kln.ac.lk", new String[]{"password", "student"});
        DEMO_USERS.put("lecturer@kln.ac.lk", new String[]{"password", "lecturer"});
        DEMO_USERS.put("admin@kln.ac.lk", new String[]{"password", "admin"});
    }

    public MyFrame() {
        setTitle("University of Kelaniya - Open Learning Portal");
        setSize(1000, 680);
        setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        setResizable(true); // window can be resized/maximized - layout adapts
        getContentPane().setBackground(PAGE_BG);

        // ---------- LEFT PANEL: campus photo with maroon overlay ----------
        java.net.URL imageUrl = this.getClass().getResource("campus.jpg");
        if (imageUrl == null) {
            System.out.println("campus.jpg NOT FOUND - check it's in the same folder as MyFrame.java, then Rebuild Project.");
        } else {
            System.out.println("campus.jpg found at: " + imageUrl);
        }
        Image campusImage = (imageUrl != null) ? new ImageIcon(imageUrl).getImage() : null;
        leftPanel = new ImagePanel(campusImage);
        add(leftPanel);

        capIcon = new JLabel("\uD83C\uDF93"); // graduation cap
        capIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 34));
        capIcon.setForeground(Color.WHITE);
        leftPanel.add(capIcon);

        systemTitle = new JLabel("UNIVERSITY MANAGEMENT SYSTEM");
        systemTitle.setFont(new Font("Arial", Font.BOLD, 19));
        systemTitle.setForeground(Color.WHITE);
        leftPanel.add(systemTitle);

        systemSubtitle = new JLabel("UNIVERSITY OF KELANIYA");
        systemSubtitle.setFont(new Font("Arial", Font.BOLD, 13));
        systemSubtitle.setForeground(GOLD);
        leftPanel.add(systemSubtitle);

        campusName = new JLabel("<html>University of<br>Kelaniya</html>");
        campusName.setFont(new Font("Arial", Font.BOLD, 32));
        campusName.setForeground(Color.WHITE);
        leftPanel.add(campusName);

        // ---------- RIGHT SIDE: grey card containing the login form ----------
        card = new RoundedPanel(30, CARD_BG, CARD_BORDER);
        add(card);

        heading = new JLabel("Account Log In");
        heading.setFont(new Font("Arial", Font.BOLD, 25));
        heading.setForeground(TEXT_DARK);
        card.add(heading);

        // ----- role selection -----
        roleLabel = new JLabel("LOGIN AS");
        roleLabel.setFont(new Font("Arial", Font.BOLD, 13));
        roleLabel.setForeground(TEXT_GREY);
        card.add(roleLabel);

        studentRadio = new JRadioButton("Student");
        lecturerRadio = new JRadioButton("Lecturer");
        adminRadio = new JRadioButton("Admin");
        roleGroup = new ButtonGroup();
        for (JRadioButton radio : new JRadioButton[]{studentRadio, lecturerRadio, adminRadio}) {
            radio.setFont(new Font("Arial", Font.PLAIN, 15));
            radio.setBackground(CARD_BG);
            radio.setForeground(TEXT_DARK);
            radio.setFocusPainted(false);
            radio.setCursor(new Cursor(Cursor.HAND_CURSOR));
            roleGroup.add(radio);
            card.add(radio);
        }
        studentRadio.setSelected(true); // sensible default

        userLabel = new JLabel("USERNAME OR EMAIL");
        userLabel.setFont(new Font("Arial", Font.BOLD, 11));
        userLabel.setForeground(TEXT_GREY);
        card.add(userLabel);

        usernameField = new RoundedTextField("e.g., name@kln.ac.lk");
        card.add(usernameField);

        passLabel = new JLabel("PASSWORD");
        passLabel.setFont(new Font("Arial", Font.BOLD, 11));
        passLabel.setForeground(TEXT_GREY);
        card.add(passLabel);

        forgotPasswordButton = new JButton("Forgot Password?");
        forgotPasswordButton.setFont(new Font("Arial", Font.PLAIN, 11));
        forgotPasswordButton.setForeground(MAROON);
        forgotPasswordButton.setBorderPainted(false);
        forgotPasswordButton.setContentAreaFilled(false);
        forgotPasswordButton.setFocusPainted(false);
        forgotPasswordButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        forgotPasswordButton.setHorizontalAlignment(SwingConstants.RIGHT);
        forgotPasswordButton.addActionListener(this);
        card.add(forgotPasswordButton);

        passwordField = new RoundedPasswordField("Enter your password");
        card.add(passwordField);

        eyeToggle = new EyeToggleButton(passwordField);
        card.add(eyeToggle);

        keepSignedIn = new JCheckBox("Keep me signed in");
        keepSignedIn.setFont(new Font("Arial", Font.PLAIN, 13));
        keepSignedIn.setBackground(CARD_BG);
        keepSignedIn.setForeground(TEXT_DARK);
        keepSignedIn.setFocusPainted(false);
        card.add(keepSignedIn);

        loginButton = new RoundedButton("LOGIN   \u2192", MAROON);
        loginButton.addActionListener(this);
        card.add(loginButton);

        // ---------- Sign Up option (below the card) ----------
        signUpButton = new JButton("Don't have an account?  Sign Up Now");
        signUpButton.setFont(new Font("Arial", Font.PLAIN, 13));
        signUpButton.setForeground(MAROON);
        signUpButton.setBorderPainted(false);
        signUpButton.setContentAreaFilled(false);
        signUpButton.setFocusPainted(false);
        signUpButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        signUpButton.setHorizontalAlignment(SwingConstants.CENTER);
        signUpButton.addActionListener(this);
        add(signUpButton);


        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                relayout();
            }
        });

        relayout();
    }


    private void relayout() {
        Dimension size = getContentPane().getSize();
        int width = Math.max(size.width, MIN_WIDTH);
        int height = Math.max(size.height, MIN_HEIGHT);

        // ----- left campus panel: fills full height, ~42% of width -----
        int leftWidth = Math.max(320, (int) (width * 0.42));
        leftPanel.setBounds(0, 0, leftWidth, height);

        capIcon.setBounds(40, 40, 50, 50);
        systemTitle.setBounds(95, 45, leftWidth - 120, 25);
        systemSubtitle.setBounds(95, 72, leftWidth - 120, 20);
        campusName.setBounds(40, height - 160, leftWidth - 60, 100);

        // ----- right card: centered in the remaining space, capped width -----
        int availableRightWidth = width - leftWidth - (SIDE_MARGIN * 2);
        int cardWidth = Math.min(CARD_MAX_WIDTH, Math.max(500, availableRightWidth));
        int cardHeight = 452;

        int cardX = leftWidth + Math.max(SIDE_MARGIN, (availableRightWidth - cardWidth) / 2 + SIDE_MARGIN);
        int cardY = Math.max(20, (height - cardHeight - 60) / 2);
        card.setBounds(cardX, cardY, cardWidth, cardHeight);

        int fieldWidth = cardWidth - (CARD_PAD * 2);

        heading.setBounds(CARD_PAD, 28, fieldWidth, 34);

        // role radio row
        roleLabel.setBounds(CARD_PAD, 76, fieldWidth, 16);
        int radioWidth = fieldWidth / 3;
        studentRadio.setBounds(CARD_PAD, 94, radioWidth, 24);
        lecturerRadio.setBounds(CARD_PAD + radioWidth, 94, radioWidth, 24);
        adminRadio.setBounds(CARD_PAD + radioWidth * 2, 94, radioWidth, 24);

        userLabel.setBounds(CARD_PAD, 132, fieldWidth, 16);
        usernameField.setBounds(CARD_PAD, 150, fieldWidth, 40);

        passLabel.setBounds(CARD_PAD, 210, 200, 16);
        forgotPasswordButton.setBounds(CARD_PAD + fieldWidth - 130, 208, 130, 18);
        passwordField.setBounds(CARD_PAD, 230, fieldWidth, 40);
        eyeToggle.setBounds(CARD_PAD + fieldWidth - 34, 230, 30, 40);

        keepSignedIn.setBounds(CARD_PAD - 4, 284, 250, 24);

        loginButton.setBounds(CARD_PAD, 328, fieldWidth, 46);


        signUpButton.setBounds(cardX, cardY + cardHeight + 14, cardWidth, 25);

        revalidate();
        repaint();
    }

    private String getSelectedRole() {
        if (studentRadio.isSelected()) return "student";
        if (lecturerRadio.isSelected()) return "lecturer";
        if (adminRadio.isSelected()) return "admin";
        return null;
    }


    private String checkLogin(String username, String password) {
        String[] record = DEMO_USERS.get(username);
        if (record == null) return null;
        if (!record[0].equals(password)) return null;
        return record[1];
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == loginButton) {
            String selectedRole = getSelectedRole();
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (selectedRole == null) {
                JOptionPane.showMessageDialog(this, "Please select a role to log in as.");
                return;
            }

            String actualRole = checkLogin(username, password);

            if (actualRole == null) {
                JOptionPane.showMessageDialog(this, "Invalid username or password.");
            } else if (!actualRole.equals(selectedRole)) {
                JOptionPane.showMessageDialog(this,
                        "This account is not registered as a " + capitalize(selectedRole) + ".");
            } else {

                switch (actualRole) {
                    case "student":
                        JOptionPane.showMessageDialog(this, "Login successful - opening Student dashboard.");
                        break;
                    case "lecturer":
                        JOptionPane.showMessageDialog(this, "Login successful - opening Lecturer dashboard.");
                        break;
                    case "admin":
                        JOptionPane.showMessageDialog(this, "Login successful - opening Admin dashboard.");
                        break;
                }
            }
        } else if (event.getSource() == signUpButton) {

        } else if (event.getSource() == forgotPasswordButton) {
            JOptionPane.showMessageDialog(this, "Password reset flow goes here.");
        }
    }

    private String capitalize(String text) {
        if (text == null || text.isEmpty()) return text;
        return Character.toUpperCase(text.charAt(0)) + text.substring(1);
    }
}