package com.smithb99.loggerbot.command;

import com.smithb99.loggerbot.Bot;
import com.smithb99.loggerbot.EventHandler;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;

import java.util.List;

public class CommandStart implements Command {
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        if (Bot.getActive(event.getGuild().getLongID())) {
            return;
        }

        IChannel channel = event.getChannel();
        Bot.sendMessage(channel, "LoggerBot " + EventHandler.getVersion() +
                " starting up.");

        long guildID = event.getGuild().getLongID();
        Bot.putGuild(guildID, true);

        Bot.sendMessage(channel, "LoggerBot started.");
    }
}
