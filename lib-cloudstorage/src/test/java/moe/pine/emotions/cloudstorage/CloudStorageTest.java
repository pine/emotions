package moe.pine.emotions.cloudstorage;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CloudStorageTest {
    @Mock
    private Storage storage;

    @Mock
    private GoogleCredentials credentials;

    @Mock
    private Blob blob;

    private CloudStorage cloudStorage;

    @BeforeEach
    public void setUp() {
        cloudStorage = new CloudStorage(storage);
    }

    @Test
    public void fromStreamTest() throws IOException {
        final InputStream inputStream = mock(InputStream.class);
        lenient().when(inputStream.read()).thenThrow(IOException.class);
        lenient().when(inputStream.read(any(byte[].class))).thenThrow(IOException.class);
        lenient().when(inputStream.read(any(byte[].class), anyInt(), anyInt())).thenThrow(IOException.class);

        assertThatThrownBy(() -> CloudStorage.fromStream(inputStream))
            .isInstanceOf(CloudStorageException.class)
            .hasCauseInstanceOf(IOException.class);
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
        assertThatThrownBy(() -> cloudStorage.get("", "name"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageStartingWith("`bucket` should not be empty");
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    public void getTest_nullBucket() {
        assertThatThrownBy(() -> cloudStorage.get(null, "name"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageStartingWith("`bucket` should not be empty");
    }

    @Test
    public void getTest_emptyName() {
        assertThatThrownBy(() -> cloudStorage.get("bucket", ""))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageStartingWith("`name` should not be empty");
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    public void getTest_nullName() {
        assertThatThrownBy(() -> cloudStorage.get("bucket", null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageStartingWith("`name` should not be empty");
    }

    @Test
    public void getTest_blobNotFound() {
        final BlobId blobId = BlobId.of("bucket", "name");
        when(storage.get(blobId)).thenReturn(null);

        assertThatThrownBy(() -> cloudStorage.get("bucket", "name"))
            .isInstanceOf(CloudStorageException.class)
            .hasMessageStartingWith("Not found :: blobId=");
    }

    @Test
    public void getTest_storageException() {
        final BlobId blobId = BlobId.of("bucket", "name");
        when(storage.get(blobId)).thenReturn(blob);
        when(blob.getContent(any())).thenThrow(StorageException.class);

        assertThatThrownBy(() -> cloudStorage.get("bucket", "name"))
            .isInstanceOf(CloudStorageException.class)
            .hasCauseInstanceOf(StorageException.class);
    }
}
