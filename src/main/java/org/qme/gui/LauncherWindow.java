package org.qme.gui;

import javax.swing.*;
import java.awt.*;

public class LauncherWindow extends JPanel {

    JPanel versionPanel = new JPanel(new GridBagLayout());
    JPanel patchnotesPanel = new JPanel(new BorderLayout());

    public LauncherWindow() {
        super(new BorderLayout());

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
        JComboBox versionsList = new JComboBox(versions);

        JLabel versionStatusLabel = new JLabel(versionsList.getSelectedItem().toString() + " is not installed");
        versionStatusLabel.setLabelFor(versionsList);

        versionPanel.add(versionsList, constraints);
        versionPanel.add(versionStatusLabel, constraints);

        // Setup patch notes panel
        JTextArea patchnotesArea = new JTextArea("AAAAAA\nAAAAAAAAAAAAAAAAAAA");
        patchnotesArea.setEditable(false);

        patchnotesPanel.add(patchnotesArea);

        // Add border
        versionPanel.setBorder(BorderFactory.createTitledBorder("Version"));
        patchnotesPanel.setBorder(BorderFactory.createTitledBorder("Changelog"));

        JPanel leftPane = new JPanel(new BorderLayout());
        leftPane.add(versionPanel, BorderLayout.PAGE_END);

        JPanel rightPane = new JPanel(new BorderLayout());
        rightPane.add(patchnotesPanel, BorderLayout.CENTER);
        rightPane.add(new JButton("Launch Game"), BorderLayout.PAGE_END);

        add(leftPane, BorderLayout.LINE_START);
        add(rightPane, BorderLayout.CENTER);
    }

}
