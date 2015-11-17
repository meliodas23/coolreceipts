package com.solutions.ibarra.coolreceipts.pojo;

/**
 * Created by Asrock01 on 3/14/2015.
 */
public class ApprovalPOJO {
    String status, patients, dateUploaded, hospital, amount, transact_id, hospital_no, hospital_start, hospital_end, patient_firstname, patient_lastname;
    String bank_app_no, deposit_conf_no, pr_url, batchno;

    public String getBatchno() {
        return batchno;
    }

    public void setBatchno(String batchno) {
        this.batchno = batchno;
    }

    int value; // 0 check disable / 1 enable

    public String getPr_url() {
        return pr_url;
    }

    public void setPr_url(String pr_url) {
        this.pr_url = pr_url;
    }

    public String getBank_app_no() {
        return bank_app_no;
    }

    public void setBank_app_no(String bank_app_no) {
        this.bank_app_no = bank_app_no;
    }

    public String getDeposit_conf_no() {
        return deposit_conf_no;
    }

    public void setDeposit_conf_no(String deposit_conf_no) {
        this.deposit_conf_no = deposit_conf_no;
    }


    public String getPatient_lastname() {
        return patient_lastname;
    }

    public void setPatient_lastname(String patient_lastname) {
        this.patient_lastname = patient_lastname;
    }

    public String getHospital_no() {
        return hospital_no;
    }

    public void setHospital_no(String hospital_no) {
        this.hospital_no = hospital_no;
    }

    public String getHospital_start() {
        return hospital_start;
    }

    public void setHospital_start(String hospital_start) {
        this.hospital_start = hospital_start;
    }

    public String getHospital_end() {
        return hospital_end;
    }

    public void setHospital_end(String hospital_end) {
        this.hospital_end = hospital_end;
    }

    public String getPatient_firstname() {
        return patient_firstname;
    }

    public void setPatient_firstname(String patient_firstname) {
        this.patient_firstname = patient_firstname;
    }

    public String getTransact_id() {
        return transact_id;
    }

    public void setTransact_id(String transact_id) {
        this.transact_id = transact_id;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getPatients() {
        return patients;
    }

    public void setPatients(String patients) {
        this.patients = patients;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDateUploaded() {
        return dateUploaded;
    }

    public void setDateUploaded(String dateUploaded) {
        this.dateUploaded = dateUploaded;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }


    public ApprovalPOJO(String status, String patients, String dateUploaded, String hospital, String amount, String transact_id, String hospital_no, String hospital_start, String hospital_end, String patient_firstname, String patient_lastname) {
        this.status = status;
        this.patients = patients;
        this.dateUploaded = dateUploaded;
        this.hospital = hospital;
        this.amount = amount;
        this.transact_id = transact_id;
        this.hospital_no = hospital_no;
        this.hospital_start = hospital_start;
        this.hospital_end = hospital_end;
        this.patient_firstname = patient_firstname;
        this.patient_lastname = patient_lastname;
    }

    public ApprovalPOJO(String transact_id, String status, String patients, String dateUploaded, String hospital, String amount, String pr_url) {
        this.transact_id = transact_id;
        this.status = status;
        this.patients = patients;
        this.dateUploaded = dateUploaded;
        this.hospital = hospital;
        this.amount = amount;
        this.pr_url = pr_url;

    }

    public ApprovalPOJO(String status, String patients, String dateUploaded, String hospital, String amount) {
        this.status = status;
        this.patients = patients;
        this.dateUploaded = dateUploaded;
        this.hospital = hospital;
        this.amount = amount;
    }


    public ApprovalPOJO(String status, String patients, String dateUploaded, String hospital, String amount, String transact_id, String hospital_no, String hospital_start, String hospital_end, String patient_firstname, String patient_lastname, String bank_app_no, String deposit_conf_no) {
        this.status = status;
        this.patients = patients;
        this.dateUploaded = dateUploaded;
        this.hospital = hospital;
        this.amount = amount;
        this.transact_id = transact_id;
        this.hospital_no = hospital_no;
        this.hospital_start = hospital_start;
        this.hospital_end = hospital_end;
        this.patient_firstname = patient_firstname;
        this.patient_lastname = patient_lastname;
        this.bank_app_no = bank_app_no;
        this.deposit_conf_no = deposit_conf_no;
    }
}
