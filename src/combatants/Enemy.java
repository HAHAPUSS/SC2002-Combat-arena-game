package combatants;

import actions.Action;
import engine.BattleContext;
import strategies.EnemyActionStrategy;
import ui.BattleUI;

public abstract class Enemy extends Combatant {
    protected EnemyActionStrategy actionStrategy;

    public Enemy(String name, int maxHp, int attack, int defense, int speed) {
        super(name, maxHp, attack, defense, speed);
    }

    public void setActionStrategy(EnemyActionStrategy strategy) {
        this.actionStrategy = strategy;
    }

    public EnemyActionStrategy getActionStrategy() {
        return actionStrategy;
    }

    public Action chooseAction(BattleContext ctx) {
        if (actionStrategy != null) {
            return actionStrategy.selectAction(this, ctx);
        }
        return null;
    }

    @Override
    public void takeTurn(BattleContext ctx, BattleUI ui) {
        Action action = chooseAction(ctx);
        if (action != null) {
            java.util.List<Combatant> targets = new java.util.ArrayList<>();
            targets.add(ctx.getPlayer());
            action.execute(this, targets, ctx);
        }
    }

    @Override
    public String getDescription() {
        return String.format("%s [HP:%d ATK:%d DEF:%d SPD:%d]",
                name, maxHp, attack, defense, speed);
    }
}
