package br.tec.gtech.pdf.dto;

import java.util.Collections;
import java.util.Map;

public class PdfRequestDto {

    private String documentTemplateName;
    private final Map<String, Object> dataMap;

    public PdfRequestDto(String documentTemplateName, Map<String, Object> dataMap) {
        this.documentTemplateName = documentTemplateName;
        this.dataMap = dataMap;
    }

    public String getDocumentTemplateName() {
        return documentTemplateName;
    }

    public Map<String, Object> getDataMap() {
        return Collections.unmodifiableMap(dataMap);
    }
}
