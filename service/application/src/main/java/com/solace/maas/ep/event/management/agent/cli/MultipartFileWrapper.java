package com.solace.maas.ep.event.management.agent.cli;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;

@Data
@Builder
public class MultipartFileWrapper implements MultipartFile, Serializable {

    private final String absolutePath;

    @Override
    public String getName() {
        return absolutePath;
    }

    @Override
    public String getOriginalFilename() {
        return null;
    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public long getSize() {
        return 0;
    }

    @Override
    public byte[] getBytes() {
        return new byte[0];
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new DataInputStream(Files.newInputStream(Paths.get(absolutePath)));
    }

    @Override
    public void transferTo(File dest) {

    }
}

