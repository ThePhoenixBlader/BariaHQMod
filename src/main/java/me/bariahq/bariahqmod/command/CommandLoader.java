package me.bariahq.bariahqmod.command;

import lombok.Getter;
import me.bariahq.bariahqmod.BariaHQMod;
import me.bariahq.bariahqmod.FreedomService;
import me.bariahq.bariahqmod.util.FLog;
import net.pravian.aero.command.handler.SimpleCommandHandler;
import org.bukkit.ChatColor;

public class CommandLoader extends FreedomService
{
    public int totalCommands;

    @Getter
    private final SimpleCommandHandler<BariaHQMod> handler;

    public CommandLoader(BariaHQMod plugin)
    {
        super(plugin);

        handler = new SimpleCommandHandler<>(plugin);
    }

    @Override
    protected void onStart()
    {
        handler.clearCommands();
        handler.setExecutorFactory(new FreedomCommandExecutor.FreedomExecutorFactory(plugin));
        handler.setCommandClassPrefix("Command_");
        handler.setPermissionMessage(ChatColor.RED + "You do not have permission to use this command.");
        handler.setOnlyConsoleMessage(ChatColor.RED + "This command can only be used from the console.");
        handler.setOnlyPlayerMessage(ChatColor.RED + "This command can only be used by players.");

        handler.loadFrom(FreedomCommand.class.getPackage());
        handler.registerAll("BariaHQMod", true);
        totalCommands = handler.getExecutors().size();

        FLog.info("Loaded " + totalCommands + " commands.");
    }

    @Override
    protected void onStop()
    {
        handler.clearCommands();
    }

}
