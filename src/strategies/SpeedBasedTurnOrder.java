package strategies;

import combatants.Combatant;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class SpeedBasedTurnOrder implements TurnOrderStrategy {

    @Override
    public List<Combatant> determineTurnOrder(List<Combatant> combatants) {
        List<Combatant> ordered = new ArrayList<>(combatants);
        // Stable sort: higher speed goes first. For ties, enemies before player
        // The caller should pass enemies before player for tie resolution to work correctly.
        // Use Collections.sort which is stable. then sort descending
        Collections.sort(ordered, (a, b) -> Integer.compare(b.getSpeed(), a.getSpeed()));
        return ordered;
    }
}
