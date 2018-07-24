package me.bariahq.bariahqmod.command;

import me.bariahq.bariahqmod.player.FPlayer;
import me.bariahq.bariahqmod.rank.Rank;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = Rank.OP, source = SourceType.ONLY_IN_GAME)
@CommandParameters(description = "Toggle your sidebar", usage = "/<command>")
public class Command_sidebar extends FreedomCommand {
    @Override
    public boolean run(CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args, boolean senderIsConsole) {
        if (args.length > 0){
            return false;
        }
        FPlayer player = plugin.pl.getPlayer(playerSender);
        if (player.hasSidebar()){
            plugin.sb.clear(playerSender);
            playerSender.sendMessage(ChatColor.AQUA + "Disabled scoreboard.");
            player.setSidebar(false);
            return true;
        } else if (!player.hasSidebar()){
            playerSender.setScoreboard(plugin.sb.getScoreboard());
            playerSender.sendMessage(ChatColor.AQUA + "Enabled scoreboard.");
            player.setSidebar(true);
            return true;
        }

        return false;
    }
}
