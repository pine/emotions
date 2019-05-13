package moe.pine.emotions.cloudstorage;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageException;
import com.google.cloud.storage.StorageOptions;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

@Slf4j
public class CloudStorage {
    @Nonnull
    private final Storage storage;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static CloudStorage fromStream(@Nonnull final InputStream credentialsStream) {
        checkNotNull(credentialsStream);

        final GoogleCredentials credentials;
        try {
            credentials = GoogleCredentials.fromStream(credentialsStream)
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));
        } catch (IOException e) {
            throw new CloudStorageException(e);
        }

        return new CloudStorage(credentials);
    }

    protected CloudStorage(@Nonnull final GoogleCredentials credentials) {
        this(
            StorageOptions.newBuilder()
                .setCredentials(checkNotNull(credentials))
                .build()
                .getService());
    }

    protected CloudStorage(@Nonnull final Storage storage) {
        this.storage = checkNotNull(storage);
    }

    public byte[] get(
        @Nonnull final String bucket,
        @Nonnull final String name
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
