package com.github.lesach;

// [DataContract]
public class DPriceHistorySerie
{
    // [DataMember(Name = "times")]
    public String times;
    // [DataMember(Name = "expires")]
    public String expires;
    // [DataMember(Name = "data")]
    public DPriceHistorySerieData data;
    // [DataMember(Name = "id")]
    public String id;
    // [DataMember(Name = "type")]
    public String type;
}
