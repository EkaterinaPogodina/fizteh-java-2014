package ru.fizteh.fivt.students.Gotovchic;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class Use extends Command { // свято верю, что эта шляпа работает.
    void useFunction(Map<String, String> storage, String[] cmdBuffer, String oldTableName) throws Exception {
        if (cmdBuffer.length == 2) {
            String tableName = System.getProperty("fizteh.db.dir") + "/" + cmdBuffer[1];
            File file = new File(tableName);
            if (file.exists()) {
                if (flag) {
                    new FillTable().fillTableFunction(storage, oldTableName);
                    storage.clear();
                }
                for (Integer i = 0; i < 16; ++i) {
                    for (Integer j = 0; j < 16; ++j) {
                        tableName = System.getProperty("fizteh.db.dir") + "/" + cmdBuffer[1] + "/"
                                + i.toString() + ".dir" + "/" + j.toString() + ".dat";
                        if (new File(tableName).exists()) {
                            fillStorage(tableName, file, storage);
                            PrintWriter writer = new PrintWriter(new File(tableName));
                            writer.print("");
                            writer.close();
                        }
                    }
                }
                System.out.println("using " + cmdBuffer[1]);
                flag = true;
            } else {
                System.err.println(cmdBuffer[1] + " not exists");
            }
        } else {
            errorFunction();
        }
    }
    void fillStorage(String tableName, File file, Map<String, String> storage) throws IOException {
        DataInputStream stream = new DataInputStream(new FileInputStream(tableName));
        file = new File(tableName);
        byte[] data = new byte[(int) file.length()];
        stream.read(data);
        int counter = 0;
        int offset = 0;
        String keyForMap = "";
        String valForMap = "";
        while (counter < file.length()) {
            offset = data[counter];
            keyForMap = new String(data, counter + 2, offset - 2, StandardCharsets.UTF_8);
            counter = counter + offset + 1;
            offset = data[counter];
            valForMap = new String(data, counter + 2, offset - 2, StandardCharsets.UTF_8);
            storage.put(keyForMap, valForMap);
            counter = counter + offset + 1;
        }
        stream.close();
    }
}

