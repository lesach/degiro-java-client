package io.trading.model.tableview;

import cat.indiketa.degiro.model.DPortfolioProducts;
import cat.indiketa.degiro.model.DPrice;
import cat.indiketa.degiro.model.DProductDescription;
import com.google.common.base.Strings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.Date;


public class ProductSchema {
    private final SimpleStringProperty productId;
    private final SimpleStringProperty productName;
    private final SimpleStringProperty currency;
    // Direct
    private final SimpleDoubleProperty ask;
    private final SimpleDoubleProperty bid;
    private final SimpleLongProperty priceTime;
    // Last
    private final SimpleDoubleProperty last;
    private final SimpleLongProperty lastTime;
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
    public ProductSchema() {
        this("-1",
                "",
                0d,
                0d,
                0l,
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
     * @param priceTime
     * @param last*
     * @param lastTime
     * @param vwdId
     * @param closePrice
     * @param closePriceDate
     * @param productTypeId
     * @param currency
     * @param symbol
     * @param isin
     * @param tradable
     */
    public ProductSchema(String id,
                         String name,
                         Double bid,
                         Double ask,
                         Long priceTime,
                         Double last,
                         Long lastTime,
                         String vwdId,
                         Double closePrice,
                         Long closePriceDate,
                         String productTypeId,
                         String currency,
                         String symbol,
                         String isin,
                         Boolean tradable) {
        this.productId = new SimpleStringProperty(id);
        this.productName = new SimpleStringProperty(name);
        this.bid = new SimpleDoubleProperty(bid);
        this.ask = new SimpleDoubleProperty(ask);
        this.priceTime = new SimpleLongProperty(priceTime);
        this.last = new SimpleDoubleProperty(last);
        this.lastTime = new SimpleLongProperty(lastTime);
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
            if (prod.getLastTime() != null) {
                setLastTime(prod.getLastTime().getTime());
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
            setProductId(Long.toString(prod.getId()));
            setProductName(prod.getName());
            setIsin(prod.getIsin());
            setCurrency(prod.getCurrency());
            setSymbol(prod.getSymbol());
            setTradable(prod.isTradable());
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
        setProductId(prod.getId());
        if (Strings.isNullOrEmpty(getProductName())) {
            setProductName(prod.getProduct());
        }
        setCurrency(prod.getCurrency());
        if (prod.getPrice() != null) {
            setAsk(prod.getPrice().doubleValue());
            setBid(prod.getPrice().doubleValue());
        }

        if (prod.getLastUpdate() != null) {
            setLastTime(prod.getLastUpdate().getTime());
        }
    }


    /**
     *
     * @param basic to adopt
     */
    public void adopt(BasicSchema basic){
        if (basic == null)
            return;
        setProductId(basic.getProductId());
        if (Strings.isNullOrEmpty(getProductName())) {
            setProductName(basic.getProductName());
        }
        setCurrency(basic.getCurrency());
    }


    public String getProductId() {
        return productId.get();
    }

    public SimpleStringProperty productIdProperty() {
        return productId;
    }

    public void setProductId(String id) {
        this.productId.set(id);
    }

    public String getProductName() {
        return productName.get();
    }

    public SimpleStringProperty productNameProperty() {
        return productName;
    }

    public void setProductName(String name) {
        this.productName.set(name);
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


    public long getLastTime() {
        return lastTime.get();
    }

    public SimpleLongProperty lastTimeProperty() {
        return lastTime;
    }

    public void setLastTime(long lastTime) {
        this.lastTime.set(lastTime);
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
