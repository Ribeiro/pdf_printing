package br.tec.gtech.pdf.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.resource.loader.StringResourceLoader;
import org.springframework.stereotype.Service;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import br.tec.gtech.pdf.dto.PdfRequestDto;
import br.tec.gtech.pdf.repository.TemplateRepository;

@Service
public class TemplateService {
    private static final String EMPTY_STRING = "";
    private static final String STRING_RESOURCE_LOADER_CLASSPATH = "string.resource.loader.classpath";
    private static final String STRING_RESOURCE_LOADER_CLASS = "string.resource.loader.class";
    private static final String STRING_RESOURCE_LOADER_REPOSITORY_CLASS = "string.resource.loader.repository.class";
    private static final String ERROR_PROCESSING_TEMPLATE = "Error processing template";
    private final VelocityEngine velocityEngine;
    private final TemplateRepository templateRepository;

    private final Map<String, String> templateCache = new ConcurrentHashMap<>();

    public TemplateService(VelocityEngine velocityEngine, TemplateRepository templateRepository) {
        this.velocityEngine = velocityEngine;
        this.templateRepository = templateRepository;
        this.velocityEngine.addProperty(STRING_RESOURCE_LOADER_REPOSITORY_CLASS,
                StringResourceLoader.class.getName());
        this.velocityEngine.addProperty(STRING_RESOURCE_LOADER_CLASS, StringResourceLoader.class.getName());
        this.velocityEngine.addProperty(STRING_RESOURCE_LOADER_CLASSPATH, EMPTY_STRING);
    }

    public ByteArrayInputStream getParsedTemplate(String name, PdfRequestDto pdfRequestDto) throws Exception {
        String content = processTemplate(name, pdfRequestDto.getDataMap());
        return generatePdfFromHtml(content);
    }

    private void addTemplateToRepository(String templateName) {
        templateCache.computeIfAbsent(templateName, key -> {
            String templateContent = templateRepository.getDefaultTemplate();
            StringResourceLoader.getRepository().putStringResource(templateName, templateContent);
            return templateContent;
        });
    }

    private String processTemplate(String templateName, Map<String, Object> data) {
        addTemplateToRepository(templateName);
        VelocityContext context = new VelocityContext(data);
        Template template = velocityEngine.getTemplate(templateName);

        try (StringWriter writer = new StringWriter()) {
            template.merge(context, writer);
            return writer.toString();
        } catch (Exception e) {
            throw new RuntimeException(ERROR_PROCESSING_TEMPLATE, e);
        }
    }

    private ByteArrayInputStream generatePdfFromHtml(String htmlContent) throws Exception {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(htmlContent, null);
            builder.toStream(baos);
            builder.run();

            return new ByteArrayInputStream(baos.toByteArray());
        }
    }
}