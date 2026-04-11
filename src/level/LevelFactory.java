package level;

import combatants.Enemy;
import combatants.Goblin;
import combatants.Wolf;
import strategies.BasicAttackStrategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//OCP / SRP: creates Level instances; add new levels here without touching BattleEngine
public class LevelFactory {

    public static Level create(int levelNo) {
        switch (levelNo) {
            case 1: return easy();
            case 2: return medium();
            case 3: return hard();
            default: return easy();
        }
    }

    public static List<Level> allLevels() {
        return Arrays.asList(easy(), medium(), hard());
    }

    // Level 1 - Easy: 3 Goblins, no backup
    private static Level easy() {
        List<Enemy> initial = new ArrayList<>();
        initial.add(makeGoblin("Goblin A"));
        initial.add(makeGoblin("Goblin B"));
        initial.add(makeGoblin("Goblin C"));
        return new Level(1, "Easy", initial, null);
    }

    // Level 2 - Medium: 1 Goblin + 1 Wolf initial, 2 Wolves backup
    private static Level medium() {
        List<Enemy> initial = new ArrayList<>();
        initial.add(makeGoblin("Goblin A"));
        initial.add(makeWolf("Wolf A"));

        List<Enemy> backup = new ArrayList<>();
        backup.add(makeWolf("Wolf B"));
        backup.add(makeWolf("Wolf C"));
        return new Level(2, "Medium", initial, backup);
    }

    // Level 3 - Hard: 2 Goblins initial, 1 Goblin + 2 Wolves backup
    private static Level hard() {
        List<Enemy> initial = new ArrayList<>();
        initial.add(makeGoblin("Goblin A"));
        initial.add(makeGoblin("Goblin B"));

        List<Enemy> backup = new ArrayList<>();
        backup.add(makeGoblin("Goblin C"));
        backup.add(makeWolf("Wolf A"));
        backup.add(makeWolf("Wolf B"));
        return new Level(3, "Hard", initial, backup);
    }

    private static Goblin makeGoblin(String name) {
        Goblin g = new Goblin(name);
        g.setActionStrategy(new BasicAttackStrategy());
        return g;
    }

    private static Wolf makeWolf(String name) {
        Wolf w = new Wolf(name);
        w.setActionStrategy(new BasicAttackStrategy());
        return w;
    }
}
