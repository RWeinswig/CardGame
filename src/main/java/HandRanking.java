import java.util.ArrayList;

public class HandRanking {

    public static int evaluate(ArrayList<Card> hand){
        if(hand==null || hand.size()!=3) return 0;

        int score = 0;

        // Three of a kind
        if(hand.get(0).getValue() == hand.get(1).getValue() &&
                hand.get(1).getValue() == hand.get(2).getValue()) return 9;

        // Pair
        if(hand.get(0).getValue() == hand.get(1).getValue() ||
                hand.get(0).getValue() == hand.get(2).getValue() ||
                hand.get(1).getValue() == hand.get(2).getValue()) score = 3;

        // Flush
        if(hand.get(0).getSuit().equals(hand.get(1).getSuit()) &&
                hand.get(1).getSuit().equals(hand.get(2).getSuit())) score = Math.max(score, 6);

        // High card
        int high = Math.max(hand.get(0).getValue(), Math.max(hand.get(1).getValue(), hand.get(2).getValue()));
        score = Math.max(score, high);

        return score;
    }
}