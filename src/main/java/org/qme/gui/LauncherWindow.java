package org.qme.gui;

import org.qme.installer.Installer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * UI stuff for the launcher
 * @author cameron
 * @since 0.0.1
 */
public class LauncherWindow extends JPanel implements ActionListener {

    JPanel versionPanel = new JPanel(new GridBagLayout());
    JPanel patchnotesPanel = new JPanel(new BorderLayout());
    JTabbedPane textAreas = new JTabbedPane();
    JLabel versionStatusLabel;
    JComboBox versionsList;
    JTextArea outputArea;

    /**
     * Installer instance
     */
    Installer installer;

    /**
     * Creates a new instance of the launcher windows.
     * @param installer the reference to an installer
     */
    public LauncherWindow(Installer installer) {
        super(new BorderLayout());
        this.installer = installer;

        // For positioning components
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1.0;

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

        JTextArea patchnotesArea = new JTextArea("AAAAAA\nAAAAAAAAAAAAAAAAAAA");
        patchnotesArea.setEditable(false);
        JScrollPane patchnotesPane = new JScrollPane(patchnotesArea);


        // Setup output panel

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane outputPane = new JScrollPane(outputArea);


        textAreas.add(outputPane, "Output");
        textAreas.add(patchnotesPane, "Changelog");

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

        installer.setProgress(outputArea);
    }

    public void actionPerformed(ActionEvent e) {
        String version = versionsList.getSelectedItem().toString();

        switch (e.getActionCommand()) {
            case "selectversion":
                JComboBox comboBox = (JComboBox) e.getSource();
                versionStatusLabel.setText(installer.isInstalled(version) ? "Version " + comboBox.getSelectedItem() + " is installed" : "Version " + comboBox.getSelectedItem() + " is not installed");
            case "launchgame":
                if (installer.isInstalled(version)) {
                    installer.launchVersion(version);
                } else {
                    if (JOptionPane.showConfirmDialog(this, "Version not installed, would you like to run the installer?") == 0) {
                        outputArea.setText("Installing version " + version);

                        // This weird swingWorker thingy is to prevent the installer from blocking the swing thread which results in the gui freezing.
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

}
