package br.tec.gtech.pdf.dto;

import java.util.HashMap;
import java.util.Map;

public class PdfRequestDto {

    private static final String PARAGRAFO_3 = "paragrafo3";
    private static final String PARAGRAFO_2 = "paragrafo2";
    private static final String PARAGRAFO_1 = "paragrafo1";
    private String paragrafo1;
    private String paragrafo2;
    private String paragrafo3;

    public String getParagrafo1() {
        return paragrafo1;
    }
    public void setParagrafo1(String paragrafo1) {
        this.paragrafo1 = paragrafo1;
    }

    public String getParagrafo2() {
        return paragrafo2;
    }
    public void setParagrafo2(String paragrafo2) {
        this.paragrafo2 = paragrafo2;
    }

    public String getParagrafo3() {
        return paragrafo3;
    }
    public void setParagrafo3(String paragrafo3) {
        this.paragrafo3 = paragrafo3;
    }

    public Map<String, Object> getDataMap(){
        Map<String, Object> data = new HashMap<>();
        data.put(PARAGRAFO_1, paragrafo1);
        data.put(PARAGRAFO_2, paragrafo2);
        data.put(PARAGRAFO_3, paragrafo3);
        return data;
    }
}