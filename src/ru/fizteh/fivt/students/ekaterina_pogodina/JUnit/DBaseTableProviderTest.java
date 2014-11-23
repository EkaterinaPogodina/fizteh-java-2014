package ru.fizteh.fivt.students.ekaterina_pogodina.JUnit;

import org.junit.Before;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Екатерина on 23.11.2014.
 */
public class DBaseTableProviderTest {
    private final Path testDirectory = Paths.get(System.getProperty("fizteh.db.dir"));
    private final Path tableDirPath = testDirectory.resolve("Test");
    private final String testFile = "filename";
    private final String tableDirectoryName = "Test";
    private final String wrongTableName = ".";
    private final String testTableName = "Test Table";
    private final String tableName = "table1";

    @Before
    public final void setUp() throws Exception {
        if (!testDirectory.toFile().exists()) {
            Files.createDirectory(testDirectory);
        }
    }

    @Test
    public final void testTableProviderCreatedForNonexistentDirectory() throws Exception{
        new DBaseTableProvider(tableDirPath.toString());

        assertTrue(tableDirPath.toFile().exists());
    }


    @Test(expected = IllegalArgumentException.class)
    public final void testProviderThrowsExceptionCreatedNotForDirectory() throws Exception {
        Path newFilePath = testDirectory.resolve(testFile);
        Files.createFile(newFilePath);
        new DBaseTableProvider(newFilePath.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testTableProviderThrowsExceptionCreatedForInvalidPath() throws Exception{
        new DBaseTableProvider("\0");
    }

    @Test
    public final void testGetTableReturnsNullIfTableNameDoesNotExist() throws Exception {
        DBaseTableProvider test = new DBaseTableProvider(testDirectory.toString());
        test.createTable(tableDirectoryName);

        assertNull(test.getTable("MyTable"));
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testGetTableThrowsExceptionCalledForNullTableName() throws Exception {
        DBaseTableProvider test = new DBaseTableProvider(testDirectory.toString());
        test.getTable(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testCreateTableThrowsExceptionCalledForNullTableName() throws Exception {
        DBaseTableProvider test = new DBaseTableProvider(testDirectory.toString());
        test.createTable(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testCreateTableThrowsExceptionCalledForWrongTableName() throws Exception {
        DBaseTableProvider test = new DBaseTableProvider(testDirectory.toString());
        test.createTable(wrongTableName);
    }

    @Test
    public final void testCreateTableOnTheDiskCalledForValidTableName() throws Exception {
        DBaseTableProvider test = new DBaseTableProvider(testDirectory.toString());
        test.createTable(testTableName);
        Path newTablePath = testDirectory.resolve(testTableName);

        assertTrue(newTablePath.toFile().exists() && newTablePath.toFile().isDirectory());
    }

    @Test
    public final void testCreateTableReturnsNullCalledForExistentOnDiskTable() throws Exception {
        Files.createDirectory(tableDirPath);
        DBaseTableProvider test = new DBaseTableProvider(testDirectory.toString());

        assertNull(test.createTable(tableDirectoryName));
    }

    @Test
    public final void testRemoveTableFromTheDiskCalledForValidTableName() throws Exception {
        DBaseTableProvider test = new DBaseTableProvider(testDirectory.toString());
        test.createTable(testTableName);
        Path newTablePath = testDirectory.resolve(testTableName);
        test.removeTable(testTableName);

        assertFalse(newTablePath.toFile().exists());
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testRemoveTableThrowsExceptionCalledForNullTableName() throws Exception {
        DBaseTableProvider test = new DBaseTableProvider(testDirectory.toString());
        test.removeTable(null);
    }

    @Test(expected = IllegalStateException.class)
    public final void testRemoveTableThrowsExceptionIfTableNameDoesNotExist() throws Exception {
        DBaseTableProvider test = new DBaseTableProvider(testDirectory.toString());
        test.removeTable("MyTable");
    }
}
