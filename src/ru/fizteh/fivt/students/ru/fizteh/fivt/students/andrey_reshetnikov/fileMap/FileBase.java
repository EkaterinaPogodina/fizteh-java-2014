package ru.fizteh.fivt.students.ru.fizteh.fivt.students.andrey_reshetnikov.fileMap;

/**
 * Created by Hoderu on 09.10.14.
 */
import java.awt.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

public class FileBase {
    public Map<String, String> m;
    private DataInputStream stream;
    private DataOutputStream streamOut;

    public void loading(String path) throws IOException {
        try {
            stream = new DataInputStream(new FileInputStream(path));
        } catch (FileNotFoundException e) {
            throw new IOException("Database file not found");
        }
        m = new HashMap<>();
        boolean eof = false;
        while (!eof) {
            try {
                String key = nextWord();
                String value = nextWord();
                if (m.containsKey(key)) {
                    throw new IOException("Double key");
                }
                m.put(key, value);
            } catch (EOFException e) {
                eof = true;
            }
        }
        stream.close();
    }

    private String nextWord() throws IOException {
        int length = stream.readInt();
        if (length < 0) {
            throw new IOException("Error of length");
        }
        ArrayList<Byte> wordBuilder = new ArrayList<>();
        try {
            for (int i = 0; i < length; i++) {
                byte symbol = stream.readByte();
                wordBuilder.add(symbol);
            }
        } catch (EOFException e) {
            throw new IOException("End of file");
        }
        byte[] word = new byte[length];
        for (int i = 0; i < length; i++) {
            word[i] = wordBuilder.get(i);
        }
        return new String(word, "UTF-8");
    }

    public void dump(String path) throws IOException {
        try {
            streamOut = new DataOutputStream(new FileOutputStream(path));
        } catch (FileNotFoundException e) {
            throw new IOException("Database file not found");
        }
        for (Map.Entry<String, String> entry: m.entrySet()) {
            try {
                writeNext(entry.getKey());
                writeNext(entry.getValue());
            } catch (IOException e) {
                throw new IOException("Problems with writing");
            }
        }
        streamOut.close();
    }

    private void writeNext(String word) throws IOException {
        byte[] byteWord = word.getBytes("UTF-8");
        streamOut.writeInt(byteWord.length);
        streamOut.write(byteWord);
    }
}
