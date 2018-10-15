package com.socket.common.log;

import java.nio.channels.SelectionKey;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author shiwen.chen
 * @date 2018-10-15 13:59
 */
public class Log {

    public static void info(String message, Object... info) {
        printLog(message, "INFO", info);
    }

    public static void error(String message, Object... info) {
        printLog(message, "ERROR", info);
    }

    private static void printLog(String message, String level, Object[] info) {
        StringBuilder builder = new StringBuilder();
        LocalDateTime dateTime = LocalDateTime.now();
        String format = dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        builder.append(format);
        builder.append(' ');
        builder.append(" : THREAD-ID [");
        builder.append(Thread.currentThread().getId());
        builder.append(']');
        builder.append(" : [")
                .append(level)
                .append("] : ");
        builder.append(message);
        if (info != null && info.length > 0) {
            for (Object s : info) {
                builder.replace(builder.indexOf("{"), builder.indexOf("}") + 1, s.toString());
            }
        }
        System.out.println(builder);
    }

    public static void main(String[] args) {
//        Log.info("nihao->{},->{}", "haha", "fds");
//        int ops = 1;
//        int code = 131;
//        System.out.println(ops & code);
//
//        System.out.println(SelectionKey.OP_ACCEPT);
//        System.out.println(SelectionKey.OP_CONNECT);
//        System.out.println(SelectionKey.OP_READ);
//        System.out.println(SelectionKey.OP_WRITE);

        int interest = 3 | 7 ;

        System.out.println(interest & 5);

    }
}
