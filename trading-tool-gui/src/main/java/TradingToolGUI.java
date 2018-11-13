import cat.indiketa.degiro.DeGiro;
import cat.indiketa.degiro.DeGiroFactory;
import cat.indiketa.degiro.utils.DCredentials;
import config.AppConfig;
import model.Context;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import provider.MarketProvider;

import javax.swing.*;

public class TradingToolGUI {
    private static final Logger logger = LogManager.getLogger(MarketProvider.class);

    public static Context context;

    /**
     * Entry point
     * @param args none expected
     */
    static public void main(String[] args) {
        logger.trace("Entering application.");
        context = new Context(AppConfig.getDegiroUserName(), AppConfig.getDegiroPassword());

        // Display GUI
        JFrame frame = new JFrame("TradingToolForm");
        frame.setContentPane(new TradingToolForm().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        logger.trace("Exiting application.");
    }
}
