package ui.component;

import javax.swing.*;
import java.awt.*;

public class UIFactory {
    public static final Color BTN_BLUE   = new Color(189, 224, 254);
    public static final Color BTN_GREEN  = new Color(204, 213, 174);
    public static final Color BTN_YELLOW = new Color(254, 250, 224);
    public static final Color BTN_RED    = new Color(250, 210, 225);
    public static final Color COLOR_PRIMARY = new Color(205, 180, 219);
    public static final Color BG_UTAMA   = new Color(255, 245, 248);

    public static JTextField createTextField(String defaultText) {
        JTextField tf = new JTextField(defaultText);
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        tf.setPreferredSize(new Dimension(280, 35));
        return tf;
    }

    public static JPasswordField createPasswordField() {
        JPasswordField pf = new JPasswordField();
        pf.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        pf.setPreferredSize(new Dimension(280, 35));
        return pf;
    }

    public static JButton createButton(String text, Color bgColor) {
        RoundedButton btn = new RoundedButton(text, 40);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btn.setBackground(bgColor);
        btn.setForeground(new Color(80, 80, 80));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}