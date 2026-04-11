package engine;

import combatants.Combatant;
import combatants.Enemy;
import combatants.Player;
import level.Level;

import java.util.ArrayList;
import java.util.List;

// SRP: holds live battle state only — no UI, no behavior
public class BattleContext {
    public static final int MAX_CONTINUES = 1;

    private final Player player;
    private final Level level;
    private final List<Enemy> initialEnemies;
    private final List<Enemy> backupEnemies;
    private final List<Enemy> activeEnemies;
    private boolean backupSpawned;
    private int currentRound;
    private int continuesUsed;

    public BattleContext(Player player, Level level) {
        this.player = player;
        this.level = level;
        this.initialEnemies = new ArrayList<>(level.getInitialWave());
        this.backupEnemies = new ArrayList<>(level.getBackupWave());
        this.activeEnemies = new ArrayList<>(level.getInitialWave());
        this.backupSpawned = false;
        this.currentRound = 0;
        this.continuesUsed = 0;
    }

    public Player getPlayer() { return player; }
    public Level getLevel() { return level; }

    public List<Enemy> getActiveEnemies() { return activeEnemies; }

    // Returns only alive enemies
    public List<Enemy> getAliveEnemies() {
        List<Enemy> alive = new ArrayList<>();
        for (Enemy e : activeEnemies) {
            if (e.isAlive()) alive.add(e);
        }
        return alive;
    }

    // Returns all combatants (enemies + player) for turn-order building
    public List<Combatant> getAllCombatants() {
        List<Combatant> all = new ArrayList<>(activeEnemies);
        all.add(player);
        return all;
    }

    public List<Enemy> getInitialEnemies() { return initialEnemies; }
    public List<Enemy> getBackupEnemies() { return backupEnemies; }
    public boolean isBackupSpawned() { return backupSpawned; }
    public int getCurrentRound() { return currentRound; }
    public void setCurrentRound(int round) { this.currentRound = round; }

    public boolean allInitialEnemiesDefeated() {
        for (Enemy e : initialEnemies) {
            if (e.isAlive()) return false;
        }
        return true;
    }

    public void triggerBackupSpawn() {
        if (!backupSpawned && !backupEnemies.isEmpty()) {
            activeEnemies.addAll(backupEnemies);
            backupSpawned = true;
        }
    }

    public boolean hasContinues() {
        return continuesUsed < MAX_CONTINUES;
    }

    public int getContinuesRemaining() {
        return MAX_CONTINUES - continuesUsed;
    }

    public void useContinue() {
        continuesUsed++;
    }

    public boolean hasPendingBackup() {
        return !backupSpawned && !backupEnemies.isEmpty();
    }

    public boolean isGameOver() {
        if (!player.isAlive()) return true;
        if (getAliveEnemies().isEmpty() && !hasPendingBackup()) return true;
        return false;
    }
}
