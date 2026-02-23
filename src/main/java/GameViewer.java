import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;


public class GameViewer extends JFrame {

    // Gives the frontend access to the backend
    private Game game;

    // Height and Width of the board
    private final int WINDOW_WIDTH = 1000;
    private final int WINDOW_HEIGHT = 1000;

    // Used to determine when to show the Instructions
    private boolean showInstructions = true;
    // Gives us access to the image that represents the deck, or just the back image of a card
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
        else if (game.isGameOver()) {
            drawGameOver(g);
        }
        else if (game.getPlayer() != null && !game.getPlayer().getHand().isEmpty()){
            drawGame(g);
        }
   }

   public void drawGame(Graphics g) {

        // X coordinate of the first card for both computer and player
       int startX = 200;
        // Y coordinate of all of the computer's cards
        int computerY = 150;

        // Sets the color to black and prints out description that says computer's hand
        g.setColor(Color.black);
        g.setFont(new Font("SansSerif", Font.BOLD, 18));
        g.drawString("Computer's Hand", startX, computerY - 20);

        // Prints out the amount of energy points computer has
       g.setColor(Color.BLUE);
       g.drawString("Energy: " + game.getComputerEnergy(), startX - 120, computerY);

       // Casts g object to more advanced 2d version, in order to access stroke width
       Graphics2D g2 = (Graphics2D) g;


       // Draws the cards on the screen in the correct locations
       for (int i = 0; i < game.getComputer().getHand().size(); i++) {
           Card card = game.getComputer().getCard(i);
           card.setPosition(startX + (i * 150), computerY);

           card.draw(g, game.isRevealComputerCards());
       }

       // If the computer won the round
       // This draws a golden box around their cards to signal they won
       if (game.getRoundWinner().equals("COMPUTER")) {

           g2.setColor(new Color(212, 175, 55)); // gold
           // Width of the stroke
           g2.setStroke(new BasicStroke(6));

           int width = 150 * game.getComputer().getHand().size();
           g2.drawRect(startX - 20, computerY - 30, width + 40, 150);
       }



       // Y coordinate of all the cards
       int playerY = 600;

        // Draws the user's energy points on the screen and a description that says user's cards
       g.drawString("Your Hand", startX, playerY - 20);
       g.setColor(Color.MAGENTA);
       g.drawString("Energy: " + game.getPlayerEnergy(), startX - 120, playerY);

       // Draws all of the user's cards
       for (int i = 0; i < game.getPlayer().getHand().size(); i++) {
           Card card = game.getPlayer().getCard(i);
           card.setPosition(startX + (i * 150), playerY);

           card.draw(g, true);
       }

       if (game.getRoundWinner().equals("PLAYER")) {
           g2.setColor(new Color(212, 175, 55)); // gold
           g2.setStroke(new BasicStroke(6));

           int width = 150 * game.getPlayer().getHand().size();
           g2.drawRect(startX - 20, playerY - 30, width + 40, 150);
       }



       int deckX = getWidth() / 2 - 50;
       int deckY = getHeight() / 2 - 50;

       AffineTransform old = g2.getTransform();
       g2.rotate(Math.toRadians(90), deckX + 50, deckY + 50);
       for (int i = 0; i < 3; i++) {
           g2.drawImage(deckImage, deckX + i*3, deckY + i*3, 100, 100, this);
       }
       g2.setTransform(old);
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

    public void drawGameOver(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        int playerScore = game.getPlayer().getPoints();
        int computerScore = game.getComputer().getPoints();

        g.setColor(Color.BLACK);
        g.fillRect(0,0,getWidth(), getHeight());

        g.setColor(Color.WHITE);
        g.setFont(new Font ("SansSerif", Font.BOLD, 60));
        g.drawString("GAME OVER", 320, 150);

        g.setFont(new Font("SansSerif", Font.BOLD, 30));
        g.drawString(game.getPlayer().getName() + ": " + playerScore, 350, 300);
        g.drawString("Computer: " + computerScore, 350, 350);

        boolean playerWon = playerScore > computerScore;
        boolean computerWon = computerScore > playerScore;

        g2.setStroke(new BasicStroke(8));
        g2.setColor(new Color(212, 175, 55));

        g2.drawRect(300, 260, 400, 60);
        g.setFont(new Font("SansSerif", Font.BOLD, 40));
        if (playerWon) {

            g.drawString(game.getPlayer().getName() + " WINS!", 350, 450);
        }
        else if (computerWon) {
            g2.drawRect(300, 310, 400, 60);

            g.drawString("COMPUTER WINS!", 350, 450);
        }
        else {

            g.drawString("IT'S A TIE!", 380, 450);
        }


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