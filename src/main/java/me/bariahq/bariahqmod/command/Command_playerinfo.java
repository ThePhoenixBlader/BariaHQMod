package me.bariahq.bariahqmod.command;

import me.bariahq.bariahqmod.rank.Rank;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import me.bariahq.bariahqmod.player.FPlayer;
import me.bariahq.bariahqmod.shop.ShopData;
import me.bariahq.bariahqmod.util.FUtil;
import org.apache.commons.lang3.StringUtils;

@CommandPermissions(level = Rank.SUPER_ADMIN, source = SourceType.BOTH)
@CommandParameters(description = "Get info on a player.", usage = "/<command> <name>", aliases = "pi")
public class Command_playerinfo extends FreedomCommand
{

    @Override
    public boolean run(CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        if (args.length != 1)
        {
            return false;
        }
        if (getPlayer(args[0]) == null)
        {
            sender.sendMessage(FreedomCommand.PLAYER_NOT_FOUND);
            return true;
        }
        final Player player = getPlayer(args[0]);
        final ShopData sd = plugin.sh.getData(player);
        FPlayer playerdata;
        playerdata = plugin.pl.getPlayer(player);
        if (args.length == 1)
        {
            msg("Name: " + player.getName(), ChatColor.AQUA);
            msg("Opped: " + (player.isOp() ? ChatColor.GREEN + "true" : ChatColor.RED + "false"), ChatColor.LIGHT_PURPLE);
            msg("Gamemode: " + player.getGameMode().name().toLowerCase().substring(0, 1).toUpperCase(), ChatColor.DARK_BLUE);
            msg("IPs: " + StringUtils.join(plugin.pl.getData(player).getIps(), ", "), ChatColor.GREEN);
            msg("Rank: " + plugin.rm.getDisplay(player).getColor() + plugin.rm.getDisplay(player).getName(), ChatColor.LIGHT_PURPLE);
            msg("Last command: " + playerdata.getLastCommand());
            msg("Muted: " + (playerdata.isMuted() ? ChatColor.GREEN + "true" : ChatColor.RED + "false"), ChatColor.DARK_AQUA);
            msg("Commandspy: " + (playerdata.cmdspyEnabled() ? ChatColor.GREEN + "true" : ChatColor.RED + "false"), ChatColor.RED);
            msg("Location: World: " + player.getLocation().getWorld().getName() + " X: " + player.getLocation().getBlockX() + " Y: " + player.getLocation().getBlockY() + " Z: " + player.getLocation().getBlockZ(), ChatColor.WHITE);
            msg("Tag: " + (playerdata.getTag() == null ? "None" : playerdata.getTag()), ChatColor.WHITE);
            msg("Coins: " + sd.getCoins(), ChatColor.DARK_PURPLE);
            msg("Has colored chat: " + (sd.isColoredchat() ? ChatColor.GREEN + "true" : ChatColor.RED + "false"), ChatColor.BLUE);
            msg("Can set custom login messages: " + (plugin.al.isStaffMember(player) ? ChatColor.GREEN + "true" : (sd.isCustomLoginMessage() ? ChatColor.GREEN + "true" : ChatColor.RED + "false")));
            msg("Custom login message: " + (plugin.al.isStaffMember(player) ? (plugin.al.getStaffMember(player).hasLoginMessage() ? ChatColor.AQUA + FUtil.colorize(plugin.al.getStaffMember(player).getLoginMessage()) : "None") : sd.getLoginMessage().equals("none") ? "None" : ChatColor.AQUA + FUtil.colorize(sd.getLoginMessage())), ChatColor.GOLD);
            return true;
        }
        return false;
    }
}

