package org.qme;

import com.formdev.flatlaf.FlatDarculaLaf;
import org.qme.gui.LauncherWindow;
import org.qme.installer.Installer;

import javax.swing.*;
import java.util.Arrays;

public class Main {

    public static boolean noGui = false;

    public static LauncherWindow launcherWindow;

    public static void main(String[] args) {

        if (Arrays.stream(args).anyMatch(arg -> arg.equalsIgnoreCase("install"))) {
            noGui = true;
        }

        Installer installer = Installer.getInstaller();
        if (installer == null) {
            System.out.println("Failed: Could not detect proper installer.");
            return;
        }


        try {
            UIManager.setLookAndFeel(new FlatDarculaLaf() );
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        if (noGui) {
            installer.install("preB");
            return;
        }
        openLauncher(installer);

    }

    private static void openLauncher(Installer installer) {
        JFrame frame = new JFrame("Qme Launcher");
        launcherWindow = new LauncherWindow(installer);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(launcherWindow);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(400, 400);
    }

}
