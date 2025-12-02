import java.util.ArrayList;
public class HandRanking {
    public static int evaluate(ArrayList<Card> hand) {

        // If user has one of these combinations, they get a ceartain amount of points

        if (isStraightFlush(hand)) {
            return 15;
        }

        if (isThreeOfAKind(hand)) {
            return 12;
        }

        if (isFlush(hand)) {
            return 9;
        }

        if (isStraight(hand)) {
            return 6;
        }

        if (isPair(hand)) {
            return 3;
        }

        return 0;
    }

    // If all three of the user's values are the same, they have three of a kind
    private static boolean isThreeOfAKind(ArrayList<Card> hand) {
        if (hand.get(0).getValue() == hand.get(1).getValue() && hand.get(1).getValue() == hand.get(2).getValue()) {
            return true;
        } else {
            return false;
        }
    }

    // If any two combinations of the cards' values are the same, they have a pair
    private static boolean isPair(ArrayList<Card> hand) {
        if (hand.get(0).getValue() == hand.get(1).getValue() ||
        hand.get(1).getValue() == hand.get(2).getValue() ||
        hand.get(0).getValue() == hand.get(2).getValue()) {
            return true;
        }
        else{
            return false;
        }
    }

    // If all three of the cards' suits are the same, they have a flush
    private static boolean isFlush(ArrayList<Card> hand) {
        if (hand.get(0).getSuit().equals(hand.get(1).getSuit()) && hand.get(1).getSuit().equals(hand.get(2).getSuit())) {
            return true;
        } else {
            return false;
        }
    }


    private static boolean isStraight(ArrayList<Card> hand) {
        // Get the value of each of the cards
        int val1 = hand.get(0).getValue();
        int val2 = hand.get(1).getValue();
        int val3 = hand.get(2).getValue();
        // Find the smallest value
        int min = Math.min(val1, Math.min(val2, val3));
        // Get the highest value
        int max = Math.max(val1, Math.max(val2, val3));
        // Get the middle value
        int mid = val1 + val2 + val3 - min - max;
        // If the mid is one greater than the min,
        // and the max is two greater than the min, then it is a straight
        if (mid == min + 1 && max == min + 2) {
            return true;
        } else {
            return false;
        }
    }

    // Check if both the straight and the flush are correct
    private static boolean isStraightFlush(ArrayList<Card> hand) {
        if (isStraight(hand) && isFlush(hand)) {
            return true;
        }
        return false;
    }

}
