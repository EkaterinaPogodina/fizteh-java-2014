package ru.fizteh.fivt.students.ekaterina_pogodina.JUnit;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import ru.fizteh.fivt.students.ekaterina_pogodina.JUnit.DBaseTableProvider;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class DBaseTableProviderTest {
    private Path testDirectory;
    private Path tableDirPath;
    private final String testFile = "filename";
    private final String tableDirectoryName = "Test";
    private final String wrongTableName = ".";
    private final String testTableName = "Test Table";
    private final String tableName = "table1";
    @Rule
    public TemporaryFolder tmpFolder = new TemporaryFolder();

    public DBaseTableProviderTest() {
    }

    @Before
    public final void setUp() throws Exception {
        String name = tmpFolder.newFolder().getAbsolutePath().toString();
        testDirectory = Paths.get(name);
        tableDirPath = this.testDirectory.resolve("Test");
        if(!testDirectory.toFile().exists()) {
            Files.createDirectory(testDirectory);
        }

    }

    @Test
    public final void testTableProviderCreatedForNonexistentDirectory() throws Exception {
        new DBaseTableProvider(tableDirPath.toString());
        assertTrue(tableDirPath.toFile().exists());
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testTableProviderThrowsExceptionCreatedForInvalidPath() throws Exception {
        new DBaseTableProvider("\0");
    }

    @Test
    public final void testGetTableReturnsNullIfTableNameDoesNotExist() throws Exception {
        DBaseTableProvider test = new DBaseTableProvider(testDirectory.toString());
        test.createTable("Test");
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
        test.createTable(".");
    }

    @Test
    public final void testCreateTableOnTheDiskCalledForValidTableName() throws Exception {
        DBaseTableProvider test = new DBaseTableProvider(testDirectory.toString());
        test.createTable("Test Table");
        Path newTablePath = testDirectory.resolve("Test Table");
        assertTrue(newTablePath.toFile().exists() && newTablePath.toFile().isDirectory());
    }

    @Test
    public final void testCreateTableReturnsNullCalledForExistentOnDiskTable() throws Exception {
        Files.createDirectory(this.tableDirPath, new FileAttribute[0]);
        DBaseTableProvider test = new DBaseTableProvider(testDirectory.toString());
        assertNull(test.createTable("Test"));
    }

    @Test
    public final void testRemoveTableFromTheDiskCalledForValidTableName() throws Exception {
        DBaseTableProvider test = new DBaseTableProvider(testDirectory.toString());
        test.createTable("Test Table");
        Path newTablePath = testDirectory.resolve("Test Table");
        test.removeTable("Test Table");
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
