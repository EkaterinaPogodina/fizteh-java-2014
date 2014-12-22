package ru.fizteh.fivt.students.ekaterina_pogodina.JUnit;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map.Entry;

import ru.fizteh.fivt.storage.strings.*;
import ru.fizteh.fivt.students.ekaterina_pogodina.filemap.DataBase;
import ru.fizteh.fivt.students.ekaterina_pogodina.multiFileMap.BaseTable;
import ru.fizteh.fivt.students.ekaterina_pogodina.shell.Rm;


import java.util.ArrayList;
import java.util.List;

public class DBaseTable<Obj> implements Table {
    static final int SIZE = 16;
    private BaseTable<String> table;
    public int savedKeys = 0;

    public DBaseTable(BaseTable baseTable) {
        table = baseTable;
    }
    public DBaseTable(Path pathTable, String name) {
        table = new BaseTable(name, pathTable);
    }

    @Override
    public String get(String key) throws IllegalArgumentException {
        if (key == null) {
            throw new IllegalArgumentException("null  key");
        }
        if (table.removed.contains(key)) {
            return null;
        }
        if (table.puted.containsKey(key)) {
            return (table.puted.get(key));
        }
        if (table.keys.containsKey(key)) {
            return (table.keys.get(key));
        }
        return null;
    }

    @Override
    public int size() {
        int size = table.keys.size();
        for (String keys: table.removed) {
            if (table.puted.containsKey(keys)) {
                size = size - 1;
            }
            if (table.keys.containsKey(keys)) {
                size = size - 1;
            }
        }
        size = size + table.puted.size();
        return size;
    }

    @Override
    public String put(String key, String value) throws IllegalArgumentException {
        if (key == null) {
            throw new IllegalArgumentException("null key");
        }
        if (value == null) {
            throw new IllegalArgumentException("null value");
        }
        String val = null;
        val = table.puted.get(key);
        if (val == null) {
            val = table.keys.get(key);
        }
        table.puted.put(key, value);
        return val;
    }

    @Override
    public String getName() {
        return table.tableName;
    }

    @Override
    public String remove(String key) throws IllegalArgumentException {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        if (table.removed.contains(key)) {
            return null;
        }
        String value = table.puted.get(key);
        if (value == null) {
            value = table.keys.get(key);
        }
        table.removed.add(key);
        return value;
    }

    @Override
    public int commit() {
        if (table.puted.size() == 0 && table.removed.size() == 0) {
            return 0;
        }
        int size = table.puted.size();

        byte b;
        int nDirectory;
        int nFile;
        for (Entry<String, String> pair : table.puted.entrySet()) {
            b = pair.getKey().getBytes()[0];
            nDirectory = b % SIZE;
            nFile = b / SIZE % SIZE;

            if (table.tableDateBase[nDirectory][nFile] == null) {
                String s;
                s = String.valueOf(nDirectory);
                s = s.concat(".dir");
                Path pathDir = table.path;
                pathDir = pathDir.resolve(s);
                try {
                    if (!pathDir.toFile().exists()) {
                        try {
                            pathDir.toFile().mkdir();
                        } catch (Exception e) {
                            System.err.println(e.getMessage());
                        }

                    }
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }

                s = String.valueOf(nFile);
                s = s.concat(".dat");
                Path pathFile = pathDir.resolve(s);
                try {
                    if (!pathFile.toFile().exists()) {
                        try {
                            pathFile.toFile().createNewFile();
                        } catch (Exception e) {
                            System.err.println(e.getMessage());
                        }
                    }
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
                try {
                    table.tableDateBase[nDirectory][nFile] = new DataBase(pathFile.toString());
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }

            }
            try {
                table.tableDateBase[nDirectory][nFile].put(pair.getKey(), pair.getValue());
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
            if (table.keys.containsKey(pair.getKey())) {
                table.keys.remove(pair.getKey());
            }
            table.keys.put(pair.getKey(), pair.getValue());
        }
        table.puted.clear();
        for (String key : table.removed) {
            b = key.getBytes()[0];
            nDirectory = b % SIZE;
            nFile = b / SIZE % SIZE;
            try {
                table.tableDateBase[nDirectory][nFile].remove(key);

            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
            table.keys.remove(key);
        }
        table.removed.clear();
        boolean f = true;
        for (int i = 0; i < SIZE; i++) {
            if (!f) {
                String s;
                s = String.valueOf(SIZE - 1);
                s = s.concat(".dir");
                Path pathDir = table.path;
                pathDir = pathDir.resolve(s);
                try {
                    if (pathDir.toFile().exists()) {
                        Rm.run(new String[]{"rm", "-r", pathDir.toString()}, true, 2);
                    }
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                }
            }
            f  = false;
            for (int j = 0; j < SIZE; j++) {
                if (table.tableDateBase[i][j] != null) {
                    f = true;
                    try {
                        table.tableDateBase[i][j].close();
                    } catch (Exception e) {
                        System.err.println(e.getMessage());
                    }
                }
            }
        }
        if (!f) {
            String s;
            s = String.valueOf(SIZE - 1);
            s = s.concat(".dir");
            Path pathDir = table.path;
            pathDir = pathDir.resolve(s);
            try {
                if (pathDir.toFile().exists()) {
                    Rm.run(new String[]{"rm", "-r", pathDir.toString()}, true, 2);
                }
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
        // кол-во новых ключей!
        return size;
    }

    @Override
    public int rollback() {
        int size = table.puted.size();
        table.removed.clear();
        table.puted.clear();
        return size;
    }

    @Override
    public List<String> list() {
        List<String> list = new ArrayList<String>();
        for (String key : table.keys.keySet()) {
            if (!table.removed.contains(key)) {
                list.add(key);
            }
        }

        for (String key : table.puted.keySet()) {
            if (!table.removed.contains(key)) {
                list.add(key);
            }
        }

        return list;
    }

}
