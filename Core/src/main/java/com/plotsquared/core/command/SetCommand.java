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
package com.plotsquared.core.command;

import com.plotsquared.core.configuration.caption.TranslatableCaption;
import com.plotsquared.core.location.Location;
import com.plotsquared.core.permissions.Permission;
import com.plotsquared.core.player.PlotPlayer;
import com.plotsquared.core.plot.Plot;
import com.plotsquared.core.util.Permissions;
import com.plotsquared.core.util.StringMan;
import net.kyori.adventure.text.minimessage.Template;

public abstract class SetCommand extends SubCommand {

    @Override
    public boolean onCommand(PlotPlayer<?> player, String[] args) {
        Location location = player.getLocation();
        Plot plot = location.getPlotAbs();
        if (plot == null) {
            player.sendMessage(TranslatableCaption.of("errors.not_in_plot"));
            return false;
        }
        if (!plot.hasOwner()) {
            if (!Permissions.hasPermission(player, Permission.PERMISSION_ADMIN_COMMAND.format(getFullId()))) {
                player.sendMessage(
                        TranslatableCaption.of("permission.no_permission"),
                        Template.of("node", Permission.PERMISSION_ADMIN_COMMAND.format(getFullId()))
                );
                player.sendMessage(TranslatableCaption.of("working.plot_not_claimed"));
                return false;
            }
        }
        if (!plot.isOwner(player.getUUID())) {
            if (!Permissions.hasPermission(player, Permission.PERMISSION_ADMIN_COMMAND.format(getFullId()))) {
                player.sendMessage(
                        TranslatableCaption.of("permission.no_permission"),
                        Template.of("node", Permission.PERMISSION_ADMIN_COMMAND.format(getFullId()))
                );
                player.sendMessage(TranslatableCaption.of("permission.no_plot_perms"));
                return false;
            }
        }
        if (args.length == 0) {
            return set(player, plot, "");
        }
        return set(player, plot, StringMan.join(args, " "));
    }

    public abstract boolean set(PlotPlayer<?> player, Plot plot, String value);

}
