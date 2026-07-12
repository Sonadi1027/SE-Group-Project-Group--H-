import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// A plain panel with rounded corners (used as the grey "card" background)
class RoundedPanel extends JPanel {

    private final int radius;
    private final Color background;
    private final Color borderColor; // can be null if no border is wanted

    public RoundedPanel(int radius, Color background, Color borderColor) {
        this.radius = radius;
        this.background = background;
        this.borderColor = borderColor;
        setLayout(null);
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // fill the rounded background
        g2.setColor(background);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

        // draw the border, only if one was given
        if (borderColor != null) {
            g2.setColor(borderColor);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
        }

        super.paintComponent(g);
    }
}

// A text field with rounded corners and grey placeholder text
class RoundedTextField extends JTextField {

    private final String placeholder;
    private final int radius = 12;

    public RoundedTextField(String placeholder) {
        this.placeholder = placeholder;
        setOpaque(false);
        setBorder(new EmptyBorder(0, 16, 0, 16));
        setFont(new Font("Arial", Font.PLAIN, 14));
        setForeground(new Color(40, 40, 40));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // white rounded background with a light grey border
        g2.setColor(Color.WHITE);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        g2.setColor(new Color(215, 215, 215));
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);

        super.paintComponent(g);

        // show placeholder text only when the field is empty
        if (getText().isEmpty()) {
            g2.setColor(new Color(160, 160, 160));
            g2.setFont(getFont());
            FontMetrics fm = g2.getFontMetrics();
            int textY = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
            g2.drawString(placeholder, 16, textY);
        }
    }
}

// A password field with rounded corners and grey placeholder text
class RoundedPasswordField extends JPasswordField {

    private final String placeholder;
    private final int radius = 12;

    public RoundedPasswordField(String placeholder) {
        this.placeholder = placeholder;
        setOpaque(false);
        setBorder(new EmptyBorder(0, 16, 0, 44)); // extra space on the right for the eye icon
        setFont(new Font("Arial", Font.PLAIN, 14));
        setForeground(new Color(40, 40, 40));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(Color.WHITE);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        g2.setColor(new Color(215, 215, 215));
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);

        // draw placeholder BEFORE the real field content, so typed text stays on top
        if (getPassword().length == 0) {
            g2.setColor(new Color(160, 160, 160));
            g2.setFont(getFont());
            FontMetrics fm = g2.getFontMetrics();
            int textY = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
            g2.drawString(placeholder, 16, textY);
        }

        super.paintComponent(g);
    }
}

// A small button that toggles password visibility on/off (the "eye" icon)
class EyeToggleButton extends JButton {

    private boolean showing = false;
    private final JPasswordField target;
    private final char realEchoChar;

    public EyeToggleButton(JPasswordField target) {
        this.target = target;
        this.realEchoChar = target.getEchoChar();

        setText("\u25CF"); // filled dot = password hidden
        setFont(new Font("Arial", Font.PLAIN, 13));
        setForeground(new Color(140, 140, 140));
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        addActionListener(e -> {
            showing = !showing;
            target.setEchoChar(showing ? (char) 0 : realEchoChar);
            setText(showing ? "\u25CB" : "\u25CF"); // open circle = password visible
        });
    }
}

// A button with a solid rounded background (used for the maroon LOGIN button)
class RoundedButton extends JButton {

    private final int radius = 12;
    private final Color baseColor;
    private final Color hoverColor;

    public RoundedButton(String text, Color baseColor) {
        super(text);
        this.baseColor = baseColor;
        this.hoverColor = baseColor.darker();
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setForeground(Color.WHITE);
        setFont(new Font("Arial", Font.BOLD, 15));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setBackground(baseColor);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(hoverColor);
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(baseColor);
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

        super.paintComponent(g);
    }
}