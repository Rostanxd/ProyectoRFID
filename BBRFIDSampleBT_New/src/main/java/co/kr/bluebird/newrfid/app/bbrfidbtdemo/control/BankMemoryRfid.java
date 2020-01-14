package co.kr.bluebird.newrfid.app.bbrfidbtdemo.control;

public class BankMemoryRfid {
    public String epc;
    public String tid;
    public String user;
    public String reserved_;

    public BankMemoryRfid(String epc, String tid, String user, String reserved_) {
        this.epc = epc;
        this.tid = tid;
        this.user = user;
        this.reserved_ = reserved_;
    }
}
