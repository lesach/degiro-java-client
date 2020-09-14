package com.github.lesach;

// [DataContract]
public class BankAccount
{
    // [DataMember(Name = "bankAccountId")]
    public long bankAccountId;
    // [DataMember( Name = "iban")]
    public String iban;
    // [DataMember( Name = "bic")]
    public String bic;
    // [DataMember( Name = "name")]
    public String name;
    // [DataMember( Name = "number")]
    public String number;
    // [DataMember( Name = "sortCode")]
    public String sortCode;
    // [DataMember(Name = "status")]
    public String status;
}
