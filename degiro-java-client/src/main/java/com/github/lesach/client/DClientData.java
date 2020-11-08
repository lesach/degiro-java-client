package com.github.lesach.client;

// [DataContract]
public class DClientData
{
    // [DataMember(Name = "id")]
    public long id;
    // [DataMember(Name = "intAccount")]
    public long intAccount;
    // [DataMember(Name = "clientRole")]
    public String clientRole;
    // [DataMember(Name = "effectiveClientRole")]
    public String effectiveClientRole;
    // [DataMember(Name = "contractType")]
    public String contractType;
    // [DataMember(Name = "username")]
    public String username;
    // [DataMember(Name = "displayName")]
    public String displayName;
    // [DataMember(Name = "email")]
    public String email;
    // [DataMember(Name = "firstContact")]
    public FirstContact firstContact;
    // [DataMember(Name = "address")]
    public Address address;
    // [DataMember(Name = "phoneNumber", IsRequired = false)]
    public String phoneNumber;
    // [DataMember(Name = "cellphoneNumber")]
    public String cellphoneNumber;
    // [DataMember(Name = "locale")]
    public String locale;
    // [DataMember(Name = "language")]
    public String language;
    // [DataMember(Name = "culture")]
    public String culture;
    // [DataMember(Name = "bankAccount")]
    public BankAccount bankAccount;
    // [DataMember(Name = "memberCode")]
    public String memberCode;
    // [DataMember(Name = "isWithdrawalAvailable")]
    public boolean isWithdrawalAvailable;
    // [DataMember(Name = "isAllocationAvailable")]
    public boolean isAllocationAvailable;
    // [DataMember(Name = "isIskClient")]
    public boolean isIskClient;
    // [DataMember(Name = "isCollectivePortfolio")]
    public boolean isCollectivePortfolio;
    // [DataMember(Name = "isAmClientActive")]
    public boolean isAmClientActive;
    // [DataMember(Name = "canUpgrade")]
    public boolean canUpgrade;
}
