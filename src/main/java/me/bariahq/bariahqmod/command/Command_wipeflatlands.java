package me.bariahq.bariahqmod.command;

import me.bariahq.bariahqmod.rank.Rank;
import me.bariahq.bariahqmod.util.FUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;

@CommandPermissions(level = Rank.ADMIN, source = SourceType.ONLY_CONSOLE)
@CommandParameters(description = "Wipe the flatlands map. Requires manual restart after command is used.", usage = "/<command>")
public class Command_wipeflatlands extends FreedomCommand
{

    @Override
    public boolean run(final CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        plugin.sf.setSavedFlag("do_wipe_flatlands", true);

        FUtil.bcastMsg("Server is going offline for flatlands wipe.", ChatColor.GRAY);

        for (Player player : server.getOnlinePlayers())
        {
            player.kickPlayer("Server is going offline for flatlands wipe, come back in a few minutes.");
        }
        File folder = new File(Bukkit.getWorldContainer() + "/flatlands");
        folder.delete();
        for (Chunk c : plugin.getServer().getWorld("flatlands").getLoadedChunks()){
            c.unload();
        }
        Bukkit.unloadWorld("flatlands", true);
        Bukkit.getServer().spigot().restart();


//after this, BHQM/TFM will regenerate "flatlands" when the server starts and loads BHQM/TFM
        return true;
    }
}
