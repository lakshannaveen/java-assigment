package ui;

import javax.swing.*;
import java.awt.*;

public class StartPageStyle {

    public static void styleButton(JButton button) {
        button.setFont(new Font("Serif", Font.BOLD, 18));
        button.setBackground(Color.GREEN);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
    }
}