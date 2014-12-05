package ru.fizteh.fivt.students.Gotovchic;

import java.util.Map;

public class Remove extends Command {
    void removeFunction(Map<String, String> storage, String[] cmdBuffer) {
        if (cmdBuffer.length == 2) {
            String v = storage.remove(cmdBuffer[1]);
            if (v != null) {
                System.out.println("removed");
            } else {
                System.out.println("not found");
            }
        } else {
            errorFunction();
        }
    }
}
