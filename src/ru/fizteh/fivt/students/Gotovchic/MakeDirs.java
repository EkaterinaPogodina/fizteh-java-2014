package ru.fizteh.fivt.students.Gotovchic;

import java.io.File;
import java.util.Map;

public class MakeDirs{
    void makeDirsFunction(Map<String, String> storage) throws Exception {
        File file = new File(System.getProperty("fizteh.db.dir"));
        if (!file.exists()) {
            file.mkdirs();
        } else {
            if (!file.isDirectory()) {
                System.err.println(System.getProperty("fizteh.db.dir") + " is not a directory");
                System.exit(1);
            } else {
                for (File sub : file.listFiles()) {
                    if (!sub.isDirectory()) {
                        System.err.println(System.getProperty("fizteh.db.dir") + "/"
                                + sub.getName() + " is not a directory");
                    }
                }
            }
        }
    }
}
