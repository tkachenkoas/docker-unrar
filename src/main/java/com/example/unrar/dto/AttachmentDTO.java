package com.example.unrar.dto;

public class AttachmentDTO {

    private String fileName;
    private byte[] content;

    public AttachmentDTO() {
    }

    public AttachmentDTO(String fileName, byte[] content) {
        this.fileName = fileName;
        this.content = content;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String name) {
        this.fileName = name;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
