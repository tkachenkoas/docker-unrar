package com.example.unrar.controller;

import com.example.unrar.dto.AttachmentDTO;
import com.example.unrar.service.UnrarService;
import org.springframework.core.env.Environment;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
public class UnrarController {

    private Environment env;

    private UnrarService unrarService;

    public UnrarController(UnrarService unrarService, Environment env) {
        this.unrarService = unrarService;
        this.env = env;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("system", env.getProperty("running.env"));
        return "index";
    }

    @PostMapping(value = "/unrar", consumes = {"multipart/form-data"})
    @ResponseBody
    public HttpEntity<byte[]> unrar(@RequestParam(value = "archive", required = true) MultipartFile archive,
                                    @RequestParam(value = "password") String password) throws Exception {
        List<AttachmentDTO> resultList = unrarService.unrarPasswordProtectedArchive(archive.getBytes(), password);
        AttachmentDTO result = resultList.get(0);

        HttpHeaders header = new HttpHeaders();
        header.setContentDisposition(ContentDisposition.builder("attachment")
                                                       .filename(result.getFileName(), StandardCharsets.UTF_8)
                                                        .build());
        header.setContentLength(result.getContent().length);
        return new HttpEntity<byte[]>(result.getContent(), header);
    }

}
