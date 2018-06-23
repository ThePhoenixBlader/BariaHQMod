package me.bariahq.bariahqmod.discord;

import me.bariahq.bariahqmod.BariaHQMod;
import me.bariahq.bariahqmod.FreedomService;
import me.bariahq.bariahqmod.config.ConfigEntry;
import me.bariahq.bariahqmod.staff.StaffMember;
import me.bariahq.bariahqmod.util.FLog;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.MessageChannel;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Discord extends FreedomService
{
    public static HashMap<String, StaffMember> LINK_CODES = new HashMap<>();
    public static List<String> VERIFY_CODES = new ArrayList();
    public static JDA bot = null;
    public static Boolean enabled = false;

    public Discord(BariaHQMod plugin)
    {
        super(plugin);
    }

    public static void sendMessage(MessageChannel channel, String message)
    {
        channel.sendMessage(message);
    }

    public static String getCodeForAdmin(StaffMember staffMember)
    {
        for (String code : LINK_CODES.keySet())
        {
            if (LINK_CODES.get(code).equals(staffMember))
            {
                return code;
            }
        }
        return null;
    }

    public void startBot()
    {
        if (ConfigEntry.DISCORD_VERIFICATION_ENABLED.getBoolean())
        {
            if (!ConfigEntry.DISCORD_VERIFICATION_BOT_TOKEN.getString().isEmpty())
            {
                enabled = true;
            }
            else
            {
                FLog.warning("No bot token was specified in the config, discord verification bot will not enable.");
            }
        }
        if (bot != null)
        {
            for (Object o : bot.getRegisteredListeners())
            {
                bot.removeEventListener(o);
            }
        }
        try
        {
            if (enabled)
            {
                bot = new JDABuilder(AccountType.BOT).setToken(ConfigEntry.DISCORD_VERIFICATION_BOT_TOKEN.getString()).addEventListener(new MessageListener()).setAudioEnabled(false).setAutoReconnect(true).buildBlocking();
                FLog.info("Discord verification bot has successfully enabled!");
            }
        }
        catch (LoginException e)
        {
            FLog.warning("An invalid token for the discord verification bot, the bot will not enable.");
        }
        catch (IllegalArgumentException | InterruptedException e)
        {
            FLog.warning("Discord verification bot failed to start.");
        }
    }

    @Override
    protected void onStart()
    {
        startBot();
    }

    @Override
    protected void onStop()
    {
        if (bot != null)
        {
            bot.shutdown();
        }
        FLog.info("Discord verification bot has successfully shutdown.");
    }
}
