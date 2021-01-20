package org.qme;

import org.qme.gui.LauncherWindow;
import org.qme.installer.Installer;

import javax.swing.*;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        //System.out.println(args[0]);

        if (!Arrays.stream(args).anyMatch(arg -> arg.equalsIgnoreCase("install"))) {
            openLauncher();
            return;
        }

        Installer installer = Installer.getInstaller();
        if (installer == null) {
            System.out.println("Failed: Could not detect proper installer.");
            return;
        }
        installer.install("preB");
    }

    private static void openLauncher() {
        JFrame frame = new JFrame("Qme Launcher");
        LauncherWindow launcher = new LauncherWindow();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(launcher);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(400, 400);
    }

}
