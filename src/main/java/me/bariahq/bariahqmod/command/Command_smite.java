package me.bariahq.bariahqmod.command;

import me.bariahq.bariahqmod.punishment.Punishment;
import me.bariahq.bariahqmod.punishment.PunishmentType;
import me.bariahq.bariahqmod.rank.Rank;
import me.bariahq.bariahqmod.util.FUtil;
import net.pravian.aero.util.Ips;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = Rank.MOD, source = SourceType.BOTH)
@CommandParameters(description = "Someone being a little bitch? Smite them down...", usage = "/<command> <bitch's name> [reason]")
public class Command_smite extends FreedomCommand
{

    public static void smite(Player bitch)
    {
        smite(bitch, null, null);
    }

    public static void smite(Player bitch, String reason, String by)
    {
        FUtil.bcastMsg(bitch.getName() + " has been a naughty, naughty child.", ChatColor.RED);

        if (reason != null)
        {
            FUtil.bcastMsg("  Reason: " + reason, ChatColor.RED);
        }

        if (by != null)
        {
            FUtil.bcastMsg("  Smitten by: " + by, ChatColor.RED);
        }

        // Deop
        bitch.setOp(false);

        // Set gamemode to survival
        bitch.setGameMode(GameMode.SURVIVAL);

        // Clear inventory
        bitch.getInventory().clear();

        // Strike with lightning effect
        final Location targetPos = bitch.getLocation();
        final World world = bitch.getWorld();
        for (int x = -1; x <= 1; x++)
        {
            for (int z = -1; z <= 1; z++)
            {
                final Location strike_pos = new Location(world, targetPos.getBlockX() + x, targetPos.getBlockY(), targetPos.getBlockZ() + z);
                world.strikeLightning(strike_pos);
            }
        }

        // Kill
        bitch.setHealth(0.0);

        if (reason != null)
        {
            bitch.sendMessage(ChatColor.RED + "You've been smitten. Reason: " + reason);
        }
    }

    @Override
    public boolean run(CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        if (args.length < 1)
        {
            return false;
        }

        final Player bitch = getPlayer(args[0]);

        String reason = null;
        if (args.length > 1)
        {
            reason = StringUtils.join(args, " ", 1, args.length);
        }

        if (bitch == null)
        {
            msg(FreedomCommand.PLAYER_NOT_FOUND);
            return true;
        }

        smite(bitch, reason, sender.getName());
        plugin.da.setAdminDeopped(bitch.getName(), true);
        plugin.pul.logPunishment(new Punishment(bitch.getName(), Ips.getIp(bitch), sender.getName(), PunishmentType.SMITE, reason));
        return true;
    }
}
