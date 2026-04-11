package strategies;

import combatants.Combatant;

import java.util.List;

public interface TurnOrderStrategy {
    List<Combatant> determineTurnOrder(List<Combatant> combatants);
}
