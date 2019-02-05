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

import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.RequestBuffer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

public class Bot {
    @SuppressWarnings("unchecked")
    private static Map<Long, Boolean> activeList = null;

    @SuppressWarnings("unchecked")
    private static Map<Long, Long> channelList = null;

    static String PREFIX = getPrefix();

    static IDiscordClient getClient(String token) {
        return new ClientBuilder()
                .withToken(token)
                .withRecommendedShardCount()
                .build();
    }

    public static void sendMessage(IChannel channel, String message) {
        RequestBuffer.request(() -> {
            try {
                channel.sendMessage(message);
            } catch (DiscordException e) {
                System.err.println("Message could not be sent: ");
                e.printStackTrace();
            }
        });
    }

    public static void serialize() {
        try {
            ConfigurationManager manager = new ConfigurationManager();
            Map<String, String> results = manager.getResults();

            String activeListName = results.get("activelistfile");
            String channelListName = results.get("channellistname");

            File activeListFile = new File(new File(LoggerBot.class
                    .getProtectionDomain().getCodeSource().getLocation()
                    .toURI()).getParentFile().getPath() + File.separator +
                    activeListName);

            File channelListFile = new File(new File(LoggerBot.class
                    .getProtectionDomain().getCodeSource().getLocation()
                    .toURI()).getParentFile().getPath() + File.separator +
                    channelListName);

            ObjectOutputStream activeListStream = new ObjectOutputStream(new FileOutputStream(activeListFile));
            activeListStream.writeObject(activeList);
            activeListStream.close();

            ObjectOutputStream channelListStream = new ObjectOutputStream(new FileOutputStream(channelListFile));
            channelListStream.writeObject(channelList);
            channelListStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    static void deserializeOrEmpty() {
        try {
            ConfigurationManager manager = new ConfigurationManager();
            Map<String, String> results = manager.getResults();

            String activeListName = results.get("activelistname");
            String channelListName = results.get("channellistname");

            File activeListFile = new File(new File(LoggerBot.class
                    .getProtectionDomain().getCodeSource().getLocation()
                    .toURI()).getParentFile().getPath() + File.separator +
                    activeListName);

            File channelListFile = new File(new File(LoggerBot.class
                    .getProtectionDomain().getCodeSource().getLocation()
                    .toURI()).getParentFile().getPath() + File.separator +
                    channelListName);

            ObjectInputStream activeListStream = new ObjectInputStream(new FileInputStream(activeListFile));
            Object activeMap = activeListStream.readObject();
            activeListStream.close();

            ObjectInputStream channelListStream = new ObjectInputStream(new FileInputStream(channelListFile));
            Object channelMap = channelListStream.readObject();
            channelListStream.close();

            activeList = (HashMap<Long, Boolean>) activeMap;
            channelList = (HashMap<Long, Long>) channelMap;

            return;
        } catch (Exception e) {
            e.printStackTrace();
        }

        activeList = new HashMap<>();
        channelList = new HashMap<>();
    }

    private static String getPrefix() {
        ConfigurationManager configManager = new ConfigurationManager();
        Map<String, String> results = configManager.getResults();

        return results.get("prefix");
    }

    public static void putGuild(long id, boolean active) {
        if (activeList.containsKey(id)) {
            activeList.remove(id);
        }

        activeList.put(id, active);
    }

    public static void putGuildNoReplace(long id, boolean active) {
        if (activeList.containsKey(id)) {
            return;
        }

        activeList.put(id, active);
    }

    public static long getChannelID(long guildID) {
        return channelList.get(guildID);
    }

    public static boolean getActive(long id) {
        return activeList.get(id);
    }
}
