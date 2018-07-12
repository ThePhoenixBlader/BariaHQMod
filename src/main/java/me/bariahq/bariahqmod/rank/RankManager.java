package me.bariahq.bariahqmod.rank;

import me.bariahq.bariahqmod.BariaHQMod;
import me.bariahq.bariahqmod.FreedomService;
import me.bariahq.bariahqmod.config.ConfigEntry;
import me.bariahq.bariahqmod.player.FPlayer;
import me.bariahq.bariahqmod.staff.StaffMember;
import me.bariahq.bariahqmod.util.FUtil;
import net.pravian.aero.util.ChatUtils;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;

public class RankManager extends FreedomService
{

    public RankManager(BariaHQMod plugin)
    {
        super(plugin);
    }

    @Override
    protected void onStart()
    {
    }

    @Override
    protected void onStop()
    {
    }

    public Displayable getDisplay(CommandSender sender)
    {
        if (!(sender instanceof Player))
        {
            return getRank(sender); // Consoles don't have display ranks
        }

        final Player player = (Player) sender;

        // Display impostors
        if (plugin.al.isStaffImposter(player))
        {
            return Rank.IMPOSTOR;
        }

        if (ConfigEntry.SERVER_ARCHITECTS.getStringList().contains(player.getName()))
        {
            return Title.ARCHITECT;
        }

        // TF Developers
        if (FUtil.TFDEVS.contains(player.getName()) && !FUtil.BHQDEVS.contains(player.getName()) && !ConfigEntry.SERVER_OWNERS.getList().contains(player.getName()) && !ConfigEntry.SERVER_FOUNDERS.getList().contains(player.getName()) && !plugin.al.isStaffImposter(player))
        {
            return Title.TFDEV;
        }

        // If a player is a donor and not on the staff list, display that
        if (plugin.dl.isDonator(player) && !plugin.al.isStaffMember(player))
        {
            return Rank.DONATOR;
        }

        // BHQ Developers
        if (FUtil.BHQDEVS.contains(player.getName()) && !ConfigEntry.SERVER_OWNERS.getList().contains(player.getName()) && !ConfigEntry.SERVER_FOUNDERS.getList().contains(player.getName()) && !plugin.al.isStaffImposter(player))
        {
            return Title.BHQDEV;
        }

        // If the player's an owner, display that
        if (ConfigEntry.SERVER_OWNERS.getList().contains(player.getName()) && !ConfigEntry.SERVER_FOUNDERS.getList().contains(player.getName()) && !plugin.al.isStaffImposter(player))
        {
            return Title.OWNER;
        }

        // If the player's the founder, display that
        if (ConfigEntry.SERVER_FOUNDERS.getList().contains(player.getName()) && !plugin.al.isStaffImposter(player))
        {
            return Title.FOUNDER;
        }

        // If the player's a manager, display that
        if (ConfigEntry.SERVER_MANAGERS.getList().contains(player.getName()) && !FUtil.BHQDEVS.contains(player.getName()) && !ConfigEntry.SERVER_OWNERS.getList().contains(player.getName()) && !ConfigEntry.SERVER_FOUNDERS.getList().contains(player.getName()) && !plugin.al.isStaffImposter(player))
        {
            return Title.MANAGER;
        }

        final Rank rank = getRank(player);

        // Non-admins don't have titles, display actual rank
        if (!rank.isStaff())
        {
            return rank;
        }

        return rank;
    }

    public Rank getRank(CommandSender sender)
    {
        if (sender instanceof Player)
        {
            return getRank((Player) sender);
        }

        // CONSOLE?
        if (sender.getName().equals("CONSOLE"))
        {
            return ConfigEntry.STAFFLIST_CONSOLE_IS_SENIOR.getBoolean() ? Rank.SENIOR_CONSOLE : Rank.ADMIN_CONSOLE;
        }

        // Console staff member, get by name
        StaffMember admin = plugin.al.getEntryByName(sender.getName());

        // Unknown console: RCON?
        if (admin == null)
        {
            return Rank.SENIOR_CONSOLE;
        }

        Rank rank = admin.getRank();

        // Get console
        if (rank.hasConsoleVariant())
        {
            rank = rank.getConsoleVariant();
        }
        return rank;
    }

    public Rank getRank(Player player)
    {
        if (plugin.al.isStaffImposter(player))
        {
            return Rank.IMPOSTOR;
        }

        final StaffMember entry = plugin.al.getStaffMember(player);
        if (entry != null)
        {
            return entry.getRank();
        }

        return player.isOp() ? Rank.OP : Rank.NON_OP;
    }

    public void updateDisplay(Player player)
    {
        if (player.isOnline())
        {
            FPlayer fPlayer = plugin.pl.getPlayer(player);
            if (plugin.al.isStaffMember(player))
            {
                Displayable display = getDisplay(player);
                if (fPlayer.getTag() == null)
                {
                    fPlayer.setTag(display.getColoredTag());
                }
                String displayName = display.getColor() + player.getName();
                player.setPlayerListName(StringUtils.substring(displayName, 0, 16));
            }
            else
            {
                fPlayer.setTag(null);
                player.setPlayerListName(null);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        final Player player = event.getPlayer();
        final FPlayer fPlayer = plugin.pl.getPlayer(player);

        // Unban admins
        boolean isAdmin = plugin.al.isStaffMember(player);
        if (isAdmin)
        {
            plugin.al.updateLastLogin(player);
        }

        // Handle impostors
        if (plugin.al.isStaffImposter(player))
        {
            FUtil.bcastMsg(ChatColor.AQUA + player.getName() + " is " + Rank.IMPOSTOR.getColoredLoginMessage());
            FUtil.bcastMsg("Warning: " + player.getName() + " has been flagged as an impostor and has been frozen!", ChatColor.RED);
            String displayName = Rank.IMPOSTOR.getColor() + player.getName();
            player.setPlayerListName(StringUtils.substring(displayName, 0, 16));
            player.getInventory().clear();
            player.setOp(false);
            player.setGameMode(GameMode.SURVIVAL);
            plugin.pl.getPlayer(player).getFreezeData().setFrozen(true);
            player.sendMessage(ChatColor.RED + "You are marked as an impostor, please verify yourself!");
            return;
        }

        // Set display
        if (isAdmin || FUtil.TFDEVS.contains(player.getName()) || FUtil.BHQDEVS.contains(player.getName()) || plugin.dl.isDonator(player) || plugin.arl.isArchitect(player))
        {
            final Displayable display = getDisplay(player);
            String loginMsg = display.getColoredLoginMessage();

            if (isAdmin)
            {
                StaffMember admin = plugin.al.getStaffMember(player);
                if (admin.hasLoginMessage())
                {
                    loginMsg = ChatUtils.colorize(admin.getLoginMessage());
                }
            }

            FUtil.bcastMsg(ChatColor.AQUA + player.getName() + " is " + loginMsg);
            plugin.pl.getPlayer(player).setTag(display.getColoredTag());
            if (plugin.al.isStaffMember(player))
            {
                StaffMember staffMember = plugin.al.getStaffMember(player);
                if (staffMember.getTag() != null)
                {
                    plugin.pl.getPlayer(player).setTag(FUtil.colorize(staffMember.getTag()));
                }
            }

            String displayName = display.getColoredTag() + ChatColor.RESET + " " + player.getName();
            try
            {
                player.setPlayerListName(StringUtils.substring(displayName, 0, 16));
            }
            catch (IllegalArgumentException ex)
            {
            }
        }
    }
}
