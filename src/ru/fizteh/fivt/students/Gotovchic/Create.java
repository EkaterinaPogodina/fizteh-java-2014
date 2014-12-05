package ru.fizteh.fivt.students.Gotovchic;

import java.io.File;

public class Create extends Command {
    void createFunction(String[] cmdBuffer) {
        if (cmdBuffer.length == 2) {
            String tableName = cmdBuffer[1];
            File file = new File(System.getProperty("fizteh.db.dir") + "/" + tableName);
            if (file.exists()) {
                System.out.println(tableName + " exists");
            } else {
                file.mkdir();
                System.out.println("created");
            }
        } else {
            errorFunction();
        }
    }
}
