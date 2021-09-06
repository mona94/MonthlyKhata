package app.prac.monthlykhata.BeanFile;

import java.util.ArrayList;

public class TransactionBean {
    /**
     * amount : 1000
     * date : 8th Feb, 2020
     * note : c/o gdfgfdg
     */

    private String amount;
    private String date;
    private String note;
    private String id;
    private String type;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
