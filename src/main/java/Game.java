import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JOptionPane;

public class Game {

    // Constants
    private static final int ROUNDS = 5;
    private static final int CARDS_PER_HAND = 3;
    private static final int STARTING_ENERGY = 3;
    private static final int MAX_CARD_VALUE = 13;
    private static final int SCORE_FOR_PAIR = 3;

    private static final String[] SUITS = {"Spades", "Hearts", "Diamonds", "Clubs"};
    private static final String[] RANKS = {"A","2","3","4","5","6","7","8","9","10","J","Q","K"};
    private static final int[] VALUES = {1,2,3,4,5,6,7,8,9,10,11,12,13};

    // Game state
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

    // Constructor
    public Game() {
        window = new GameViewer(this);
        sc = new Scanner(System.in);

        String name = JOptionPane.showInputDialog(window,
                "Enter your name to begin:",
                "Player Name",
                JOptionPane.QUESTION_MESSAGE);

        if(name == null || name.trim().isEmpty()) name = "Player";

        deck = new Deck(RANKS, SUITS, VALUES, window);
        player = new Player(name);
        computer = new Player("Computer");

        // Show instructions in GUI
        window.repaint();
        System.out.println("Press Enter in console to start the game...");
        sc.nextLine();
        window.hideInstructions();
    }

    // Getters
    public boolean isRevealComputerCards() { return revealComputerCards; }
    public String getRoundWinner() { return roundWinner; }
    public Player getComputer() { return computer; }
    public Player getPlayer() { return player; }
    public boolean isGameOver() { return gameOver; }
    public int getPlayerEnergy() { return playerEnergy; }
    public int getComputerEnergy() { return computerEnergy; }

    // Main game loop
    public void playGame() {
        System.out.println("Welcome to Triad Tactics!");

        for(int round=1; round<=ROUNDS; round++){
            startRound(round);

            int playerBonus = playerTurn();
            revealComputerCards = true;
            window.repaint();

            System.out.println("\nComputer will now play. Press Enter to reveal its cards.");
            sc.nextLine();
            int computerBonus = computerTurn();

            scoreRound(playerBonus, computerBonus);

            window.repaint();
            System.out.println("\nWinner highlighted. Press Enter to see scoreboard.");
            sc.nextLine();

            window.drawScoreboard(window.getGraphics(),
                    player.getPoints(), computer.getPoints(), round);

            System.out.println("\nScoreboard displayed. Press Enter for next round.");
            sc.nextLine();

            player.clearHand();
            computer.clearHand();
            roundWinner = "";
            window.repaint();
        }

        endGame();
    }

    private void startRound(int round){
        playerEnergy = STARTING_ENERGY;
        computerEnergy = STARTING_ENERGY;
        dealHands();
        window.repaint();
    }

    private void dealHands(){
        player.clearHand();
        computer.clearHand();
        deck.shuffle();
        revealComputerCards = false;

        for(int i=0; i<CARDS_PER_HAND; i++){
            player.addCard(deck.deal());
            computer.addCard(deck.deal());
        }
    }

    private int playerTurn(){
        int bonus = 0;
        while(playerEnergy > 0){
            System.out.println("Energy: "+playerEnergy);
            System.out.println("1) Power Up 2) Change Suit 3) Pass");

            String choice = sc.nextLine();

            if(choice.equals("1")) playerPowerUp();
            else if(choice.equals("2")) playerSuitChange();
            else if(choice.equals("3")) { bonus++; playerEnergy--; }
            else System.out.println("Invalid choice.");

            window.repaint();
        }
        return bonus;
    }

    private void playerPowerUp(){
        System.out.println("Choose card index (1-3):");
        int choice = sc.nextInt() - 1;
        sc.nextLine();
        if(isValidIndex(choice)){
            Card card = player.getCard(choice);
            int maxAdd = MAX_CARD_VALUE - card.getValue();
            if(maxAdd>0){
                card.powerUp((int)(Math.random()*maxAdd)+1);
            } else System.out.println("Cannot power up a King!");
            playerEnergy--;
        } else System.out.println("Invalid index.");
    }

    private void playerSuitChange(){
        System.out.println("Choose card index (1-3):");
        int choice = sc.nextInt() - 1;
        sc.nextLine();
        if(isValidIndex(choice)){
            Card card = player.getCard(choice);
            card.setSuit(SUITS[(int)(Math.random()*SUITS.length)]);
            playerEnergy--;
        } else System.out.println("Invalid index.");
    }

    private boolean isValidIndex(int index){
        return index>=0 && index<CARDS_PER_HAND;
    }

    // Computer AI
    private int computerTurn(){
        int bonus = 0;
        while(computerEnergy>0){
            if(shouldPass()){ bonus++; computerEnergy--; System.out.println("Computer passes."); }
            else if(tryImproveThreeOfAKind()){ computerEnergy--; }
            else if(tryCompleteFlush()){ computerEnergy--; }
            else { if(randomComputerMove()) bonus++; computerEnergy--; }

            window.repaint();
            sc.nextLine();
        }
        return bonus;
    }

    private boolean shouldPass(){
        return HandRanking.evaluate(computer.getHand()) > SCORE_FOR_PAIR;
    }

    private boolean tryImproveThreeOfAKind(){
        if(HandRanking.evaluate(computer.getHand()) != SCORE_FOR_PAIR) return false;

        Card c1 = computer.getCard(0);
        Card c2 = computer.getCard(1);
        Card c3 = computer.getCard(2);

        if(c1.getValue() != c2.getValue() && c1.getValue() != c3.getValue()){ powerUp(c1); return true; }
        if(c2.getValue() != c1.getValue() && c2.getValue() != c3.getValue()){ powerUp(c2); return true; }
        if(c3.getValue() != c1.getValue() && c3.getValue() != c2.getValue()){ powerUp(c3); return true; }

        return false;
    }

    private boolean tryCompleteFlush(){
        ArrayList<Card> hand = computer.getHand();
        if(!oneAwayFromFlush(hand)) return false;

        Card c1 = hand.get(0), c2 = hand.get(1), c3 = hand.get(2);

        if(!c1.getSuit().equals(c2.getSuit()) && !c1.getSuit().equals(c3.getSuit())){ c1.setSuit(randomSuit()); return true; }
        if(!c2.getSuit().equals(c1.getSuit()) && !c2.getSuit().equals(c3.getSuit())){ c2.setSuit(randomSuit()); return true; }
        if(!c3.getSuit().equals(c1.getSuit()) && !c3.getSuit().equals(c2.getSuit())){ c3.setSuit(randomSuit()); return true; }

        return false;
    }

    private boolean randomComputerMove(){
        int choice = (int)(Math.random()*3)+1;
        Card card = computer.getCard((int)(Math.random()*CARDS_PER_HAND));
        if(choice==1){ powerUp(card); }
        else if(choice==2){ card.setSuit(randomSuit()); }
        else { System.out.println("Computer passed."); return true; }
        return false;
    }

    private void powerUp(Card card){
        int maxAdd = MAX_CARD_VALUE - card.getValue();
        if(maxAdd>0) card.powerUp((int)(Math.random()*maxAdd)+1);
    }

    private String randomSuit(){ return SUITS[(int)(Math.random()*SUITS.length)]; }

    private boolean oneAwayFromFlush(ArrayList<Card> hand){
        String s1 = hand.get(0).getSuit(), s2 = hand.get(1).getSuit(), s3 = hand.get(2).getSuit();
        return (s1.equals(s2) && !s3.equals(s1)) || (s1.equals(s3) && !s2.equals(s1)) || (s2.equals(s3) && !s1.equals(s2));
    }

    private void scoreRound(int bonus, int computerBonus){
        int playerScore = HandRanking.evaluate(player.getHand()) + bonus;
        int computerScore = HandRanking.evaluate(computer.getHand()) + computerBonus;

        System.out.println("\nROUND RESULTS");
        System.out.println(player.getName()+" hand: "+player.getHand()+" Score: "+playerScore);
        System.out.println("Computer hand: "+computer.getHand()+" Score: "+computerScore);

        roundWinner="";
        if(playerScore==computerScore){
            int pMax = Math.max(player.getCard(0).getValue(), Math.max(player.getCard(1).getValue(), player.getCard(2).getValue()));
            int cMax = Math.max(computer.getCard(0).getValue(), Math.max(computer.getCard(1).getValue(), computer.getCard(2).getValue()));
            if(pMax>cMax){ roundWinner="PLAYER"; playerScore++; System.out.println(player.getName()+" has high card!"); }
            else if(cMax>pMax){ roundWinner="COMPUTER"; computerScore++; System.out.println("Computer has high card!"); }
            else System.out.println("It's a tie!");
        } else if(playerScore>computerScore){ roundWinner="PLAYER"; playerScore++; }
        else { roundWinner="COMPUTER"; computerScore++; }

        player.addPoints(playerScore);
        computer.addPoints(computerScore);
    }

    private void endGame(){
        gameOver=true;
        window.repaint();
        window.showRestartButton();
    }

    public static void main(String[] args){
        Game game = new Game();
        game.playGame();
    }
}