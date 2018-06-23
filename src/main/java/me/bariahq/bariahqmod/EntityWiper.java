package me.bariahq.bariahqmod;

import me.bariahq.bariahqmod.config.ConfigEntry;
import me.bariahq.bariahqmod.util.FUtil;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EntityWiper extends FreedomService
{

    private static final long WIPE_RATE = 5 * 20L;
    //
    private final List<Class<? extends Entity>> wipables = new ArrayList<>();
    //
    private BukkitTask wipeTask;

    public EntityWiper(BariaHQMod plugin)
    {
        super(plugin);
        wipables.add(EnderCrystal.class);
        wipables.add(EnderSignal.class);
        wipables.add(ExperienceOrb.class);
        wipables.add(Projectile.class);
        wipables.add(FallingBlock.class);
        wipables.add(Firework.class);
        wipables.add(Item.class);
        wipables.add(ThrownPotion.class);
        wipables.add(ThrownExpBottle.class);
        wipables.add(AreaEffectCloud.class);
        wipables.add(Egg.class);
        wipables.add(Snowball.class);
        wipables.add(Fireball.class);
        wipables.add(DragonFireball.class);
        wipables.add(LargeFireball.class);
    }

    @Override
    protected void onStart()
    {
        if (!ConfigEntry.AUTO_ENTITY_WIPE.getBoolean())
        {
            return;
        }

        wipeTask = new BukkitRunnable()
        {

            @Override
            public void run()
            {
                wipeEntities(!ConfigEntry.ALLOW_EXPLOSIONS.getBoolean(), false);
            }
        }.runTaskTimer(plugin, WIPE_RATE, WIPE_RATE);

    }

    @Override
    protected void onStop()
    {
        FUtil.cancel(wipeTask);
        wipeTask = null;
    }

    public boolean canWipe(Entity entity, boolean wipeExplosives, boolean wipeVehicles)
    {
        if (wipeExplosives)
        {
            if (Explosive.class.isAssignableFrom(entity.getClass()))
            {
                return true;
            }
        }

        if (wipeVehicles)
        {
            if (Boat.class.isAssignableFrom(entity.getClass()))
            {
                return true;
            }
            else if (Minecart.class.isAssignableFrom(entity.getClass()))
            {
                return true;
            }
        }

        Iterator<Class<? extends Entity>> it = wipables.iterator();
        while (it.hasNext())
        {
            if (it.next().isAssignableFrom(entity.getClass()))
            {
                return true;
            }
        }

        return false;
    }

    public int wipeEntities(boolean wipeExplosives, boolean wipeVehicles)
    {
        int removed = 0;

        Iterator<World> worlds = Bukkit.getWorlds().iterator();
        while (worlds.hasNext())
        {
            Iterator<Entity> entities = worlds.next().getEntities().iterator();
            while (entities.hasNext())
            {
                Entity entity = entities.next();
                if (canWipe(entity, wipeExplosives, wipeVehicles))
                {
                    entity.remove();
                    removed++;
                }
            }
        }

        return removed;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onItemDrop(PlayerDropItemEvent event)
    {
        if (!ConfigEntry.AUTO_ENTITY_WIPE.getBoolean())
        {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onContainerBreak(BlockBreakEvent event)
    {
        if (!ConfigEntry.AUTO_ENTITY_WIPE.getBoolean())
        {
            return;
        }

        BlockState state = event.getBlock().getState();
        if (!(state instanceof InventoryHolder))
        {
            return;
        }

        Inventory inv = ((InventoryHolder) state).getInventory();
        inv.clear();
    }

}
