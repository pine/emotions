package moe.pine.emotions.cloudstorage;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
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
    public Storage storage;

    @Mock
    public GoogleCredentials credentials;

    @Mock
    public Blob blob;

    @Test
    public void constructorTest() {
        new CloudStorage(credentials);
    }

    @Test
    public void getTest() {
        final CloudStorage cloudStorage = new CloudStorage(storage);

        final BlobId blobId = BlobId.of("bucket", "name");
        when(storage.get(blobId)).thenReturn(blob);
        when(blob.getContent(Blob.BlobSourceOption.generationMatch()))
            .thenReturn(new byte[]{0x12, 0x34});

        final byte[] content = cloudStorage.get("bucket", "name");
        assertArrayEquals(new byte[]{0x12, 0x34}, content);
    }

    @Test
    public void getEmptyBucketTest() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("`bucket` should not be empty");

        final CloudStorage cloudStorage = new CloudStorage(storage);
        cloudStorage.get("", "name");
    }

    @Test
    public void getEmptyNameTest() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("`name` should not be empty");

        final CloudStorage cloudStorage = new CloudStorage(storage);
        cloudStorage.get("bucket", "");
    }

}
