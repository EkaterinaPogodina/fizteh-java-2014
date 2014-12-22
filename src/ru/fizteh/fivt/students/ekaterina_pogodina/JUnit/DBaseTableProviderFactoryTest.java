package ru.fizteh.fivt.students.ekaterina_pogodina.JUnit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import ru.fizteh.fivt.storage.strings.TableProvider;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;

import static org.junit.Assert.assertTrue;

public class DBaseTableProviderFactoryTest {
    private Path testDirectory;
    @Rule
    public TemporaryFolder tmpFolder = new TemporaryFolder();

    public DBaseTableProviderFactoryTest() {
    }

    @Before
    public final void setUp() throws Exception {
        String name = tmpFolder.newFolder().getAbsolutePath().toString();
        testDirectory = Paths.get(name);
        if(!testDirectory.toFile().exists()) {
            Files.createDirectory(testDirectory);
        }

    }

    @Test(expected = IllegalArgumentException.class)
    public final void testFactoryThrowsExceptionCreatedNullTableHolder() {
        DataBaseTableProviderFactory test = new DataBaseTableProviderFactory();
        test.create(null);
    }

    @Test
    public final void testTableHolderFactoryCreatedNewValidTableHolder() {
        DataBaseTableProviderFactory test = new DataBaseTableProviderFactory();
        TableProvider testProvider = test.create(this.testDirectory.toString());
        testProvider.createTable("MyTable");
        assertTrue(testDirectory.resolve("MyTable").toFile().exists());
    }
}

