import javax.swing.*;
import java.awt.*;

public class Card {
    private int value;
    private String suit;
    private String rank;
    private int number;
    private Image image;

    private static Image backImage = new ImageIcon("src/main/resources/53.png").getImage();

    private GameViewer cardMat;
    private int x;
    private int y;
    private boolean show;

    public Card(String rank, String suit, int value, int number, GameViewer cardMat) {
        this.rank = rank;
        this.suit = suit;
        this.value = value;
        this.number = number;

        this.image = new ImageIcon("src/main/resources/" + String.valueOf(number) +".png").getImage();
        this.cardMat = cardMat;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getSuit() {
        return suit;
    }

    public void setSuit(String suit) {
        this.suit = suit;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public void powerUp(int add) {
        value += add;
    }

    public boolean isShow() {
        return show;
    }

    @Override
    public String toString() {
        return rank + " of " + suit;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void draw(Graphics g, boolean faceUp){
        if (faceUp) {
            g.drawImage(image, x, y, 100, 100, cardMat);
        } else {
            g.drawImage(backImage, x, y, 100, 100, cardMat);
        }
    }

}
