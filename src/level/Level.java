package level;

import combatants.Enemy;

import java.util.ArrayList;
import java.util.List;

// SRP: pure data container for a level's configuration
public class Level {
    private final int number;
    private final String difficultyName;
    private final List<Enemy> initialWave;
    private final List<Enemy> backupWave;

    public Level(int number, String difficultyName,
                 List<Enemy> initialWave, List<Enemy> backupWave) {
        this.number = number;
        this.difficultyName = difficultyName;
        this.initialWave = new ArrayList<>(initialWave);
        if (backupWave != null) {
            this.backupWave = new ArrayList<>(backupWave);
        } else {
            this.backupWave = new ArrayList<>();
        }
    }

    public int getNumber() { return number; }
    public String getDifficultyName() { return difficultyName; }
    public List<Enemy> getInitialWave() { return initialWave; }
    public List<Enemy> getBackupWave() { return backupWave; }
    public boolean hasBackup() { return !backupWave.isEmpty(); }

    public String getDescription() {
        return "Level " + number + " (" + difficultyName + ")";
    }
}
