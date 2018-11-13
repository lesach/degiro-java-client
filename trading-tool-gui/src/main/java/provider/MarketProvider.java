package provider;

import cat.indiketa.degiro.DeGiro;
import cat.indiketa.degiro.model.DProductDescription;
import cat.indiketa.degiro.model.DProductSearch;
import cat.indiketa.degiro.model.DProductType;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.util.List;

public class MarketProvider {
    private static final Logger logger = LogManager.getLogger(MarketProvider.class);

    private DeGiro degiro;

    /**
     * Constructor
     * @param degiro current connection
     */
    public MarketProvider(DeGiro degiro) {
        this.degiro = degiro;
    }

    /**
     * Find product
     * @return product description
     */
    public List<DProductDescription> findProduct(String text, DProductType type, int limit, int offset){
        try {
            // Search products by text, signature:
            // DProductSearch searchProducts(String text, DProductType type, int limit, int offset);
            DProductSearch ps = degiro.searchProducts("telepizza", type, limit, offset);
            return ps.getProducts();
        }
        catch (Exception e) {
            logger.error("ERROR in findProduct", e);
        }
        return null;
    }
}
