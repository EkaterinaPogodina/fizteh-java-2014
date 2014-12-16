package ru.fizteh.fivt.students.ekaterina_pogodina.JUnit;

import org.junit.Before;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;
import ru.fizteh.fivt.storage.strings.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class DBaseTableTest {
    private Path testDirectory ;
    private final String tableName = "Table1";
    private Path tableDirectoryPath = testDirectory.resolve(tableName);
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
        testDirectory = Paths.get(System.getProperty(name));
        tableDirectoryPath = testDirectory.resolve(tableName);
        if (!testDirectory.toFile().exists()) {
            Files.createDirectory(testDirectory);
        }
    }

    @Test
    public final void testDBTableCreatedFromDirectoryWithNonDirectories() throws IOException {
        Files.createDirectory(tableDirectoryPath);
        Path newFilePath = tableDirectoryPath.resolve(testFile);
        newFilePath.toFile().createNewFile();
        new DBaseTable(testDirectory, tableName);
    }

    @Test
    public final void testDBTableCreatedFromDirWithWrongSubdirectories() throws IOException {
        Files.createDirectory(tableDirectoryPath);
        Path newSubdirectory = tableDirectoryPath.resolve("subdirectory");
        Files.createDirectory(newSubdirectory);
        new DBaseTable(testDirectory, tableName);
    }

    @Test
    public final void testDBTableCreatedFromDirWithEmptySubdirs() throws IOException {
        Files.createDirectory(tableDirectoryPath);
        Path newSubdirectory = tableDirectoryPath.resolve(validSubdirectory);
        Files.createDirectory(newSubdirectory);
        new DBaseTable(testDirectory, tableName);
    }

    @Test
    public final void testDBTableCreatedFromDirWithSubdirsWithWrongFiles() throws IOException {
        Files.createDirectory(tableDirectoryPath);
        Path newSubdirectory = tableDirectoryPath.resolve(validSubdirectory);
        Files.createDirectory(newSubdirectory);
        Path newFilePath = newSubdirectory.resolve(testFile);
        newFilePath.toFile().createNewFile();
        new DBaseTable(testDirectory, tableName);
    }

    //GetTests.
    @Test
    public final void testGetReturnsNullIfKeyIsNotFound() throws IOException {
        Files.createDirectory(tableDirectoryPath);
        Table test = new DBaseTable(testDirectory, tableName);

        assertNull(test.get(testKey1));
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testGetThrowsIllegalArgumentExceptionCalledForNullKey() throws IOException {
        Files.createDirectory(tableDirectoryPath);
        Table test = new DBaseTable(testDirectory, tableName);
        test.get(null);
    }

    @Test
    public final void testGetCalledForNonComittedKey() throws IOException {
        Files.createDirectory(tableDirectoryPath);
        Table test = new DBaseTable(testDirectory, tableName);

        assertNull(test.put(testKey1, testValue1));

        assertEquals(testValue1, test.get(testKey1));
    }

    @Test
    public final void testGetCalledForComittedKey() throws IOException {
        Files.createDirectory(tableDirectoryPath);
        Table test = new DBaseTable(testDirectory, tableName);

        assertNull(test.put(testKey1, testValue1));

        test.commit();

        assertEquals(testValue1, test.get(testKey1));
    }

    @Test
    public final void testGetCalledForDeletedKeyBeforeCommit() throws IOException {
        Files.createDirectory(tableDirectoryPath);
        Table test = new DBaseTable(testDirectory, tableName);

        assertNull(test.put(testKey1, testValue1));

        assertEquals(testValue1, test.remove(testKey1));

        assertNull(test.get(testKey1));
    }

    //PutTests.
    @Test(expected = IllegalArgumentException.class)
    public final void testPutThrowsIllegalArgumentExceptionCalledForNullKey() throws IOException {
        Files.createDirectory(tableDirectoryPath);
        Table test = new DBaseTable(testDirectory, tableName);
        test.put(null, testValue1);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testPutThrowsExceptionCalledForNullValue() throws IOException {
        Files.createDirectory(tableDirectoryPath);
        Table test = new DBaseTable(testDirectory, tableName);
        test.put(testKey1, null);
    }

    @Test
    public final void testPutReturnsNullIfKeyHasNotBeenWrittenYet() throws IOException {
        Files.createDirectory(tableDirectoryPath);
        Table test = new DBaseTable(testDirectory, tableName);

        assertNull(test.put(testKey1, testValue1));
    }

    @Test
    public final void testPutReturnsOldValueIfKeyExists() throws IOException {
        Files.createDirectory(tableDirectoryPath);
        Table test = new DBaseTable(testDirectory, tableName);
        test.put(testKey1, testValue1);

        assertEquals(testValue1, test.put(testKey1, testValue2));
    }

    //RemoveTests.
    @Test(expected = IllegalArgumentException.class)
    public final void testRemoveThrowsExceptionCalledForNullKey() throws IOException {
        Files.createDirectory(tableDirectoryPath);
        Table test = new DBaseTable(testDirectory, tableName);
        test.remove(null);
    }

    @Test
    public final void testRemoveReturnsNullIfKeyIsNotFound() throws Exception {
        Files.createDirectory(tableDirectoryPath);
        Table test = new DBaseTable(testDirectory, tableName);

        assertNull(test.remove(testKey1));
    }

    @Test
    public final void testRemoveCalledForDeletedKeyBeforeCommit() throws IOException {
        Files.createDirectory(tableDirectoryPath);
        Table test = new DBaseTable(testDirectory, tableName);

        assertNull(test.put(testKey1, testValue1));

        assertEquals(testValue1, test.remove(testKey1));

        assertNull(test.remove(testKey1));
    }

    @Test
    public final void testRemoveCalledForDeletedKeyAfterCommit() throws IOException {
        Files.createDirectory(tableDirectoryPath);
        Table test = new DBaseTable(testDirectory, tableName);

        assertNull(test.put(testKey1, testValue1));

        assertEquals(testValue1, test.remove(testKey1));

        test.commit();

        assertNull(test.remove(testKey1));
    }

    @Test
    public final void testCommitCreatesRealFileOnTheDisk() throws IOException {
        Files.createDirectory(tableDirectoryPath);
        Table test = new DBaseTable(testDirectory, tableName);

        assertNull(test.put(testKey1, testValue1));

        test.commit();
        String subdirectoryName = Math.abs(testKey1.getBytes("UTF-8")[0] % SIZEDIR) + ".dir";
        String fileName = Math.abs((testKey1.getBytes("UTF-8")[0] / SIZEDIR) % SIZEDAT) + ".dat";
        Path filePath = Paths.get(testDirectory.toString(), test.getName(), subdirectoryName, fileName);

        assertTrue(filePath.toFile().exists());
    }

    @Test
    public final void testCommitOverwritesCommitedKey() throws IOException {
        Files.createDirectory(tableDirectoryPath);
        Table test = new DBaseTable(testDirectory, tableName);

        assertNull(test.put(testKey1, testValue1));

        test.commit();

        assertEquals(testValue1, test.put(testKey1, testValue2));

        test.commit();

        assertEquals(testValue2, test.get(testKey1));
    }

    @Test
    public final void testCommitRemovesExistentKeyAfterCommit() throws IOException {
        Files.createDirectory(tableDirectoryPath);
        Table test = new DBaseTable(testDirectory, tableName);

        assertNull(test.put(testKey1, testValue1));

        test.commit();

        assertEquals(testValue1, test.remove(testKey1));

        test.commit();

        assertNull(test.get(testKey1));
    }

    @Test
    public final void testCommitEmptiedAfterLoadingTable() throws IOException {
        Files.createDirectory(tableDirectoryPath);
        Table test = new DBaseTable(testDirectory, tableName);

        assertNull(test.put(testKey1, testValue1));

        test.commit();

        assertEquals(testValue1, test.remove(testKey1));

        test.commit();
        String subdirectoryName = Math.abs(testKey1.getBytes()[0] % SIZEDIR) + ".dir";
        String fileName = Math.abs((testKey1.getBytes()[0] / SIZEDIR) % SIZEDAT) + ".dat";
        Path filePath = Paths.get(testDirectory.toString(), test.getName(), subdirectoryName, fileName);

        assertFalse(filePath.toFile().exists());
    }

    @Test
    public final void testCommitReturnsNonZeroChangesPuttingNewRecord() throws IOException {
        Files.createDirectory(tableDirectoryPath);
        Table test = new DBaseTable(testDirectory, tableName);

        assertNull(test.put(testKey1, testValue1));

        assertEquals(1, test.commit());
    }

    @Test
    public final void testCommitReturnsNotZeroChangesRewriting() throws IOException {
        Files.createDirectory(tableDirectoryPath);
        Table test = new DBaseTable(testDirectory, tableName);

        assertNull(test.put(testKey1, testValue1));

        assertEquals(testValue1, test.put(testKey1, testValue2));

        assertEquals(1, test.commit());
    }

    @Test
    public final void testCommitNoChanges() throws IOException {
        Files.createDirectory(tableDirectoryPath);
        Table test = new DBaseTable(testDirectory, tableName);

        assertNull(test.put(testKey1, testValue1));

        assertEquals(1, test.commit());

        assertEquals(0, test.commit());
    }

    //RollbackTests.
    @Test
    public final void testRollbackAfterPuttingNewKey() throws IOException {
        Files.createDirectory(tableDirectoryPath);
        Table test = new DBaseTable(testDirectory, tableName);

        assertEquals(0, test.size());

        assertNull(test.put(testKey1, testValue1));

        assertEquals(1, test.size());

        assertEquals(1, test.rollback());

        assertEquals(0, test.size());

        assertNull(test.get(testKey1));
    }

    @Test
    public final void testRollbackNoChanges() throws IOException {
        Files.createDirectory(tableDirectoryPath);
        Table test = new DBaseTable(testDirectory, tableName);

        assertNull(test.put(testKey1, testValue1));

        test.rollback();

        assertEquals(0, test.size());

        assertEquals(0, test.rollback());
    }

    //List tests.
    @Test
    public final void testListCalledForEmptyTable() throws IOException {
        Files.createDirectory(tableDirectoryPath);
        Table test = new DBaseTable(testDirectory, tableName);

        assertTrue(test.list().isEmpty());
    }

    @Test
    public final void testListCalledForNonEmptyNewTable() throws IOException {
        Files.createDirectory(tableDirectoryPath);
        Table test = new DBaseTable(testDirectory, tableName);

        assertNull(test.put(testKey1, testValue1));

        assertNull(test.put(testKey2, testValue2));

        Set<String> expectedKeySet = new HashSet<>();
        expectedKeySet.add(testKey1);
        expectedKeySet.add(testKey2);
        Set<String> tableKeySet = new HashSet<>();
        tableKeySet.addAll(test.list());

        assertEquals(expectedKeySet, tableKeySet);
    }

    @Test
    public final void testListCalledForNonEmptyCommitedTable() throws IOException {
        Files.createDirectory(tableDirectoryPath);
        Table test = new DBaseTable(testDirectory, tableName);

        assertNull(test.put(testKey1, testValue1));

        assertNull(test.put(testKey2, testValue2));

        test.commit();

        assertEquals(testValue2, test.remove(testKey2));

        Set<String> expectedKeySet = new HashSet<>();
        expectedKeySet.add(testKey1);
        Set<String> tableKeySet = new HashSet<>();
        tableKeySet.addAll(test.list());

        assertEquals(expectedKeySet, tableKeySet);
    }

    //Size tests.
    @Test
    public final void testSizeCalledForNonEmptyNonCommitedTable() throws IOException {
        Files.createDirectory(tableDirectoryPath);
        Table test = new DBaseTable(testDirectory, tableName);

        assertNull(test.put(testKey1, testValue1));

        assertNull(test.put(testKey2, testValue2));

        assertEquals(testValue2, test.remove(testKey2));

        assertEquals(1, test.size());
    }

    @Test
    public final void testSizeCalledForNonEmptyCommitedTable() throws IOException {
        Files.createDirectory(tableDirectoryPath);
        Table test = new DBaseTable(testDirectory, tableName);

        assertNull(test.put(testKey1, testValue1));

        assertNull(test.put(testKey2, testValue2));

        test.commit();

        assertEquals(testValue2, test.remove(testKey2));

        assertEquals(1, test.size());
    }
}