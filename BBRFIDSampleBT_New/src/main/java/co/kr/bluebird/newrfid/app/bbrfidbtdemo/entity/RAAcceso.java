package co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity;

import java.util.List;

public class RAAcceso {
    public List<RAPrograma> programas;

    public List<RAPrograma> getProgramas() {
        return programas;
    }

    public void setProgramas(List<RAPrograma> programas) {
        this.programas = programas;
    }

    public RAAcceso(List<RAPrograma> programas) {
        this.programas = programas;
    }
}
