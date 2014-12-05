package ru.fizteh.fivt.students.Gotovchic;

import java.util.Map;

public class BatchMode extends Command {
    void batchModeFunction(String commands, Map<String, String> storage, String[] cmdBuffer) throws Exception {
        String tableName = new String();
        commands = commands.replaceAll("\\s+", " ");
        String [] commandsSeq = commands.split(";");
        for (int ind = 0; ind < commandsSeq.length; ++ind) {
            commandsSeq[ind] = commandsSeq[ind].replaceAll("\\W", " ");
            commandsSeq[ind] = commandsSeq[ind].replaceAll("\\s+", " ");
        }
        for (Integer bufInd = 0; bufInd < commandsSeq.length; ++bufInd) {
            if (commandsSeq[bufInd].equals("\\s+")) {
                Integer look = 0;
                for (int tmpInd = 0; tmpInd < commandsSeq.length; ++tmpInd) {
                    if (!commandsSeq[tmpInd].equals("s\\+")) {
                        commandsSeq[look] = commandsSeq[tmpInd];
                    }
                    ++look;
                }
                break;
            }
        }
        Integer parseInd = -1;
        while (parseInd < commandsSeq.length) {
            ++parseInd;
            if (parseInd >= commandsSeq.length) {
                break;
            }
            cmdBuffer = commandsSeq[parseInd].split(" ");
            if (cmdBuffer.length == 0) {
                continue;
            }
            for (Integer bufInd = 0; bufInd < cmdBuffer.length; ++bufInd) {
                cmdBuffer[bufInd] = cmdBuffer[bufInd].replaceAll("\\s+", "");
            }
            if (cmdBuffer[0].equals("") && cmdBuffer.length > 1) {
                String temp = new String();
                for (Integer k = 1; k < cmdBuffer.length; ++k) {
                    temp += cmdBuffer[k];
                    if (k != cmdBuffer.length - 1) {
                        temp += " ";
                    }
                }
                cmdBuffer = temp.split(" ");
            }
            if (cmdBuffer[0].equals("put")) {
                if (flag) {
                    new Put().putFunction(storage, cmdBuffer);
                }
                else {
                    System.out.println("no table");
                }
            }
            if (cmdBuffer[0].equals("get")) {
                if  (flag) {
                    new Get().getFunction(storage, cmdBuffer, tableName);
                }
                else {
                    System.out.println("no table");
                }
            }
            if (cmdBuffer[0].equals("remove")) {
                if (flag) {
                    new Remove().removeFunction(storage, cmdBuffer);
                }
                else {
                    System.out.println("no table");
                }
            }
            if (cmdBuffer[0].equals("list")) {
                if (flag) {
                    new List().listFunction(storage);
                }
                else {
                    System.out.println("no table");
                }
            }
            if (cmdBuffer[0].equals("exit")) {
                if (flag) {
                    new FillTable().fillTableFunction(storage, tableName);
                }
                new Exit().exitFunction();
            }
            if (cmdBuffer[0].equals("create")) {
                new Create().createFunction(cmdBuffer);
            }
            if (cmdBuffer[0].equals("drop")) {
                new Drop().dropFunction(cmdBuffer);
            }
            if (cmdBuffer[0].equals("use")) {
                new Use().useFunction(storage, cmdBuffer, tableName);
                if (flag) {
                    tableName = cmdBuffer[1];
                }
            }
            if (cmdBuffer[0].equals("show") && cmdBuffer[1].equals("tables")) {
                new ShowTables().showTablesFunction(storage, tableName);
            }
        }
    }
}
