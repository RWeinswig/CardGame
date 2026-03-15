import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    private ArrayList<Card> cards;
    private GameViewer window;

    public Deck(String[] ranks, String[] suits, int[] values, GameViewer window){
        cards = new ArrayList<>();
        this.window = window;

        for(int i=0; i<ranks.length; i++){
            for(String suit : suits){
                cards.add(new Card(ranks[i], suit, values[i]));
            }
        }
    }

    public void shuffle(){
        Collections.shuffle(cards);
    }

    public Card deal(){
        if(cards.isEmpty()) return null;
        return cards.remove(0);
    }
}