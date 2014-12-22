package ru.fizteh.fivt.students.ekaterina_pogodina.JUnit;

import ru.fizteh.fivt.storage.strings.TableProvider;
import ru.fizteh.fivt.storage.strings.Table;
import ru.fizteh.fivt.students.ekaterina_pogodina.multiFileMap.TableManager;

import java.io.File;


public class DBaseTableProvider implements TableProvider {

    public TableManager tableManager;

    public DBaseTableProvider(String dir) throws Exception {
        if (dir == null | dir == "\0") {
            throw new IllegalArgumentException();
        }
        tableManager = new TableManager(dir);
    }

    @Override
    public Table getTable(String name) {
        if (name == null) {
            throw new IllegalArgumentException("key's name is null");
        }
        if (tableManager.basicTables.containsKey(name)) {
            try {
                return tableManager.basicTables.get(name);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Illegal key's name", e);
            }
        }
        return null;
    }

    @Override
    public Table createTable(String name) {
        if (name == null | name == "\0" | name == "." ) {
            throw new IllegalArgumentException("key's name is null");
        }
        if (tableManager.basicTables.containsKey(name)) {
            return null;
        }
        try {
            tableManager.create(name);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        try {
            return tableManager.basicTables.get(name);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Illegal key's name", e);
        }
    }

    @Override
    public void removeTable(String name) {
        if (name == null) {
            throw new IllegalArgumentException("key's name is null");
        }
        if (!tableManager.basicTables.containsKey(name)) {
            throw new IllegalStateException("there is no such table");
        }
        try {
            try {
                boolean f = tableManager.drop(name);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Illegal table's name", e);
        }
    }
}
