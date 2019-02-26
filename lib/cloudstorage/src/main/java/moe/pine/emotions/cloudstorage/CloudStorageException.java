package moe.pine.emotions.cloudstorage;

public class CloudStorageException extends RuntimeException {
    public CloudStorageException(String message) {
        super(message);
    }

    public CloudStorageException(Throwable cause) {
        super(cause);
    }
}
