package ru.fizteh.fivt.students.Gotovchic;

import java.util.Map;

public class Put extends Command {
    void putFunction(Map<String, String> storage, String[] cmdBuffer) {
        if (cmdBuffer.length == 3) {
            String k = cmdBuffer[1];
            String v = cmdBuffer[2];
            String prevV = storage.put(k, v);
            if (prevV == null) {
                System.out.println("new");
            } else {
                System.out.println("overwrite");
                System.out.println(prevV);
            }
        }  else {
            errorFunction();
        }
    }
}
