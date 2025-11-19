import java.util.ArrayList;

public class Player {
    ArrayList<Card> hand;
    int points;
    String name;

    public Player(ArrayList<Card> hand, String name) {
        this.hand = hand;
        points = 0;
        this.name = name;
    }

    public void addPoints(int pointsToAdd) {
        points += pointsToAdd;
    }

    public void addCard(Card card) {
        hand.add(card);
    }

    public String toString(){
        return name + " has " + points + " points. \n " + name + "'s cards: " + hand;
    }

}
