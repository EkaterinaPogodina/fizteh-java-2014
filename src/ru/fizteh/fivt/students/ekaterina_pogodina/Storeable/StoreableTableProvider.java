package ru.fizteh.fivt.students.ekaterina_pogodina.Storeable;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.text.ParseException;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import ru.fizteh.fivt.storage.structured.ColumnFormatException;
import ru.fizteh.fivt.storage.structured.Storeable;
import ru.fizteh.fivt.storage.structured.Table;
import ru.fizteh.fivt.storage.structured.TableProvider;
import ru.fizteh.fivt.students.ekaterina_pogodina.JUnit.DBaseTableProvider;
import ru.fizteh.fivt.students.ekaterina_pogodina.multiFileMap.TableManager;

public class StoreableTableProvider implements TableProvider {
    private StoreableTableProviderFactory factory;
    public TableManager table;
    private String root;

    public StoreableTableProvider(StoreableTableProviderFactory factory, String root) throws IOException, Exception {
        this.root = root;
        this.factory = factory;
        this.table = new TableManager(root);

    }

    public StoreableTable createTable(String name, List<Class<?>> columnTypes) throws IOException {

        if ((name == null) || (!(name.matches("\\w+")))) {
            throw new IllegalArgumentException("wrong table name " + name);
        }

        if ((columnTypes == null) || (columnTypes.isEmpty())) {
            throw new IllegalArgumentException("incorrect column types");
        }
        if (table.basicTables.containsKey(name)) {
            return null;
        } else {
            if (!(new File(root, name)).mkdir()) {
                throw new IllegalStateException("unable to make directory " + name);
            }

            try (PrintStream writtenSignature = new PrintStream(new File(root + File.separator + name, "signature.tsv"))) {
                for (int column = 0; column < columnTypes.size(); ++column) {
                    if (column != 0) {
                        writtenSignature.print(" ");
                    }
                    Class<?> type = columnTypes.get(column);
                    if (type == null) {
                        throw new IllegalArgumentException("wrong column type");
                    }
                    String typeName = null;
                    typeName = TypeName.getAppropriateName(type);

                    writtenSignature.print(typeName);
                }
            }

            try {
                //table.basicTables.put(name, new StoreableTable(root + File.separator + name, name, this, columnTypes));
            } catch (IOException catchedException) {
                throw new IllegalStateException(catchedException);
            }
        }
        return tables.get(name);
    }

    public StoreableTable getTable(String name) {

        if ((name == null) || (!(name.matches("\\w+")))) {
            throw new IllegalArgumentException("wrong table name: " + name);
        }

        try {
            if ((tables.get(name) == null) && (new File(root, name).isDirectory())) {
                try {
                    tables.put(name, new StoreableTable(root + File.separator + name, name, this));
                    tables.get(name).getFilesMap().readData();
                } catch (IOException catchedException) {
                    //do nothing
                }
            }
            return tables.get(name);
        } finally {
            providerLock.readLock().unlock();
        }
    }

    public StoreableImplementation deserialize(Table table, String value) throws ParseException {
        try {
            return StoreableUtils.deserialize(table, value);
        } catch (XMLStreamException catchedException) {
            throw new ParseException(catchedException.getMessage(), 0);
        }
    }

    public String serialize(Table table, Storeable value) throws ColumnFormatException {
        try {
            return StoreableUtils.serialize(table, value);
        } catch (XMLStreamException catchedException) {
            throw new ColumnFormatException(catchedException);
        }
    }

    public StoreableImplementation createFor(Table table) {
        return new StoreableImplementation(table);
    }

    public StoreableImplementation createFor(Table table, List<?> values) throws ColumnFormatException {

        StoreableImplementation builtStoreable = null;

        if (table.getColumnsCount() != values.size()) {
            throw new IndexOutOfBoundsException();
        }
        builtStoreable = createFor(table);

        for (int column = 0; column < values.size(); ++column) {
            builtStoreable.setColumnAt(column, values.get(column));
        }

        return builtStoreable;
    }

    public void removeTable(String name) throws IOException {

    }

    public List<String> getTableNames() {

    }
}