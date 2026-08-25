public class Luck {
    /**
     * ASCII banner printed by the application.
     *
     * @return banner string
     */
    private static final String BANNER =
            " _                _        \n"
                    + "| |    _   _  ___| | __    \n"
                    + "| |   | | | |/ __| |/ /    \n"
                    + "| |___| |_| | (__|   <     \n"
                    + "|_____|\\__,_|\\___|_|\\_\\    \n";

    public static String getBanner() {
        return BANNER;
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> createAndShowGui());
    }

    private static void createAndShowGui() {
        javax.swing.JFrame frame = new javax.swing.JFrame("Luck");
        javax.swing.JTextArea area = new javax.swing.JTextArea(getBanner());
        area.setEditable(false);
        area.setFont(new java.awt.Font(java.awt.Font.MONOSPACED, java.awt.Font.PLAIN, 14));
        area.setBackground(java.awt.Color.BLACK);
        area.setForeground(java.awt.Color.GREEN);
        area.setBorder(javax.swing.BorderFactory.createEmptyBorder(10,10,10,10));

        frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().add(new javax.swing.JScrollPane(area));
        frame.setSize(420, 220);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
