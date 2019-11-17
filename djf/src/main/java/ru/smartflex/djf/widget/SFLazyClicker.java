package ru.smartflex.djf.widget;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class SFLazyClicker {

    private static final long CLICKER_MS_DELAY = 200;

    private ReentrantLock lock = new ReentrantLock();
    private Condition cond = lock.newCondition();
    private ILazyClickerAssistant assistant = null;

    SFLazyClicker() {
        super();
    }

    void doClickInThread(Object ast) {
        new SFLazyClickerThread(this, ast);
    }

    private void doClick(Object ast) {

        lock.lock();
        try {

            assistant.click(ast);

            // to cancel previous threads
            cond.signalAll();

            boolean flag = cond.await(CLICKER_MS_DELAY, TimeUnit.MILLISECONDS);

            if (!flag) {
                // time is over. this is the last thread click
                assistant.doAction(ast);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }

    }

    @SuppressWarnings("unused")
    public void setAssistant(ILazyClickerAssistant assistant) {
        this.assistant = assistant;
    }

    public class SFLazyClickerThread extends Thread {

        private SFLazyClicker clicker;
        private Object obj;

        SFLazyClickerThread(SFLazyClicker cclicker, Object obj) {
            this.clicker = cclicker;
            this.obj = obj;
            start();
        }

        public void run() {
            clicker.doClick(obj);
        }
    }

}
