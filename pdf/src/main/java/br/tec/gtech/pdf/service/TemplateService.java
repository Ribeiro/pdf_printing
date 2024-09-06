package br.tec.gtech.pdf.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
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

    private static final PDType1Font FONT = PDType1Font.HELVETICA;
    private static final float FONT_SIZE = 12;
    private static final float LEADING = 1.5f * FONT_SIZE; // Espaçamento entre linhas
    private static final float MARGIN = 25;
    private static final float PAGE_HEIGHT = PDRectangle.A4.getHeight() - 2 * MARGIN;
    private static final float PAGE_WIDTH = PDRectangle.A4.getWidth() - 2 * MARGIN;
    private float Y_POSITION = PAGE_HEIGHT - MARGIN;
    private static final String SPACE = "\\s+";

    public TemplateService(VelocityEngine velocityEngine, TemplateRepository templateRepository) {
        this.velocityEngine = velocityEngine;
        this.templateRepository = templateRepository;
    }

    public Template findByName(String name) {
        // return templateRepository.findByName(name).orElseThrow(() -> new
        // RuntimeException("Template not found"));
        return getTemplate();
    }

    public ByteArrayInputStream getParsedTemplate(String name, PdfRequestDto pdfRequestDto) throws IOException {
        Template template = findByName(name);
        String content = getHtmlContent(template, pdfRequestDto);

        PDDocument pdfDocument = new PDDocument();

        // Inicializando a lista para armazenar as linhas que cabem em uma página
        List<String> lines = new ArrayList<>();
        List<String> words = Arrays.asList(content.split(SPACE));
        StringBuilder line = new StringBuilder();

        // Itera sobre as palavras e quebra em linhas que cabem na largura da página
        for (String word : words) {
            if (FONT.getStringWidth(line + word) / 1000 * FONT_SIZE > PAGE_WIDTH) {
                lines.add(line.toString());
                line = new StringBuilder(word);
            } else {
                if (line.length() > 0) {
                    line.append(" ");
                }
                line.append(word);
            }
        }
        if (line.length() > 0) {
            lines.add(line.toString());
        }

        // Itera sobre as linhas para adicionar texto em múltiplas páginas
        PDPageContentStream contentStream = null;
        for (String currentLine : lines) {
            if (Y_POSITION - LEADING < MARGIN || contentStream == null) {
                if (contentStream != null) {
                    contentStream.endText();
                    contentStream.close();
                }
                PDPage page = new PDPage(PDRectangle.A4);
                pdfDocument.addPage(page);
                contentStream = new PDPageContentStream(pdfDocument, page);
                contentStream.setFont(FONT, FONT_SIZE);
                contentStream.beginText();
                contentStream.newLineAtOffset(MARGIN, Y_POSITION);
            }
            contentStream.showText(currentLine);
            contentStream.newLineAtOffset(0, -LEADING);
            Y_POSITION -= LEADING;
        }

        if (contentStream != null) {
            contentStream.endText();
            contentStream.close();
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        pdfDocument.save(byteArrayOutputStream);
        pdfDocument.close();

        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    }

    private Template getTemplate() {
        Template template = new Template();
        template.setName("defaultTemplate");
        template.setContent(
                "<html><body><h1>PDF Generation</h1><ul>#foreach($string in $strings)<li>$string</li>#end</ul></body></html>");
        return template;
    }

    private String getHtmlContent(Template template, PdfRequestDto pdfRequestDto) {
        VelocityContext context = new VelocityContext();
        context.put("strings", pdfRequestDto.getStrings());

        StringWriter writer = new StringWriter();
        velocityEngine.evaluate(context, writer, "Template", template.getContent());

        return writer.toString();
    }

}