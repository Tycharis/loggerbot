package com.smithb99.loggerbot.command;

import com.smithb99.loggerbot.Bot;
import com.smithb99.loggerbot.EventHandler;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;

import java.util.List;

public class CommandStop implements Command {
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        if (!Bot.getActive(event.getGuild().getLongID())) {
            return;
        }

        IChannel channel = event.getChannel();
        Bot.sendMessage(channel, "LoggerBot " +
                EventHandler.getVersion() + " shutting down.");

        long guildID = event.getGuild().getLongID();
        Bot.putGuild(guildID, false);

        Bot.sendMessage(channel, "LoggerBot shut down.");
    }
}
