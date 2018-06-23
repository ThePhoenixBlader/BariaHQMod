package me.bariahq.bariahqmod.command;

import me.bariahq.bariahqmod.rank.Rank;
import me.bariahq.bariahqmod.util.FUtil;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = Rank.MOD, source = SourceType.ONLY_IN_GAME)
@CommandParameters(description = "Take a selfie or throw the camera!", usage = "/<command> <selfie | throw>", aliases = "cam")
public class Command_camera extends FreedomCommand
{

    @Override
    public boolean run(CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        if (args.length > 0)
        {
            if (args[0].equalsIgnoreCase("selfie"))
            {
                FUtil.staffAction(playerSender.getName(), "Has started taking selfies!", false);
                playerSender.chat("But first, let me take a selfie!");
                return true;
            }
            if (args[0].equalsIgnoreCase("throw"))
            {
                FUtil.staffAction(playerSender.getName(), "Has thrown the fucking camera!", true);
                playerSender.chat("Fuck this camera, I'm destroying it for youtube views!");
                final Location targetPos = playerSender.getLocation();
                final World world = playerSender.getWorld();
                for (int x = -1; x <= 1; x++)
                {
                    for (int z = -1; z <= 1; z++)
                    {
                        final Location strike_pos = new Location(world, targetPos.getBlockX() + x, targetPos.getBlockY(), targetPos.getBlockZ() + z);
                        world.strikeLightning(strike_pos);
                    }
                }
                playerSender.setHealth(0.0);
                return true;
            }
        }
        return false;
    }
}
