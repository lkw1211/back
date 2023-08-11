package com.boardgaming.api.application.file;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Objects;

@Slf4j
@Service
public class FileServiceR2Impl implements FileService {
    private final AmazonS3 r2Client;
    private final String bucket;
    private final String publicUrl;

    public FileServiceR2Impl(
        final AmazonS3 r2Client,
        @Value("${r2.bucket}") final String bucket,
        @Value("${r2.publicUrl}") String publicUrl
    ) {
        this.r2Client = r2Client;
        this.bucket = bucket;
        this.publicUrl = publicUrl;
    }

    @Override
    public void putObject(final String key, final MultipartFile file) {
        try {
            InputStream inputStream = file.getInputStream();

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());

            putObject(key, inputStream, metadata);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void putObject(final String key, final InputStream inputStream, final ObjectMetadata objectMetadata) {
        try {
            if (inputStream.markSupported()) {
                inputStream.mark(Integer.MAX_VALUE);
            }
            r2Client.putObject(bucket, key, inputStream, objectMetadata);
            if (inputStream.markSupported()) {
                inputStream.reset();
            }
            inputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteObject(final String key) {
        r2Client.deleteObject(bucket, key);
    }

    @Override
    public String getUrl(final String fileName) {
        return String.join("/", publicUrl, fileName);
    }

    @Override
    public String getKey(final String url) {
        return Objects.nonNull(url) ? url.replaceFirst(publicUrl + "/", "") : null;
    }

    @Override
    public String uploadAndGetUrl(
        final String fileName,
        final String url
    ) {
        try {
            URL fileUrl = new URL(url);
            URLConnection connection = fileUrl.openConnection();

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(connection.getContentType());
            metadata.setContentLength(connection.getContentLength());

            InputStream inputStream = new ByteArrayInputStream(StreamUtils.copyToByteArray(connection.getInputStream()));

            putObject(fileName, inputStream, metadata);

            return getUrl(fileName);
        } catch (IOException e) {
            log.error("uploadAndGetUrl " + e);
            return null;
        }
    }
}
