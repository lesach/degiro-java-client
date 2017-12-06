package cat.indiketa.degiro.utils;

import cat.indiketa.degiro.log.DLog;
import cat.indiketa.degiro.model.DCashFunds;
import cat.indiketa.degiro.model.DCashFunds.DCashFund;
import cat.indiketa.degiro.model.DOrderTime;
import cat.indiketa.degiro.model.DOrderType;
import cat.indiketa.degiro.model.DOrders;
import cat.indiketa.degiro.model.DOrders.DOrder;
import cat.indiketa.degiro.model.DPortfolio;
import cat.indiketa.degiro.model.DPortfolio.DProductRow;
import cat.indiketa.degiro.model.DLastTransactions;
import cat.indiketa.degiro.model.DLastTransactions.DTransaction;
import cat.indiketa.degiro.model.DPrice;
import cat.indiketa.degiro.model.raw.DRawCashFunds;
import cat.indiketa.degiro.model.raw.DRawOrders;
import cat.indiketa.degiro.model.raw.DRawPortfolio;
import cat.indiketa.degiro.model.raw.DRawPortfolio.Value;
import cat.indiketa.degiro.model.raw.DRawPortfolio.Value_;
import cat.indiketa.degiro.model.raw.DRawTransactions;
import cat.indiketa.degiro.model.raw.DRawVwdPrice;
import com.google.common.base.CaseFormat;
import com.google.common.base.Strings;
import com.google.common.primitives.Doubles;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author indiketa
 */
public class DUtils {

    private static final SimpleDateFormat HM_FORMAT = new SimpleDateFormat("HH:mm:ss");

    public static DPortfolio convert(DRawPortfolio rawPortfolio) {
        DPortfolio portfolio = new DPortfolio();
        List<DProductRow> active = new LinkedList<>();
        List<DProductRow> inactive = new LinkedList<>();

        for (Value value : rawPortfolio.getPortfolio().getValue()) {
            DProductRow productRow = convertProduct(value);

            if (productRow.getSize() == 0) {
                inactive.add(productRow);
            } else {
                active.add(productRow);
            }
        }

        portfolio.setActive(active);
        portfolio.setInactive(inactive);

        return portfolio;

    }

    public static DProductRow convertProduct(Value row) {
        DProductRow productRow = new DProductRow();

        for (Value_ value : row.getValue()) {

            try {

                String methodName = "set" + CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, value.getName());

                switch (value.getName()) {
                    case "id":
                    case "size":
                    case "change":
                    case "contractSize":
                        long longValue = (long) (double) value.getValue();
                        DProductRow.class.getMethod(methodName, long.class).invoke(productRow, longValue);
                        break;
                    case "product":
                    case "currency":
                    case "exchangeBriefCode":
                    case "productCategory":
                        String stringValue = (String) value.getValue();
                        DProductRow.class.getMethod(methodName, String.class).invoke(productRow, stringValue);
                        break;
                    case "lastUpdate":
                        System.err.println(value.getValue());
                        break;
                    case "closedToday":
                    case "tradable":
                        Boolean booleanValue = (Boolean) value.getValue();
                        DProductRow.class.getMethod(methodName, boolean.class).invoke(productRow, booleanValue);
                        break;
                    case "price":
                    case "value":
                    case "closePrice":
                        BigDecimal bdValue = new BigDecimal((double) value.getValue());
                        if (bdValue.scale() > 4) {
                            bdValue = bdValue.setScale(4, RoundingMode.HALF_UP);
                        }
                        DProductRow.class.getMethod(methodName, BigDecimal.class).invoke(productRow, bdValue);
                        break;

                }
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                DLog.MANAGER.error("Error while setting value of portfolio", e);
            }
        }

        return productRow;
    }

    public static DCashFunds convert(DRawCashFunds rawCashFunds) {
        DCashFunds cashFunds = new DCashFunds();
        List<DCashFund> list = new LinkedList<>();

        for (Value value : rawCashFunds.getCashFunds().getValue()) {
            DCashFund cashFund = convertCash(value);
            list.add(cashFund);
        }

        cashFunds.setCashFunds(list);

        return cashFunds;

    }

    public static DCashFund convertCash(Value row) {

        DCashFund cashFund = new DCashFund();

        for (Value_ value : row.getValue()) {

            try {

                String methodName = "set" + CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, value.getName());

                switch (value.getName()) {
                    case "id":
                        int intValue = (int) (double) value.getValue();
                        DCashFund.class.getMethod(methodName, int.class).invoke(cashFund, intValue);
                        break;
                    case "currencyCode":
                        String stringValue = (String) value.getValue();
                        DCashFund.class.getMethod(methodName, String.class).invoke(cashFund, stringValue);
                        break;
                    case "lastUpdate":
                        System.err.println(value.getValue());
                        break;
                    case "value":
                        BigDecimal bdValue = new BigDecimal((double) value.getValue());
                        if (bdValue.scale() > 4) {
                            bdValue = bdValue.setScale(4, RoundingMode.HALF_UP);
                        }
                        DCashFund.class.getMethod(methodName, BigDecimal.class).invoke(cashFund, bdValue);
                        break;

                }
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                DLog.MANAGER.error("Error while setting value of cash fund", e);
            }

        }
        return cashFund;
    }

    public static DOrders convert(DRawOrders rawOrders) {
        DOrders orders = new DOrders();
        List<DOrder> list = new LinkedList<>();

        for (Value value : rawOrders.getOrders().getValue()) {
            DOrder order = convertOrder(value);
            list.add(order);
        }

        orders.setOrders(list);

        return orders;

    }

    public static DOrder convertOrder(Value row) {

        DOrder order = new DOrder();

        for (Value_ value : row.getValue()) {

            try {

                String methodName = "set" + CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, value.getName());

                switch (value.getName()) {
                    case "contractType":
                    case "contractSize":
                        int intValue = (int) (double) value.getValue();
                        DOrder.class.getMethod(methodName, int.class).invoke(order, intValue);
                        break;
                    case "productId":
                    case "size":
                    case "quantity":
                        long longValue = (long) (double) value.getValue();
                        DOrder.class.getMethod(methodName, long.class).invoke(order, longValue);
                        break;
                    case "id":
                    case "product":
                    case "currency":
                    case "buysell":
                        String stringValue = (String) value.getValue();
                        DOrder.class.getMethod(methodName, String.class).invoke(order, stringValue);
                        break;

                    case "date":
                        Calendar calendar = processDate((String) value.getValue());
                        DOrder.class.getMethod(methodName, Calendar.class).invoke(order, calendar);
                        break;
                    case "orderType":
                        order.setOrderType(DOrderType.getOrderByValue((int) (double) value.getValue()));
                        break;
                    case "orderTime":
                        order.setOrderTime(DOrderTime.getOrderByValue((int) (double) value.getValue()));
                        break;
                    case "price":
                    case "stopPrice":
                    case "totalOrderValue":
                        BigDecimal bdValue = new BigDecimal((double) value.getValue());
                        if (bdValue.scale() > 4) {
                            bdValue = bdValue.setScale(4, RoundingMode.HALF_UP);
                        }
                        DOrder.class.getMethod(methodName, BigDecimal.class).invoke(order, bdValue);
                        break;

                    case "isModifiable":
                    case "isDeletable":
                        Boolean booleanValue = (Boolean) value.getValue();
                        DOrder.class.getMethod(methodName, boolean.class).invoke(order, booleanValue);
                        break;

                }
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                DLog.MANAGER.error("Error while setting value of order", e);
            }

        }
        return order;
    }

    private static Calendar processDate(String date) {
        Calendar parsed = null;

        date = Strings.nullToEmpty(date);

        if (date.contains(":")) {
            parsed = Calendar.getInstance();
            parsed.set(Calendar.HOUR_OF_DAY, Integer.parseInt(date.split(":")[0]));
            parsed.set(Calendar.MINUTE, Integer.parseInt(date.split(":")[1]));
            parsed.set(Calendar.SECOND, 0);
            parsed.set(Calendar.MILLISECOND, 0);
        } else if (date.contains("/")) {
            parsed = Calendar.getInstance();
            int month = Integer.parseInt(date.split("/")[1]) - 1;

            if (parsed.get(Calendar.MONTH) < month) {
                parsed.add(-1, Calendar.YEAR);
            }

            parsed.set(Calendar.MONTH, month);
            parsed.set(Calendar.DATE, Integer.parseInt(date.split("/")[1]));
            parsed.set(Calendar.HOUR_OF_DAY, 0);
            parsed.set(Calendar.MINUTE, 0);
            parsed.set(Calendar.SECOND, 0);
            parsed.set(Calendar.MILLISECOND, 0);

        } else {
        }
        return parsed;
    }

    public static DLastTransactions convert(DRawTransactions rawOrders) {
        DLastTransactions transactions = new DLastTransactions();
        List<DTransaction> list = new LinkedList<>();

        for (Value value : rawOrders.getTransactions().getValue()) {
            DTransaction order = convertTransaction(value);
            list.add(order);
        }

        transactions.setTransactions(list);

        return transactions;

    }

    public static DTransaction convertTransaction(Value row) {

        DTransaction transaction = new DTransaction();

        for (Value_ value : row.getValue()) {

            try {

                String methodName = "set" + CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, value.getName());

                switch (value.getName()) {
                    case "contractType":
                    case "contractSize":
                        int intValue = (int) (double) value.getValue();
                        DTransaction.class.getMethod(methodName, int.class).invoke(transaction, intValue);
                        break;
                    case "productId":
                    case "size":
                    case "quantity":
                    case "id":
                        long longValue = (long) (double) value.getValue();
                        DTransaction.class.getMethod(methodName, long.class).invoke(transaction, longValue);
                        break;
                    case "product":
                    case "currency":
                    case "buysell":
                        String stringValue = (String) value.getValue();
                        DTransaction.class.getMethod(methodName, String.class).invoke(transaction, stringValue);
                        break;

                    case "date":
                        Calendar calendar = processDate((String) value.getValue());
                        DTransaction.class.getMethod(methodName, Calendar.class).invoke(transaction, calendar);
                        break;
                    case "orderType":
                        transaction.setOrderType(DOrderType.getOrderByValue((int) (double) value.getValue()));
                        break;
                    case "orderTime":
                        transaction.setOrderTime(DOrderTime.getOrderByValue((int) (double) value.getValue()));
                        break;
                    case "price":
                    case "stopPrice":
                    case "totalOrderValue":
                        BigDecimal bdValue = new BigDecimal((double) value.getValue());
                        if (bdValue.scale() > 4) {
                            bdValue = bdValue.setScale(4, RoundingMode.HALF_UP);
                        }
                        DTransaction.class.getMethod(methodName, BigDecimal.class).invoke(transaction, bdValue);
                        break;

                    case "isModifiable":
                    case "isDeletable":
                        Boolean booleanValue = (Boolean) value.getValue();
                        DTransaction.class.getMethod(methodName, boolean.class).invoke(transaction, booleanValue);
                        break;

                }
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                DLog.MANAGER.error("Error while setting value of order", e);
            }

        }
        return transaction;
    }

    public static List<DPrice> convert(List<DRawVwdPrice> data) {

        Set<Long> issues = new HashSet<>(100);
        Map<String, String> dataMap = new HashMap<>(data.size());

        if (data != null) {
            for (DRawVwdPrice dRawVwdPrice : data) {

                if (Strings.nullToEmpty(dRawVwdPrice.getM()).equals("a_req")) {
                    String firstVal = dRawVwdPrice.getV().get(0);
                    issues.add(Long.parseLong(firstVal.substring(0, firstVal.indexOf("."))));

                }

                if (dRawVwdPrice.getV() != null && !dRawVwdPrice.getV().isEmpty()) {
                    String v2 = null;

                    if (dRawVwdPrice.getV().size() > 1) {
                        v2 = dRawVwdPrice.getV().get(1);
                    }

                    dataMap.put(dRawVwdPrice.getV().get(0), v2);
                }
            }
        }

        List<DPrice> prices = new ArrayList<>(issues.size());

        for (Long issue : issues) {
            DPrice price = new DPrice();
            price.setIssueId(issue);
            price.setBid(Doubles.tryParse(getData(issue, "BidPrice", dataMap)));
            price.setAsk(Doubles.tryParse(getData(issue, "AskPrice", dataMap)));
            price.setLast(Doubles.tryParse(getData(issue, "LastPrice", dataMap)));
            String df = getData(issue, "LastTime", dataMap);
            if (!Strings.isNullOrEmpty(df)) {
                try {
                    price.setLastTime(HM_FORMAT.parse(df));
                    Date d = new Date();
                    price.getLastTime().setDate(1);
                    price.getLastTime().setYear(d.getYear());
                    price.getLastTime().setMonth(d.getMonth());
                    price.getLastTime().setDate(d.getDate());
                    if (price.getLastTime().getTime() > System.currentTimeMillis()) {
                        price.getLastTime().setTime(price.getLastTime().getTime() - 1 * 24 * 60 * 60 * 1000);
                    }
                } catch (ParseException e) {
                    DLog.MANAGER.warn("Exception parsing lastTime: " + df + " from issue " + issue);
                }
            }

            prices.add(price);
        }

        return prices;

    }

    private static String getData(long issue, String name, Map<String, String> dataMap) {

        String retVal = "";

        if (dataMap.containsKey(issue + "." + name)) {
            retVal = Strings.nullToEmpty(dataMap.get(dataMap.get(issue + "." + name)));
        }

        return retVal;

    }

}