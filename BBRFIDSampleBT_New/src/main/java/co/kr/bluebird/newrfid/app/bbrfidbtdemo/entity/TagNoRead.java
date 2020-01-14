package co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity;

import java.util.List;

public class TagNoRead {
    public List<String> epc;

    public List<String> getEpc() {
        return epc;
    }

    public void setEpc(List<String> epc) {
        this.epc = epc;
    }

    public TagNoRead(List<String> epc) {
        this.epc = epc;
    }
}
