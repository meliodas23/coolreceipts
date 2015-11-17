package com.solutions.ibarra.coolreceipts.pojo;

/**
 * Created by Asrock01 on 4/4/2015.
 */
public class ApprovedPOJO {

    String hospital_no, lastname, firstname, start_date, end_date, pr_number, amount;
    String transact_id, status, pr_url;

    public String getTransact_id() {
        return transact_id;
    }

    public void setTransact_id(String transact_id) {
        this.transact_id = transact_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPr_url() {
        return pr_url;
    }

    public void setPr_url(String pr_url) {
        this.pr_url = pr_url;
    }

    public String getHospital_no() {
        return hospital_no;
    }

    public void setHospital_no(String hospital_no) {
        this.hospital_no = hospital_no;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getPr_number() {
        return pr_number;
    }

    public void setPr_number(String pr_number) {
        this.pr_number = pr_number;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public ApprovedPOJO(String hospital_no, String lastname, String firstname, String start_date, String end_date, String pr_number, String amount, String transact_id, String status, String pr_url) {
        this.hospital_no = hospital_no;
        this.lastname = lastname;
        this.firstname = firstname;
        this.start_date = start_date;
        this.end_date = end_date;
        this.pr_number = pr_number;
        this.amount = amount;
        this.transact_id = transact_id;
        this.status = status;
        this.pr_url = pr_url;

    }


}
