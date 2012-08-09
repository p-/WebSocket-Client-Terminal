/*
 *  Copyright (C) 2011 Alphabot / Peter Stockli
 *  
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *  
 *  	http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License. 
 */
package com.alphabot.websocket.client.terminal;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFrame;

// Refactor to other class
import de.roderick.weberknecht.WebSocket;
import de.roderick.weberknecht.WebSocketEventHandler;
import de.roderick.weberknecht.WebSocketException;
import de.roderick.weberknecht.WebSocketMessage;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * The application's main frame.
 */
public class WebSocketClientTerminalView extends FrameView {

    public WebSocketClientTerminalView(SingleFrameApplication app) {
        super(app);

        initComponents();
        logArea.setEditable(false);

        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);

		messageField.setEnabled(false);
		messageButton.setEnabled(false);
			
        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String) (evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer) (evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
                }
            }
        });
    }

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = WebSocketClientTerminalApp.getApplication().getMainFrame();
            aboutBox = new WebSocketClientTerminalAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        WebSocketClientTerminalApp.getApplication().show(aboutBox);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        addressText = new javax.swing.JLabel();
        addressField = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        logArea = new javax.swing.JTextArea();
        connectButton = new javax.swing.JButton();
        messageField = new javax.swing.JTextField();
        messageButton = new javax.swing.JButton();
        protocolLabel = new javax.swing.JLabel();
        protocolField = new javax.swing.JTextField();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        clearLogMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();

        mainPanel.setName("mainPanel"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(com.alphabot.websocket.client.terminal.WebSocketClientTerminalApp.class).getContext().getResourceMap(WebSocketClientTerminalView.class);
        addressText.setText(resourceMap.getString("addressText.text")); // NOI18N
        addressText.setName("addressText"); // NOI18N

        addressField.setText(resourceMap.getString("addressField.text")); // NOI18N
        addressField.setName("addressField"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        logArea.setBackground(resourceMap.getColor("logArea.background")); // NOI18N
        logArea.setColumns(20);
        logArea.setRows(5);
        logArea.setName("logArea"); // NOI18N
        jScrollPane1.setViewportView(logArea);

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(com.alphabot.websocket.client.terminal.WebSocketClientTerminalApp.class).getContext().getActionMap(WebSocketClientTerminalView.class, this);
        connectButton.setAction(actionMap.get("connectClick")); // NOI18N
        connectButton.setText(resourceMap.getString("connectButton.text")); // NOI18N
        connectButton.setName("connectButton"); // NOI18N

        messageField.setText(resourceMap.getString("messageField.text")); // NOI18N
        messageField.setName("messageField"); // NOI18N

        messageButton.setAction(actionMap.get("sendClick")); // NOI18N
        messageButton.setText(resourceMap.getString("messageButton.text")); // NOI18N
        messageButton.setName("messageButton"); // NOI18N

        protocolLabel.setText(resourceMap.getString("protocolLabel.text")); // NOI18N
        protocolLabel.setName("protocolLabel"); // NOI18N

        protocolField.setText(resourceMap.getString("protocolField.text")); // NOI18N
        protocolField.setName("protocolField"); // NOI18N

        org.jdesktop.layout.GroupLayout mainPanelLayout = new org.jdesktop.layout.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, mainPanelLayout.createSequentialGroup()
                .add(18, 18, 18)
                .add(messageField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 577, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(messageButton)
                .addContainerGap())
            .add(mainPanelLayout.createSequentialGroup()
                .add(9, 9, 9)
                .add(mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(addressText)
                    .add(protocolLabel))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(mainPanelLayout.createSequentialGroup()
                        .add(addressField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 443, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(connectButton))
                    .add(protocolField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 204, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 698, Short.MAX_VALUE)
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(addressText)
                    .add(addressField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(connectButton))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(protocolLabel)
                    .add(protocolField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(messageButton)
                    .add(messageField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
        );

        menuBar.setName("menuBar"); // NOI18N

        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        clearLogMenuItem.setAction(actionMap.get("clearLog")); // NOI18N
        clearLogMenuItem.setText(resourceMap.getString("clearLogMenuItem.text")); // NOI18N
        clearLogMenuItem.setName("clearLogMenuItem"); // NOI18N
        fileMenu.add(clearLogMenuItem);

        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        statusPanel.setName("statusPanel"); // NOI18N

        statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

        progressBar.setName("progressBar"); // NOI18N

        org.jdesktop.layout.GroupLayout statusPanelLayout = new org.jdesktop.layout.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(statusPanelSeparator, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 698, Short.MAX_VALUE)
            .add(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(statusMessageLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 502, Short.MAX_VALUE)
                .add(progressBar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(statusAnimationLabel)
                .addContainerGap())
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(statusPanelLayout.createSequentialGroup()
                .add(statusPanelSeparator, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(statusPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(statusMessageLabel)
                    .add(statusAnimationLabel)
                    .add(progressBar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(3, 3, 3))
        );

        setComponent(mainPanel);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
    }// </editor-fold>//GEN-END:initComponents

    @Action
    public void connectClick() {
        if (!connected) {
            try {
                URI url = new URI(addressField.getText());
				String protocolName = protocolField.getText();
				if (protocolName.isEmpty()) {
					protocolName = null;
				}
                websocket = new WebSocket(url, protocolName);


                // Register Event Handlers
                websocket.setEventHandler(new WebSocketEventHandler() {

                    public void onOpen() {
                        writeLogEntry("[open]");
                    }

                    public void onMessage(WebSocketMessage wsm) {
                        writeLogEntry("[I] " + wsm.getText());
                    }

                    public void onClose() {
                        writeLogEntry("[close]");
                        connected = false;
                        changeUIState();
                    }

                    public void onPing() {
                        writeLogEntry("[ping]");
                    }

                    public void onPong() {
                        writeLogEntry("[pong]");
                    }
                });

                // Establish WebSocket Connection
                websocket.connect();

                connected = true;
                changeUIState();

            } catch (URISyntaxException use) {
                writeLogEntry("[exception] " + use.getMessage());
                use.printStackTrace();
            } catch (WebSocketException wse) {
                writeLogEntry("[exception] " + wse.getMessage());
                wse.printStackTrace();
            }


        } else {
            disconnect();
        }


    }

    private void disconnect() {
        try {
            connected = false;
            changeUIState();
            websocket.close();
        } catch (WebSocketException wse) {
            wse.printStackTrace();
        }
    }

    private void changeUIState() {
        if (connected) {
            connectButton.setText("Disconnect");
            statusMessageLabel.setText("Connected");
			protocolField.setEnabled(false);
			addressField.setEnabled(false);
			messageField.setEnabled(true);
			messageButton.setEnabled(true);

            if (!busyIconTimer.isRunning()) {
                statusAnimationLabel.setIcon(busyIcons[0]);
                busyIconIndex = 0;
                busyIconTimer.start();
            }

        } else {
            connectButton.setText("Connect");
            statusMessageLabel.setText("Not Connected");
			protocolField.setEnabled(true);
			addressField.setEnabled(true);
			messageField.setEnabled(false);
			messageButton.setEnabled(false);

            if (busyIconTimer.isRunning()) {
                busyIconTimer.stop();
                statusAnimationLabel.setIcon(idleIcon);
            }
        }

    }

    private void writeLogEntry(String logMessage) {
        logArea.append(logMessage + "\n");
    }

    @Action
    public void sendClick() {
        try {
            websocket.send(messageField.getText());
            writeLogEntry("[O] " + messageField.getText());
        } catch (WebSocketException wse) {
            wse.printStackTrace();
        } finally {
            messageField.setText("");
        }
    }

    @Action
    public void clearLog() {
        logArea.setText("");
    }
    private WebSocket websocket;
    private boolean connected;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField addressField;
    private javax.swing.JLabel addressText;
    private javax.swing.JMenuItem clearLogMenuItem;
    private javax.swing.JButton connectButton;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea logArea;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JButton messageButton;
    private javax.swing.JTextField messageField;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JTextField protocolField;
    private javax.swing.JLabel protocolLabel;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    // End of variables declaration//GEN-END:variables
    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;
    private JDialog aboutBox;
}
