package xyz.notanipgrabber.chunkgiver;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ChunkGiverCommand implements CommandExecutor, TabCompleter {

    private DatabaseManager databaseManager;

    public ChunkGiverCommand(DatabaseManager d) {
        databaseManager = d;
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
        if (args.length >= 2) {
            if (args[1].equalsIgnoreCase("Enable")) {
                if (args[0].equalsIgnoreCase("Sound")) {
                    databaseManager.setPlayerSoundSetting(player.getUniqueId().toString(), true);

                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&dClaims&7] &aEnabled &fChunk Sound!"));
                } else if (args[0].equalsIgnoreCase("Both")) {
                    databaseManager.setPlayerSoundSetting(player.getUniqueId().toString(), true);
                    databaseManager.setPlayerMessageSetting(player.getUniqueId().toString(), true);

                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&dClaims&7] &aEnabled &fChunk Message and Sound!"));
                }  else if (args[0].equalsIgnoreCase("Message")) {
                    databaseManager.setPlayerMessageSetting(player.getUniqueId().toString(), true);

                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&dClaims&7] &aEnabled &fChunk Message!"));
                }
            } else if (args[1].equalsIgnoreCase("Disable")) {
                if (args[0].equalsIgnoreCase("Sound")) {
                    databaseManager.setPlayerSoundSetting(player.getUniqueId().toString(), false);

                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&dClaims&7] &cDisabled &fChunk Sound!"));
                } else if (args[0].equalsIgnoreCase("Both")) {
                    databaseManager.setPlayerSoundSetting(player.getUniqueId().toString(), false);
                    databaseManager.setPlayerMessageSetting(player.getUniqueId().toString(), false);

                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&dClaims&7] &cDisabled &fChunk Message and Sound!"));
                }  else if (args[0].equalsIgnoreCase("Message")) {
                    databaseManager.setPlayerMessageSetting(player.getUniqueId().toString(), false);

                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&dClaims&7] &cDisabled &fChunk Message!"));
                }
            }

        }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) { // First argument
            completions.add("Message");
            completions.add("Both");
            completions.add("Sound");
        } else if (args.length == 2) { // Second argument
            completions.add("Enable");
            completions.add("Disable");
        }

        return completions;
    }

}
