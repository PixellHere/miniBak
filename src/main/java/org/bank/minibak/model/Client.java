package org.bank.minibak.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "clients")
public class Client extends User{

    @Column(unique = true, nullable = false)
    private String transferTag;

    @Column(unique = true, nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private BigDecimal balance;

    @Column()
    private String profilePicture;

    protected Client() {}

    public Client(String email, String password, String firstName, String lastName, String transferID, String phoneNumber, String profilePicture) {
        super(email, password, firstName, lastName);
        this.transferTag = transferID;
        this.phoneNumber = phoneNumber;
        this.balance = BigDecimal.ZERO;
        this.profilePicture = profilePicture;
    }

    public String getTransferTag() {
        return transferTag;
    }

    public void setTransferTag(String transferTag) {
        this.transferTag = transferTag;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
}
