package com.solutions.ibarra.coolreceipts.pojo;

/**
 * Created by Asrock01 on 3/14/2015.
 */
public class IssuedPOJO {
    String transact_id, hospital_no, patient_lastname, patient_firstname, hospital_start, hospital_end, dateUploaded, or_url;
    String status, patients, date_received, hospital, amount, bank_app_no, deposit_conf_no;

    public String getOr_url() {
        return or_url;
    }

    public void setOr_url(String or_url) {
        this.or_url = or_url;
    }

    public String getTransact_id() {
        return transact_id;
    }

    public void setTransact_id(String transact_id) {
        this.transact_id = transact_id;
    }

    public String getHospital_no() {
        return hospital_no;
    }

    public void setHospital_no(String hospital_no) {
        this.hospital_no = hospital_no;
    }

    public String getPatient_lastname() {
        return patient_lastname;
    }

    public void setPatient_lastname(String patient_lastname) {
        this.patient_lastname = patient_lastname;
    }

    public String getPatient_firstname() {
        return patient_firstname;
    }

    public void setPatient_firstname(String patient_firstname) {
        this.patient_firstname = patient_firstname;
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

    public String getDateUploaded() {
        return dateUploaded;
    }

    public void setDateUploaded(String dateUploaded) {
        this.dateUploaded = dateUploaded;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPatients() {
        return patients;
    }

    public void setPatients(String patients) {
        this.patients = patients;
    }

    public String getDate_received() {
        return date_received;
    }

    public void setDate_received(String date_received) {
        this.date_received = date_received;
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

    public IssuedPOJO(String status, String patients, String date_received, String hospital, String amount, String bank_app_no, String deposit_conf_no) {
        this.status = status;
        this.patients = patients;
        this.date_received = date_received;
        this.hospital = hospital;
        this.amount = amount;
        this.bank_app_no = bank_app_no;
        this.deposit_conf_no = deposit_conf_no;
    }

    public IssuedPOJO(String patients, String transact_id, String hospital_no, String patient_lastname, String patient_firstname, String hospital_start, String hospital_end, String dateUploaded, String amount, String hospital, String status, String bank_app_no, String deposit_conf_no, String or_url ){
        this.patients = patients;
        this.transact_id = transact_id;
        this.hospital_no = hospital_no;
        this.patient_lastname = patient_lastname;
        this.patient_firstname = patient_firstname;
        this.hospital_start = hospital_start;
        this.hospital_end = hospital_end;
        this.dateUploaded = dateUploaded;
        this.amount = amount;
        this.hospital = hospital;
        this.status = status;
        this.bank_app_no = bank_app_no;
        this.deposit_conf_no = deposit_conf_no;
        this.or_url = or_url;
    }
}
