import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    private ArrayList<Card> cards;
    private int cardsLeft;

    public Deck(String[] ranks, String[] suits, int[] values, GameViewer viewer) {
        cards = new ArrayList<Card>();
        buildDeck(ranks, suits, values, viewer);
        cardsLeft = cards.size();
        shuffle();
    }

    private void buildDeck(String[] ranks, String[] suits, int[] values, GameViewer viewer) {
        for (int suitIndex = 0; suitIndex < suits.length; suitIndex++) {
            for (int rankIndex = 0; rankIndex < ranks.length; rankIndex++ ) {
                int imageNumber = calculateImageNumber(rankIndex, suitIndex);
                cards.add(new Card(suits[suitIndex], values[rankIndex], imageNumber, viewer));
            }
        }
    }

    private int calculateImageNumber(int rankIndex, int suitIndex) {
        return rankIndex * 4 + (suitIndex + 1);
    }

    public boolean isEmpty() {
        return cardsLeft == 0;
    }

    public int getCardsLeft() {
        return cardsLeft;
    }

    public Card deal() {
        if (isEmpty()) {
            return null;
        }
        cardsLeft --;
        return cards.get(cardsLeft);
    }

    public void shuffle() {
        Collections.shuffle(cards);
        cardsLeft = cards.size();
    }
}