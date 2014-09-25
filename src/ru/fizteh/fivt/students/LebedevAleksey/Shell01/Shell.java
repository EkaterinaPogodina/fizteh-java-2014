package ru.fizteh.fivt.students.LebedevAleksey.Shell01;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.List;

public class Shell extends CommandParser {
    String getCurrentFolder() {
        return System.getProperty("user.dir");
    }

    @Override
    protected boolean invokeCommands(List<ParsedCommand> commands) throws ParserException {
        for (ParsedCommand command : commands) {
            if (command.getCommandName() != null) {
                //command.getCommandName()!=null -> empty command - ignored
                CommandNames commandType = CommandNames.getCommand(command.getCommandName());
                Command action;
                switch (commandType) {
                    case CMD_LS:
                        action = new CommandLs(command.getArguments());
                        break;
                    case CMD_PWD:
                        action = new CommandPwd(command.getArguments());
                        break;
                    case CMD_EXIT:
                        return exit();
                    case CMD_CD:
                        action = new CommandCd(command.getArguments());
                        break;
                    case CMD_CAT:
                        action = new CommandCat(command.getArguments());
                        break;
                    case CMD_MKDIR:
                        action = new CommandMkdir(command.getArguments());
                        break;
                    default:
                        throw new CannotParseCommandException("Unknown command");
                }
                action.invoke();
            }
        }
        return true;
    }

    private enum CommandNames {
        CMD_LS("ls"),
        CMD_PWD("pwd"),
        CMD_CD("cd"),
        CMD_CAT("cat"),
        CMD_MKDIR("mkdir"),
        CMD_EXIT("exit");
        private String typeValue;

        private CommandNames(String type) {
            typeValue = type;
        }

        public static CommandNames getCommand(String pType) throws CannotParseCommandException {
            for (CommandNames type : CommandNames.values()) {
                if (type.getName().equals(pType)) {
                    return type;
                }
            }
            throw new CannotParseCommandException("Unknown command");
        }

        public String getName() {
            return typeValue;
        }

    }

    private abstract class Command {
        protected static final String FILE_NOT_FOUND_MESSAGE = "': No such file or directory";
        protected static final String ACCESS_DENIED_ERROR = "': Can't open file or directory";
        private String[] arguments;

        protected Command(String[] args) {
            arguments = args;
        }

        protected Command() {
            arguments = null;
        }

        abstract void invoke() throws CommandInvokeException;

        protected String getFirstArgument() throws CommandInvokeException {
            if (arguments.length == 1) {
                return arguments[0];
            } else {
                throw new CommandInvokeException("Wrong arguments!", CommandNames.CMD_CD.getName());
            }
        }

        protected void assertNoArgs(String[] args) throws CommandInvokeException {
            if (args.length > 0) {
                throw new CommandInvokeException("This command have no arguments.", CommandNames.CMD_PWD.getName());
            }
        }
    }

    private class CommandLs extends Command {
        CommandLs(String[] args) throws CommandInvokeException {
            super(args);
            assertNoArgs(args);
        }

        @Override
        void invoke() throws CommandInvokeException {
            File[] files = new File(getCurrentFolder()).listFiles();
            if (files == null) {
                throw new CommandInvokeException("Can't found current directory.", CommandNames.CMD_LS.getName());
            } else {
                for (File file : files) {
                    System.out.println(file.getName());
                }
            }
        }
    }

    private class CommandCd extends Command {
        CommandCd(String[] args) {
            super(args);
        }

        @Override
        void invoke() throws CommandInvokeException {
            String arg = getFirstArgument();
            String currentPath = getCurrentFolder();
            try {
                Path path = new File(currentPath).toPath();
                File newPath = path.resolve(arg).toAbsolutePath().toFile();
                if (newPath.exists() && newPath.isDirectory()) {
                    System.setProperty("user.dir", newPath.getCanonicalPath());
                } else {
                    throw new CommandInvokeException("'" + arg + FILE_NOT_FOUND_MESSAGE,
                            CommandNames.CMD_CD.getName());
                }
            } catch (SecurityException | IOError ex) {
                throw new CommandInvokeException("'" + arg + ACCESS_DENIED_ERROR, CommandNames.CMD_CD.getName(), ex);
            } catch (InvalidPathException | UnsupportedOperationException | IOException ex) {
                throw new CommandInvokeException("'" + arg + FILE_NOT_FOUND_MESSAGE, CommandNames.CMD_CD.getName(), ex);
            }

        }
    }

    private class CommandCat extends Command {
        CommandCat(String[] args) {
            super(args);
        }

        @Override
        void invoke() throws CommandInvokeException {
            String filename = getFirstArgument();
            try (FileInputStream stream = new FileInputStream(filename)) {
                try (InputStreamReader reader = new InputStreamReader(stream)) {
                    boolean canRead = true;
                    while (canRead) {
                        int symbol = reader.read();
                        if (symbol < 0) {
                            canRead = false;
                        } else {
                            System.out.print((char) symbol);
                        }
                    }
                    System.out.println();
                }
            } catch (Throwable ex) {
                throw new CommandInvokeException("'" + filename + FILE_NOT_FOUND_MESSAGE,
                        CommandNames.CMD_CAT.getName());
            }
        }
    }

    private class CommandMkdir extends Command {
        CommandMkdir(String[] args) {
            super(args);
        }

        @Override
        void invoke() throws CommandInvokeException {
            String filename = getFirstArgument();
            try {
                Files.createDirectory(new File(getCurrentFolder()).toPath().resolve(filename));
            } catch (Throwable ex) {
                throw new CommandInvokeException("'" + filename + FILE_NOT_FOUND_MESSAGE,
                        CommandNames.CMD_CAT.getName());
            }
        }
    }

    private class CommandPwd extends Command {
        CommandPwd(String[] args) throws CommandInvokeException {
            super(args);
            assertNoArgs(args);
        }

        @Override
        void invoke() throws CommandInvokeException {
            System.out.println(getCurrentFolder());
        }
    }
}
