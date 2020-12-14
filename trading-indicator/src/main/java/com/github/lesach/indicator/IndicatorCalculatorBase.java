package com.github.lesach.indicator;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public abstract class IndicatorCalculatorBase<T>
{
    public List<Ohlc> getOhlcList() {
        return OhlcList;
    }

    public void setOhlcList(List<Ohlc> ohlcList) {
        OhlcList = ohlcList;
    }

    public List<Ohlc> OhlcList;

    public void Load(String path) throws IOException, CsvException {
        Reader reader = Files.newBufferedReader(Paths.get(URI.create(path)));
        CSVReader csvReader = new CSVReader(reader);
        List<String[]> list = csvReader.readAll();
        if (!list.isEmpty()) {
            int fieldCount = list.stream().findFirst().get().length;
            String[] headers = list.stream().findFirst().get();
            OhlcList = new ArrayList<>();
            for(String[] csv : list) {
                if (csv != headers) {
                    Ohlc ohlc = new Ohlc();
                    for (int i = 0; i < fieldCount; i++) {
                        switch (headers[i]) {
                            case "Date":
                                ohlc.setDate(LocalDateTime.parse(csv[i], DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
                                break;
                            case "Open":
                                ohlc.setOpen(new BigDecimal(csv[i]));
                                break;
                            case "High":
                                ohlc.setHigh(new BigDecimal(csv[i]));
                                break;
                            case "Low":
                                ohlc.setLow(new BigDecimal(csv[i]));
                                break;
                            case "Close":
                                ohlc.setClose(new BigDecimal(csv[i]));
                                break;
                            case "Volume":
                                ohlc.setVolume(new BigDecimal(csv[i]));
                                break;
                            case "Adj Close":
                                ohlc.setAdjClose(new BigDecimal(csv[i]));
                                break;
                            default:
                                break;
                        }
                    }
                    OhlcList.add(ohlc);
                }
            }
        }
        reader.close();
        csvReader.close();
    }

    public void Load(List<Ohlc> ohlcList)
    {
        this.OhlcList = ohlcList;
    }

    public abstract T Calculate();
}
