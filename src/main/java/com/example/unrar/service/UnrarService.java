package com.example.unrar.service;

import com.example.unrar.dto.AttachmentDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UnrarService {

    List<AttachmentDTO> unrarPasswordProtectedArchive (AttachmentDTO archive, String password);

}
