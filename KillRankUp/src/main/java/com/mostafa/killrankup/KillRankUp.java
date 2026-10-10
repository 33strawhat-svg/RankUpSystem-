package com.mostafa.killrankup;

import org.bukkit.plugin.java.JavaPlugin;

public class KillRankUp extends JavaPlugin {
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        getLogger().info("KillRankUp enabled!");
    }
}
