package com.solutions.ibarra.coolreceipts.pojo;

/**
 * Created by Asrock01 on 4/4/2015.
 */
public class BatchPOJO {
    String status, batchno, patients, dateuploaded, amount, timeuploaded;

    public String getTimeuploaded() {
        return timeuploaded;
    }

    public void setTimeuploaded(String timeuploaded) {
        this.timeuploaded = timeuploaded;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBatchno() {
        return batchno;
    }

    public void setBatchno(String batchno) {
        this.batchno = batchno;
    }

    public String getPatients() {
        return patients;
    }

    public void setPatients(String patients) {
        this.patients = patients;
    }

    public String getDateuploaded() {
        return dateuploaded;
    }

    public void setDateuploaded(String dateuploaded) {
        this.dateuploaded = dateuploaded;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public BatchPOJO(String status, String batchno, String patients, String dateuploaded, String amount){
        this.status = status;
        this.batchno = batchno;
        this.patients = patients;
        this.dateuploaded = dateuploaded;
        this.amount = amount;

    }
}
