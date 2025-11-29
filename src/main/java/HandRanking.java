import java.util.ArrayList;
public class HandRanking {
    public static int evaluate(ArrayList<Card> hand) {

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

    private static boolean isThreeOfAKind(ArrayList<Card> hand) {
        if (hand.get(0).getValue() == hand.get(1).getValue() && hand.get(1).getValue() == hand.get(2).getValue()) {
            return true;
        } else {
            return false;
        }
    }

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

    private static boolean isFlush(ArrayList<Card> hand) {
        if (hand.get(0).getSuit() == hand.get(1).getSuit() && hand.get(1).getSuit() == hand.get(2).getSuit()) {
            return true;
        } else {
            return false;
        }
    }

    private static boolean isStraight(ArrayList<Card> hand) {

    }

}
