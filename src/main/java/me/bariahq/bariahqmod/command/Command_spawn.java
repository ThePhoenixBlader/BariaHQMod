package me.bariahq.bariahqmod.command;

import me.bariahq.bariahqmod.config.ConfigEntry;
import me.bariahq.bariahqmod.rank.Rank;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = Rank.IMPOSTOR, source = SourceType.ONLY_IN_GAME)
@CommandParameters(description = "Go to spawn", usage = "/<command>")
public class Command_spawn extends FreedomCommand
{

    @Override
    public boolean run(CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        if (args.length > 0)
        {
            return false;
        }

        World world = Bukkit.getWorld(ConfigEntry.SPAWN_WORLD_NAME.getString());

        if (world == null)
        {
            msg("That world doesn't exist!", ChatColor.RED);
            return true;
        }

        Location location = world.getBlockAt(ConfigEntry.SPAWN_X_POSITION.getInteger(), ConfigEntry.SPAWN_Y_POSITION.getInteger(), ConfigEntry.SPAWN_Z_POSITION.getInteger()).getLocation();
        playerSender.teleport(location);
        msg("Teleporting...");
        return true;
    }
}
