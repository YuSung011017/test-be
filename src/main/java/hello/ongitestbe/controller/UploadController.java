package hello.ongitestbe.controller;

import hello.ongitestbe.dto.UploadResponse;
import hello.ongitestbe.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UploadController {

    private final S3Service s3Service;

    @PostMapping("/upload")
    public UploadResponse upload(@RequestParam String fileName, @RequestParam String contentType) {
        return new UploadResponse(fileName, s3Service.generatePresignedUrl(fileName, contentType));
    }

    @PostMapping("/notify")
    public ResponseEntity<?> notifyGcp(@RequestBody Map<String, String> body) {
        String imageUrl = body.get("url");

        RestTemplate rest = new RestTemplate();
        String gcpUrl = "http://10.178.0.2:8000/analyze"; // GCP 내부 IP 주소
        ResponseEntity<String> res = rest.postForEntity(gcpUrl, Map.of("url", imageUrl), String.class);
        return ResponseEntity.ok(res.getBody());
    }
}