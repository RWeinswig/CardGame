import java.util.ArrayList;

public class Player {
    private String name;
    private ArrayList<Card> hand;
    private int points;

    public Player(String name){
        this.name = name;
        this.hand = new ArrayList<>();
        this.points = 0;
    }

    public String getName(){ return name; }
    public ArrayList<Card> getHand(){ return hand; }
    public void addCard(Card card){ hand.add(card); }
    public Card getCard(int index){ return hand.get(index); }
    public void clearHand(){ hand.clear(); }

    public void addPoints(int pts){ points += pts; }
    public int getPoints(){ return points; }
    public void resetPoints(){ points = 0; }
}