package com.subnit.concurrent;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

/**
 * description:
 * date : create in 21:19 2018/6/13
 * modified by :
 *
 * @author subo
 */
public class MyThreadPool {
    private ArrayList<MyThread> threadList;

    private ArrayBlockingQueue<Runnable> taskQueue;

    private int threadNum = 0;

    private int workThreadNum = 0;

    private Boolean shutDown = false;

    private final ReentrantLock mainLock = new ReentrantLock();

    public MyThreadPool(int threadNum) {
        this.threadNum = threadNum;
    }

    public void execute(Runnable runnable) {
        try {
            mainLock.lock();
            if (workThreadNum < threadNum) {
                MyThread myThread = new MyThread(runnable);
                myThread.start();
                workThreadNum++;
            } else {
                taskQueue.add(runnable);
            }
        } finally {
            mainLock.unlock();
        }


    }


    class MyThread extends Thread {
        private Runnable task;

        public MyThread(Runnable runnable) {
            this.task = runnable;
        }

        @Override
        public void run() {
            while (!shutDown) {
                if (task != null) {
                    task.run();
                    task = null;
                } else {
                    Runnable queueTask = taskQueue.poll();
                    if (queueTask != null) {
                        queueTask.run();
                    }
                }


            }
        }
    }

}
