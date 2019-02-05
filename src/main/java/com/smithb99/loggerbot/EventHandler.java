/* LoggerBot: Logs user join and leave events
 * Copyright (C) 2017 Braedon Smith

 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.smithb99.loggerbot;

import com.smithb99.loggerbot.command.Command;
import com.smithb99.loggerbot.command.CommandStart;
import com.smithb99.loggerbot.command.CommandStop;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.guild.member.UserJoinEvent;
import sx.blah.discord.handle.impl.events.guild.member.UserLeaveEvent;
import sx.blah.discord.handle.impl.events.shard.ShardReadyEvent;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.Permissions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventHandler {
    private static String version;
    private static Map<String, Command> commandMap = new HashMap<>();

    static {
        commandMap.put("stop", new CommandStop());
        commandMap.put("start", new CommandStart());
    }

    EventHandler(String version) {
        EventHandler.version = version;
    }

    public static String getVersion() {
        return version;
    }

    @SuppressWarnings("unused")
    @EventSubscriber
    public void onShardReady(ShardReadyEvent event) {
        List<IGuild> guilds = event.getShard().getGuilds();

        for (IGuild guild : guilds) {
            Bot.putGuildNoReplace(guild.getLongID(), false);
        }
    }

    @SuppressWarnings("unused")
    @EventSubscriber
    public void onUserLeave(UserLeaveEvent event) {
        long channel = Bot.getChannelID(event.getGuild().getLongID());

        Bot.sendMessage(event.getGuild().getChannelByID(channel),
                String.format("User %s left %s.", event.getUser().getName(),
                        event.getGuild().getName()));
    }

    @SuppressWarnings("unused")
    @EventSubscriber
    public void onUserJoin(UserJoinEvent event) {
        long channel = Bot.getChannelID(event.getGuild().getLongID());

        Bot.sendMessage(event.getGuild().getChannelByID(channel),
                String.format("User %s joined %s.", event.getUser().getName(),
                        event.getGuild().getName()));
    }

    @SuppressWarnings("unused")
    @EventSubscriber
    public void onMessageReceived(MessageReceivedEvent event) {
        String[] args = event.getMessage().getContent().split(" ");

        if (args.length != 2) {
            return;
        }

        if (!(args[0].equals(Bot.PREFIX))) {
            return;
        }

        if (!(event.getAuthor().getPermissionsForGuild(event.getGuild())
                .contains(Permissions.MANAGE_SERVER))) {
            Bot.sendMessage(event.getChannel(), "Insufficient permissions.");
            return;
        }

        String command = args[1];

        List<String> argsList = new ArrayList<>(Arrays.asList(args));

        if (commandMap.containsKey(command)) {
            commandMap.get(command).runCommand(event, argsList);
        }
    }
}
