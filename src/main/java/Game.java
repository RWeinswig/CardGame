import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JOptionPane;

public class Game {

    private static final int ROUNDS = 5;
    private static final int CARDS_PER_HAND = 3;
    private static final int STARTING_ENERGY = 3;
    private static final int MAX_CARD_VALUE = 13;
    private static final int SCORE_FOR_PAIR = 3;
    private static final String[] SUITS = {"Spades", "Hearts", "Diamonds", "Clubs"};
    private static final String[] RANKS = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
    private static final int[] VALUES = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};

    private boolean revealComputerCards = false;
    private boolean gameOver = false;
    private String roundWinner = "";

    private int playerEnergy;
    private int computerEnergy;

    private Scanner sc;
    private Deck deck;
    private Player player;
    private Player computer;
    private GameViewer window;

    public Game() {
        window = new GameViewer(this);
        sc = new Scanner(System.in);

        // FIX: Use a visual popup to ask for the name so the user knows the game is waiting!
        String name = JOptionPane.showInputDialog(window, "Enter your name to begin:", "Player Name", JOptionPane.QUESTION_MESSAGE);
        if (name == null || name.trim().isEmpty()) {
            name = "Player"; // default name if they hit cancel
        }

        window.hideInstructions();

        deck = new Deck(RANKS, SUITS, VALUES, window);
        player = new Player(name);
        computer = new Player("Computer");
    }

    public boolean isRevealComputerCards() { return revealComputerCards; }
    public String getRoundWinner() { return roundWinner; }
    public void setRevealComputerCards(boolean reveal) { this.revealComputerCards = reveal; }
    public Player getComputer() { return computer; }
    public Player getPlayer() { return player; }
    public boolean isGameOver() { return gameOver; }
    public int getPlayerEnergy() { return playerEnergy; }
    public int getComputerEnergy() { return computerEnergy; }

    private void hideHands() {
        player.clearHand();
        computer.clearHand();
        roundWinner = "";
        window.repaint();
    }

    public void playGame() {
        for (int round = 1; round <= ROUNDS; round++) {
            startRound(round);
            int playerBonus = playerTurn();

            revealComputerCards = true;
            window.repaint();
            waitForEnter();

            int computerBonus = computerTurn();

            scoreRound(playerBonus, computerBonus);
            window.repaint();

            System.out.println("\nWinner is highlighted for this round! Press Enter in the console to see the scoreboard: ");
            waitForEnter();
            hideHands();

            window.drawScoreboard(window.getGraphics(), player.getPoints(), computer.getPoints(), round);
            System.out.println("\nHere is the scoreboard! Press Enter in the console to continue to the next round: ");
            waitForEnter();
        }
        endGame();
    }

    private void startRound(int round) {
        playerEnergy = STARTING_ENERGY;
        computerEnergy = STARTING_ENERGY;
        dealHands();
        window.repaint();
    }

    private void dealHands() {
        player.clearHand();
        computer.clearHand();
        deck.shuffle();
        setRevealComputerCards(false);

        for (int i = 0; i < CARDS_PER_HAND; i++) {
            player.addCard(deck.deal());
            computer.addCard(deck.deal());
        }
    }

    private void waitForEnter() {
        System.out.println("Press Enter in the console to continue...");
        sc.nextLine();
    }

    private int playerTurn() {
        int bonus = 0;
        while (playerEnergy > 0) {
            System.out.println("Energy: " + playerEnergy);
            System.out.println("Make your selection in the console. 1) Power Up 2) Change Suit 3) Pass");
            String choice = sc.nextLine();

            if (choice.equals("1")) {
                playerPowerUp();
            } else if (choice.equals("2")) {
                playerSuitChange();
            } else if (choice.equals("3")) {
                bonus++;
                playerEnergy--;
            } else {
                System.out.println("Invalid Choice. Please pick 1, 2, or 3.");
            }
            window.repaint();
        }
        return bonus;
    }

    private void playerPowerUp() {
        System.out.println("Choose card index (1-3)");
        int choice = sc.nextInt() - 1;
        sc.nextLine();

        if (isValidIndex(choice)) {
            Card card = player.getCard(choice);
            int maxAdd = MAX_CARD_VALUE - card.getValue();
            if (maxAdd > 0) {
                int add = (int) (Math.random() * maxAdd) + 1;
                card.powerUp(add);
            } else {
                System.out.println("You can't power up a king!!! You just wasted a turn. ");
            }
            playerEnergy--;
        } else {
            System.out.println("Please choose a valid index");
        }
    }

    private void playerSuitChange() {
        System.out.println("Choose card index (1-3)");
        int choice = sc.nextInt() - 1;
        sc.nextLine();

        if (isValidIndex(choice)) {
            Card card = player.getCard(choice);
            card.setSuit(SUITS[(int) (Math.random() * SUITS.length)]);
            playerEnergy--;
        }
    }

    private boolean isValidIndex(int index) {
        return index >= 0 && index < CARDS_PER_HAND;
    }

    private int computerTurn() {
        int bonus = 0;
        while (computerEnergy > 0) {
            if (shouldPass()) {
                System.out.println("Computer passes. ");
                bonus++;
                computerEnergy--;
            } else if (tryImproveThreeOfAKind()) {
                computerEnergy--;
            } else if (tryCompleteFlush()) {
                computerEnergy--;
            } else {
                if (randomComputerMove()) {
                    bonus ++;
                }
                computerEnergy--;
            }
            window.repaint();
            waitForEnter();
        }
        return bonus;
    }

    private boolean shouldPass() {
        return HandRanking.evaluate(computer.getHand()) > SCORE_FOR_PAIR;
    }

    private boolean tryImproveThreeOfAKind() {
        if (HandRanking.evaluate(computer.getHand()) != SCORE_FOR_PAIR) {
            return false;
        }

        Card c1 = computer.getCard(0);
        Card c2 = computer.getCard(1);
        Card c3 = computer.getCard(2);

        if (c1.getValue() != c2.getValue() && c1.getValue() != c3.getValue()) {
            powerUp(c1);
            return true;
        } else if (c2.getValue() != c1.getValue() && c2.getValue() != c3.getValue()) {
            powerUp(c2);
            return true;
        } else if (c3.getValue() != c1.getValue() && c3.getValue() != c2.getValue()) {
            powerUp(c3);
            return true;
        }
        return false;
    }

    private boolean tryCompleteFlush() {
        if (!oneAwayFromFlush(computer.getHand())) {
            return false;
        }

        Card c1 = computer.getCard(0);
        Card c2 = computer.getCard(1);
        Card c3 = computer.getCard(2);

        if (!c1.getSuit().equals(c2.getSuit()) && !c1.getSuit().equals(c3.getSuit())){
            c1.setSuit(randomSuit());
            return true;
        } else if (!c2.getSuit().equals(c1.getSuit()) && !c2.getSuit().equals(c3.getSuit())){
            c2.setSuit(randomSuit());
            return true;
        } else if (!c3.getSuit().equals(c1.getSuit()) && !c3.getSuit().equals(c2.getSuit())){
            c3.setSuit(randomSuit());
            return true;
        }
        return false;
    }

    private boolean randomComputerMove() {
        int choice = (int) (Math.random() * 3) + 1;
        int index = (int) (Math.random() * CARDS_PER_HAND);

        Card card = computer.getCard(index);

        if (choice == 1) {
            powerUp(card);
        } else if (choice == 2) {
            card.setSuit(randomSuit());
        } else {
            System.out.println("Computer Passed");
            return true;
        }
        return false;
    }

    private void powerUp(Card card) {
        int maxAdd = MAX_CARD_VALUE - card.getValue();
        if (maxAdd > 0) {
            card.powerUp((int)(Math.random() * maxAdd) + 1);
        }
    }

    private String randomSuit() {
        return SUITS[(int)(Math.random() * SUITS.length)];
    }

    private boolean oneAwayFromFlush(ArrayList<Card> hand) {
        String suit1 = hand.get(0).getSuit();
        String suit2 = hand.get(1).getSuit();
        String suit3 = hand.get(2).getSuit();

        return (suit1.equals(suit2) && !suit3.equals(suit1)) ||
                (suit1.equals(suit3) && !suit2.equals(suit1)) ||
                (suit2.equals(suit3) && !suit1.equals(suit2));
    }

    private void scoreRound(int bonus, int computerBonus) {
        int playerScore = HandRanking.evaluate(player.getHand());
        int computerScore = HandRanking.evaluate(computer.getHand());

        playerScore += bonus;
        computerScore += computerBonus;

        System.out.println("\n   ROUND RESULTS   ");
        System.out.println("Your hand: " + player.getHand() + "  Score: " + playerScore);
        System.out.println("Computer hand: " + computer.getHand() + "  Score: " + computerScore);

        roundWinner = "";
        if (playerScore == computerScore){
            System.out.println("The round is a tie. Let's see who wins with the High Card!");
            int p1 = player.getCard(0).getValue();
            int p2 = player.getCard(1).getValue();
            int p3 = player.getCard(2).getValue();

            int c1 = computer.getCard(0).getValue();
            int c2 = computer.getCard(1).getValue();
            int c3 = computer.getCard(2).getValue();

            if (Math.max(p1, Math.max(p2, p3)) > Math.max(c1, Math.max(c2, c3))) {
                System.out.println(player.getName() + " has the high card! You get a point!");
                roundWinner = "PLAYER";
                playerScore ++;
            } else if (Math.max(p1, Math.max(p2, p3)) < Math.max(c1, Math.max(c2, c3))) {
                System.out.println("The computer has the high card! It gets a point!");
                roundWinner = "COMPUTER";
                computerScore ++;
            } else {
                System.out.println("It's a tie! Nobody gets a point!");
            }
        } else if (playerScore < computerScore) {
            roundWinner = "COMPUTER";
            System.out.println("The computer wins this round. They get a point!");
            computerScore ++;
        } else {
            roundWinner = "PLAYER";
            System.out.println(player.getName() + " wins this round. You get a point!");
            playerScore ++;
        }

        player.addPoints(playerScore);
        computer.addPoints(computerScore);
    }

    private void endGame() {
        gameOver = true;
        window.repaint();
        window.showRestartButton();
    }

    public void restartGame() {
        gameOver = false;
        roundWinner = "";
        revealComputerCards = false;

        player.clearHand();
        computer.clearHand();
        player.resetPoints();
        computer.resetPoints();

        deck = new Deck(RANKS, SUITS, VALUES, window);

        window.repaint();

        new Thread(() -> {
            playGame();
        }).start();
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.playGame();
    }
}