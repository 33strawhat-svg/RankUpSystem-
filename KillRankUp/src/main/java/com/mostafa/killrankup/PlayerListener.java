package com.mostafa.killrankup;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.persistence.PersistentDataType;

public class PlayerListener implements Listener {

    private final NamespacedKey killsKey;

    public PlayerListener(KillRankUp plugin) {
        this.killsKey = new NamespacedKey(plugin, "kills_count");
    }

    private int getKills(Player player) {
        return player.getPersistentDataContainer().getOrDefault(killsKey, PersistentDataType.INTEGER, 0);
    }

    private void setKills(Player player, int kills) {
        player.getPersistentDataContainer().set(killsKey, PersistentDataType.INTEGER, kills);
    }

    private void refreshTag(Player player) {
        KillRank rank = KillRank.getByKills(getKills(player));
        if (rank == KillRank.NONE) return;

        Component prefix = LegacyComponentSerializer.legacyAmpersand().deserialize(rank.getPrefix());
        Component newName = prefix.append(Component.text(player.getName()));
        
        player.displayName(newName);
        player.playerListName(newName);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        refreshTag(event.getPlayer());
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player killer = victim.getKiller();

        // ??? ??????? ????? ?????? (????????)
        if (killer == null || killer.equals(victim)) return;

        int newKills = getKills(killer) + 1;
        setKills(killer, newKills);

        KillRank oldRank = KillRank.getByKills(newKills - 1);
        KillRank newRank = KillRank.getByKills(newKills);

        if (newRank != oldRank) {
            String msg = "&a&lLevel Up! &7You are now " + newRank.getPrefix().trim() + " &7(&c+" + newRank.getDamage() + " Damage&7)";
            killer.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(msg));
            killer.playSound(killer.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
            
            refreshTag(killer);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player killer)) return;

        String weapon = killer.getInventory().getItemInMainHand().getType().name();
        if (!weapon.endsWith("_SWORD")) return;

        double bonus = KillRank.getByKills(getKills(killer)).getDamage();
        if (bonus > 0) {
            event.setDamage(event.getDamage() + bonus);
        }
    }
}
