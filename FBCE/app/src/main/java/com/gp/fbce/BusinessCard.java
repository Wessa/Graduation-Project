package com.gp.fbce;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by MW on 3/14/2016.
 */
public class BusinessCard implements Serializable {

    private String id;
    private String global_id;
    private String name;
    private String phone;
    private String address;
    private String title;
    private String website;
    private String company;
    private String email;
    private String note;

    public BusinessCard() {

        id = null;
        name = null;
        title = null;
        email = null;
        company = null;
        website = null;
        address = null;
        phone = null;
        note = null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGlobal_id() {
        return global_id;
    }

    public void setGlobal_id(String global_id) {
        this.global_id = global_id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getName() {
        return name;
    }

    public void setName(String fName) {
        this.name = fName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public JSONObject toJson() throws JSONException {

        JSONObject card = new JSONObject();

        final String WEBSITE = "website";
        final String COMPANY = "company";
        final String EMAIL = "email";
        final String TITLE = "title";
        final String NAME = "name";
        final String ADDRESS = "address";
        final String PHONE = "phone";
        //final String ID = "id";

        if ( this.website == null )
            this.website = " ";

        else if ( this.address == null )
            this.address = " ";

        else if ( this.company == null )
            this.company = " ";

        else if ( this.phone == null )
            this.phone = " ";

        else if ( this.name == null )
            this.name = " ";

        else if ( this.title == null )
            this.title = " ";

        else if ( this.email == null )
            this.email = " ";

        card.put(WEBSITE, this.website);
        card.put(COMPANY, this.company);
        card.put(EMAIL, this.email);
        card.put(TITLE, this.title);
        card.put(NAME, this.name);
        card.put(ADDRESS, this.address);
        card.put(PHONE, this.phone);
        //card.put(ID, this.id);

        return card;
    }
}
