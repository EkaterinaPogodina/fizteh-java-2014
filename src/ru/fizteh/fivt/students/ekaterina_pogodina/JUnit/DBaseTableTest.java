package ru.fizteh.fivt.students.ekaterina_pogodina.JUnit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.util.HashSet;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import ru.fizteh.fivt.students.ekaterina_pogodina.JUnit.DBaseTable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class DBaseTableTest {
    private Path testDirectory;
    private final String tableName = "Table1";
    private Path tableDirectoryPath;
    private final String testKey1 = "key1";
    private final String testKey2 = "key2";
    private final String testValue1 = "value1";
    private final String testValue2 = "value2";
    private final String testFile = "Testfile.txt";
    private final String validSubdirectory = "1.dir";
    private static final int SIZEDIR = 16;
    private static final int SIZEDAT = 16;
    @Rule
    public TemporaryFolder tmpFolder = new TemporaryFolder();

    @Before
    public final void setUp() throws IOException {
        String name = tmpFolder.newFolder().getAbsolutePath().toString();
        testDirectory = Paths.get(name);
        tableDirectoryPath = testDirectory.resolve("Table1");
        if(!testDirectory.toFile().exists()) {
            Files.createDirectory(this.testDirectory);
        }

    }

    @Test
    public final void testGetReturnsNullIfKeyIsNotFound() throws IOException {
        Files.createDirectory(tableDirectoryPath);
        DBaseTable test = new DBaseTable(testDirectory, "Table1");
        assertNull(test.get("key1"));
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testGetThrowsIllegalArgumentExceptionCalledForNullKey() throws IOException {
        Files.createDirectory(tableDirectoryPath);
        DBaseTable test = new DBaseTable(testDirectory, "Table1");
        test.get(null);
    }

    @Test
    public final void testGetCalledForNonComittedKey() throws IOException {
        Files.createDirectory(tableDirectoryPath);
        DBaseTable test = new DBaseTable(testDirectory, "Table1");
        assertNull(test.put("key1", "value1"));
        assertEquals("value1", test.get("key1"));
    }

    @Test
    public final void testGetCalledForComittedKey() throws IOException {
        Files.createDirectory(tableDirectoryPath);
        DBaseTable test = new DBaseTable(testDirectory, "Table1");
        assertNull(test.put("key1", "value1"));
        test.commit();
        assertEquals("value1", test.get("key1"));
    }

    @Test
    public final void testGetCalledForDeletedKeyBeforeCommit() throws IOException {
        Files.createDirectory(tableDirectoryPath);
        DBaseTable test = new DBaseTable(testDirectory, "Table1");
        assertNull(test.put("key1", "value1"));
        assertEquals("value1", test.remove("key1"));
        assertNull(test.get("key1"));
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testPutThrowsIllegalArgumentExceptionCalledForNullKey() throws IOException {
        Files.createDirectory(tableDirectoryPath);
        DBaseTable test = new DBaseTable(testDirectory, "Table1");
        test.put(null, "value1");
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testPutThrowsExceptionCalledForNullValue() throws IOException {
        Files.createDirectory(tableDirectoryPath);
        DBaseTable test = new DBaseTable(testDirectory, "Table1");
        test.put("key1", null);
    }

    @Test
    public final void testPutReturnsNullIfKeyHasNotBeenWrittenYet() throws IOException {
        Files.createDirectory(tableDirectoryPath);
        DBaseTable test = new DBaseTable(testDirectory, "Table1");
        assertNull(test.put("key1", "value1"));
    }

    @Test
    public final void testPutReturnsOldValueIfKeyExists() throws IOException {
        Files.createDirectory(this.tableDirectoryPath);
        DBaseTable test = new DBaseTable(testDirectory, "Table1");
        test.put("key1", "value1");
        assertEquals("value1", test.put("key1", "value2"));
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testRemoveThrowsExceptionCalledForNullKey() throws IOException {
        Files.createDirectory(tableDirectoryPath);
        DBaseTable test = new DBaseTable(testDirectory, "Table1");
        test.remove(null);
    }

    @Test
    public final void testRemoveReturnsNullIfKeyIsNotFound() throws Exception {
        Files.createDirectory(this.tableDirectoryPath);
        DBaseTable test = new DBaseTable(testDirectory, "Table1");
        assertNull(test.remove("key1"));
    }

    @Test
    public final void testRemoveCalledForDeletedKeyBeforeCommit() throws IOException {
        Files.createDirectory(tableDirectoryPath);
        DBaseTable test = new DBaseTable(testDirectory, "Table1");
        assertNull(test.put("key1", "value1"));
        assertEquals("value1", test.remove("key1"));
        assertNull(test.remove("key1"));
    }

    @Test
    public final void testRemoveCalledForDeletedKeyAfterCommit() throws IOException {
        Files.createDirectory(tableDirectoryPath);
        DBaseTable test = new DBaseTable(testDirectory, "Table1");
        assertNull(test.put("key1", "value1"));
        assertEquals("value1", test.remove("key1"));
        test.commit();
        assertNull(test.remove("key1"));
    }

    @Test
    public final void testCommitCreatesRealFileOnTheDisk() throws IOException {
        Files.createDirectory(tableDirectoryPath);
        DBaseTable test = new DBaseTable(testDirectory, "Table1");
        assertNull(test.put("key1", "value1"));
        test.commit();
        String subdirectoryName = Math.abs("key1".getBytes()[0] % 16) + ".dir";
        String fileName = Math.abs("key1".getBytes()[0] / 16 % 16) + ".dat";
        Path filePath = Paths.get(testDirectory.toString() + test.getName() + subdirectoryName + fileName);
        assertTrue(filePath.toFile().exists());
    }

    @Test
    public final void testCommitOverwritesCommitedKey() throws IOException {
        Files.createDirectory(tableDirectoryPath);
        DBaseTable test = new DBaseTable(testDirectory, "Table1");
        assertNull(test.put("key1", "value1"));
        test.commit();
        assertEquals("value1", test.put("key1", "value2"));
        test.commit();
        assertEquals("value2", test.get("key1"));
    }

    @Test
    public final void testCommitRemovesExistentKeyAfterCommit() throws IOException {
        Files.createDirectory(tableDirectoryPath);
        DBaseTable test = new DBaseTable(testDirectory, "Table1");
        assertNull(test.put("key1", "value1"));
        test.commit();
        assertEquals("value1", test.remove("key1"));
        test.commit();
        assertNull(test.get("key1"));
    }

    @Test
    public final void testCommitReturnsNonZeroChangesPuttingNewRecord() throws IOException {
        Files.createDirectory(tableDirectoryPath);
        DBaseTable test = new DBaseTable(testDirectory, "Table1");
        assertNull(test.put("key1", "value1"));
        assertEquals(1, test.commit());
    }

    @Test
    public final void testCommitReturnsNotZeroChangesRewriting() throws IOException {
        Files.createDirectory(tableDirectoryPath);
        DBaseTable test = new DBaseTable(this.testDirectory, "Table1");
        assertNull(test.put("key1", "value1"));
        assertEquals("value1", test.put("key1", "value2"));
        assertEquals(1, test.commit());
    }

    @Test
    public final void testCommitNoChanges() throws IOException {
        Files.createDirectory(tableDirectoryPath);
        DBaseTable test = new DBaseTable(testDirectory, "Table1");
        assertNull(test.put("key1", "value1"));
        assertEquals(1, test.commit());
        assertEquals(0, test.commit());
    }

    @Test
    public final void testRollbackAfterPuttingNewKey() throws IOException {
        Files.createDirectory(tableDirectoryPath);
        DBaseTable test = new DBaseTable(testDirectory, "Table1");
        assertEquals(0, test.size());
        assertNull(test.put("key1", "value1"));
        assertEquals(1, test.size());
        assertEquals(1, test.rollback());
        assertEquals(0, test.size());
        assertNull(test.get("key1"));
    }

    @Test
    public final void testRollbackNoChanges() throws IOException {
        Files.createDirectory(tableDirectoryPath);
        DBaseTable test = new DBaseTable(testDirectory, "Table1");
        assertNull(test.put("key1", "value1"));
        test.rollback();
        assertEquals(0L, (long) test.size());
        assertEquals(0L, (long) test.rollback());
    }

    @Test
    public final void testListCalledForEmptyTable() throws IOException {
        Files.createDirectory(this.tableDirectoryPath, new FileAttribute[0]);
        DBaseTable test = new DBaseTable(this.testDirectory, "Table1");
        assertTrue(test.list().isEmpty());
    }

    @Test
    public final void testListCalledForNonEmptyNewTable() throws IOException {
        Files.createDirectory(this.tableDirectoryPath, new FileAttribute[0]);
        DBaseTable test = new DBaseTable(this.testDirectory, "Table1");
        assertNull(test.put("key1", "value1"));
        assertNull(test.put("key2", "value2"));
        HashSet expectedKeySet = new HashSet();
        expectedKeySet.add("key1");
        expectedKeySet.add("key2");
        HashSet tableKeySet = new HashSet();
        tableKeySet.addAll(test.list());
        assertEquals(expectedKeySet, tableKeySet);
    }

    @Test
    public final void testListCalledForNonEmptyCommitedTable() throws IOException {
        Files.createDirectory(this.tableDirectoryPath, new FileAttribute[0]);
        DBaseTable test = new DBaseTable(this.testDirectory, "Table1");
        assertNull(test.put("key1", "value1"));
        assertNull(test.put("key2", "value2"));
        test.commit();
        assertEquals("value2", test.remove("key2"));
        HashSet expectedKeySet = new HashSet();
        expectedKeySet.add("key1");
        HashSet tableKeySet = new HashSet();
        tableKeySet.addAll(test.list());
        assertEquals(expectedKeySet, tableKeySet);
    }

    @Test
    public final void testSizeCalledForNonEmptyNonCommitedTable() throws IOException {
        Files.createDirectory(tableDirectoryPath);
        DBaseTable test = new DBaseTable(testDirectory, "Table1");
        assertNull(test.put("key1", "value1"));
        assertNull(test.put("key2", "value2"));
        assertEquals("value2", test.remove("key2"));
        assertEquals(1, test.size());
    }

    @Test
    public final void testSizeCalledForNonEmptyCommitedTable() throws IOException {
        Files.createDirectory(tableDirectoryPath);
        DBaseTable test = new DBaseTable(testDirectory, "Table1");
        assertNull(test.put("key1", "value1"));
        assertNull(test.put("key2", "value2"));
        test.commit();
        assertEquals("value2", test.remove("key2"));
        assertEquals(1, test.size());
    }
}