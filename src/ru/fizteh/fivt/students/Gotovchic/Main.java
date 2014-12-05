package ru.fizteh.fivt.students.Gotovchic;

import java.util.HashMap;
import java.util.Map;

public class Main {
    static Integer tableNum = 16; // Equivalent to "#define tableNum 16" in C/C++.
    public static void main(String[] args) throws Exception {
        Map<String, String> storage = new HashMap<String, String>();
        new MakeDirs().makeDirsFunction(storage); //УБРАТЬ storage???
        String [] cmdBuffer = new String[1024];
        if (args.length == 0) {
            new InteractiveMode().interactiveModeFunction(storage, cmdBuffer);
        } else {
            String commands = "";
            for (int ind = 0; ind < args.length; ++ind) {
                commands = commands + args[ind] + " ";
            }
            new BatchMode().batchModeFunction(commands, storage, cmdBuffer);
            //new FillTable().fillTableFunction(storage, "1");
            new Exit().exitFunction();
        }
    }
}
