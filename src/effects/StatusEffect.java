package effects;

// default meathods because each class only overrides the one that actually applies to it (stun-causesstun, defend-bonusdefense, smokebomb-immune)
public interface StatusEffect {
    void tick();
    boolean isExpired();
    String getName();
    default int getBonusDefense() { return 0; }
    default boolean causeStun() { return false; }
    default boolean immune() { return false; }
}
