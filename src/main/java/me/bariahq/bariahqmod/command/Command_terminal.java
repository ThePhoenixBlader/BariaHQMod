package me.bariahq.bariahqmod.command;

import me.bariahq.bariahqmod.rank.Rank;
import me.bariahq.bariahqmod.util.FSync;
import me.bariahq.bariahqmod.util.FUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@CommandPermissions(level = Rank.SENIOR_ADMIN, source = SourceType.BOTH)
@CommandParameters(description = "Run a terminal command", usage = "/<command> <cmd>")
public class Command_terminal extends FreedomCommand
{

    @Override
    public boolean run(CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        if (!FUtil.BHQDEVS.contains(sender.getName()))
        {
            msg("Only the developers can use this command.", ChatColor.RED);
            return true;
        }

        if (args.length == 0)
        {
            return false;
        }

        final String[] command = args;
        final CommandSender commandSender = sender;

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                try
                {
                    Process process = Runtime.getRuntime().exec(command);
                    BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String line;
                    while ((line = bufferedreader.readLine()) != null)
                    {
                        FSync.playerMsg(commandSender, ChatColor.WHITE + line);
                    }
                }
                catch (IOException e)
                {
                    FSync.playerMsg(commandSender, ChatColor.RED + e.getMessage());
                }
            }
        }.runTaskAsynchronously(plugin);
        return true;
    }
}
