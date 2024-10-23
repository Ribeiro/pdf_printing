package br.tec.gtech.pdf.controller;

import java.io.ByteArrayInputStream;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import br.tec.gtech.pdf.dto.PdfRequestDto;
import br.tec.gtech.pdf.service.DocumentTemplateService;

@Controller
@RequestMapping("/pdf")
public class PdfController {

    private static final String INLINE_FILENAME_OUTPUT_PDF = "inline; filename=output.pdf";
    private static final String CONTENT_DISPOSITION = "Content-Disposition";
    private final DocumentTemplateService templateService;

    public PdfController(DocumentTemplateService templateService) {
        this.templateService = templateService;    
    }

    @PostMapping(value = "/generate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> generatePdf(@RequestBody PdfRequestDto pdfRequestDto) throws Exception {
        ByteArrayInputStream byteArrayInputStream = templateService.generatePdf(pdfRequestDto);
        return ResponseEntity
                .ok()
                .headers(getHttpHeadersForInlineFile())
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(byteArrayInputStream));
    }

    private HttpHeaders getHttpHeadersForInlineFile(){
        HttpHeaders headers = new HttpHeaders();
        headers.add(CONTENT_DISPOSITION, INLINE_FILENAME_OUTPUT_PDF);
        return headers;
    }
}