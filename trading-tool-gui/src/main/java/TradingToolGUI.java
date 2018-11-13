import javax.swing.*;

public class TradingToolGUI {

    /**
     * Entry point
     * @param args none expected
     */
    static public void main(String[] args) {
        JFrame frame = new JFrame("TradingToolForm");
        frame.setContentPane(new TradingToolForm().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
