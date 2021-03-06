package com.github.lesach.client;

import com.github.lesach.client.raw.Value;

import java.util.ArrayList;
import java.util.List;

// [DataContract]
public class DPortfolio
{
    // [DataMember(Name = "lastUpdated")]
    public long lastUpdated;
    // [DataMember(Name = "name")]
    public String name;
    // [DataMember(Name = "value")]
    public List<Value> value;
    // [DataMember(Name = "isAdded")]
    public boolean isAdded;

    public DPortfolio()
    {
        value = new ArrayList<Value>();
    }
}

