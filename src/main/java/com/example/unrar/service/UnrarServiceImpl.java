package com.example.unrar.service;

import com.example.unrar.dto.AttachmentDTO;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class UnrarServiceImpl implements UnrarService {

    private static final String ROOT_DIR = System.getProperty("user.home") + "/temp/";
    private static final String TEMP_NAME = "temp_archive.rar";

    @Value( "${unrar.command}" )
    private String unrarCommand;

    @Override
    public List<AttachmentDTO> unrarPasswordProtectedArchive(AttachmentDTO archive, String password) {
        String tempDir = RandomStringUtils.randomAlphabetic(6).toLowerCase();
        try {
            File folder = writeTempFileAndGetFolder(archive, tempDir);
            unrar(folder, password);
            List<AttachmentDTO> result = readExtractedFiles(folder);
            cleanTempFiles(folder);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void cleanTempFiles(File folder) throws IOException {
        FileUtils.deleteDirectory(folder);
    }

    private List<AttachmentDTO> readExtractedFiles(File folder) throws IOException {
        List<AttachmentDTO> result = new ArrayList<>();
        for (File file : folder.listFiles()) {
            if (file.getName().contains(TEMP_NAME)) continue;
            byte[] fileContent = Files.readAllBytes(file.toPath());
            result.add(new AttachmentDTO(file.getName(), fileContent));
        }
        return result;
    }

    private void unrar(File folder, String password) throws IOException, InterruptedException {
        List<String> commands = new ArrayList<>();
        commands.add(unrarCommand);
        if (password != null && !password.isEmpty()) {
            commands.add("-p" + password);
        }
        commands.add("x");
        commands.add(TEMP_NAME);
        commands.add("*.*");
        System.out.println(commands);
        ProcessBuilder pb = new ProcessBuilder(commands);
        pb.directory(folder);
        pb.start().waitFor();
    }

    private File writeTempFileAndGetFolder(AttachmentDTO archive, String tempDir) throws IOException {
        String fileName = ROOT_DIR + tempDir + "/" + TEMP_NAME;
        System.out.println("Target filename: " + fileName);
        File file = new File(fileName);
        file.getParentFile().mkdirs();
        try (FileOutputStream fw = new FileOutputStream(file)){
            fw.write(archive.getContent());
        }
        return new File(file.getParent());
    }

    private String randomString() {
        byte[] array = new byte[7];
        new Random().nextBytes(array);
        return new String(array, Charset.forName("UTF-8"));
    }
}
