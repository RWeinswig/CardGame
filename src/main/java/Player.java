import java.util.ArrayList;

public class Player {
    private ArrayList<Card> hand;
    private int points;
    private String name;

    public Player(ArrayList<Card> hand, String name) {
        this.hand = hand;
        points = 0;
        this.name = name;
    }

    public Player(String name) {
        hand = new ArrayList<Card>();
        points = 0;
        this.name = name;
    }
    public void addPoints(int pointsToAdd) {
        points += pointsToAdd;
    }

    public int getPoints() {
        return points;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Card> getHand() {
        return hand;
    }

    public Card getCard(int index) {
        return hand.get(index);
    }

    public void addCard(Card card) {
        hand.add(card);
    }

    public void clearHand() {
        hand.clear();
    }

    public String toString(){
        return name + " has " + points + " points. \n " + name + "'s cards: " + hand;
    }

}
