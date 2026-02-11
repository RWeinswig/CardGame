import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

public class GameViewer extends JFrame {

    private Game game;
    private final int WINDOW_WIDTH = 1000;
    private final int WINDOW_HEIGHT = 1000;


    public GameViewer(Game game) {

        this.game = game;
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("The Game");
        this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        this.setVisible(true);

    }

   @Override
    public void paint(Graphics g) {

        drawInstructions(g);
   }


    private void drawInstructions(Graphics g) {
        g.setColor(Color.BLACK);
        Font instructionsFont = new Font("SansSerif", Font.PLAIN, 14);
        g.setFont(instructionsFont);
        g.drawString("=== TRIAD TACTICS ===", 250, 50);
        g.drawString("A 3-card battle game. Each round:", 250, 100);
        g.drawString("• You and computer draw 3 cards.", 250, 150);
        g.drawString("• You have 3 energy to spend:", 250, 200);
        g.drawString("  1. Power Up (+ random value to any card of your choice, you cannot subtract from a cards value)", 250, 250);
        g.drawString("  2. Change Suit", 250, 300);
        g.drawString("  3. Pass (+1 bonus point)", 250, 350);
        g.drawString("• Hands are scored. Higher score wins the round.", 250, 400);
        g.drawString("• After 5 rounds, highest total wins.", 250, 450);
        g.drawString("•Please enter your name: ", 250, 500);
    }
}