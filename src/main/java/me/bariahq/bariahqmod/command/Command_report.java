package me.bariahq.bariahqmod.command;

import me.bariahq.bariahqmod.config.ConfigEntry;
import me.bariahq.bariahqmod.discord.Discord;
import me.bariahq.bariahqmod.rank.Rank;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.awt.*;

@CommandPermissions(level = Rank.OP, source = SourceType.ONLY_IN_GAME, blockHostConsole = true)
@CommandParameters(description = "Report a player for staff members to see.", usage = "/<command> <player> <reason>")
public class Command_report extends FreedomCommand
{

    @Override
    public boolean run(CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        if (args.length < 2)
        {
            return false;
        }

        Player player = getPlayer(args[0]);

        if (player == null)
        {
            msg(PLAYER_NOT_FOUND);
            return true;
        }

        if (sender instanceof Player)
        {
            if (player.equals(playerSender))
            {
                msg(ChatColor.RED + "Please, don't try to report yourself.");
                return true;
            }
        }

        if (plugin.al.isStaffMember(player))
        {
            msg(ChatColor.RED + "You can not report a staff member.");
            return true;
        }

        String report = StringUtils.join(ArrayUtils.subarray(args, 1, args.length), " ");
        plugin.cm.reportAction(playerSender, player, report);
        //
            TextChannel channel = Discord.bot.getTextChannelById(ConfigEntry.DISCORD_REPORTS_CHANNELID.getString());
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("New Report");
            eb.setAuthor(sender.getName());
            eb.addBlankField(true);
            eb.setThumbnail(Discord.bot.getTextChannelById(ConfigEntry.DISCORD_REPORTS_CHANNELID.getString()).getGuild().getIconUrl());
            eb.addField("Reported User", player.getName(), true);
            eb.addField("Reason", report, true);
            eb.addField("Reported Player's World", player.getWorld().getName(), true);
            eb.addField("Reporter's World", playerSender.getWorld().getName(), true);
            eb.addField("Reported Player's Coords", player.getLocation().getX() + ", " + player.getLocation().getY() + ", " + player.getLocation().getZ(), true);
            eb.setColor(Color.red);
            channel.sendMessage(eb.build()).queue();
            //feature coming later on to enable or disable this discord report system via config

        msg(ChatColor.GREEN + "Thank you, all online staff members have been notified." + ChatColor.RED + "Please note that only the current online admins will see it" + ChatColor.GREEN + ".");

        return true;
    }
}
