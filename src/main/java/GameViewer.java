import javax.swing.*;
import java.awt.*;

public class GameViewer extends JPanel {
    private Game game;
    private boolean showInstructions = true;

    public GameViewer(Game game){
        this.game = game;

        JFrame frame = new JFrame("Triad Tactics");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600,400);
        frame.add(this);
        frame.setVisible(true);
    }

    public void hideInstructions() {
        showInstructions = false;
        repaint();
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.fillRect(0,0,getWidth(),getHeight());

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 16));

        if(showInstructions){
            g.drawString("Welcome to Triad Tactics!", 50, 50);
            g.drawString("Instructions:", 50, 80);
            g.drawString("1) You and the computer get 3 cards each.", 50, 110);
            g.drawString("2) Each turn, you can:", 50, 140);
            g.drawString("   a) Power Up a card (increase value)", 70, 170);
            g.drawString("   b) Change Suit of a card", 70, 200);
            g.drawString("   c) Pass to gain a bonus point", 70, 230);
            g.drawString("3) Highest hand wins the round. 5 rounds total.", 50, 260);
            g.drawString("Press Enter in the console to start the game.", 50, 290);
        }
    }

    public void drawScoreboard(Graphics g, int playerPoints, int computerPoints, int round){
        System.out.println("Round " + round + " Scoreboard: Player " + playerPoints + " - Computer " + computerPoints);
    }

    public void showRestartButton(){
        System.out.println("Game Over! You can restart.");
    }
}