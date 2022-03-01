/*
 *       _____  _       _    _____                                _
 *      |  __ \| |     | |  / ____|                              | |
 *      | |__) | | ___ | |_| (___   __ _ _   _  __ _ _ __ ___  __| |
 *      |  ___/| |/ _ \| __|\___ \ / _` | | | |/ _` | '__/ _ \/ _` |
 *      | |    | | (_) | |_ ____) | (_| | |_| | (_| | | |  __/ (_| |
 *      |_|    |_|\___/ \__|_____/ \__, |\__,_|\__,_|_|  \___|\__,_|
 *                                    | |
 *                                    |_|
 *            PlotSquared plot management system for Minecraft
 *               Copyright (C) 2014 - 2022 IntellectualSites
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package me.totalfreedom.plotsquared;

import com.google.common.base.Function;
import com.plotsquared.core.command.CommandCaller;
import com.plotsquared.core.permissions.PermissionHandler;
import com.plotsquared.core.permissions.PermissionHolder;
import com.plotsquared.core.player.PlotPlayer;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import me.totalfreedom.totalfreedommod.TotalFreedomMod;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class PlotSquaredHandler
{
    public static final boolean DEBUG = true;
    public static final Logger LOGGER = Bukkit.getPluginManager().getPlugin("PlotSquared").getLogger();
    private static Function<Player, Boolean> adminProvider;

    public boolean isAdmin(PlotPlayer<?> plotPlayer)
    {
        final Player player = getPlayer(plotPlayer);
        if (player == null)
        {
            return false;
        }
        return isAdmin(player);
    }

    public boolean isAdmin(PermissionHolder holder)
    {
        if (!(holder instanceof PlotPlayer<?> plotPlayer)) return true;
        final Player player = getPlayer(plotPlayer);
        if (player == null)
        {
            return false;
        }
        return isAdmin(player);
    }

    @SuppressWarnings("unchecked")
    public boolean isAdmin(Player player)
    {
        TotalFreedomMod tfm = getTFM();
        return tfm.al.isAdmin(player);
    }

    public static Player getPlayer(PlotPlayer<?> plotPlayer)
    {
        return Bukkit.getPlayer(plotPlayer.getUUID());
    }

    public boolean hasTFMPermission(PermissionHolder holder, String permission)
    {
        if (holder instanceof PlotPlayer<?> player)
        {
            return hasTFMPermission(player, permission);
        }
        else
        {
            // Console?
            return true;
        }
    }

    public boolean hasTFMPermission(PlotPlayer<?> player, String permission)
    {
        List<String> adminOnlyPermissions = Arrays.asList(
                "plots.worldedit.bypass", "plots.area", "plots.grant.add", "plots.debugallowunsafe", "plots.debugroadgen", "plots.debugpaste",
                "plots.createroadschematic", "plots.merge", "plots.unlink", "plots.area", "plots.setup", "plots.set.flag.other", "plots.reload",
                "plots.backup", "plots.debug");
        if (!isAdmin(player))
        {
            return !permission.startsWith("plots.admin") && !adminOnlyPermissions.contains(permission);
        }
        return true;
    }

    public static Player getPlayer(String match)
    {
        match = match.toLowerCase();

        Player found = null;
        int delta = Integer.MAX_VALUE;
        for (Player player : Bukkit.getOnlinePlayers())
        {
            if (player.getName().toLowerCase().startsWith(match))
            {
                int curDelta = player.getName().length() - match.length();
                if (curDelta < delta)
                {
                    found = player;
                    delta = curDelta;
                }
                if (curDelta == 0)
                {
                    break;
                }
            }
        }

        for (Player player : Bukkit.getOnlinePlayers())
        {
            if (player.getName().toLowerCase().contains(match))
            {
                return player;
            }
        }
        return found;
    }

    public static TotalFreedomMod getTFM()
    {
        final Plugin tfm = Bukkit.getPluginManager().getPlugin("TotalFreedomMod");
        if (tfm == null)
        {
            LOGGER.warning("Could not resolve plugin: TotalFreedomMod");
        }

        return (TotalFreedomMod)tfm;
    }

    public void debug(String debug)
    {
        if (DEBUG)
        {
            info(debug);
        }
    }

    public void warning(String warning)
    {
        LOGGER.warning(warning);
    }

    public void info(String info)
    {
        LOGGER.info(info);
    }
}
