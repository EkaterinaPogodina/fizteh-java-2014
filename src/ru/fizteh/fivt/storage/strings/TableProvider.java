package ru.fizteh.fivt.storage.strings;

public interface TableProvider {

    Table getTable(String name);

    Table createTable(String name) throws Exception;

    void removeTable(String name) throws Exception;
}
