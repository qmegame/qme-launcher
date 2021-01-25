package org.qme;

import com.formdev.flatlaf.FlatDarculaLaf;
import org.qme.gui.LauncherWindow;
import org.qme.installer.Installer;
import org.qme.release.QmeReleaseManager;

import javax.swing.*;
import java.io.IOException;
import java.util.Arrays;

/**
 * The main entry point for the launcher
 * @author cameron
 * @since 1.0.0
 */
public class Main {

    /**
     * This is true of the application is started with the -install argument. No UI should be loaded.
     */
    public static boolean noGui = false;

    /**
     * A reference to the class what all UI is handled
     * @see LauncherWindow
     */
    public static LauncherWindow launcherWindow;

    /**
     * A reference to the class where release information is stored
     * @see QmeReleaseManager
     */
    public static QmeReleaseManager releaseManager;

    /**
     * Main application entry point
     * @param args
     */
    public static void main(String[] args) {

        // Checks args
        if (Arrays.stream(args).anyMatch(arg -> arg.equalsIgnoreCase("install"))) {
            noGui = true;
        }

        // Setup installer
        Installer installer = Installer.getInstaller();
        if (installer == null) {
            System.out.println("Failed: Could not detect proper installer.");
            return;
        }

        // Gets releases
        try {
            releaseManager = new QmeReleaseManager();
        } catch (IOException e) {
            // TODO: Don't force the user to fetch releases cache them instead when they are offline to allow for easier offline play
            System.out.println("Failed: Could not get releases from github.");
            e.printStackTrace();
        }

        // Sets theme to Flatlaf theme
        try {
            UIManager.setLookAndFeel(new FlatDarculaLaf() );
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        // Installs if install argument is set
        if (noGui) {
            installer.install(releaseManager.getLatestStable());
        } else {
            openLauncher(installer);
        }
    }

    /**
     * Initialises the launcher GUI
     * @param installer the correct installer instance to be used
     */
    private static void openLauncher(Installer installer) {
        JFrame frame = new JFrame("Qme Launcher");
        launcherWindow = new LauncherWindow(installer, releaseManager);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(launcherWindow);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(700, 400);
    }

}
