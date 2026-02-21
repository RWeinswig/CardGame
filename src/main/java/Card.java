import javax.swing.*;
import java.awt.*;

public class Card {
    private int value;
    private String suit;
    private int number;
    private Image image;
    private String rank;

    private static Image backImage = new ImageIcon("src/main/resources/53.png").getImage();
    private static final String[] ranks = {"A","2","3","4","5","6","7","8","9","10","J","Q","K"};

    private GameViewer cardMat;
    private int x;
    private int y;
    private boolean show;

    public Card(String suit, int value, int number, GameViewer cardMat) {

        this.suit = suit;
        this.value = value;
        this.number = number;
        this.rank = ranks[value - 1];

        this.image = new ImageIcon("src/main/resources/" + String.valueOf(number) +".png").getImage();
        this.cardMat = cardMat;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;


        int rankIndex = (value - 1) % 13;
        this.rank = ranks[rankIndex];
        updateImage();
    }

    public String getSuit() {
        return suit;
    }

    public void setSuit(String suit) {
        this.suit = suit;
        updateImage();
    }





    public void powerUp(int add) {
        setValue(value + add);
    }

    public boolean isShow() {
        return show;
    }

    public void updateImage() {
        int suitIndex = 0;

        switch(suit) {
            case "Spades": suitIndex = 0; break;
            case "Hearts": suitIndex = 1; break;
            case "Diamonds": suitIndex = 2; break;
            case "Clubs": suitIndex = 3; break;

        }

        int rankIndex = 0;
        switch(rank) {
            case "A": rankIndex = 0; break;
            case "2": rankIndex = 1; break;
            case "3": rankIndex = 2; break;
            case "4": rankIndex = 3; break;
            case "5": rankIndex = 4; break;
            case "6": rankIndex = 5; break;
            case "7": rankIndex = 6; break;
            case "8": rankIndex = 7; break;
            case "9": rankIndex = 8; break;
            case "10": rankIndex = 9; break;
            case "J": rankIndex = 10; break;
            case "Q": rankIndex = 11; break;
            case "K": rankIndex = 12; break;
        }

        number = rankIndex * 4 + (suitIndex + 1); // Same logic as Deck constructor
        image = new ImageIcon("src/main/resources/" + number + ".png").getImage();
    }


    @Override
    public String toString() {
        String[] ranks = {"A","2","3","4","5","6","7","8","9","10","J","Q","K"};
        return ranks[value - 1] + " of " + suit;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void draw(Graphics g, boolean faceUp){
        if (faceUp) {
            g.drawImage(image, x, y, 80, 120, cardMat);
        } else {
            g.drawImage(backImage, x, y, 80, 120, cardMat);
        }
    }

}
