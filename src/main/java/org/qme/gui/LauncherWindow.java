package org.qme.gui;

import org.qme.installer.Installer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LauncherWindow extends JPanel implements ActionListener {

    JProgressBar progressBar;
    ProgressMonitor monitor;
    JPanel versionPanel = new JPanel(new GridBagLayout());
    JPanel patchnotesPanel = new JPanel(new BorderLayout());
    JTabbedPane textAreas = new JTabbedPane();
    JLabel versionStatusLabel;
    JComboBox versionsList;
    JTextArea outputArea;
    Installer installer;

    public LauncherWindow(Installer installer) {
        super(new BorderLayout());
        this.installer = installer;

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1.0;

        /* Login panel for the future when we add authentication
        JTextField usernameField = new JTextField(10);
        JPasswordField passwordField = new JPasswordField(10);

        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setLabelFor(usernameField);
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setLabelFor(passwordField);

        loginPanel.add(usernameLabel, constraints);
        loginPanel.add(usernameField, constraints);
        loginPanel.add(passwordLabel, constraints);
        loginPanel.add(passwordField, constraints);
        */

        // Setup version panel
        String[] versions = {"preA", "preB"};
        versionsList = new JComboBox(versions);

        versionsList.addActionListener(this);
        versionsList.setActionCommand("selectversion");

        versionStatusLabel = new JLabel("");
        versionStatusLabel.setLabelFor(versionsList);

        versionPanel.add(versionsList, constraints);
        versionPanel.add(versionStatusLabel, constraints);

        versionsList.setSelectedIndex(0);

        // Setup patch notes panel
        JTextArea patchnotesArea = new JTextArea("");
        patchnotesArea.setEditable(false);

        textAreas.add(patchnotesArea, "Output");

        // Setup output panel
        outputArea = new JTextArea("AAAAAA\nAAAAAAAAAAAAAAAAAAA");
        outputArea.setEditable(false);

        textAreas.add(outputArea, "Changelog");

        patchnotesPanel.add(textAreas);

        // Setup launch button
        JButton launchButton = new JButton("Launch Game");
        launchButton.addActionListener(this);
        launchButton.setActionCommand("launchgame");

        // Add border
        versionPanel.setBorder(BorderFactory.createTitledBorder("Version"));
        patchnotesPanel.setBorder(BorderFactory.createTitledBorder("Output"));

        JPanel leftPane = new JPanel(new BorderLayout());
        leftPane.add(versionPanel, BorderLayout.PAGE_END);

        JPanel rightPane = new JPanel(new BorderLayout());
        rightPane.add(patchnotesPanel, BorderLayout.CENTER);
        rightPane.add(launchButton, BorderLayout.PAGE_END);

        add(leftPane, BorderLayout.LINE_START);
        add(rightPane, BorderLayout.CENTER);

        initialiseProgress();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equalsIgnoreCase("selectversion")) {
            JComboBox comboBox = (JComboBox) e.getSource();
            versionStatusLabel.setText(installer.isInstalled(comboBox.getSelectedItem().toString()) ? "Version " + comboBox.getSelectedItem() + " is installed" : "Version " + comboBox.getSelectedItem() + " is not installed");
        } else if (e.getActionCommand().equalsIgnoreCase("launchgame")) {

            String version = versionsList.getSelectedItem().toString();

            if (installer.isInstalled(version)) {
                installer.launchVersion(version);
            } else {
                int i = JOptionPane.showConfirmDialog(this, "Version not installed, would you like to run the installer?");
                if (i == 0) {
                    outputArea.setText("Installing version " + version);
                    SwingWorker swingWorker = new SwingWorker() {
                        @Override
                        protected Object doInBackground() {
                            installer.install(version);
                            return null;
                        }
                    };
                    swingWorker.execute();
                }
            }
        }
    }

    public void initialiseProgress() {
        installer.setProgress(outputArea);
    }

}
