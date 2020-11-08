package com.github.lesach.strategy.engine;

import com.github.lesach.client.DNewOrder;
import com.github.lesach.client.DOrder;
import com.github.lesach.client.DOrderConfirmation;
import com.github.lesach.client.DPlacedOrder;

import java.time.LocalDateTime;

public class EngineCommand
{
    public LocalDateTime Date;
    public DNewOrder NewOrder;
    public DOrder Order;
    public DOrderConfirmation OrderConfirmation;
    public DPlacedOrder PlacedOrder;

}

