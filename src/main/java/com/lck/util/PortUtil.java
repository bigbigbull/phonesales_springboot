package com.lck.util;

import javax.swing.*;
import java.io.IOException;
import java.net.ServerSocket;

/**
 * describe:用于判断某个端口是否已启动
 *
 * @author lichangkai
 * @date 2019/04/29
 */
public class PortUtil {

    public static boolean testPort(int port) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            serverSocket.close();
            return false;
        } catch (IOException e) {
            return true;
        }
    }

    public static void checkPort(int port, String server, boolean shutdown) {
        if (!testPort(port)) {
            if (shutdown) {
                String message = String.format("在端口 %d 未检查得到 %s 启动%n", port, server);
                JOptionPane.showMessageDialog(null, message);
                System.exit(1);
            } else {
                String message = String.format("在端口 %d 未检查得到 %s 启动%n,是否继续?", port, server);
                if (JOptionPane.OK_OPTION != JOptionPane.showConfirmDialog(null, message)) {
                    System.exit(1);
                }
            }
        }
    }
}
