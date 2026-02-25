// Card Game by Ryan Weinswig
// February 25

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    private ArrayList<Card> cards;
    private int cardsLeft;

    // Creates a full deck with the provided ranks, suits, and values
    // Reshuffles the deck after creation
    public Deck(String[] ranks, String[] suits, int[] values, GameViewer viewer) {
        cards = new ArrayList<Card>();
        buildDeck(ranks, suits, values, viewer);

    cardsLeft = cards.size();
    shuffle();
    }

    // Goes through all the suits and values and adds the cards to the deck
    private void buildDeck(String[] ranks, String[] suits, int[] values, GameViewer viewer) {
        for (int suitIndex = 0; suitIndex < suits.length; suitIndex++) {
            for (int rankIndex = 0; rankIndex < ranks.length; rankIndex++ ) {
                int imageNumber = calculateImageNumber(rankIndex, suitIndex);
                cards.add(new Card(suits[suitIndex], values[rankIndex], imageNumber, viewer));
            }
        }
    }

    // Calculates the correct image number based on rank and suit
    private int calculateImageNumber(int rankIndex, int suitIndex) {
        return rankIndex * 4 + (suitIndex + 1);
    }

    // Check if the no undealt cards remain
    public boolean isEmpty() {
        return cardsLeft == 0;
    }

    // Number of undealt cards remaining
    public int getCardsLeft() {
        return cardsLeft;
    }

    // Deals a card from the deck
    // Cards are dealt from the "top"
    public Card deal() {
        if (isEmpty()) {
            return null;
        }
        cardsLeft --;
        return cards.get(cardsLeft);
    }

    // Randomly shuffles the deck using the Collections utility
    // Sets the cardsLeft to the full deck size
    public void shuffle() {
        Collections.shuffle(cards);
        cardsLeft = cards.size();
    }

}
