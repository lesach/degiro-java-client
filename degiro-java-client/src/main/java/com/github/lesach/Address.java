package com.github.lesach;

// [DataContract]
public class Address
{
    // [DataMember( Name = "streetAddress")] 
    public String streetAddress;
    // [DataMember( Name = "streetAddressNumber")]
    public String streetAddressNumber;
    // [DataMember( Name = "streetAddressExt")]
    public String streetAddressExt;
    // [DataMember( Name = "zip")]
    public String zip;
    // [DataMember( Name = "city")]
    public String city;
    // [DataMember( Name = "country")]
    public String country;
    // [DataMember( Name = "postalCode", IsRequired = false)]
    public String postalCode;
}
