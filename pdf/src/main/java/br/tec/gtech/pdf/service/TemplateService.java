package br.tec.gtech.pdf.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.resource.loader.StringResourceLoader;
import org.springframework.stereotype.Service;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import br.tec.gtech.pdf.dto.PdfRequestDto;
import br.tec.gtech.pdf.model.DocumentTemplate;
import br.tec.gtech.pdf.repository.TemplateRepository;

@Service
public class TemplateService {
    private final VelocityEngine velocityEngine;
    private final TemplateRepository templateRepository;
    private final Map<String, String> localTemplateCache;

    public TemplateService(VelocityEngine velocityEngine, TemplateRepository templateRepository) {
        this.velocityEngine = velocityEngine;
        this.templateRepository = templateRepository;
        this.localTemplateCache = new ConcurrentHashMap<>();
        templateRepository.getAll().forEach(this::addTemplateToVelocityRepositoryAndLocalCache);
    }

    public ByteArrayInputStream generatePdf(PdfRequestDto pdfRequestDto) throws Exception {
        String content = processTemplate(pdfRequestDto.getDocumentTemplateName(), pdfRequestDto.getDataMap());
        return generatePdfFromHtml(content);
    }

    private void addTemplateToVelocityRepositoryAndLocalCache(DocumentTemplate template) {
        String templateName = template.getName();
        String templateContent = template.getContent();
        localTemplateCache.computeIfAbsent(templateName, key -> {
            StringResourceLoader.getRepository().putStringResource(templateName, templateContent);
            return templateContent;
        });
    }

    private String processTemplate(String templateName, Map<String, Object> data) {
        VelocityContext context = new VelocityContext(data);
        Template template = velocityEngine.getTemplate(templateName);

        try (StringWriter writer = new StringWriter()) {
            template.merge(context, writer);
            return writer.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error processing template", e);
        }
    }

    private ByteArrayInputStream generatePdfFromHtml(String htmlContent) throws Exception {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();

            builder.withHtmlContent(htmlContent, null);
            builder.useFastMode();
            builder.toStream(baos);
            builder.run();

            return new ByteArrayInputStream(baos.toByteArray());
        }
    }
}