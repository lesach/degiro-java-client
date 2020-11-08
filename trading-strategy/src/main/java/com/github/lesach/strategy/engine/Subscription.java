package com.github.lesach.strategy.engine;

import com.github.lesach.client.DPrice;
import com.github.lesach.client.DPriceListener;
import com.github.lesach.strategy.DeGiroClientInterface;
import com.github.lesach.client.exceptions.DeGiroException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;


public class Subscription
{
    private DeGiroClientInterface deGiroClient;
    public Function<DPrice, Boolean> Update;
    private final DPriceListener priceListener;
    public List<DPrice> Prices = new ArrayList<>();

    /// <summary>
    /// Constructor
    /// </summary>
    public Subscription(DeGiroClientInterface deGiroClient)
    {
        this.deGiroClient = deGiroClient;
        priceListener = this::RefreshPrices;
        Initialize();
    }

    /// <summary>
    /// Initialize
    /// </summary>
    public void Initialize() {
        deGiroClient.AddPriceListener(priceListener);
    }

    /// <summary>
    /// Add a product to the Subscription
    /// </summary>
    /// <param name="productId"></param>
    /// <param name="productName"></param>
    public void Add(String productName, String vwId) throws DeGiroException {
        if (Prices.stream().noneMatch(p -> p.getIssueId().equals(vwId)))
        {
            DPrice price = new DPrice() {{ setVwdProductName(productName); setIssueId(vwId); }};
            Prices.add(price);
            deGiroClient.SubscribeToPrice(price.getIssueId());
        }
    }

    /// <summary>
    /// Add a product to the Subscription
    /// </summary>
    /// <param name="productId"></param>
    /// <param name="productName"></param>
    public void Remove(String vwId)
    {
        if (Prices.stream().anyMatch(p -> p.getIssueId().equals(vwId)))
        {
            Prices.removeIf(p -> p.getIssueId().equals(vwId));
            deGiroClient.UnsubscribeToPrice(vwId);
            if (Prices.size() == 0)
                deGiroClient.ClearPriceSubscriptions();
        }
    }

    /// <summary>
    ///
    /// </summary>
    /// <param name="price"></param>
    public void RefreshPrices(DPrice price)
    {
        for(DPrice p : Prices)
        {
            if (p.getIssueId().equals(price.getIssueId()))
            {

                p.setAsk(price.getAsk());
                p.setBid(price.getBid());
                p.setRefreshTime(LocalDateTime.now());
            }
        }
        if (this.Update != null)
            this.Update.apply(price);
    }
}
