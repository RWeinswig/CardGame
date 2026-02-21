import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;


public class GameViewer extends JFrame {

    private Game game;
    private final int WINDOW_WIDTH = 1000;
    private final int WINDOW_HEIGHT = 1000;
    private boolean showInstructions = true;
    private Image deckImage = new ImageIcon("src/main/resources/53.png").getImage();


    public GameViewer(Game game) {

        this.game = game;
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("The Game");
        this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        this.setVisible(true);

    }

   @Override
    public void paint(Graphics g) {
        g.setColor(Color.WHITE);
      g.fillRect(0, 0, getWidth(), getHeight());

        if (showInstructions) {
            drawInstructions(g);
        }
        else if (game.getPlayer() != null && !game.getPlayer().getHand().isEmpty()){
            drawGame(g);
        }
   }

   public void drawGame(Graphics g) {

        int startX = 200;
        int computerY = 150;

        g.setColor(Color.black);
        g.setFont(new Font("SansSerif", Font.BOLD, 18));
        g.drawString("Computer's Hand", startX, computerY - 20);

       for (int i = 0; i < game.getComputer().getHand().size(); i++) {
           Card card = game.getComputer().getCard(i);
           card.setPosition(startX + (i * 150), computerY);

           // Only show face up if allowed
           card.draw(g, game.isRevealComputerCards());
       }

       int playerY = 600;
       g.drawString("Your Hand", startX, playerY - 20);

       for (int i = 0; i < game.getPlayer().getHand().size(); i++) {
           Card card = game.getPlayer().getCard(i);
           card.setPosition(startX + (i * 150), playerY);

           card.draw(g, true);
       }

       Graphics2D g2d = (Graphics2D) g;

       int deckX = getWidth() / 2 - 50;
       int deckY = getHeight() / 2 - 50;

       AffineTransform old = g2d.getTransform();
       g2d.rotate(Math.toRadians(90), deckX + 50, deckY + 50);
       for (int i = 0; i < 3; i++) {
           g2d.drawImage(deckImage, deckX + i*3, deckY + i*3, 100, 100, this);
       }
       g2d.setTransform(old);
   }

    public void drawScoreboard(Graphics g, int playerScore, int computerScore, int round) {
        int bottom = getHeight() - 50;
        int top = 50;
        int barMaxHeight = bottom - top;
        int MAX_SCORE = 100;



        g.setColor(Color.DARK_GRAY);
        g.fillRect(600, 80, 300, 160);
        g.setColor(Color.WHITE);
        g.drawRect(600, 80, 300, 160);
        g.setColor(Color.BLACK);
        g.setFont(new Font("SansSerif", Font.BOLD, 18));
        g.drawString("=== SCOREBOARD ===", 700, 100);
        g.setFont(new Font("SansSerif", Font.BOLD, 16));
        g.setColor(Color.CYAN);
        g.drawString("Round: " + round, 700, 130);
        g.setColor(Color.GREEN);
        g.drawString(game.getPlayer().getName() + ": " + playerScore, 650, 170);
        g.setColor(Color.RED);
        g.drawString("Computer: " + computerScore, 650, 200);

        int playerHeight = (int)((playerScore / (double)MAX_SCORE) * barMaxHeight);
        int computerHeight = (int)((computerScore / (double)MAX_SCORE) * barMaxHeight);
        g.setColor(Color.GREEN);

        g.fillRect(650, bottom - playerHeight, 60, playerHeight);
        g.setColor(Color.RED);
        g.fillRect(750, bottom - computerHeight, 60, computerHeight);

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

    public void hideInstructions() {
        showInstructions = false;
        repaint();
    }
}