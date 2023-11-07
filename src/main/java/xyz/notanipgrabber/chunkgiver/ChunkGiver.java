package xyz.notanipgrabber.chunkgiver;

import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public final class ChunkGiver extends JavaPlugin implements Listener {
    private HashMap<String, BukkitRunnable> playerTimers = new HashMap<String, BukkitRunnable>();
    private DatabaseManager databaseManager;

    @Override
    public void onEnable() {
        databaseManager = new DatabaseManager();
        databaseManager.connect();

        getServer().getPluginManager().registerEvents(this, this);
        getCommand("chunk").setExecutor(new ChunkGiverCommand(databaseManager));
        getCommand("chunk").setTabCompleter(new ChunkGiverCommand(databaseManager));
    }

    @Override
    public void onDisable() {
        databaseManager.disconnect();
    }


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        databaseManager.savePlayerData(player.getUniqueId().toString(), true, true);
        giveChunks(player);

    }

    public void giveChunks(Player player) {
        BukkitRunnable timer = new BukkitRunnable() {
            @Override
            public void run() {
                getServer().dispatchCommand(getServer().getConsoleSender(), "lands admin player " + player.getName() + " give permission chunks " + (getLandChunksMax(player) + 1));
                if (databaseManager.getPlayerSoundSetting(player.getUniqueId().toString()) && databaseManager.getPlayerMessageSetting(player.getUniqueId().toString())) {

                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1.0f, 1.0f);
                    sendActionBar(player, ChatColor.translateAlternateColorCodes('&', "&e&lClaims &fYou Have Been Given 1 Chunk!"));
                } else if (databaseManager.getPlayerMessageSetting(player.getUniqueId().toString())) {
                    sendActionBar(player, ChatColor.translateAlternateColorCodes('&', "&e&lClaims &fYou Have Been Given 1 Chunk!"));
                } else if (databaseManager.getPlayerSoundSetting(player.getUniqueId().toString())) {
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1.0f, 1.0f);
                } else {

                }

                giveChunks(player);
            }
        };

        timer.runTaskLater(this, 20L * 60L * 30L); // Run every 30 minutes (20 t    icks per second)
    }

    public void sendActionBar(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
    }


    public int getLandChunksMax(Player player) {
        String placeholder = "%lands_land_chunks_max%";
        String value = PlaceholderAPI.setPlaceholders(player, placeholder);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            // Handle parsing error
            return 0; // Return a default value if parsing fails
        }
    }

}
