package ru.fizteh.fivt.students.Gotovchic;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class FillTable {
    public void fillTableFunction(Map<String, String> storage, String tableName) throws Exception {
        for (Map.Entry<String, String> entry : storage.entrySet()) {
            int hashcode = entry.getKey().hashCode();
            Integer ndirectory = hashcode % 16;
            Integer nfile = hashcode / 16 % 16;
            String fileName = System.getProperty("fizteh.db.dir") + "/" + tableName
                    + "/" + ndirectory.toString() + ".dir";
            File file = new File(fileName);
            if (!file.exists()) {
                file.mkdir();
            }
            fileName += "/" + nfile.toString() + ".dat";
            file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            byte[] bytesKey = (" " + entry.getKey() + " ").getBytes(StandardCharsets.UTF_8);
            DataOutputStream stream = new DataOutputStream(new FileOutputStream(fileName, true));
            stream.write((int) bytesKey.length);
            stream.write(bytesKey);
            byte[] bytesVal = ((" " + entry.getValue() + " ").getBytes(StandardCharsets.UTF_8));
            stream.write((Integer) bytesVal.length);
            stream.write(bytesVal);
            stream.close();
        }
    }
}

