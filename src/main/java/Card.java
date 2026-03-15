public class Card {
    private String suit;
    private String rank;
    private int value;

    public Card(String rank, String suit, int value){
        this.rank = rank;
        this.suit = suit;
        this.value = value;
    }

    public String getSuit(){ return suit; }
    public String getRank(){ return rank; }
    public int getValue(){ return value; }

    public void setSuit(String suit){ this.suit = suit; }

    public void powerUp(int amount){
        value += amount;
        if(value > 13) value = 13;
    }

    @Override
    public String toString(){
        return rank + " of " + suit;
    }
}