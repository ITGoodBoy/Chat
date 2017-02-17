package client.gui_client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class ClientGuiView extends JFrame {
    private ClientGuiController controller;
    private JTextArea messages;
    private JTextArea users;
    private JTextArea userMessage;
    private JButton sendMessage;
    private JPanel jPanel;

    public ClientGuiView(ClientGuiController controller)
    {
        this.controller = controller;
        initView();
    }

    private void initView() {
        jTextAreaInitialize();
        jButtonInitialize();
        jPanelInitialize();
        jFrameInitialize();
    }

    private void jFrameInitialize() {
        this.getContentPane().add(jPanel, BorderLayout.SOUTH);
        this.getContentPane().add(new JScrollPane(messages), BorderLayout.WEST);
        this.getContentPane().add(new JScrollPane(users), BorderLayout.EAST);
        this.pack();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private void jPanelInitialize() {
        jPanel = new JPanel(new BorderLayout());
        jPanel.add(userMessage, BorderLayout.NORTH);
        jPanel.add(sendMessage, BorderLayout.WEST);
    }

    private void jButtonInitialize() {
        sendMessage = new JButton("Отправить");
        sendMessage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.sendTextMessage(userMessage.getText());
                userMessage.setText("");
            }
        });
    }

    private void jTextAreaInitialize() {
        messages = new JTextArea(15, 45);
        messages.setEditable(false);
        messages.setLineWrap(true);

        users = new JTextArea(15, 12);
        users.setEditable(false);

        userMessage = new JTextArea(5, 40);
        userMessage.setEditable(false);
        userMessage.setLineWrap(true);
    }


    public String getUserName() {
        return JOptionPane.showInputDialog(
                this,
                "Введите ваше имя:",
                "Конфигурация клиента",
                JOptionPane.QUESTION_MESSAGE);
    }

    public void setClientConnected(boolean clientConnected) {
        userMessage.setEditable(clientConnected);
        if (clientConnected) {
            JOptionPane.showMessageDialog(
                    this,
                    "Соединение с сервером установлено",
                    "Чат",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Клиент не подключен к серверу",
                    "Чат",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void refreshUsers() {
        ClientGuiModel model = controller.getModel();
        StringBuilder sb = new StringBuilder();
        for (String userName : model.getAllUserNames()) {
            sb.append(userName).append("\n");
        }
        users.setText(sb.toString());
    }

    public void refreshMessages() {
        String message = controller.getModel().getNewMessage().replaceAll("\n", "");
        messages.append(message + "\n");
    }

}
