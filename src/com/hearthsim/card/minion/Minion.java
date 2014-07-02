package com.hearthsim.card.minion;

import com.json.*;
import com.hearthsim.card.Card;
import com.hearthsim.exception.HSException;
import com.hearthsim.util.BoardState;
import com.hearthsim.util.DeepCopyable;

public class Minion extends Card {
	
	boolean taunt_;
	boolean divineShield_;
	boolean windFury_;
	boolean charge_;
	
	boolean hasAttacked_;
	
	protected byte health_;
	protected byte maxHealth_;
	protected byte baseHealth_;
	
	protected byte attack_;
	protected byte baseAttack_;

	public Minion(String name, byte mana, byte attack, byte health, byte baseAttack, byte baseHealth, byte maxHealth) {
		this(name, mana, attack, health, baseAttack, baseHealth, maxHealth, false, false, false, false, false, true, false);
	}
	
	public Minion(	String name,
					byte mana,
					byte attack,
					byte health,
					byte baseAttack,
					byte baseHealth,
					byte maxHealth,
					boolean taunt,
					boolean divineShield,
					boolean windFury,
					boolean charge,
					boolean hasAttacked,
					boolean isInHand,
					boolean hasBeenUsed) {
		super(name, mana, hasBeenUsed, isInHand);
		attack_ = attack;
		health_ = health;
		taunt_ = taunt;
		divineShield_ = divineShield;
		windFury_ = windFury;
		charge_ = charge;
		hasAttacked_ = hasAttacked;
		baseAttack_ = baseAttack;
		baseHealth_ = baseHealth;
		maxHealth_ = maxHealth;
	}
	
	public boolean getTaunt() {
		return taunt_;
	}
	
	public void setTaunt(boolean taunt) {
		taunt_ = taunt;
	}
	
	public byte getHealth() {
		return health_;
	}
	
	public void setHealth(byte health) {
		health_ = health;
	}
	
	public byte getAttack() {
		return attack_;
	}
	
	public void setAttack(byte attack) {
		attack_ = attack;
	}
	
	public boolean getDivineShield() {
		return divineShield_;
	}
	
	public void setDivineShield(boolean divineShield) {
		divineShield_ = divineShield;
	}
	
	public boolean hasAttacked() {
		return hasAttacked_;
	}
	
	public void hasAttacked(boolean hasAttacked) {
		hasAttacked_ = hasAttacked;
	}

	public boolean getCharge() {
		return charge_;
	}
	
	public void setCharge(boolean value) {
		charge_ = value;
	}
	
	public void attack(Minion minion) {
		minion.attacked(attack_);
		this.attacked(minion.getAttack());
 	}
	
	public void attacked(byte damage) {
		if (!divineShield_) {
			health_ = (byte)(health_ - damage);
		} else {
			divineShield_ = false;
		}
	}
	
	
	/**
	 * 
	 * Use the card on the given target
	 * 
	 * The Minion class can have two states: one as a card in the hand, and the other as a minion on the field.
	 * 
	 * @param thisCardIndex The index (position) of the card in the hand
	 * @param playerIndex The index of the target player.  0 if targeting yourself or your own minions, 1 if targeting the enemy
	 * @param minionIndex The index of the target minion.
	 * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
	 * 
	 * @return The boardState is manipulated and returned
	 */
	@Override
	public BoardState useOn(int thisCardIndex, int playerIndex, int minionIndex, BoardState boardState) {
		
		if (this.hasBeenUsed_) {
			//Card is already used, nothing to do
			return null;
		}
		
		if (playerIndex == 0) {
			if (isInHand_ && boardState.getNumMinions_p0() < 7) {
				//not on the board yet... which means the card is in the hand.  So, place it on the specified location.
				if (minionIndex == 0) {
					//can't place it to the left of the hero!
					return null;
				}
				if (!charge_) {
					this.hasAttacked_ = true;
					this.hasBeenUsed_ = true;
				}
				boardState.placeMinion_p0(this, minionIndex - 1);
				boardState.setMana_p0(boardState.getMana_p0() - this.mana_);
				boardState.removeCard_hand(thisCardIndex);
				return boardState;
								
			} else {
				//can't attack the player's own minions or the player's own hero
				return null;				
			}
		} else {
		
			if (!isInHand_) {
				//already on the board, so use it to attack stuff
				if (minionIndex == 0) {
					//To Do:  check for taunts!!!
					//attack the enemy hero
					this.attack(boardState.getHero_p1());
					return boardState;
				} else {
					Minion target = boardState.getMinion_p1(minionIndex - 1);
					this.attack(target);
					if (target.getHealth() <= 0) {
						boardState.removeMinion_p1(target);
					}
					if (this.health_ <= 0) {
						boardState.removeMinion_p0(thisCardIndex);
					}
					return boardState;
				}
			} else {
				return null;
			}
		}
	}

	@Override
	public JSONObject toJSON() {
		JSONObject json = super.toJSON();
		json.put("type", "Minion");
		json.put("attack", attack_);
		json.put("baseAttack", baseAttack_);
		json.put("health", health_);
		json.put("baseHealth", baseHealth_);
		json.put("maxHealth", maxHealth_);
		json.put("taunt", taunt_);
		json.put("divineShield", divineShield_);
		json.put("windFury", windFury_);
		json.put("charge", charge_);
		json.put("hasAttacked", hasAttacked_);
		return json;
	}
	
	@Override
	public DeepCopyable deepCopy() {
		return new Minion(
				this.name_,
				this.mana_,
				this.attack_,
				this.health_,
				this.baseAttack_,
				this.baseHealth_,
				this.maxHealth_,
				this.taunt_,
				this.divineShield_,
				this.windFury_,
				this.charge_,
				this.hasAttacked_,
				this.isInHand_,
				this.hasBeenUsed());
	}
	
	@Override
	public boolean equals(Object other) {
		if (!super.equals(other)) {
			return false;
		}
		if (maxHealth_ != ((Minion)other).maxHealth_)
			return false;
		if (baseHealth_ != ((Minion)other).baseHealth_)
			return false;
		if (baseAttack_ != ((Minion)other).baseAttack_)
			return false;
		if (taunt_ != ((Minion)other).taunt_)
			return false;
		if (divineShield_ != ((Minion)other).divineShield_)
			return false;
		if (windFury_ != ((Minion)other).windFury_)
			return false;
		if (hasAttacked_ != ((Minion)other).hasAttacked_)
			return false;
		
		return true;
	}

}
