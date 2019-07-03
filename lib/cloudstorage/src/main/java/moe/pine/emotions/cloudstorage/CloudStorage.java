package moe.pine.emotions.cloudstorage;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageException;
import com.google.cloud.storage.StorageOptions;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;

@Slf4j
public class CloudStorage {
    private final Storage storage;

    public static CloudStorage fromStream(final InputStream credentialsStream) {
        Objects.requireNonNull(credentialsStream);

        final GoogleCredentials credentials;
        try {
            credentials = GoogleCredentials.fromStream(credentialsStream)
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));
        } catch (IOException e) {
            throw new CloudStorageException(e);
        }

        return new CloudStorage(credentials);
    }

    protected CloudStorage(final GoogleCredentials credentials) {
        this(
            StorageOptions.newBuilder()
                .setCredentials(Objects.requireNonNull(credentials))
                .build()
                .getService());
    }

    protected CloudStorage(final Storage storage) {
        this.storage = Objects.requireNonNull(storage);
    }

    public byte[] get(
        final String bucket,
        final String name
    ) {
        checkArgument(StringUtils.isNotEmpty(bucket), "`bucket` should not be empty");
        checkArgument(StringUtils.isNotEmpty(name), "`name` should not be empty");

        final BlobId blobId = BlobId.of(bucket, name);
        final Blob blob = storage.get(blobId);
        if (blob == null) {
            throw new CloudStorageException(
                String.format("Not found :: blobId=%s", blobId.toString()));
        }
        log.info("Downloaded blob info :: blob={}", blob.toString());

        final byte[] content;
        try {
            content = blob.getContent(Blob.BlobSourceOption.generationMatch());
        } catch (StorageException e) {
            throw new CloudStorageException(e);
        }
        log.info("Downloaded blob content :: content-length={}", content.length);

        return content;
    }
}
