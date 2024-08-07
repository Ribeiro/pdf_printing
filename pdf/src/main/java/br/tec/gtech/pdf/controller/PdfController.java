package br.tec.gtech.pdf.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Optional;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import br.tec.gtech.pdf.dto.PdfRequestDto;
import br.tec.gtech.pdf.model.Template;
import br.tec.gtech.pdf.repository.TemplateRepository;

@Controller
@RequestMapping("/pdf")
public class PdfController {

    private final VelocityEngine velocityEngine;
    private final TemplateRepository templateRepository;

    public PdfController(VelocityEngine velocityEngine, TemplateRepository templateRepository) {
        this.velocityEngine = velocityEngine;
        this.templateRepository = templateRepository;
    }

    @PostMapping(value = "/generate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> generatePdf(@RequestBody PdfRequestDto pdfRequestDto) throws IOException {

        Template template = null;
        Optional<Template> templateOptional = templateRepository.findByName("defaultTemplate");
        if (!templateOptional.isPresent()) {
            //return ResponseEntity.status(404).build();
            template = getTemplate();
        }

        //Template template = templateOptional.get();

        VelocityContext context = new VelocityContext();
        context.put("strings", pdfRequestDto.getStrings());

        StringWriter writer = new StringWriter();
        velocityEngine.evaluate(context, writer, "Template", template.getContent());

        String htmlContent = writer.toString();

        PDDocument pdfDocument = new PDDocument();
        PDPage page = new PDPage();
        pdfDocument.addPage(page);

        PDPageContentStream contentStream = new PDPageContentStream(pdfDocument, page);
        contentStream.setFont(PDType1Font.HELVETICA, 12);
        contentStream.beginText();
        contentStream.newLineAtOffset(25, 700);
        contentStream.showText(htmlContent);
        contentStream.endText();
        contentStream.close();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        pdfDocument.save(byteArrayOutputStream);
        pdfDocument.close();

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=output.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(byteArrayInputStream));
    }

    private Template getTemplate(){
        Template template = new Template();
        template.setName("defaultTemplate");
        template.setContent("<html><body><h1>PDF Generation</h1><ul>#foreach($string in $strings)<li>$string</li>#end</ul></body></html>");
        return template;
    }
}