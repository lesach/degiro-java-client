package com.github.lesach.raw;

import java.util.List;

// [DataContract]
public class RawOrders
{
    // [DataMember(Name = "lastUpdated")]
    public long lastUpdated;
    // [DataMember(Name = "name")]
    public String name;
    // [DataMember(Name = "value")]
    public List<Value> value = null;
}
