package strategies;

import actions.Action;
import combatants.Enemy;
import engine.BattleContext;

public interface EnemyActionStrategy {
    Action selectAction(Enemy enemy, BattleContext ctx);
}
