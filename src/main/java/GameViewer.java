import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

public class GameViewer extends JFrame {

    private Game game;
    private JButton restartButton;

    private static final int WINDOW_WIDTH = 1000;
    private static final int WINDOW_HEIGHT = 1000;
    private static final int CARD_SPACING = 150;
    private static final int WINNER_BORDER_THICKNESS = 6;
    private static final int WINNER_PADDING_X = 20;
    private static final int WINNER_PADDING_Y = 30;
    private static final int WINNER_BOX_HEIGHT = 150;
    private static final int SCOREBOARD_MARGIN = 50;
    private static final int MAX_SCORE = 100;
    private static final int DECK_STACK_COUNT = 3;
    private static final int DECK_STACK_SHIFT = 3;
    private static final Color GOLD = new Color(212, 175, 55);
    private static final int DECK_WIDTH = 100;
    private static final int DECK_HEIGHT = 100;

    private boolean showInstructions = true;
    private static final Image deckImage = new ImageIcon("src/main/resources/53.png").getImage();

    public GameViewer(Game game) {
        this.game = game;
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("The Game");
        this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        this.setLayout(null);

        // Setup the Restart Button
        restartButton = new JButton("Restart Game");
        restartButton.setBounds(WINDOW_WIDTH / 2 - 100, WINDOW_HEIGHT / 2 + 100, 200, 50);
        restartButton.setFont(new Font("SansSerif", Font.BOLD, 18));
        restartButton.setVisible(false);

        // When clicked, hide the button and tell the backend to restart
        restartButton.addActionListener(e -> {
            restartButton.setVisible(false);
            game.restartGame();
        });

        this.add(restartButton);
        this.setVisible(true);
    }

    public void showRestartButton() {
        restartButton.setVisible(true);
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        // 1. Draw the background first
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());

        // 2. Draw your game elements
        if (showInstructions) {
            drawInstructions(g);
        } else if (game.isGameOver()) {
            drawGameOver(g);
        } else if (game.getPlayer() != null && !game.getPlayer().getHand().isEmpty()) {
            drawGame(g);
        }

        // 3. Paint standard Swing components safely over your custom graphics
        super.paintComponents(g);
    }

    public void drawGame(Graphics g) {
        int startX = 200;
        int computerY = 150;

        g.setColor(Color.black);
        g.setFont(new Font("SansSerif", Font.BOLD, 18));
        g.drawString("Computer's Hand", startX, computerY - 20);

        g.setColor(Color.BLUE);
        g.drawString("Energy: " + game.getComputerEnergy(), startX - 120, computerY);

        Graphics2D g2 = (Graphics2D) g;

        for (int i = 0; i < game.getComputer().getHand().size(); i++) {
            Card card = game.getComputer().getCard(i);
            card.setPosition(startX + (i * CARD_SPACING), computerY);
            card.draw(g, game.isRevealComputerCards());
        }

        if (game.getRoundWinner().equals("COMPUTER")) {
            g2.setColor(GOLD);
            g2.setStroke(new BasicStroke(WINNER_BORDER_THICKNESS));
            int width = CARD_SPACING * game.getComputer().getHand().size();
            g2.drawRect(startX - WINNER_PADDING_X, computerY - WINNER_PADDING_Y, width + (WINNER_PADDING_X * 2), WINNER_BOX_HEIGHT);
        }

        int playerY = 600;

        g.setColor(Color.black);
        g.drawString("Your Hand", startX, playerY - 20);
        g.setColor(Color.MAGENTA);
        g.drawString("Energy: " + game.getPlayerEnergy(), startX - 120, playerY);

        for (int i = 0; i < game.getPlayer().getHand().size(); i++) {
            Card card = game.getPlayer().getCard(i);
            card.setPosition(startX + (i * CARD_SPACING), playerY);
            card.draw(g, true);
        }

        if (game.getRoundWinner().equals("PLAYER")) {
            g2.setColor(GOLD);
            g2.setStroke(new BasicStroke(6));
            int width = CARD_SPACING * game.getPlayer().getHand().size();
            g2.drawRect(startX - WINNER_PADDING_X, playerY - WINNER_PADDING_Y, width + (WINNER_PADDING_X * 2), WINNER_BOX_HEIGHT);
        }

        int deckX = getWidth() / 2 - 50;
        int deckY = getHeight() / 2 - 50;

        AffineTransform old = g2.getTransform();
        g2.rotate(Math.toRadians(90), deckX + 50, deckY + 50);

        for (int i = 0; i < DECK_STACK_COUNT; i++) {
            g2.drawImage(deckImage, deckX + i*DECK_STACK_SHIFT, deckY + i*DECK_STACK_SHIFT, DECK_WIDTH, DECK_HEIGHT, this);
        }
        g2.setTransform(old);
    }

    public void drawScoreboard(Graphics g, int playerScore, int computerScore, int round) {
        int bottom = getHeight() - SCOREBOARD_MARGIN;
        int top = SCOREBOARD_MARGIN;
        int barMaxHeight = bottom - top;

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
        g2.setColor(GOLD);
        g2.drawRect(300, 260, 400, 60);

        g.setFont(new Font("SansSerif", Font.BOLD, 40));
        if (playerWon) {
            g.drawString(game.getPlayer().getName() + " WINS!", 350, 450);
        } else if (computerWon) {
            g.drawString("COMPUTER WINS!", 350, 450);
        } else {
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
        g.drawString("  1. Power Up (+ random value)", 250, 250);
        g.drawString("  2. Change Suit", 250, 300);
        g.drawString("  3. Pass (+1 bonus point)", 250, 350);
        g.drawString("• Hands are scored. Higher score wins.", 250, 400);
        g.drawString("• After 5 rounds, highest total wins.", 250, 450);
        g.drawString("• Follow the popups and terminal prompts to play! ", 250, 500);
    }

    public void hideInstructions() {
        showInstructions = false;
        repaint();
    }
}