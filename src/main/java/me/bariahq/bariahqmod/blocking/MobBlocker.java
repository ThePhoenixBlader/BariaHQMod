package me.bariahq.bariahqmod.blocking;

import me.bariahq.bariahqmod.BariaHQMod;
import me.bariahq.bariahqmod.FreedomService;
import me.bariahq.bariahqmod.config.ConfigEntry;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntitySpawnEvent;

public class MobBlocker extends FreedomService
{

    public MobBlocker(BariaHQMod plugin)
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

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntitySpawn(EntitySpawnEvent event)
    {
        if (!(event instanceof LivingEntity))
        {
            return;
        }

        Entity e = event.getEntity();
        if (e instanceof Attributable)
        {
            if (((Attributable) e).getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue() > 255.0)
            {
                ((Attributable) e).getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(255.0);
            }
            if (((Attributable) e).getAttribute(Attribute.GENERIC_FOLLOW_RANGE).getBaseValue() > 255.0)
            {
                ((Attributable) e).getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(255.0);
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onCreatureSpawn(CreatureSpawnEvent event)
    {
        if (!ConfigEntry.MOB_LIMITER_ENABLED.getBoolean())
        {
            return;
        }

        final Entity spawned = event.getEntity();
        if (spawned instanceof EnderDragon)
        {
            if (ConfigEntry.MOB_LIMITER_DISABLE_DRAGON.getBoolean())
            {
                event.setCancelled(true);
                return;
            }
        }
        else if (spawned instanceof Ghast)
        {
            if (ConfigEntry.MOB_LIMITER_DISABLE_GHAST.getBoolean())
            {
                event.setCancelled(true);
                return;
            }
        }
        else if (spawned instanceof Slime)
        {
            if (ConfigEntry.MOB_LIMITER_DISABLE_SLIME.getBoolean())
            {
                event.setCancelled(true);
                return;
            }
        }
        else if (spawned instanceof Giant)
        {
            if (ConfigEntry.MOB_LIMITER_DISABLE_GIANT.getBoolean())
            {
                event.setCancelled(true);
                return;
            }
        }
        else if (spawned instanceof Bat)
        {
            event.setCancelled(true);
            return;
        }

        int mobLimiterMax = ConfigEntry.MOB_LIMITER_MAX.getInteger();

        if (mobLimiterMax <= 0)
        {
            return;
        }

        int mobcount = 0;
        for (Entity entity : event.getLocation().getWorld().getLivingEntities())
        {
            if (!(entity instanceof HumanEntity))
            {
                mobcount++;
            }
        }

        if (mobcount > mobLimiterMax)
        {
            event.setCancelled(true);
        }
    }

}
