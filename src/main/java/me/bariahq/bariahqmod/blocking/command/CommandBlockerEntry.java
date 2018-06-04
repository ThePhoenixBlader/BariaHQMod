package me.bariahq.bariahqmod.blocking.command;

import lombok.Getter;
import me.bariahq.bariahqmod.util.FUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class CommandBlockerEntry
{

    @Getter
    private final CommandBlockerRank rank;
    @Getter
    private final CommandBlockerAction action;
    @Getter
    private final String command;
    @Getter
    private final String subCommand;
    @Getter
    private final String message;

    public CommandBlockerEntry(CommandBlockerRank rank, CommandBlockerAction action, String command, String message)
    {
        this(rank, action, command, null, message);
    }

    public CommandBlockerEntry(CommandBlockerRank rank, CommandBlockerAction action, String command, String subCommand, String message)
    {
        this.rank = rank;
        this.action = action;
        this.command = command;
        this.subCommand = (subCommand == null ? null : subCommand.toLowerCase().trim());
        this.message = (message == null || message.equals("_") ? "That command is blocked." : message);
    }

    public void doActions(CommandSender sender)
    {
        if (action == CommandBlockerAction.BLOCK_UNKNOWN)
        {
            FUtil.playerMsg(sender, "Unknown command. Type \"help\" for help.", ChatColor.RESET);
            return;
        }

        FUtil.playerMsg(sender, FUtil.colorize(message));
    }
}
