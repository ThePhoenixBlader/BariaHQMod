package me.bariahq.bariahqmod.architect;

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

public class ArchitectList extends FreedomService
{

    public static final String CONFIG_FILENAME = "architects.yml";

    @Getter
    private final Map<String, Architect> architects = Maps.newHashMap();
    private final Map<String, Architect> nameTable = Maps.newHashMap();
    private final Map<String, Architect> ipTable = Maps.newHashMap();
    //
    private final YamlConfig config;

    public ArchitectList(BariaHQMod plugin)
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
                return isArchitect(player);
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

        architects.clear();
        for (String key : config.getKeys(false))
        {
            ConfigurationSection section = config.getConfigurationSection(key);
            if (section == null)
            {
                logger.warning("Invalid architect list format: " + key);
                continue;
            }

            Architect architect = new Architect(key);
            architect.loadFrom(section);

            if (!architect.isValid())
            {
                FLog.warning("Could not load master builder: " + key + ". Missing details!");
                continue;
            }

            architects.put(key, architect);
        }

        updateTables();
        FLog.info("Loaded " + architects.size() + " architects with " + ipTable.size() + " IPs)");
    }

    public void save()
    {
        // Clear the config
        for (String key : config.getKeys(false))
        {
            config.set(key, null);
        }

        for (Architect architect : architects.values())
        {
            architect.saveTo(config.createSection(architect.getConfigKey()));
        }

        config.save();
    }

    public synchronized boolean isArchitectSync(CommandSender sender)
    {
        return isArchitect(sender);
    }

    public boolean isArchitect(CommandSender sender)
    {
        if (!(sender instanceof Player))
        {
            return true;
        }

        Architect architect = getArchitect((Player) sender);

        return architect != null;
    }

    public Map<String, Architect> getAllArchitects()
    {
        return this.architects;
    }

    public Architect getArchitect(CommandSender sender)
    {
        if (sender instanceof Player)
        {
            return getArchitect((Player) sender);
        }

        return getEntryByName(sender.getName());
    }

    public Architect getArchitect(Player player)
    {
        String ip = Ips.getIp(player);
        Architect architect = getEntryByName(player.getName());

        // By name
        if (architect != null)
        {
            // Check if we're in online mode or if we have a matching IP
            if (server.getOnlineMode() || architect.getIps().contains(ip))
            {
                if (!architect.getIps().contains(ip))
                {
                    // Add the new IP if needed
                    architect.addIp(ip);
                    save();
                    updateTables();
                }
                return architect;
            }
        }

        return null;
    }

    public boolean isArchitect(Player player)
    {
        return getArchitect(player) != null;
    }

    public Architect getEntryByName(String name)
    {
        return nameTable.get(name.toLowerCase());
    }

    public Architect getEntryByIp(String ip)
    {
        return ipTable.get(ip);
    }

    public Architect getEntryByIpFuzzy(String needleIp)
    {
        final Architect directArchitect = getEntryByIp(needleIp);
        if (directArchitect != null)
        {
            return directArchitect;
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
        final Architect architect = getArchitect(player);
        if (architect == null)
        {
            return;
        }

        architect.setLastLogin(new Date());
        architect.setName(player.getName());
        save();
    }

    public boolean isArchitectImpostor(Player player)
    {
        return getEntryByName(player.getName()) != null && !isArchitect(player);
    }

    public boolean isIdentityMatched(Player player)
    {
        if (server.getOnlineMode())
        {
            return true;
        }

        Architect architect = getArchitect(player);
        return architect != null && architect.getName().equalsIgnoreCase(player.getName());
    }

    public boolean addArchitect(Architect architect)
    {
        if (!architect.isValid())
        {
            logger.warning("Could not add architect: " + architect.getConfigKey() + " architect is missing details!");
            return false;
        }

        final String key = architect.getConfigKey();

        architects.put(key, architect);
        updateTables();

        architect.saveTo(config.createSection(key));
        config.save();

        return true;
    }

    public boolean removeArchitect(Architect architect)
    {
        if (architects.remove(architect.getConfigKey()) == null)
        {
            return false;
        }
        updateTables();

        config.set(architect.getConfigKey(), null);
        config.save();

        return true;
    }

    public void updateTables()
    {
        nameTable.clear();
        ipTable.clear();

        for (Architect architect : architects.values())
        {
            nameTable.put(architect.getName().toLowerCase(), architect);

            for (String ip : architect.getIps())
            {
                ipTable.put(ip, architect);
            }

        }
    }

    public Set<String> getArchitectNames()
    {
        return nameTable.keySet();
    }

    public Set<String> getArchitectIps()
    {
        return ipTable.keySet();
    }
}
