package me.bariahq.bariahqmod.command;

import me.bariahq.bariahqmod.config.ConfigEntry;
import me.bariahq.bariahqmod.rank.Rank;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = Rank.MOD, source = SourceType.ONLY_IN_GAME)
@CommandParameters(description = "Set a new spawn", usage = "/<command>")
public class Command_setspawn extends FreedomCommand
{

    @Override
    public boolean run(CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        if (args.length > 0)
        {
            return false;
        }

        ConfigEntry.SPAWN_WORLD_NAME.setString(playerSender.getLocation().getWorld().getName());
        ConfigEntry.SPAWN_X_POSITION.setInteger(playerSender.getLocation().getBlockX());
        ConfigEntry.SPAWN_Y_POSITION.setInteger(playerSender.getLocation().getBlockY());
        ConfigEntry.SPAWN_Z_POSITION.setInteger(playerSender.getLocation().getBlockZ());
        msg("New spawn set!");
        return true;
    }
}
