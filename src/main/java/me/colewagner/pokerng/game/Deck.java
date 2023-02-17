package me.colewagner.pokerng.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {

    private List<Card> cards;

    public Deck() {
        cards = new ArrayList<>();
        for(Card.Rank rank : Card.Rank.values()) {
            for(Card.Suit suit : Card.Suit.values()) {
                cards.add(new Card(rank, suit));
            }
        }
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card deal() {
        if(cards.isEmpty()) {
            return null;
        }
        return cards.remove(0);
    }

    public void returnCardToDeck(Card card) {
        if(card != null) {
            cards.add(card);
        }
    }
}
