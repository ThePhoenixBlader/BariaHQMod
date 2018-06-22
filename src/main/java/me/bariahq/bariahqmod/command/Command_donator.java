/*
package me.bariahq.bariahqmod.command;

import me.bariahq.bariahqmod.banning.Ban;
import me.bariahq.bariahqmod.player.PlayerData;
import me.bariahq.bariahqmod.rank.Rank;
import me.bariahq.bariahqmod.util.FUtil;
import java.util.ArrayList;
import java.util.List;
import me.bariahq.bariahqmod.donator.DonatorMember;
import me.bariahq.bariahqmod.shop.ShopData;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = Rank.OP, source = SourceType.BOTH, blockHostConsole = true)
@CommandParameters(description = "Donator perks", usage = "/<command> [arguments]", aliases = "gtfo")
public class Command_donator extends FreedomCommand
{
    @Override
    public boolean run(CommandSender sender, Player playerSender, Command cmd, String label, String[] args, boolean senderIsConsole)
    {
        ShopData sd = plugin.sh.getData(playerSender);
        if (plugin.dl.isDonator(sender))
        {
            if (args.length == 0)
            {
                msg("Donator perks are found in command form here.");
                msg("Perks: customloginmessage");
                return true;
            }
            if (args.length == 1)
            {
                if (args[0].equals("customloginmessage"))
                {
                    String loginMessage = args[1];
                    sd.setLoginMessage(args[1]);
                    if (args[1] == null)
                    {
                        return false;
                    }
                    ChatColor.translateAlternateColorCodes('&', args[1]);
                    msg("Your login message is now " + args[1]);
                    return true;
                }
            }
        }
        else
        {
            msg("You are not a donator to this server.");
            return true;
        }
        return true;
    }
}
*/ 