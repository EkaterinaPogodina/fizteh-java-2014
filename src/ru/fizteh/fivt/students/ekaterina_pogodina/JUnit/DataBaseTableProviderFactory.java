package ru.fizteh.fivt.students.ekaterina_pogodina.JUnit;

import ru.fizteh.fivt.storage.strings.*;

public class DataBaseTableProviderFactory implements TableProviderFactory {

    @Override
    public TableProvider create(String dir) {
        TableProvider table = null;
        try {
            table =  new DBaseTableProvider(dir);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return table;
    }
}
