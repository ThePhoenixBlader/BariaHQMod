/*
 */
package me.bariahq.bariahqmod.command;

import me.bariahq.bariahqmod.donator.DonatorMember;
import me.bariahq.bariahqmod.player.FPlayer;
import me.bariahq.bariahqmod.rank.Rank;
import me.bariahq.bariahqmod.util.FUtil;
import net.pravian.aero.util.Ips;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Date;

@CommandPermissions(level = Rank.OP, source = SourceType.BOTH, blockHostConsole = true)
@CommandParameters(description = "Manage donators.", usage = "/<command> <list | reload | | <add | remove | info> <username>>")
public class Command_dconfig extends FreedomCommand
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
                msg("Donators: " + StringUtils.join(plugin.dl.getDonatorNames(), ", "), ChatColor.GOLD);

                return true;
            }

            case "reload":
            {
                checkRank(Rank.SENIOR_ADMIN);

                FUtil.staffAction(sender.getName(), "Reloading the donator list", true);
                plugin.dl.load();
                msg("Donator list reloaded!");
                return true;
            }

            case "info":
            {
                if (args.length < 2)
                {
                    return false;
                }

                checkRank(Rank.MOD);

                DonatorMember donator = plugin.dl.getEntryByName(args[1]);

                if (donator == null)
                {
                    final Player player = getPlayer(args[1]);
                    if (player != null)
                    {
                        donator = plugin.dl.getDonator(player);
                    }
                }

                if (donator == null)
                {
                    msg("Donator not found: " + args[1]);
                }
                else
                {
                    msg(donator.toString());
                }

                return true;
            }

            case "add":
            {
                if (args.length < 2)
                {
                    return false;
                }
                checkRank(Rank.ADMIN);

                // Player already on the list?
                final Player player = getPlayer(args[1]);
                if (player != null && plugin.dl.isDonator(player))
                {
                    msg("That player is already on the donator list.");
                    return true;
                }

                // Find the old staff list entry
                String name = player != null ? player.getName() : args[1];
                DonatorMember donator = null;
                for (DonatorMember loopDonator : plugin.dl.getAllDonators().values())
                {
                    if (loopDonator.getName().equalsIgnoreCase(name))
                    {
                        donator = loopDonator;
                        break;
                    }
                }

                if (donator == null) // New staff member
                {
                    if (player == null)
                    {
                        msg(FreedomCommand.PLAYER_NOT_FOUND);
                        return true;
                    }

                    FUtil.staffAction(sender.getName(), "Adding " + player.getName() + " to the donator list", true);
                    plugin.dl.addDonator(new DonatorMember(player));
                    plugin.rm.updateDisplay(player);
                }
                else // Existing staff member
                {
                    FUtil.staffAction(sender.getName(), "Readding " + donator.getName() + " to the donator list", true);

                    if (player != null)
                    {
                        donator.setName(player.getName());
                        donator.addIp(Ips.getIp(player));
                    }

                    donator.setLastLogin(new Date());

                    plugin.dl.save();
                    plugin.dl.updateTables();
                    plugin.rm.updateDisplay(player);
                }

                if (player != null)
                {
                    final FPlayer fPlayer = plugin.pl.getPlayer(player);
                    if (fPlayer.getFreezeData().isFrozen())
                    {
                        fPlayer.getFreezeData().setFrozen(false);
                        msg(player.getPlayer(), "You have been unfrozen.");
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

                checkRank(Rank.ADMIN);

                Player player = getPlayer(args[1]);
                DonatorMember donator = player != null ? plugin.dl.getDonator(player) : plugin.dl.getEntryByName(args[1]);

                if (donator == null)
                {
                    msg("Donator not found: " + args[1]);
                    return true;
                }

                FUtil.staffAction(sender.getName(), "Removing " + donator.getName() + " from the donator list", true);
                plugin.dl.removeDonator(donator);
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
