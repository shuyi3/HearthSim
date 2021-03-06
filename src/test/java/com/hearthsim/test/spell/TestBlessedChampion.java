package com.hearthsim.test.spell;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.basic.minion.BoulderfistOgre;
import com.hearthsim.card.basic.minion.DreadInfernal;
import com.hearthsim.card.basic.minion.RaidLeader;
import com.hearthsim.card.classic.spell.rare.BlessedChampion;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class TestBlessedChampion {

    private HearthTreeNode board;
    private PlayerModel currentPlayer;
    private PlayerModel waitingPlayer;
    private Deck deck;

    @Before
    public void setup() throws HSException {
        board = new HearthTreeNode(new BoardModel());
        currentPlayer = board.data_.getCurrentPlayer();
        waitingPlayer = board.data_.getWaitingPlayer();

        Minion minion0_0 = new DreadInfernal();
        Minion minion0_1 = new BoulderfistOgre();
        Minion minion0_2 = new RaidLeader();
        Minion minion1_0 = new BoulderfistOgre();
        Minion minion1_1 = new RaidLeader();

        Card fb = new BlessedChampion();
        currentPlayer.placeCardHand(fb);

        currentPlayer.setMana((byte) 8);
        waitingPlayer.setMana((byte) 8);

        currentPlayer.setMaxMana((byte) 8);
        waitingPlayer.setMaxMana((byte) 8);

        minion0_0.placeMinion(PlayerSide.CURRENT_PLAYER, currentPlayer.getHero(), board, true);
        minion0_1.placeMinion(PlayerSide.CURRENT_PLAYER, currentPlayer.getHero(), board, true);
        minion0_2.placeMinion(PlayerSide.CURRENT_PLAYER, currentPlayer.getHero(), board, true);
        minion1_0.placeMinion(PlayerSide.WAITING_PLAYER, waitingPlayer.getHero(), board, true);
        minion1_1.placeMinion(PlayerSide.WAITING_PLAYER, waitingPlayer.getHero(), board, true);
    }

    @Test
    public void test1() throws HSException {
        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 1, board);

        assertFalse(ret == null);

        assertEquals(currentPlayer.getHand().size(), 0);
        assertEquals(currentPlayer.getNumMinions(), 3);
        assertEquals(waitingPlayer.getNumMinions(), 2);
        assertEquals(currentPlayer.getMana(), 3);
        assertEquals(waitingPlayer.getMana(), 8);
        assertEquals(currentPlayer.getHero().getHealth(), 30);
        assertEquals(waitingPlayer.getHero().getHealth(), 30);
        assertEquals(currentPlayer.getCharacter(1).getTotalHealth(), 2);
        assertEquals(currentPlayer.getCharacter(2).getTotalHealth(), 7);
        assertEquals(currentPlayer.getCharacter(3).getTotalHealth(), 6);
        assertEquals(waitingPlayer.getCharacter(1).getTotalHealth(), 2);
        assertEquals(waitingPlayer.getCharacter(2).getTotalHealth(), 7);

        assertEquals(currentPlayer.getCharacter(1).getTotalAttack(), 4);
        assertEquals(currentPlayer.getCharacter(2).getTotalAttack(), 7);
        assertEquals(currentPlayer.getCharacter(3).getTotalAttack(), 7);
        assertEquals(waitingPlayer.getCharacter(1).getTotalAttack(), 2);
        assertEquals(waitingPlayer.getCharacter(2).getTotalAttack(), 7);
    }

    @Test
    public void test2() throws HSException {
        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 2, board);

        assertFalse(ret == null);

        assertEquals(currentPlayer.getHand().size(), 0);
        assertEquals(currentPlayer.getNumMinions(), 3);
        assertEquals(waitingPlayer.getNumMinions(), 2);
        assertEquals(currentPlayer.getMana(), 3);
        assertEquals(waitingPlayer.getMana(), 8);
        assertEquals(currentPlayer.getHero().getHealth(), 30);
        assertEquals(waitingPlayer.getHero().getHealth(), 30);
        assertEquals(currentPlayer.getCharacter(1).getTotalHealth(), 2);
        assertEquals(currentPlayer.getCharacter(2).getTotalHealth(), 7);
        assertEquals(currentPlayer.getCharacter(3).getTotalHealth(), 6);
        assertEquals(waitingPlayer.getCharacter(1).getTotalHealth(), 2);
        assertEquals(waitingPlayer.getCharacter(2).getTotalHealth(), 7);

        assertEquals(currentPlayer.getCharacter(1).getTotalAttack(), 2);
        assertEquals(currentPlayer.getCharacter(2).getTotalAttack(), 15);
        assertEquals(currentPlayer.getCharacter(3).getTotalAttack(), 7);
        assertEquals(waitingPlayer.getCharacter(1).getTotalAttack(), 2);
        assertEquals(waitingPlayer.getCharacter(2).getTotalAttack(), 7);
    }
}
