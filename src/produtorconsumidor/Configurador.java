package produtorconsumidor;

import com.jcraft.jsch.*;
import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.util.Scanner;

public class Configurador {

    public static void main(String[] arg) {
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter the quantity of buffers: ");
        int buffersQtd = scan.nextInt();
        for ( int i = 0; i < buffersQtd; i++ ) {
            System.out.println("A prompt will pop up to configure it: ");
            configureInstance();
        }
        
        System.out.print("Next, you must configure the manager: ");
        for ( int i = 0; i < 2; i++ ) {
            System.out.println("A prompt will pop up to configure it: ");
            configureInstance();
        }
        
        System.out.print("Now, enter the quantity of consumers: ");
        int consumersQtd = scan.nextInt();
        for ( int i = 0; i < consumersQtd; i++ ) {
            System.out.println("A prompt will pop up to configure it: ");
            configureInstance();
        }
        
        System.out.print("Finally, enter the quantity of producers: ");
        int producersQtd = scan.nextInt();
        for ( int i = 0; i < producersQtd; i++ ) {
            System.out.println("A prompt will pop up to configure it: ");
            configureInstance();
        }
        
    }
    
    public static void configureInstance() {
        try {
            JSch jsch = new JSch();

            String host = JOptionPane.showInputDialog("Enter username@hostname",
                        System.getProperty("user.name")
                        + "@localhost");
            String user = host.substring(0, host.indexOf('@'));
            host = host.substring(host.indexOf('@') + 1);

            Session session = jsch.getSession(user, host, 22);
            
            // username and password will be given via UserInfo interface
            UserInfo ui = new MyUserInfo();
            session.setUserInfo(ui);
            session.connect();

            String command = JOptionPane.showInputDialog("Enter command",
                    "java produtorconsumidor/Class args");

            Channel channel = session.openChannel("exec");
            // first command sets the path where the java code is located
            ((ChannelExec) channel).setCommand("cd github/producerconsumer/build/classes;" + command);

            channel.setInputStream(null);

            ((ChannelExec) channel).setErrStream(System.err);

            InputStream in = channel.getInputStream();

            channel.connect();

            byte[] tmp = new byte[1024];
            while (true) {
                while (in.available() > 0) {
                    int i = in.read(tmp, 0, 1024);
                    if (i < 0) {
                        break;
                    }
                    System.out.print(new String(tmp, 0, i));
                }
                if (channel.isClosed()) {
                    if (in.available() > 0) {
                        continue;
                    }
                    System.out.println("exit-status: " + channel.getExitStatus());
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ee) {
                }
            }
            channel.disconnect();
            session.disconnect();
        } catch (JSchException e) {
            System.err.println(e);
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public static class MyUserInfo implements UserInfo, UIKeyboardInteractive {

        @Override
        public String getPassword() {
            return passwd;
        }

        @Override
        public boolean promptYesNo(String str) {
            Object[] options = {"yes", "no"};
            int foo = JOptionPane.showOptionDialog(null,
                    str,
                    "Warning",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.WARNING_MESSAGE,
                    null, options, options[0]);
            return foo == 0;
        }

        String passwd;
        JTextField passwordField = (JTextField) new JPasswordField(20);

        @Override
        public String getPassphrase() {
            return null;
        }

        @Override
        public boolean promptPassphrase(String message) {
            return true;
        }

        public boolean promptPassword(String message) {
            Object[] ob = {passwordField};
            int result
                    = JOptionPane.showConfirmDialog(null, ob, message,
                            JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                passwd = passwordField.getText();
                return true;
            } else {
                return false;
            }
        }

        @Override
        public void showMessage(String message) {
            JOptionPane.showMessageDialog(null, message);
        }
        final GridBagConstraints gbc
                = new GridBagConstraints(0, 0, 1, 1, 1, 1,
                        GridBagConstraints.NORTHWEST,
                        GridBagConstraints.NONE,
                        new Insets(0, 0, 0, 0), 0, 0);
        private Container panel;

        @Override
        public String[] promptKeyboardInteractive(String destination,
                String name,
                String instruction,
                String[] prompt,
                boolean[] echo) {
            panel = new JPanel();
            panel.setLayout(new GridBagLayout());

            gbc.weightx = 1.0;
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.gridx = 0;
            panel.add(new JLabel(instruction), gbc);
            gbc.gridy++;

            gbc.gridwidth = GridBagConstraints.RELATIVE;

            JTextField[] texts = new JTextField[prompt.length];
            for (int i = 0; i < prompt.length; i++) {
                gbc.fill = GridBagConstraints.NONE;
                gbc.gridx = 0;
                gbc.weightx = 1;
                panel.add(new JLabel(prompt[i]), gbc);

                gbc.gridx = 1;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.weighty = 1;
                if (echo[i]) {
                    texts[i] = new JTextField(20);
                } else {
                    texts[i] = new JPasswordField(20);
                }
                panel.add(texts[i], gbc);
                gbc.gridy++;
            }

            if (JOptionPane.showConfirmDialog(null, panel,
                    destination + ": " + name,
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE)
                    == JOptionPane.OK_OPTION) {
                String[] response = new String[prompt.length];
                for (int i = 0; i < prompt.length; i++) {
                    response[i] = texts[i].getText();
                }
                return response;
            } else {
                return null;  // cancel
            }
        }
    }
}