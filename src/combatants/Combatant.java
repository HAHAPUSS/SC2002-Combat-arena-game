package combatants;

import effects.StatusEffect;
import engine.BattleContext;
import ui.BattleUI;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class Combatant {
    protected String name;
    protected int maxHp;
    protected int currentHp;
    protected int attack;
    protected int defense;
    protected int speed;
    protected List<StatusEffect> activeEffects;

    public Combatant(String name, int maxHp, int attack, int defense, int speed) {
        this.name = name;
        this.maxHp = maxHp;
        this.currentHp = maxHp;
        this.attack = attack;
        this.defense = defense;
        this.speed = speed;
        this.activeEffects = new ArrayList<>();
    }

    // getters
    // set for attack only as speed doesnt change throughout and defense is added temporarily by getBonusDefense() only, the basic defense does not change.
    //but wizard arcane blast permanently boost attack
    public String getName() { return name; }
    public int getMaxHp() { return maxHp; }
    public int getCurrentHp() { return currentHp; }
    public int getAttack() { return attack; }
    public void setAttack(int attack) { this.attack = attack; }
    public int getDefense() { return defense; }
    public int getSpeed() { return speed; }
    public List<StatusEffect> getActiveEffects() { return activeEffects; }

    // for each e (type: StatusEffect) in the "activeEffects" list
    public int getEffectiveDefense() {
        int bonus = 0;
        for (StatusEffect e : activeEffects) {
            bonus += e.getBonusDefense();
        }
        return defense + bonus;
    }

    public boolean isAlive() {
        return currentHp > 0;
    }

    public boolean isStunned() {
        for (StatusEffect e : activeEffects) {
            if (e.causeStun() && !e.isExpired()) {
                return true;
            }
        }
        return false;
    }

    public boolean hasSmokeBombProtection() {
        for (StatusEffect e : activeEffects) {
            if (e.immune() && !e.isExpired()) {
                return true;
            }
        }
        return false;
    }

    public void takeDamage(int damage) {
        currentHp = currentHp - damage;
        if (currentHp < 0) {
            currentHp = 0;
        }
    }

    public void revive() {
        this.currentHp = this.maxHp;
        this.activeEffects.clear();
    }

    public void heal(int amount) {
        currentHp = currentHp + amount;
        if (currentHp > maxHp) {
            currentHp = maxHp;
        }
    }

    public void addEffect(StatusEffect e) {
        //if we add a stuneffect, then need to remove any existing stun first
        //hence for x in activeEffects, remove x if x.causeStun() returns true
        if (e.causeStun()) {
            activeEffects.removeIf(x -> x.causeStun());
        }
        activeEffects.add(e);
    }

    //called at end of each round
    //tick round-based effects (Defend, SmokeBomb) and remove expired ones
    public void tickRoundEffects() {
        Iterator<StatusEffect> it = activeEffects.iterator();
        while (it.hasNext()) {
            StatusEffect e = it.next();
            //dont tick stun as stun is turn-based
            if (!e.causeStun()) {
                e.tick();
                if (e.isExpired()) {
                    it.remove();
                }
            }
        }
    }

    //Called when this combatant's turn is processed while stunned.
    //Decrease the stun counter and removes if expired.
    public void decrementStun() {
        Iterator<StatusEffect> it = activeEffects.iterator();
        while (it.hasNext()) {
            StatusEffect e = it.next();
            if (e.causeStun()) {
                e.tick();
                if (e.isExpired()) {
                    it.remove();
                }
            }
        }
    }

    public abstract String getDescription();

    //force subclasses to provide their own implementation on how to take turn
    //BattleEngine calls this on any Combatant without caring if its a player or enemy instanceof checks
    //LSP: combatants are interchangeable and we dont need to check which type is each one
    //OCP: if we add another combatant type, we dont need to add another else if and check instanceof in BattleEngine
    public abstract void takeTurn(BattleContext ctx, BattleUI ui);
}
