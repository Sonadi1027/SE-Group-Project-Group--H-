import javax.swing.*;
import java.awt.*;

class ImagePanel extends JPanel {

    private final Image backgroundImage;

    public ImagePanel(Image backgroundImage) {
        this.backgroundImage = backgroundImage;
        setLayout(null);
        setOpaque(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // draw the campus photo stretched to fill the whole panel
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }

        // maroon tint on top so white text stays readable over the photo
        g.setColor(new Color(60, 8, 10, 165));
        g.fillRect(0, 0, getWidth(), getHeight());
    }
}