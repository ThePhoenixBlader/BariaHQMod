package me.bariahq.bariahqmod.donator;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import lombok.Getter;
import me.bariahq.bariahqmod.BariaHQMod;
import me.bariahq.bariahqmod.FreedomService;
import me.bariahq.bariahqmod.util.FLog;
import me.bariahq.bariahqmod.util.FUtil;
import net.pravian.aero.config.YamlConfig;
import net.pravian.aero.util.Ips;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.ServicePriority;

import java.util.Date;
import java.util.Map;
import java.util.Set;

public class DonatorList extends FreedomService
{
    public static final String CONFIG_FILENAME = "donators.yml";

    @Getter
    private final Map<String, DonatorMember> donators = Maps.newHashMap();
    private final Map<String, DonatorMember> nameTable = Maps.newHashMap();
    private final Map<String, DonatorMember> ipTable = Maps.newHashMap();
    //
    private final YamlConfig config;

    public DonatorList(BariaHQMod plugin)
    {
        super(plugin);

        this.config = new YamlConfig(plugin, CONFIG_FILENAME, true);
    }

    @Override
    protected void onStart()
    {
        load();

        server.getServicesManager().register(Function.class, new Function<Player, Boolean>()
        {
            @Override
            public Boolean apply(Player player)
            {
                return isDonator(player);
            }
        }, plugin, ServicePriority.Normal);
    }

    @Override
    protected void onStop()
    {
        save();
    }

    public void load()
    {
        config.load();

        donators.clear();
        for (String key : config.getKeys(false))
        {
            ConfigurationSection section = config.getConfigurationSection(key);
            if (section == null)
            {
                logger.warning("Invalid donator list format: " + key);
                continue;
            }

            DonatorMember donator = new DonatorMember(key);
            donator.loadFrom(section);

            if (!donator.isValid())
            {
                FLog.warning("Could not load donator: " + key + ". Missing details!");
                continue;
            }

            donators.put(key, donator);
        }

        updateTables();
        FLog.info("Loaded " + donators.size() + " donators with " + ipTable.size() + " IPs)");
    }

    public void save()
    {
        // Clear the config
        for (String key : config.getKeys(false))
        {
            config.set(key, null);
        }

        for (DonatorMember donator : donators.values())
        {
            donator.saveTo(config.createSection(donator.getConfigKey()));
        }

        config.save();
    }

    public synchronized boolean isDonatorSync(CommandSender sender)
    {
        return isDonator(sender);
    }

    public boolean isDonator(CommandSender sender)
    {
        if (!(sender instanceof Player))
        {
            return true;
        }

        DonatorMember donator = getDonator((Player) sender);

        return donator != null;
    }

    public Map<String, DonatorMember> getAllDonators()
    {
        return this.donators;
    }

    public DonatorMember getDonator(CommandSender sender)
    {
        if (sender instanceof Player)
        {
            return getDonator((Player) sender);
        }

        return getEntryByName(sender.getName());
    }

    public DonatorMember getDonator(Player player)
    {
        String ip = Ips.getIp(player);
        DonatorMember donator = getEntryByName(player.getName());

        // By name
        if (donator != null)
        {
            // Check if we're in online mode or if we have a matching IP
            if (server.getOnlineMode() || donator.getIps().contains(ip))
            {
                if (!donator.getIps().contains(ip))
                {
                    // Add the new IP if needed
                    donator.addIp(ip);
                    save();
                    updateTables();
                }
                return donator;
            }
        }

        // By ip
        donator = getEntryByIp(ip);
        if (donator != null)
        {
            // Set the new username
            donator.setName(player.getName());
            save();
            updateTables();
        }

        return null;
    }

    public DonatorMember getEntryByName(String name)
    {
        return nameTable.get(name.toLowerCase());
    }

    public DonatorMember getEntryByIp(String ip)
    {
        return ipTable.get(ip);
    }

    public DonatorMember getEntryByIpFuzzy(String needleIp)
    {
        final DonatorMember directDonator = getEntryByIp(needleIp);
        if (directDonator != null)
        {
            return directDonator;
        }

        for (String ip : ipTable.keySet())
        {
            if (FUtil.fuzzyIpMatch(needleIp, ip, 3))
            {
                return ipTable.get(ip);
            }
        }

        return null;
    }

    public void updateLastLogin(Player player)
    {
        final DonatorMember donator = getDonator(player);
        if (donator == null)
        {
            return;
        }

        donator.setLastLogin(new Date());
        donator.setName(player.getName());
        save();
    }

    public boolean isDonatorImpostor(Player player)
    {
        return getEntryByName(player.getName()) != null && !isDonator(player);
    }

    public boolean isIdentityMatched(Player player)
    {
        if (server.getOnlineMode())
        {
            return true;
        }

        DonatorMember donator = getDonator(player);
        return donator != null && donator.getName().equalsIgnoreCase(player.getName());
    }

    public boolean addDonator(DonatorMember donator)
    {
        if (!donator.isValid())
        {
            logger.warning("Could not add donator: " + donator.getConfigKey() + " donator is missing details!");
            return false;
        }

        final String key = donator.getConfigKey();

        // Store donator, update views
        donators.put(key, donator);
        updateTables();

        // Save donator
        donator.saveTo(config.createSection(key));
        config.save();

        return true;
    }

    public boolean removeDonator(DonatorMember donator)
    {
        // Remove donator, update views
        if (donators.remove(donator.getConfigKey()) == null)
        {
            return false;
        }
        updateTables();

        // 'Unsave' donator
        config.set(donator.getConfigKey(), null);
        config.save();

        return true;
    }

    public void updateTables()
    {
        nameTable.clear();
        ipTable.clear();

        for (DonatorMember donator : donators.values())
        {
            nameTable.put(donator.getName().toLowerCase(), donator);

            for (String ip : donator.getIps())
            {
                ipTable.put(ip, donator);
            }

        }
    }

    public Set<String> getDonatorNames()
    {
        return nameTable.keySet();
    }

    public Set<String> getDonatorIps()
    {
        return ipTable.keySet();
    }

}
