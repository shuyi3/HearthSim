package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;

public class Lightspawn extends Minion {

    private static final boolean HERO_TARGETABLE = true;
    private static final byte SPELL_DAMAGE = 0;

    public Lightspawn() {
        super();
        spellDamage_ = SPELL_DAMAGE;
        heroTargetable_ = HERO_TARGETABLE;
    }

    @Override
    public byte getTotalAttack() {
        return this.getTotalHealth();
    }
}