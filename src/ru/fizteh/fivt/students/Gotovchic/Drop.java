package ru.fizteh.fivt.students.Gotovchic;

import java.io.File;
import java.io.IOException;

public class Drop extends Command {
    void dropFunction(String[] cmdBuffer) throws IOException { // Equivalent to rm -r in Shell problem.
        if (cmdBuffer.length == 2) {
            String tableName = System.getProperty("fizteh.db.dir") + "/" + cmdBuffer[1];
            if (new File(tableName).exists()) {
                recRem(System.getProperty("fizteh.db.dir") + "/" + cmdBuffer[1]);
                System.out.println("dropped");
            } else {
                System.out.println(cmdBuffer[1] + " not exists");
            }
        } else {
            errorFunction();
        }
    }
    void recRem(String myFile) throws IOException {
        File file = new File(myFile);
        if (!file.exists()) {
            return;
        }
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                recRem(f.getAbsolutePath());
            }
        }
        file.delete();
    }
}
