package me.bariahq.bariahqmod.command;

import me.bariahq.bariahqmod.architect.Architect;
import me.bariahq.bariahqmod.player.FPlayer;
import me.bariahq.bariahqmod.rank.Rank;
import me.bariahq.bariahqmod.util.FUtil;
import net.pravian.aero.util.Ips;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Date;

@CommandPermissions(level = Rank.OP, source = SourceType.BOTH)
@CommandParameters(description = "Manage architects", usage = "/<command> <list | reload | <add | remove | info <player>>")
public class Command_architectconfig extends FreedomCommand
{

    @Override
    public boolean run(CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        if (args.length < 1)
        {
            return false;
        }

        switch (args[0])
        {
            case "list":
            {
                msg("Architects: " + StringUtils.join(plugin.arl.getArchitectNames(), ", "), ChatColor.GOLD);
                return true;
            }

            case "reload":
            {
                checkRank(Rank.SENIOR_ADMIN);
                FUtil.staffAction(sender.getName(), "Reloading the Architect list", true);
                plugin.arl.load();
                msg("Architect has been reload!");
                return true;
            }

            case "info":
            {
                if (args.length < 2)
                {
                    return false;
                }

                checkRank(Rank.MOD);

                Architect architect = plugin.arl.getEntryByName(args[1]);

                if (architect == null)
                {
                    Player player = getPlayer(args[1]);
                    if (player != null)
                    {
                        architect = plugin.arl.getArchitect(player);
                    }
                }

                if (architect == null)
                {
                    msg("Architect not found: " + args[1]);
                }
                else
                {
                    msg(architect.toString());
                }

                return true;
            }

            case "add":
            {
                if (args.length < 2)
                {
                    return false;
                }

                checkConsole();
                checkRank(Rank.ADMIN);

                final Player player = getPlayer(args[1]);
                if (player != null && plugin.arl.isArchitect(player))
                {
                    msg("That player is already on the Architect list.");
                    return true;
                }

                String name = player != null ? player.getName() : args[1];
                Architect architect = null;
                for (Architect loopArchitect : plugin.arl.getAllArchitects().values())
                {
                    if (loopArchitect.getName().equalsIgnoreCase(name))
                    {
                        architect = loopArchitect;
                        break;
                    }
                }

                if (architect == null)
                {
                    checkRank(Rank.SENIOR_ADMIN);
                    if (!FUtil.isManager(sender.getName()))
                    {
                        noPerms();
                    }

                    if (player == null)
                    {
                        msg(FreedomCommand.PLAYER_NOT_FOUND);
                        return true;
                    }

                    FUtil.staffAction(sender.getName(), "Adding " + player.getName() + " to the Architect list", true);
                    plugin.arl.addArchitect(new Architect(player));
                    if (player != null)
                    {
                        plugin.rm.updateDisplay(player);
                    }
                }
                else
                {
                    FUtil.staffAction(sender.getName(), "Readding " + architect.getName() + " to the Architect list", true);

                    if (player != null)
                    {
                        architect.setName(player.getName());
                        architect.addIp(Ips.getIp(player));
                    }

                    architect.setLastLogin(new Date());

                    plugin.arl.save();
                    plugin.arl.updateTables();
                    if (player != null)
                    {
                        plugin.rm.updateDisplay(player);
                    }
                }

                if (player != null)
                {
                    final FPlayer fPlayer = plugin.pl.getPlayer(player);
                    if (fPlayer.getFreezeData().isFrozen())
                    {
                        fPlayer.getFreezeData().setFrozen(false);
                        msg(player.getPlayer(), "You have been unfrozen.");
                    }

                    if (!player.isOp())
                    {
                        player.setOp(true);
                        player.sendMessage(YOU_ARE_OP);
                    }
                }
                return true;
            }

            case "remove":
            {
                if (args.length < 2)
                {
                    return false;
                }

                checkConsole();
                checkRank(Rank.SENIOR_ADMIN);
                if (!FUtil.isManager(sender.getName()))
                {
                    noPerms();
                }

                Player player = getPlayer(args[1]);
                Architect architect = player != null ? plugin.arl.getArchitect(player) : plugin.arl.getEntryByName(args[1]);

                if (architect == null)
                {
                    msg("Architect not found: " + args[1]);
                    return true;
                }

                FUtil.staffAction(sender.getName(), "Removing " + architect.getName() + " from the Architect list", true);
                plugin.arl.removeArchitect(architect);
                if (player != null)
                {
                    plugin.rm.updateDisplay(player);
                }
                return true;
            }

            default:
            {
                return false;
            }
        }
    }
}
