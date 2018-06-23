package me.bariahq.bariahqmod.command;

import me.bariahq.bariahqmod.rank.Displayable;
import me.bariahq.bariahqmod.rank.Rank;
import me.bariahq.bariahqmod.staff.StaffMember;
import me.bariahq.bariahqmod.util.FLog;
import me.bariahq.bariahqmod.util.FUtil;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

@CommandPermissions(level = Rank.MOD, source = SourceType.ONLY_IN_GAME)
@CommandParameters(description = "Vanish/unvanish yourself.", usage = "/<command>", aliases = "v")
public class Command_vanish extends FreedomCommand
{
    public static ArrayList<Player> VANISHED = new ArrayList<Player>();

    public boolean run(final CommandSender sender, final Player playerSender, final Command cmd, final String commandLabel, final String[] args, final boolean senderIsConsole)
    {
        Displayable display = plugin.rm.getDisplay(playerSender);
        String loginMsg = display.getColoredLoginMessage();
        String displayName = display.getColor() + playerSender.getName();
        String tag = display.getColoredTag();
        StaffMember staff = plugin.al.getStaffMember(playerSender);
        if (VANISHED.contains(playerSender))
        {
            msg(ChatColor.GOLD + "You have been unvanished.");
            if (staff.hasLoginMessage())
            {
                loginMsg = FUtil.colorize(staff.getLoginMessage());
            }
            FUtil.bcastMsg(ChatColor.AQUA + playerSender.getName() + " is " + loginMsg);
            FUtil.bcastMsg(playerSender.getName() + " joined the game", ChatColor.YELLOW);
            if (staff.getTag() != null)
            {
                tag = FUtil.colorize(staff.getTag());
            }
            else
            {
                plugin.pl.getPlayer(playerSender).setTag(tag);
            }
            FLog.info(playerSender.getName() + " is no longer vanished.");
            for (Player player : server.getOnlinePlayers())
            {
                player.showPlayer(playerSender);
            }
            plugin.esb.setVanished(playerSender.getName(), false);
            playerSender.removePotionEffect(PotionEffectType.INVISIBILITY);
            playerSender.setPlayerListName(StringUtils.substring(displayName, 0, 16));
            VANISHED.remove(playerSender);
        }
        else
        {
            msg("You have been vanished.", ChatColor.GOLD);
            FUtil.bcastMsg(playerSender.getName() + " left the game", ChatColor.YELLOW);
            FLog.info(playerSender.getName() + " is now vanished.");
            for (Player player : server.getOnlinePlayers())
            {
                player.hidePlayer(playerSender);
            }
            plugin.esb.setVanished(playerSender.getName(), true);
            playerSender.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 1000000, 1, false, true));
            VANISHED.add(playerSender);
        }
        return true;
    }
}
