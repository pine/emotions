package moe.pine.emotions.cloudstorage;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.junit.Assert.assertArrayEquals;
import static org.mockito.Mockito.when;

public class CloudStorageTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    private Storage storage;

    @Mock
    private GoogleCredentials credentials;

    @Mock
    private Blob blob;

    private CloudStorage cloudStorage;

    @Before
    public void setUp() {
        cloudStorage = new CloudStorage(storage);
    }

    @Test
    public void constructorTest() {
        new CloudStorage(credentials);
    }

    @Test
    public void getTest() {
        final BlobId blobId = BlobId.of("bucket", "name");
        when(storage.get(blobId)).thenReturn(blob);
        when(blob.getContent(Blob.BlobSourceOption.generationMatch()))
            .thenReturn(new byte[]{0x12, 0x34});

        final byte[] content = cloudStorage.get("bucket", "name");
        assertArrayEquals(new byte[]{0x12, 0x34}, content);
    }

    @Test
    public void getTest_emptyBucket() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("`bucket` should not be empty");
        cloudStorage.get("", "name");
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    public void getTest_nullBucket() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("`bucket` should not be empty");
        cloudStorage.get(null, "name");
    }

    @Test
    public void getTest_emptyName() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("`name` should not be empty");
        cloudStorage.get("bucket", "");
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    public void getTest_nullName() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("`name` should not be empty");
        cloudStorage.get("bucket", null);
    }

    @Test
    public void getTest_blobNotFound() {
        expectedException.expect(CloudStorageException.class);
        expectedException.expectMessage("Not found :: blobId=");

        final BlobId blobId = BlobId.of("bucket", "name");
        when(storage.get(blobId)).thenReturn(null);

        cloudStorage.get("bucket", "name");
    }
}
