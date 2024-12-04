package tn.pfe.CnotConnectV1.dto;

import java.time.Instant;

public class FileDetails {
    private String name;
    private long size; 
    private Instant lastModified;

    public FileDetails(String name, long size, Instant lastModified) {
        this.name = name;
        this.size = size;
        this.lastModified = lastModified;
    }

   

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public Instant getLastModified() {
        return lastModified;
    }

    public void setLastModified(Instant lastModified) {
        this.lastModified = lastModified;
    }
}