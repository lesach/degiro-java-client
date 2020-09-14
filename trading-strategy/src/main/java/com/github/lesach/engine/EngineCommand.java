package com.github.lesach.engine;

import com.github.lesach.DNewOrder;
import com.github.lesach.DOrder;
import com.github.lesach.DOrderConfirmation;
import com.github.lesach.DPlacedOrder;

import java.time.LocalDateTime;

public class EngineCommand
{
    public LocalDateTime Date;
    public DNewOrder NewOrder;
    public DOrder Order;
    public DOrderConfirmation OrderConfirmation;
    public DPlacedOrder PlacedOrder;

}

