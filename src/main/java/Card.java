import javax.swing.*;
import java.awt.*;

public class Card {
    // Constants
    private static final int CARD_WIDTH = 80;
    private static final int CARD_HEIGHT = 120;
    private static Image backImage = new ImageIcon("src/main/resources/53.png").getImage();
    private static final String[] ranks = {"A","2","3","4","5","6","7","8","9","10","J","Q","K"};

    // Instance variables
    private int value;
    private String suit;
    private int number;
    private Image image;

    private int x;
    private int y;

    private GameViewer viewer;

    public Card(String suit, int value, int number, GameViewer viewer) {
        this.suit = suit;
        this.value = value;
        this.number = number;
        this.viewer = viewer;
        updateImage();
    }

    public int getValue() {
        return value;
    }

    public String getSuit() {
        return suit;
    }

    public void setValue(int value) {
        this.value = value;
        updateImage();
    }

    public void setSuit(String suit) {
        this.suit = suit;
        updateImage();
    }

    public void powerUp(int add) {
        setValue(value + add);
    }

    public void updateImage() {
        int suitIndex = getSuitIndex();
        int rankIndex = value - 1;
        number = rankIndex * 4 + (suitIndex + 1);
        image = new ImageIcon("src/main/resources/" + number + ".png").getImage();
    }

    private int getSuitIndex() {
        switch (suit) {
            case "Spades": return 0;
            case "Hearts": return 1;
            case "Diamonds": return 2;
            case "Clubs": return 3;
        }
        return -1;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void draw(Graphics g, boolean faceUp){
        if (faceUp) {
            g.drawImage(image, x, y, CARD_WIDTH, CARD_HEIGHT, viewer);
        } else {
            g.drawImage(backImage, x, y, CARD_WIDTH, CARD_HEIGHT, viewer);
        }
    }

    @Override
    public String toString() {
        return ranks[value - 1] + " of " + suit;
    }
}