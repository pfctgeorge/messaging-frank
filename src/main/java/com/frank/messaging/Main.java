package com.frank.messaging;

public class Main {
    public static void main(String[] args) throws Exception {
        long pid = ProcessHandle.current().pid();
        System.out.println("Process Id: " + pid);

        int sum = 0;
        for (int i = 0; i < 1000; i++) {
            Thread.sleep(15);
            sum += i;
        }
        System.out.println(sum);
    }
}

// JVM garbage collection; monitoring
