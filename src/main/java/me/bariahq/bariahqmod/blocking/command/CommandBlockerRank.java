package me.bariahq.bariahqmod.blocking.command;

import me.bariahq.bariahqmod.BariaHQMod;
import me.bariahq.bariahqmod.rank.Rank;
import me.bariahq.bariahqmod.staff.StaffMember;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public enum CommandBlockerRank
{

    ANYONE("a"),
    OP("o"),
    MOD("m"),
    ADMIN("a"),
    SENIOR("s"),
    NOBODY("n");
    //
    private final String token;

    private CommandBlockerRank(String token)
    {
        this.token = token;
    }

    public static CommandBlockerRank fromSender(CommandSender sender)
    {
        if (!(sender instanceof Player))
        {
            return ADMIN;
        }

        StaffMember admin = BariaHQMod.plugin().al.getStaffMember(sender);
        if (admin != null)
        {
            if (admin.getRank() == Rank.SENIOR_ADMIN)
            {
                return SENIOR;
            }
            return ADMIN;
        }

        if (sender.isOp())
        {
            return OP;
        }

        return ANYONE;

    }

    public static CommandBlockerRank fromToken(String token)
    {
        for (CommandBlockerRank rank : CommandBlockerRank.values())
        {
            if (rank.getToken().equalsIgnoreCase(token))
            {
                return rank;
            }
        }
        return ANYONE;
    }

    public String getToken()
    {
        return this.token;
    }

    public boolean hasPermission(CommandSender sender)
    {
        return fromSender(sender).ordinal() >= ordinal();
    }
}
