package com.github.lesach.client;

import com.github.lesach.client.raw.Value_;

import java.util.List;

// [DataContract]
public class DTotalPortfolio
{
    // [DataMember( Name = "lastUpdated")]
    public long lastUpdated;
    // [DataMember( Name = "name")]
    public String name;
    // [DataMember( Name = "value")]
    public List<Value_> value = null;
    // [DataMember( Name = "isAdded")]
    public boolean isAdded;
}

