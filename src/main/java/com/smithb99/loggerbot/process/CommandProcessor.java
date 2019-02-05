package com.smithb99.loggerbot.process;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class CommandProcessor implements Runnable {
    private static Map<String, TextCommand> textCommands = new HashMap<>();

    static {
        textCommands.put("shutdown", new CommandShutdown());
        textCommands.put("help", new CommandHelp());
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);

        String input = scanner.nextLine();

        List<String> args = Arrays.asList(input.split(" "));
        String alias = args.get(0);
        //args.remove(alias);

        if (textCommands.containsKey(alias)) {
            textCommands.get(alias).runCommand(args);
        } else {
            System.out.println("Unknown command. Type 'help' for help.");
        }
    }
}
