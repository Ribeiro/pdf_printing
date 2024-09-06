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
import br.tec.gtech.pdf.service.TemplateService;

@Controller
@RequestMapping("/pdf")
public class PdfController {

    private final TemplateService templateService;

    public PdfController(TemplateService templateService) {
        this.templateService = templateService;    
    }

    @PostMapping(value = "/generate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> generatePdf(@RequestBody PdfRequestDto pdfRequestDto) throws Exception {
        ByteArrayInputStream byteArrayInputStream = templateService.getParsedTemplate("defaultTemplate", pdfRequestDto);
        return ResponseEntity
                .ok()
                .headers(getHttpHeadersForInlineFile())
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(byteArrayInputStream));
    }

    private HttpHeaders getHttpHeadersForInlineFile(){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=output.pdf");
        return headers;
    }
}