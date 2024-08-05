package main;

import javax.swing.*;

public class OctoberEats {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                System.out.println("Iniciando Menu Principal");
                MainMenuUI mainMenuUI = new MainMenuUI();
                mainMenuUI.setVisible(true);
            }
        });
    }
}