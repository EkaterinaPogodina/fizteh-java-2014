package ru.fizteh.fivt.students.Gotovchic;

import java.util.Map;
import java.util.Scanner;

public class InteractiveMode extends Command {
    void interactiveModeFunction(Map<String, String> storage, String[] cmdBuffer) throws Exception {
        Scanner sc = new Scanner(System.in);
        String cmd = "";
        String tableName = new String();
        while (true) {
            System.out.print("$ ");
            cmd = sc.nextLine();
            cmd = cmd.replaceAll("\\s+", " ");
            cmdBuffer = cmd.split(" ");
            if (cmdBuffer[0].equals("") && cmdBuffer.length > 0) {
                cmd = "";
                for (int ind = 1; ind < cmdBuffer.length; ++ind) {
                    cmd = cmd + cmdBuffer[ind] + " ";
                }
                cmdBuffer = cmd.split(" ");
            }
            if (cmdBuffer[0].equals("put")) {
                if (flag) {
                    new Put().putFunction(storage, cmdBuffer);
                } else {
                    System.out.println("no table");
                }
            }
            if (cmdBuffer[0].equals("get")) {
                if  (flag) {
                    new Get().getFunction(storage, cmdBuffer, tableName);
                } else {
                    System.out.println("no table");
                }
            }
            if (cmdBuffer[0].equals("remove")) {
                if (flag) {
                    new Remove().removeFunction(storage, cmdBuffer);
                } else {
                    System.out.println("no table");
                }
            }
            if (cmdBuffer[0].equals("list")) {
                if (flag) {
                    new List().listFunction(storage);
                } else {
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


