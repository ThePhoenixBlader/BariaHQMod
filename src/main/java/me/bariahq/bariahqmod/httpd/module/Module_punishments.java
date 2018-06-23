package me.bariahq.bariahqmod.httpd.module;

import me.bariahq.bariahqmod.BariaHQMod;
import me.bariahq.bariahqmod.httpd.HTTPDaemon;
import me.bariahq.bariahqmod.httpd.NanoHTTPD;
import me.bariahq.bariahqmod.punishment.PunishmentList;
import me.bariahq.bariahqmod.staff.StaffMember;

import java.io.File;

public class Module_punishments extends HTTPDModule
{

    public Module_punishments(BariaHQMod plugin, NanoHTTPD.HTTPSession session)
    {
        super(plugin, session);
    }

    @Override
    public NanoHTTPD.Response getResponse()
    {
        File punishmentFile = new File(plugin.getDataFolder(), PunishmentList.CONFIG_FILENAME);

        if (punishmentFile.exists())
        {
            final String remoteAddress = socket.getInetAddress().getHostAddress();
            if (!isAuthorized(remoteAddress))
            {
                return new NanoHTTPD.Response(NanoHTTPD.Response.Status.NOT_FOUND, NanoHTTPD.MIME_PLAINTEXT,
                        "You may not view the punishment list, Your IP, " + remoteAddress + ", is not registered to an admin on the server.");
            }
            else
            {
                return HTTPDaemon.serveFileBasic(new File(plugin.getDataFolder(), PunishmentList.CONFIG_FILENAME));
            }
        }
        else
        {
            return new NanoHTTPD.Response(NanoHTTPD.Response.Status.NOT_FOUND, NanoHTTPD.MIME_PLAINTEXT,
                    "Error 404: Not Found - The requested resource was not found on this server.");
        }
    }

    private boolean isAuthorized(String address)
    {
        StaffMember entry = plugin.al.getEntryByIp(address);
        return entry != null && entry.isActive();
    }
}
