package me.bariahq.bariahqmod;

import me.bariahq.bariahqmod.config.ConfigEntry;
import me.bariahq.bariahqmod.shop.ShopData;
import me.bariahq.bariahqmod.util.FUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.*;

public class Sideboard extends FreedomService {

    public Sideboard(BariaHQMod plugin)
    {
        super(plugin);
    }

    @Override
    protected void onStart() {

    }

    @Override
    protected void onStop() {

    }

    ScoreboardManager manager = Bukkit.getScoreboardManager();

    Scoreboard board = manager.getNewScoreboard();
    Team team = board.registerNewTeam("Players");

    Objective objective = board.registerNewObjective("test", "dummy");



    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        ShopData sd = plugin.sh.getData(player);
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(FUtil.colorize(ConfigEntry.SERVER_NAME.getString()));
        Score line = objective.getScore(ChatColor.DARK_GRAY + "----------------------");
        line.setScore(11);
        Score playerthing = objective.getScore(ChatColor.DARK_AQUA + "   Player");
        playerthing.setScore(10);
        Score name = objective.getScore(ChatColor.AQUA + "Name: " + ChatColor.GRAY + player.getName());
        name.setScore(9);
        Score world = objective.getScore(ChatColor.AQUA + "World: " + ChatColor.GRAY + player.getWorld().getName());
        world.setScore(8);
        Score coins = objective.getScore(ChatColor.AQUA + "Coins: " + ChatColor.GRAY + "/coins");
        coins.setScore(7);
        Score blank = objective.getScore("          ");
        blank.setScore(6);
        Score sitesthing = objective.getScore(ChatColor.DARK_AQUA + "   Sites");
        sitesthing.setScore(5);
        Score forum = objective.getScore(ChatColor.AQUA + "Forum: " + ChatColor.GRAY + "bariahq.net");
        forum.setScore(4);
        Score vote = objective.getScore(ChatColor.AQUA + "Vote: " + ChatColor.GRAY + "bariahq.net/vote");
        vote.setScore(3);
        Score discord = objective.getScore(ChatColor.AQUA + "Discord: " + ChatColor.GRAY + "/discord");
        discord.setScore(2);
        Score lastline = objective.getScore(ChatColor.DARK_GRAY + "----------------------");
        lastline.setScore(1);
        team.addPlayer(player);
        team.setDisplayName("Players");
        team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);

        player.setScoreboard(board);

        //this looks soo messy lmao and most of these methods have new methods but the old ones still work so hey why not
    }
    @EventHandler
    public void onQuit(PlayerQuitEvent event){

        event.getPlayer().setScoreboard(manager.getNewScoreboard());
        board.resetScores(event.getPlayer());
        team.removePlayer(event.getPlayer());
    }
}
