package combatants;

import actions.Action;
import engine.BattleContext;
import items.Item;
import ui.BattleUI;

import java.util.ArrayList;
import java.util.List;

public abstract class Player extends Combatant {
    protected List<Item> items;
    protected int specialSkillCooldown;
    protected boolean tookTurnThisRound;

    public Player(String name, int maxHp, int attack, int defense, int speed) {
        super(name, maxHp, attack, defense, speed);
        this.items = new ArrayList<>();
        this.specialSkillCooldown = 0;
        this.tookTurnThisRound = false;
    }

    public boolean canUseSpecialSkill() {
        return specialSkillCooldown == 0;
    }

    public int getSpecialSkillCooldown() {
        return specialSkillCooldown;
    }

    public void setSpecialSkillCooldown(int n) {
        this.specialSkillCooldown = n;
    }

    //Called at end of each round. If player took a turn and cooldown > 0, decrease it.
    public void decrementCooldownIfActed() {
        if (tookTurnThisRound && specialSkillCooldown > 0) {
            specialSkillCooldown--;
        }
        tookTurnThisRound = false;
    }

    public boolean isTookTurnThisRound() {
        return tookTurnThisRound;
    }

    public void setTookTurnThisRound(boolean b) {
        this.tookTurnThisRound = b;
    }

    public abstract void executeSpecialSkill(List<Combatant> targets, BattleContext ctx);

    public abstract boolean requiresTargetForSpecialSkill();

    public void addItem(Item item) {
        if (items.size() < 2) {
            items.add(item);
        }
    }

    public void useItem(int index, List<Combatant> targets, BattleContext ctx) {
        if (index < 0 || index >= items.size()) {
            System.out.println("Invalid item index.");
            return;
        }
        Item item = items.get(index);
        item.use(this, targets, ctx);
        items.remove(index);
    }

    public boolean hasItems() {
        return !items.isEmpty();
    }

    public List<Item> getItems() {
        return items;
    }

    public abstract String getSpecialSkillName();

    @Override
    public void takeTurn(BattleContext ctx, BattleUI ui) {
        Action action = ui.promptPlayerAction(this, ctx);
        action.execute(this, new java.util.ArrayList<>(), ctx);
        this.tookTurnThisRound = true;
    }
}
