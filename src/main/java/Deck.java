import java.util.ArrayList;

public class Deck {
    ArrayList<Card> cards;
    int cardsLeft;

    public Deck(String[] ranks, String[] suits, int[] values) {
        cards = new ArrayList<Card>();
        // Go through and add all of the suits and values
    for (String suit: suits) {
        for (int i=0; i< ranks.length; i++) {
            cards.add(new Card(ranks[i], suit, values[i]));
        }
    }

    // Amount of cards
    cardsLeft = cards.size();
    shuffle();
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

    public void shuffle() {
        for (int i=cards.size() - 1; i>=0; i--) {
            int random = (int)(Math.random() * (i+1));

            // this is where we want to swap cards[i] and cards[random]
            Card temp = cards.get(i);
            cards.set(i, cards.get(random));
            cards.set(random, temp);
        }
        // Want to reset the number of cards remaining back to the full deck
        cardsLeft = cards.size();
    }

}
