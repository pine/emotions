package moe.pine.emotions.spring_utils;

import org.springframework.core.io.ByteArrayResource;

public class NamedByteArrayResource extends ByteArrayResource {
    private final String filename;

    public NamedByteArrayResource(byte[] byteArray, String filename) {
        super(byteArray);
        this.filename = filename;
    }

    @Override
    public String getFilename() {
        return filename;
    }

    @Override
    public boolean equals(Object other) {
        return super.equals(other);
    }

    @Override
    @SuppressWarnings("RedundantMethodOverride")
    public int hashCode() {
        return super.hashCode();
    }
}
