package moe.pine.emotions.cloudstorage;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageException;
import com.google.cloud.storage.StorageOptions;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class CloudStorage {
    private final Storage storage;

    public CloudStorage(@NotNull final InputStream credentialsStream) {
        final GoogleCredentials credentials;
        try {
            credentials = GoogleCredentials.fromStream(credentialsStream)
                .createScoped(ImmutableList.of("https://www.googleapis.com/auth/cloud-platform"));
        } catch (IOException e) {
            throw new CloudStorageException(e);
        }

        storage = StorageOptions.newBuilder()
            .setCredentials(credentials)
            .build()
            .getService();
    }

    @VisibleForTesting
    CloudStorage(@NotNull final Storage storage) {
        this.storage = storage;
    }

    public byte[] get(
        @NotNull final String bucket,
        @NotNull final String name
    ) {
        final BlobId blobId = BlobId.of(bucket, name);
        final Blob blob = storage.get(blobId);
        if (blob == null) {
            throw new CloudStorageException(
                String.format("Not found :: blobId=%s", blobId.toString()));
        }
        log.info(String.format(
            "Downloaded blob info :: blob=%s", blob.toString()));

        final byte[] content;
        try {
            content = blob.getContent(Blob.BlobSourceOption.generationMatch());
        } catch (StorageException e) {
            throw new CloudStorageException(e);
        }
        log.info(String.format(
            "Downloaded blob content :: content-length=%d", content.length));

        return content;
    }
}
