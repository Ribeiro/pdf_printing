package br.tec.gtech.pdf.repository;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;
import org.springframework.stereotype.Repository;
import br.tec.gtech.pdf.model.DocumentTemplate;

@Repository
public class TemplateRepository {

    private static final String TEMPLATE_CONTENT = "<!DOCTYPE html>\n" +
            "<html>\n" +
            "<head>\n" +
            "    <meta charset=\"UTF-8\" />\n" +
            "    <title>Template de Exemplo</title>\n" +
            "</head>\n" +
            "<body>\n" +
            "    <h1>Exemplo de Documento</h1>\n" +
            "    <p>$paragrafo1</p>\n" +
            "    <p>$paragrafo2</p>\n" +
            "    <p>$paragrafo3</p>\n" +
            "    <p>Este é o quarto parágrafo, que é fixo no template.</p>\n" +
            "    <p>Este é o quinto parágrafo, também fixo no template.</p>\n" +
            "</body>\n" +
            "</html>";

    public List<DocumentTemplate> getAll() {
        return IntStream.rangeClosed(1, 3)
                .mapToObj(i -> {
                    var id = (long) i;
                    return new DocumentTemplate(id, "Template-" + i, TEMPLATE_CONTENT);
                })
                .toList();
    }

}