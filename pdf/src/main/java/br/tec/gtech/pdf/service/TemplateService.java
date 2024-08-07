package br.tec.gtech.pdf.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.stereotype.Service;
import br.tec.gtech.pdf.dto.PdfRequestDto;
import br.tec.gtech.pdf.model.Template;
import br.tec.gtech.pdf.repository.TemplateRepository;

@Service
public class TemplateService {

    private final VelocityEngine velocityEngine;
    private final TemplateRepository templateRepository;

    public TemplateService(VelocityEngine velocityEngine, TemplateRepository templateRepository) {
        this.velocityEngine = velocityEngine;
        this.templateRepository = templateRepository;
    }

    public Template findByName(String name) {
        //return templateRepository.findByName(name).orElseThrow(() -> new RuntimeException("Template not found"));
        return getTemplate();
    }

    public ByteArrayInputStream getParsedTemplate(String name, PdfRequestDto pdfRequestDto) throws IOException {
        Template template = findByName(name);

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

        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    }

    private Template getTemplate(){
        Template template = new Template();
        template.setName("defaultTemplate");
        template.setContent("<html><body><h1>PDF Generation</h1><ul>#foreach($string in $strings)<li>$string</li>#end</ul></body></html>");
        return template;
    }
}