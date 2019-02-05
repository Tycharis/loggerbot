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

package com.smithb99.loggerbot.process;

import com.smithb99.loggerbot.Bot;
import com.smithb99.loggerbot.LoggerBot;

import java.util.List;

public class CommandShutdown implements TextCommand {
    public void runCommand(List<String> args) {
        LoggerBot.setActive(false);

        System.out.println("Shutting down...");

        Bot.serialize();

        LoggerBot.client.logout();

        System.out.println("Shutdown complete.");
    }
}
