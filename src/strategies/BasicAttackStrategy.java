package strategies;

import actions.Action;
import actions.BasicAttack;
import combatants.Enemy;
import engine.BattleContext;


public class BasicAttackStrategy implements EnemyActionStrategy {

    @Override
    public Action selectAction(Enemy enemy, BattleContext ctx) {
        return new BasicAttack();
    }
}
