package ru.fizteh.fivt.students.ekaterina_pogodina.Storeable;

import ru.fizteh.fivt.students.ekaterina_pogodina.multiFileMap.TableManager;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class Interpreter {
    private String dir;
    public Interpreter() throws Exception {
        //dir = System.getProperty("fizteh.db.dir");
        //TableManager table = new TableManager(dir);
        StoreableTableProvider provider = null;
        try {
            provider = (new StoreableTableProviderFactory()).create(System.getProperty("fizteh.db.dir"));
            Scanner scanner = new Scanner(System.in);
            try {
                do {
                    System.out.print("$ ");
                    String[] input = scanner.nextLine().split(";");
                    for (int i = 0; i < input.length; ++i) {
                        if (input[i].length() > 0) {
                            String[] buffer = input[i].trim().split("\\s+");
                            try {
                                Parser.parse(buffer, provider.table);
                            } catch (Exception exception) {
                                System.err.println(exception.getMessage());
                            }
                        }
                    }
                } while(true);
            } catch (Exception e) {
                System.err.println(e.getMessage());
                System.exit(1);
            }
            scanner.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    public Interpreter(String[] args) {
        List<String> cmdWithArgs = new ArrayList<String>();
        StoreableTableProvider provider = null;
        try {
            provider = (new StoreableTableProviderFactory()).create(System.getProperty("fizteh.db.dir"));
            if (args.length > 0) {
                try {
                    StringBuilder helpArray = new StringBuilder();
                    for (int i = 0; i < args.length; ++i) {
                        helpArray.append(args[i]).append(' ');
                    }
                    String longStr = helpArray.toString();
                    String[] input = longStr.split(";");
                    for (int i = 0; i < input.length; ++i) {
                        if (input[i].length() > 0) {
                            String[] buffer = input[i].trim().split("\\s+");
                            try {
                                Parser.parse(buffer, provider);
                            } catch (Exception exception) {
                                System.err.println(exception.getMessage());
                            }
                        }
                    }
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                    System.exit(1);
                }
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }

    }
}