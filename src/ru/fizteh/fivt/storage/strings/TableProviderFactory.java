package ru.fizteh.fivt.storage.strings;

public interface TableProviderFactory {
    TableProvider create(String dir) throws Exception;
}
