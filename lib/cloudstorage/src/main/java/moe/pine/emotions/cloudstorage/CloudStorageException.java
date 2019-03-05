package moe.pine.emotions.cloudstorage;


@SuppressWarnings("WeakerAccess")
public class CloudStorageException extends RuntimeException {
    CloudStorageException(String message) {
        super(message);
    }

    CloudStorageException(Throwable cause) {
        super(cause);
    }
}
