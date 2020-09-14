package com.github.lesach.raw;

import java.util.List;

/**
 *
 * @author indiketa
 */
public class DRawPortfolio {

    private Portfolio portfolio;

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }

    public static class Portfolio {

        private Long lastUpdated;
        private String name;
        private List<Value> value = null;
        private Boolean isAdded;

        public Long getLastUpdated() {
            return lastUpdated;
        }

        public void setLastUpdated(Long lastUpdated) {
            this.lastUpdated = lastUpdated;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<Value> getValue() {
            return value;
        }

        public void setValue(List<Value> value) {
            this.value = value;
        }

        public Boolean getIsAdded() {
            return isAdded;
        }

        public void setIsAdded(Boolean isAdded) {
            this.isAdded = isAdded;
        }

    }
}
