package br.tec.gtech.pdf.repository;

import org.springframework.stereotype.Repository;

@Repository
public class TemplateRepository  {
    public String getDefaultTemplate(){
        String templateContent = "<!DOCTYPE html>\n" +
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
        return templateContent;
    }
}