package me.bariahq.bariahqmod.command;

import me.bariahq.bariahqmod.player.FPlayer;
import me.bariahq.bariahqmod.player.PlayerData;
import me.bariahq.bariahqmod.rank.Rank;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = Rank.MOD, source = SourceType.BOTH)
@CommandParameters(description = "Talk privately with other staff members.", usage = "/<command> <message>", aliases = "o,ac")
public class Command_staffchat extends FreedomCommand
{

    @Override
    public boolean run(CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {FPlayer player = plugin.pl.getPlayer(playerSender);
        if (args.length > 0)
        {
            plugin.cm.adminChat(sender, StringUtils.join(args, " "));
            return true;
        } else {
            if (!player.inAdminChat()){
                player.setAdminChat(true);
                playerSender.sendMessage(ChatColor.AQUA + "Staff chat enabled.");
                return true;
            } else if (player.inAdminChat()){
                player.setAdminChat(false);
                playerSender.sendMessage(ChatColor.AQUA + "Staff chat disabled.");
                return true;
            }
        }


        return false;
    }
}
