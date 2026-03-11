// Card Game by Ryan Weinswig
// February 25

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;


public class GameViewer extends JFrame {

    // Gives the frontend access to the backend
    private Game game;

    // Height and Width of the board
    private static final int WINDOW_WIDTH = 1000;
    private static final int WINDOW_HEIGHT = 1000;

    // Horizontal spacing between cards when drawn in a hand
    private static final int CARD_SPACING = 150;

    // Thickness of gold border drawn around winning hand
    private static final int WINNER_BORDER_THICKNESS = 6;
    // Horizontal padding added to the left and right of the winning hand box
    private static final int WINNER_PADDING_X = 20;
    // Vertical padding added above the winning hand
    private static final int WINNER_PADDING_Y = 30;
    // X coordinate for the instruction
    private static final int INSTRUCTIONS_X = 250;
    // Height of the winner highlight box
    private static final int WINNER_BOX_HEIGHT = 150;

    // Margin from the top and bottom edges
    private static final int SCOREBOARD_MARGIN = 50;
    // Maximum possible score used to scale scoreboard bar
    private static final int MAX_SCORE = 100;

    // Number of overlapping cards drawn to visually represent the deck
    private static final int DECK_STACK_COUNT = 3;
    // Pixel offset between each stacked card
    private static final int DECK_STACK_SHIFT = 3;

    // Gold color
    private static final Color GOLD = new Color(212, 175, 55);

    // Deck size constants
    private static final int DECK_WIDTH = 100;
    private static final int DECK_HEIGHT = 100;

    // Used to determine when to show the Instructions
    private boolean showInstructions = true;
    // Gives us access to the image that represents the deck, or just the back image of a card
    private static final Image deckImage = new ImageIcon("src/main/resources/53.png").getImage();


    //
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

      // Check to see if at the beginning of the game and we need to draw instructions
       // Want to check this first since this is the first thing that should happen whether the user plays or not
        if (showInstructions) {
            drawInstructions(g);
        }
        // Otherwise check to see if the game has ended and the user has lost
        else if (game.isGameOver()) {
            drawGameOver(g);
        }
        // Only print out the cards and hands if the user has a hand and cards
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
           card.setPosition(startX + (i * CARD_SPACING), computerY);

           card.draw(g, game.isRevealComputerCards());
       }

       // If the computer won the round
       // This draws a golden box around their cards to signal they won
       if (game.getRoundWinner().equals("COMPUTER")) {

           g2.setColor(GOLD); // gold
           // Width of the stroke
           g2.setStroke(new BasicStroke(WINNER_BORDER_THICKNESS));
           //
           int width = CARD_SPACING * game.getComputer().getHand().size();
           g2.drawRect(startX - WINNER_PADDING_X, computerY - WINNER_PADDING_Y, width + (WINNER_PADDING_X * 2), WINNER_BOX_HEIGHT);
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
           card.setPosition(startX + (i * CARD_SPACING), playerY);

           card.draw(g, true);
       }

       // If the player won the round
       if (game.getRoundWinner().equals("PLAYER")) {
           // gold
           g2.setColor(GOLD);
           g2.setStroke(new BasicStroke(6));

           // Draw the golden box the width around the user's hand depending on how many cards are in their hand
           int width = CARD_SPACING * game.getPlayer().getHand().size();
           g2.drawRect(startX - WINNER_PADDING_X, playerY - WINNER_PADDING_Y, width + (WINNER_PADDING_X * 2), WINNER_BOX_HEIGHT);
       }

       // This is the x and y coordinates for the deck in the middle
       int deckX = getWidth() / 2 - 50;
       int deckY = getHeight() / 2 - 50;

       // We need to keep track of the original rotation in order to ensure that when we print out new cards they will be in the correct direction
       AffineTransform old = g2.getTransform();
       // This allows us to rotate the cards in the deck sideways
       g2.rotate(Math.toRadians(90), deckX + 50, deckY + 50);

       // Draws three cards slightly shifted from each other to give the appearance that there are multiple cards
       for (int i = 0; i < DECK_STACK_COUNT; i++) {
           g2.drawImage(deckImage, deckX + i*DECK_STACK_SHIFT, deckY + i*DECK_STACK_SHIFT, DECK_WIDTH, DECK_HEIGHT, this);
       }
       // Reset the transformation back to normal
       g2.setTransform(old);
   }

   // Method visualizes the scoreboard
    public void drawScoreboard(Graphics g, int playerScore, int computerScore, int round) {
        // This is the lowest we want the box to go that draws the scoreboard
        int bottom = getHeight() - SCOREBOARD_MARGIN;
        // Highest y value we want to get to
        int top = SCOREBOARD_MARGIN;
        // This is the max height we want the bar to be
        int barMaxHeight = bottom - top;

        // Prints out the scoreboard and the other text around it
        g.setColor(Color.BLACK);
        g.setFont(new Font("SansSerif", Font.BOLD, 18));
        g.drawString("=== SCOREBOARD ===", 700, 100);
        g.setFont(new Font("SansSerif", Font.BOLD, 16));
        g.setColor(Color.ORANGE);
        g.drawString("Round: " + round, 700, 130);
        g.setColor(Color.GREEN);
        g.drawString(game.getPlayer().getName() + ": " + playerScore, 650, 170);
        g.setColor(Color.RED);
        g.drawString("Computer: " + computerScore, 650, 200);

        // This creates the height for both the player's and the computer's bars
        // This does it by dividing the scores by the max possible score and then multiplying it by the max height of the bar
        int playerHeight = (int)((playerScore / (double)MAX_SCORE) * barMaxHeight);
        int computerHeight = (int)((computerScore / (double)MAX_SCORE) * barMaxHeight);

        // Player's bar
        g.setColor(Color.GREEN);
        g.fillRect(650, bottom - playerHeight, 60, playerHeight);
        // Computer's bar
        g.setColor(Color.RED);
        g.fillRect(750, bottom - computerHeight, 60, computerHeight);

    }

    // Method to draw the ending animation
    public void drawGameOver(Graphics g) {
        // Casts g object to more advanced 2d version, in order to access stroke width
        Graphics2D g2 = (Graphics2D) g;

        // Get the scores of both the user and the computer
        int playerScore = game.getPlayer().getPoints();
        int computerScore = game.getComputer().getPoints();

        // Creates a black background for more contrast
        g.setColor(Color.BLACK);
        g.fillRect(0,0,getWidth(), getHeight());

        // Prints out that the game is over
        g.setColor(Color.WHITE);
        g.setFont(new Font ("SansSerif", Font.BOLD, 60));
        g.drawString("GAME OVER", 320, 150);

        // Prints out the scores of the user and of the computer
        g.setFont(new Font("SansSerif", Font.BOLD, 30));
        g.drawString(game.getPlayer().getName() + ": " + playerScore, 350, 300);
        g.drawString("Computer: " + computerScore, 350, 350);

        // Store both a playerWon and computerWon boolean
        // We need both in order to check if there is a tie or not
        boolean playerWon = playerScore > computerScore;
        boolean computerWon = computerScore > playerScore;

        // Set the stroke width of the golden rectangles that surround both scores
        g2.setStroke(new BasicStroke(8));
        // Set the color to gold
        g2.setColor(GOLD);
        // Draw the rectangles to surround both scores
        g2.drawRect(300, 260, 400, 60);

        // Print out which player won
        g.setFont(new Font("SansSerif", Font.BOLD, 40));
        // If the player won
        if (playerWon) {
            // Print out that they won
            g.drawString(game.getPlayer().getName() + " WINS!", 350, 450);
        }
        // Otherwise if the computer won
        else if (computerWon) {
            // Print out that the computer won
            g.drawString("COMPUTER WINS!", 350, 450);
        }
        // Otherwise
        else {
            // Print out that it was a tie
            g.drawString("IT'S A TIE!", 380, 450);
        }


    }

    private void drawInstructions(Graphics g) {
        g.setColor(Color.BLACK);
        Font instructionsFont = new Font("SansSerif", Font.PLAIN, 14);
        g.setFont(instructionsFont);
        g.drawString("=== TRIAD TACTICS ===", INSTRUCTIONS_X, 50);
        g.drawString("A 3-card battle game. Each round:", INSTRUCTIONS_X, 100);
        g.drawString("• You and computer draw 3 cards.", INSTRUCTIONS_X, 150);
        g.drawString("• You have 3 energy to spend:", INSTRUCTIONS_X, 200);
        g.drawString("  1. Power Up (+ random value to any card of your choice, you cannot subtract from a cards value)", INSTRUCTIONS_X, 250);
        g.drawString("  2. Change Suit", INSTRUCTIONS_X, 300);
        g.drawString("  3. Pass (+1 bonus point)", INSTRUCTIONS_X, 350);
        g.drawString("  4. Discard (Discard a card of your choice and draw a new card)", INSTRUCTIONS_X, 400);
        g.drawString("• Hands are scored. Higher score wins the round.", INSTRUCTIONS_X, 450);
        g.drawString("• After 5 rounds, highest total wins.", INSTRUCTIONS_X, 500);
        g.drawString("•Please enter your name: ", INSTRUCTIONS_X, 550);
    }

    public void hideInstructions() {
        // If given the message to hide the instructions
        // Set the variable show Instructions to false
        showInstructions = false;
        // Repaint the window now without the instructions
        repaint();
    }
}