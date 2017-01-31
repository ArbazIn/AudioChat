package com.example.arbaz.audiochat.Model;

import java.io.Serializable;

/**
 * Created by arbaz on 7/10/16.
 */
public class ContactList implements Serializable {
    public String ContactImage;
    public String ContactName;
    public String ContactNumber;

    public ContactList(String contactImage, String contactName, String contactNumber) {
        ContactImage = contactImage;
        ContactName = contactName;
        ContactNumber = contactNumber;
    }

    public ContactList() {
    }

    public String getContactImage() {
        return ContactImage;
    }

    public void setContactImage(String contactImage) {
        ContactImage = contactImage;
    }

    public String getContactName() {
        return ContactName;
    }

    public void setContactName(String contactName) {
        ContactName = contactName;
    }

    public String getContactNumber() {
        return ContactNumber;
    }

    public void setContactNumber(String contactNumber) {
        ContactNumber = contactNumber;
    }
}