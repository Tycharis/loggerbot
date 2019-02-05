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

import com.smithb99.loggerbot.process.CommandProcessor;
import sx.blah.discord.api.IDiscordClient;

import java.util.Map;

public class LoggerBot {
    public static IDiscordClient client;
    private static boolean active;

    public static void main(String[] args) {
        ConfigurationManager configManager = new ConfigurationManager();
        Map<String, String> properties = configManager.getResults();

        String version = properties.get("version");
        String token = properties.get("token");

        System.out.println(version);

        client = Bot.getClient(token);
        client.getDispatcher().registerListener(
                new EventHandler(version));

        Bot.deserializeOrEmpty();

        client.login();

        setActive(true);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            client.logout();
            e.printStackTrace();
            return;
        }
        System.out.println("LoggerBot " + version +
                ": Type 'help' to get started.\n");

        while (active) {
            System.out.print("loggerbot-" + version + "$ ");
            new Thread(new CommandProcessor()).run();
        }
    }

    public static void setActive(boolean active) {
        LoggerBot.active = active;
    }
}
