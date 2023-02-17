package me.colewagner.pokerng.game;

public class Card {

    enum Rank {
        TWO,
        THREE,
        FOUR,
        FIVE,
        SIX,
        SEVEN,
        EIGHT,
        NINE,
        TEN,
        JACK,
        QUEEN,
        KING,
        ACE;
    }

    enum Suit {
        HEARTS,
        DIAMONDS,
        SPADES,
        CLUBS;
    }

    private Rank rank;
    private Suit suit;

    public Card(Rank rank, Suit suit) {
        setRank(rank);
        setSuit(suit);
    }

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public Suit getSuit() {
        return suit;
    }

    public void setSuit(Suit suit) {
        this.suit = suit;
    }

}
