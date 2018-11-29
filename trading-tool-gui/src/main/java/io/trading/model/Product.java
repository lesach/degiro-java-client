package io.trading.model;

import cat.indiketa.degiro.model.DPortfolioProducts;
import cat.indiketa.degiro.model.DPrice;
import cat.indiketa.degiro.model.DProductDescription;
import com.google.common.base.Strings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.Date;


public class Product {
    private final SimpleLongProperty id;
    private final SimpleStringProperty name;
    private final SimpleDoubleProperty bid;
    private final SimpleDoubleProperty ask;
    private final SimpleDoubleProperty last;
    private final SimpleLongProperty priceTime;
    private final SimpleStringProperty vwdId;
    private final SimpleDoubleProperty closePrice;
    private final SimpleLongProperty closePriceDate;
    private final SimpleStringProperty productTypeId;
    private final SimpleStringProperty symbol;
    private final SimpleStringProperty isin;
    private final SimpleBooleanProperty tradable;


    /**
     * Basic Constructor
     */
    public Product() {
        this(-1l,
                "",
                0d,
                0d,
                0d,
                0l,
                "",
                0d,
                0l,
                "",
                "",
                "",
                "",
                false);
    }

    /**
     * Full constructor
     * @param id
     * @param name
     * @param bid
     * @param ask
     * @param last
     * @param priceTime
     * @param vwdId
     * @param closePrice
     * @param closePriceDate
     * @param productTypeId
     * @param currency
     * @param symbol
     * @param isin
     * @param tradable
     */
    public Product(Long id,
                   String name,
                   Double bid,
                   Double ask,
                   Double last,
                   Long priceTime,
                   String vwdId,
                   Double closePrice,
                   Long closePriceDate,
                   String productTypeId,
                   String currency,
                   String symbol,
                   String isin,
                   Boolean tradable) {
        this.id = new SimpleLongProperty(id);
        this.name = new SimpleStringProperty(name);
        this.bid = new SimpleDoubleProperty(bid);
        this.ask = new SimpleDoubleProperty(ask);
        this.last = new SimpleDoubleProperty(last);
        this.priceTime = new SimpleLongProperty(priceTime);
        this.vwdId = new SimpleStringProperty(vwdId);
        this.closePrice = new SimpleDoubleProperty(closePrice);
        this.closePriceDate = new SimpleLongProperty(closePriceDate);
        this.productTypeId = new SimpleStringProperty(productTypeId);
        this.currency = new SimpleStringProperty(currency);
        this.symbol = new SimpleStringProperty(symbol);
        this.isin = new SimpleStringProperty(isin);
        this.tradable = new SimpleBooleanProperty(tradable);
    }


    /**
     * Update product prices
     * @param prod new price
     */
    public void adopt(DPrice prod) {
        if (prod != null) {
            if (prod.getBid() != null) {
                setBid(prod.getBid());
            }
            if (prod.getAsk() != null) {
                setAsk(prod.getAsk());
            }
            if (prod.getLast() != null) {
                setLast(prod.getLast());
            }
            setPriceTime(new Date().getTime());
        }
    }


    /**
     *
     * @param prod to adopt
     */
    public void adopt(DProductDescription prod) {
        if (prod != null) {
            setName(prod.getName());
            setVwdId(prod.getVwdId());
            setProductTypeId(prod.getProductTypeId().toString());
            setClosePrice(prod.getClosePrice());
            setTradable(prod.isTradable());
            if (prod.getClosePriceDate() != null)
                setClosePriceDate(prod.getClosePriceDate().getTime());
            else
                setClosePriceDate(0l);
        }
    }

    /**
     *
     * @param prod to adopt
     */
    public void adopt(DPortfolioProducts.DPortfolioProduct prod){
        if (prod == null) {
            return;
        }
        setId(prod.getId());
        if (Strings.isNullOrEmpty(getName())) {
            setName(prod.getProduct());
        }
        setCurrency(prod.getCurrency());
        if (prod.getPrice() != null) {
            setAsk(prod.getPrice().doubleValue());
            setBid(prod.getPrice().doubleValue());
        }

        if (prod.getLastUpdate() != null) {
            setPriceTime(prod.getLastUpdate().getTime());
        }
    }


    public long getId() {
        return id.get();
    }

    public SimpleLongProperty idProperty() {
        return id;
    }

    public void setId(long id) {
        this.id.set(id);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public double getBid() {
        return bid.get();
    }

    public SimpleDoubleProperty bidProperty() {
        return bid;
    }

    public void setBid(double bid) {
        this.bid.set(bid);
    }

    public double getAsk() {
        return ask.get();
    }

    public SimpleDoubleProperty askProperty() {
        return ask;
    }

    public void setAsk(double ask) {
        this.ask.set(ask);
    }

    public double getLast() {
        return last.get();
    }

    public SimpleDoubleProperty lastProperty() {
        return last;
    }

    public void setLast(double last) {
        this.last.set(last);
    }

    public long getPriceTime() {
        return priceTime.get();
    }

    public SimpleLongProperty priceTimeProperty() {
        return priceTime;
    }

    public void setPriceTime(long priceTime) {
        this.priceTime.set(priceTime);
    }


    public String getVwdId() {
        return vwdId.get();
    }

    public SimpleStringProperty vwdIdProperty() {
        return vwdId;
    }

    public void setVwdId(String vwdId) {
        this.vwdId.set(vwdId);
    }

    public double getClosePrice() {
        return closePrice.get();
    }

    public SimpleDoubleProperty closePriceProperty() {
        return closePrice;
    }

    public void setClosePrice(double closePrice) {
        this.closePrice.set(closePrice);
    }

    public long getClosePriceDate() {
        return closePriceDate.get();
    }

    public SimpleLongProperty closePriceDateProperty() {
        return closePriceDate;
    }

    public void setClosePriceDate(long closePriceDate) {
        this.closePriceDate.set(closePriceDate);
    }


    public String getProductTypeId() {
        return productTypeId.get();
    }

    public SimpleStringProperty productTypeIdProperty() {
        return productTypeId;
    }

    public void setProductTypeId(String productTypeId) {
        this.productTypeId.set(productTypeId);
    }


    public String getCurrency() {
        return currency.get();
    }

    public SimpleStringProperty currencyProperty() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency.set(currency);
    }

    private final SimpleStringProperty currency;

    public String getSymbol() {
        return symbol.get();
    }

    public SimpleStringProperty symbolProperty() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol.set(symbol);
    }

    public String getIsin() {
        return isin.get();
    }

    public SimpleStringProperty isinProperty() {
        return isin;
    }

    public void setIsin(String isin) {
        this.isin.set(isin);
    }

    public boolean isTradable() {
        return tradable.get();
    }

    public SimpleBooleanProperty tradableProperty() {
        return tradable;
    }

    public void setTradable(boolean tradable) {
        this.tradable.set(tradable);
    }

}
