package com.mostafa.killrankup;

public enum KillRank {
    NONE(0, 0.0, ""),
    STARTER(15, 0.5, "&7[Starter] "),
    FIGHTER(30, 1.0, "&a[Fighter] "),
    STRATEGIC(45, 1.5, "&b[Strategic] "),
    WARRIOR(65, 2.0, "&e[Warrior] "),
    KILLER(80, 2.5, "&c[Killer] "),
    DOMINATOR(110, 3.0, "&4[Dominator] "),
    MASTER(130, 3.5, "&d[Master] "),
    LEGEND(160, 4.0, "&6&l[Legend] ");

    private final int kills;
    private final double damage;
    private final String prefix;

    KillRank(int kills, double damage, String prefix) {
        this.kills = kills;
        this.damage = damage;
        this.prefix = prefix;
    }

    public int getKills() { return kills; }
    public double getDamage() { return damage; }
    public String getPrefix() { return prefix; }

    public static KillRank getByKills(int kills) {
        KillRank current = NONE;
        for (KillRank rank : values()) {
            if (kills >= rank.getKills()) {
                current = rank;
            }
        }
        return current;
    }
}
