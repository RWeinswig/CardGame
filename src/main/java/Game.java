import java.util.Scanner;
public class Game {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        playGame();
    }

    public static void printInstructions() {
        System.out.println("Triad Tactics is a 3-card strategy came where the player and computer draw hands and make choices each round. The player can spend energy to power up a card (by increasing its value by 2), swapping a card's suit, or pass to save energy for bonus points. After the player acts, the computer decides whether to improve its hand by swapping, powering up, or changing suits. Once both sides finish their actions, the hands are evaluated for combinations like three-of-a-kind, straights, flushes, or pairs, plus the extra bonuses. The higher-scoring hand wins the round and earns an additional point. After 5 rounds, the player with most total points win the game.");
    }

    public static void playGame() {
        printInstructions();
        System.out.println("Whenever you are ready to continue, hit enter: ");
        String name = sc.nextLine();
    }



}
