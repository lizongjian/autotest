package com.autotest;

import org.apache.log4j.Logger;
import org.apache.log4j.net.SimpleSocketServer;

public class App {

    private static final Logger log = Logger.getLogger(SimpleSocketServer.class);
 
    public static void main(String[] args) throws Exception {
 
        System.out.println("start log4j Socket Server  Port 5000");
 
        String[] argss = { "5000", "src/log4j.properties" };
 
        SimpleSocketServer.main(argss);
 
        log.info("succ");
    }    
}
