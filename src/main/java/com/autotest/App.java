package com.autotest;

import org.apache.log4j.Logger;
import org.apache.log4j.net.SimpleSocketServer;

public class App {

    private static final Logger log = Logger.getLogger(SimpleSocketServer.class);
 
    public static void main(String[] args) throws Exception {
 
//        System.out.println("start log4j Socket Server  Port 5000");
// 
//        String[] argss = { "5000", "src/log4j.properties" };
// 
//        SimpleSocketServer.main(argss);
// 
//        log.info("succ");
    	System.out.println(toBinary(10));
    }   
    
    public static String toBinary(int num) {// num = 10 
            String str = "";
            while (num != 0) {
                str = num % 2 + str;//给定的数循环除以2，直到商为0或者1为止 将每一步除的结果的余数记录下来 第一位 0 
                num = num / 2; //不断除2 
            }
            return str;
        }
}
