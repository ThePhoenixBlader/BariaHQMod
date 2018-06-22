package me.bariahq.bariahqmod;

import me.bariahq.bariahqmod.fun.Trailer;
import java.io.File;
import me.bariahq.bariahqmod.staff.StaffList;
import me.bariahq.bariahqmod.banning.BanManager;
import me.bariahq.bariahqmod.banning.PermbanList;
import me.bariahq.bariahqmod.blocking.BlockBlocker;
import me.bariahq.bariahqmod.blocking.DisguiseBlocker;
import me.bariahq.bariahqmod.blocking.EventBlocker;
import me.bariahq.bariahqmod.blocking.InteractBlocker;
import me.bariahq.bariahqmod.blocking.MobBlocker;
import me.bariahq.bariahqmod.blocking.PotionBlocker;
import me.bariahq.bariahqmod.blocking.command.CommandBlocker;
import me.bariahq.bariahqmod.bridge.BukkitTelnetBridge;
import me.bariahq.bariahqmod.bridge.CoreProtectBridge;
import me.bariahq.bariahqmod.bridge.EssentialsBridge;
import me.bariahq.bariahqmod.bridge.LibsDisguisesBridge;
import me.bariahq.bariahqmod.bridge.WorldEditBridge;
import me.bariahq.bariahqmod.caging.Cager;
import me.bariahq.bariahqmod.command.CommandLoader;
import me.bariahq.bariahqmod.config.MainConfig;
import me.bariahq.bariahqmod.discord.Discord;
import me.bariahq.bariahqmod.freeze.Freezer;
import me.bariahq.bariahqmod.fun.ItemFun;
import me.bariahq.bariahqmod.fun.Jumppads;
import me.bariahq.bariahqmod.fun.Landminer;
import me.bariahq.bariahqmod.fun.Lightning;
import me.bariahq.bariahqmod.fun.CrescentRose;
import me.bariahq.bariahqmod.fun.MP44;
import me.bariahq.bariahqmod.fun.Minigun;
import me.bariahq.bariahqmod.httpd.HTTPDaemon;
import me.bariahq.bariahqmod.leveling.LevelManager;
import me.bariahq.bariahqmod.player.PlayerList;
import me.bariahq.bariahqmod.rank.RankManager;
import me.bariahq.bariahqmod.shop.Shop;
import me.bariahq.bariahqmod.shop.ShopGUIListener;
import me.bariahq.bariahqmod.util.FLog;
import me.bariahq.bariahqmod.util.FUtil;
import me.bariahq.bariahqmod.util.MethodTimer;
import me.bariahq.bariahqmod.world.WorldManager;
import net.pravian.aero.component.service.ServiceManager;
import net.pravian.aero.plugin.AeroPlugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import java.io.InputStream;
import java.util.Properties;
import me.bariahq.bariahqmod.donator.DonatorList;


public class BariaHQMod extends AeroPlugin<BariaHQMod>
{

    public static final String CONFIG_FILENAME = "config.yml";
    //
    public static String pluginName;
    public static String pluginVersion;
    //
    public static final BuildProperties build = new BuildProperties();
    //
    public MainConfig config;
    //
    // Services
    public ServiceManager<BariaHQMod> services;
    public ServerInterface si;
    public SavedFlags sf;
    public WorldManager wm;
    public StaffList al;
    public RankManager rm;
    public CommandLoader cl;
    public CommandBlocker cb;
    public EventBlocker eb;
    public BlockBlocker bb;
    public DisguiseBlocker db;
    public MobBlocker mb;
    public InteractBlocker ib;
    public PotionBlocker pb;
    public LoginProcess lp;
    public AntiNuke nu;
    public AntiSpam as;
    public AntiSpamBot asb;
    public PlayerList pl;
    public Shop sh;
    public ShopGUIListener sl;
    public Announcer an;
    public ChatManager cm;
    public Data da;
    public Discord dc;
    public BanManager bm;
    public PermbanList pm;
    public ProtectArea pa;
    public ServiceChecker sc;
    public GameRuleHandler gr;
    public CommandSpy cs;
    public Cager ca;
    public Freezer fm;
    public Orbiter or;
    public Muter mu;
    public Fuckoff fo;
    public AutoKick ak;
    public MovementValidator mv;
    public EntityWiper ew;
    public ServerPing sp;
    public ItemFun it;
    public Landminer lm;
    public MP44 mp;
    public Minigun mg;
    public Jumppads jp;
    public Trailer tr;
    public HTTPDaemon hd;
    public Lightning ln;
    public CrescentRose cr;
    public LevelManager lvm;
    public DonatorList dl;
    //
    // Bridges
    public ServiceManager<BariaHQMod> bridges;
    public BukkitTelnetBridge btb;
    public CoreProtectBridge cpb;
    public EssentialsBridge esb;
    public LibsDisguisesBridge ldb;
    public WorldEditBridge web;

    @Override
    public void load()
    {
        BariaHQMod.pluginName = plugin.getDescription().getName();

        FLog.setPluginLogger(plugin.getLogger());
        FLog.setServerLogger(server.getLogger());

        build.load(plugin);
    }

    @Override
    public void enable()
    {
        FLog.info("Created by Madgeek1450 and Prozza");
        FLog.info("Modified by ZeroEpoch1969");
        FLog.info("Version " + build.version);
        FLog.info("Compiled " + build.date + " by " + build.author);

        final MethodTimer timer = new MethodTimer();
        timer.start();

        // Warn if we're running on a wrong version
        ServerInterface.warnVersion();

        // Delete unused files
        FUtil.deleteCoreDumps();
        FUtil.deleteFolder(new File("./_deleteme"));

        // Convert old config files
        new ConfigConverter(plugin).convert();

        BackupManager backups = new BackupManager(this);
        backups.createBackups(BariaHQMod.CONFIG_FILENAME, true);
        backups.createBackups(StaffList.CONFIG_FILENAME);
        backups.createBackups(DonatorList.CONFIG_FILENAME);
        backups.createBackups(PermbanList.CONFIG_FILENAME);

        config = new MainConfig(this);
        config.load();

        // Start services
        services = new ServiceManager<>(plugin);
        si = services.registerService(ServerInterface.class);
        sf = services.registerService(SavedFlags.class);
        wm = services.registerService(WorldManager.class);
        al = services.registerService(StaffList.class);
        rm = services.registerService(RankManager.class);
        dl = services.registerService(DonatorList.class);
        lvm = services.registerService(LevelManager.class);
        cl = services.registerService(CommandLoader.class);
        cb = services.registerService(CommandBlocker.class);
        eb = services.registerService(EventBlocker.class);
        bb = services.registerService(BlockBlocker.class);
        db = services.registerService(DisguiseBlocker.class);
        mb = services.registerService(MobBlocker.class);
        ib = services.registerService(InteractBlocker.class);
        pb = services.registerService(PotionBlocker.class);
        lp = services.registerService(LoginProcess.class);
        nu = services.registerService(AntiNuke.class);
        as = services.registerService(AntiSpam.class);
        asb = services.registerService(AntiSpamBot.class);

        pl = services.registerService(PlayerList.class);
        sh = services.registerService(Shop.class);
        sl = services.registerService(ShopGUIListener.class);
        an = services.registerService(Announcer.class);
        cm = services.registerService(ChatManager.class);
        da = services.registerService(Data.class);
        dc = services.registerService(Discord.class);
        bm = services.registerService(BanManager.class);
        pm = services.registerService(PermbanList.class);
        pa = services.registerService(ProtectArea.class);
        sc = services.registerService(ServiceChecker.class);
        gr = services.registerService(GameRuleHandler.class);

        // Single admin utils
        cs = services.registerService(CommandSpy.class);
        ca = services.registerService(Cager.class);
        fm = services.registerService(Freezer.class);
        or = services.registerService(Orbiter.class);
        mu = services.registerService(Muter.class);
        fo = services.registerService(Fuckoff.class);
        ak = services.registerService(AutoKick.class);

        mv = services.registerService(MovementValidator.class);
        ew = services.registerService(EntityWiper.class);
        sp = services.registerService(ServerPing.class);

        // Fun
        it = services.registerService(ItemFun.class);
        lm = services.registerService(Landminer.class);
        ln = services.registerService(Lightning.class);
        cr = services.registerService(CrescentRose.class);
        mp = services.registerService(MP44.class);
        mg = services.registerService(Minigun.class);
        jp = services.registerService(Jumppads.class);
        tr = services.registerService(Trailer.class);

        // HTTPD
        hd = services.registerService(HTTPDaemon.class);
        services.start();

        // Start bridges
        bridges = new ServiceManager<>(plugin);
        btb = bridges.registerService(BukkitTelnetBridge.class);
        cpb = bridges.registerService(CoreProtectBridge.class);
        esb = bridges.registerService(EssentialsBridge.class);
        ldb = bridges.registerService(LibsDisguisesBridge.class);
        web = bridges.registerService(WorldEditBridge.class);
        bridges.start();

        timer.update();
        FLog.info("Version " + pluginVersion + " for " + ServerInterface.COMPILE_NMS_VERSION + " enabled in " + timer.getTotal() + "ms");

        // Add spawnpoints later - https://github.com/TotalFreedom/TotalFreedomMod/issues/438
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                plugin.pa.autoAddSpawnpoints();
            }
        }.runTaskLater(plugin, 60L);
    }

    @Override
    public void disable()
    {
        // Stop services and bridges
        bridges.stop();
        services.stop();

        server.getScheduler().cancelTasks(plugin);

        FLog.info("Plugin disabled");
    }

    public static BariaHQMod plugin()
    {
        for (Plugin plugin : Bukkit.getPluginManager().getPlugins())
        {
            if (plugin.getName().equalsIgnoreCase(pluginName))
            {
                return (BariaHQMod) plugin;
            }
        }
        return null;
    }

    public static class BuildProperties
    {

        public String author;
        public String codename;
        public String version;
        public String number;
        public String date;
        public String head;

        public void load(BariaHQMod plugin)
        {
            try
            {
                final Properties props;
                final Properties gitprops;
                try (InputStream in = plugin.getResource("build.properties"))
                {
                    props = new Properties();
                    props.load(in);
                }
                try (InputStream in = plugin.getResource("git.properties"))
                {
                    gitprops = new Properties();
                    gitprops.load(in);
                }

                author = props.getProperty("buildAuthor", "unknown");
                codename = props.getProperty("buildCodeName", "unknown");
                version = props.getProperty("buildVersion", pluginVersion);
                number = props.getProperty("buildNumber", "1");
                date = gitprops.getProperty("git.build.time", "unknown");
                head = gitprops.getProperty("git.commit.id.abbrev", "unknown");
            }
            catch (Exception ex)
            {
                FLog.severe("Could not load build properties!");
                FLog.severe(ex);
            }
        }

        public String formattedVersion()
        {
            return pluginVersion + "." + number + " (" + head + ")";
        }
    }

}
