package com.github.lesach;

import java.util.List;

/**
 *
 * @author indiketa
 */
public class DPortfolioProducts {

    private List<DPortfolioProduct> active;
    private List<DPortfolioProduct> inactive;

    public List<DPortfolioProduct> getActive() {
        return active;
    }

    public void setActive(List<DPortfolioProduct> active) {
        this.active = active;
    }

    public List<DPortfolioProduct> getInactive() {
        return inactive;
    }

    public void setInactive(List<DPortfolioProduct> inactive) {
        this.inactive = inactive;
    }

}
