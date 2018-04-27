package wrongsides.cherrypicker.domain;

import java.util.HashMap;
import java.util.Map;

public enum Unit {

    CUBIC_METRES("m3"),
    KILOMETRES("km");

    private String[] aliases;

    private static final Map<String, Unit> UNIT_MAP = new HashMap<>();

    static {
        for (Unit unit : Unit.values()) {
            for (String alias : unit.aliases) {
                UNIT_MAP.put(alias, unit);
            }
        }
    }

    Unit(String... aliases) {
        this.aliases = aliases;
    }

    public static Unit from(String alias) {
        return UNIT_MAP.get(alias);
    }
}
