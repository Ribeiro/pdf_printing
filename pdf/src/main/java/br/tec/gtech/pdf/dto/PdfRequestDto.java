package br.tec.gtech.pdf.dto;

import java.util.List;

public class PdfRequestDto {

    private List<String> strings;

    public List<String> getStrings() {
        return strings;
    }

    public void setStrings(List<String> strings) {
        this.strings = strings;
    }
}