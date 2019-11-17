package ru.smartflex.djf;

import java.awt.Cursor;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JFrame;

import ru.smartflex.djf.controller.FormStack;
import ru.smartflex.djf.widget.SFDialogForm;
import ru.smartflex.djf.widget.TaskStatusLevelEnum;
import ru.smartflex.djf.widget.WaitLongPanel;
import ru.smartflex.djf.widget.WaitLongThread;

public class FrameHelper {

    private static WaitLongThread worker = null;
    private static WaitSemafor semafor = new WaitSemafor();

    private FrameHelper() {
    }

    public static void showStatusMessage(TaskStatusLevelEnum status, String msg) {
        Djf.getConfigurator().getFrame().showStatusMessage(status, msg);
    }

    public static void showWaitLongPanel(String msg) {
        startWaitPanel(msg);
    }

    public static void showWaitLongPanel() {
        startWaitPanel(null);
    }

    private static void startWaitPanel(String msg) {
        semafor.startWaitProcess();

        WaitLongPanel waitLongPanel = new WaitLongPanel(msg);

        if (FormStack.getCurrentFormBag() == null || !FormStack.getCurrentFormBag().isFormModal()) {
            JFrame frame = (JFrame) Djf.getConfigurator().getFrame();
            frame.setGlassPane(waitLongPanel);
        } else {
            SFDialogForm dial = (SFDialogForm) FormStack.getCurrentFormBag().getFormWrapper().getObjectUI();
            dial.setGlassPane(waitLongPanel);
        }

        worker = new WaitLongThread(waitLongPanel, semafor);
        worker.execute();

    }

    private static void clearWorker() {
        if (worker != null) {
            worker.cancel(true);
            worker = null;
        }
    }

    public static void hideWaitLongPanel() {
        try {
            if (semafor.isStopBlinkingAllowed()) {
                clearWorker();

                WaitLongPanel waitLongPanel;

                if (!FormStack.getCurrentFormBag().isFormModal()) {
                    JFrame frame = (JFrame) Djf.getConfigurator().getFrame();
                    waitLongPanel = (WaitLongPanel) frame.getGlassPane();
                } else {
                    SFDialogForm dial = (SFDialogForm) FormStack.getCurrentFormBag().getFormWrapper().getObjectUI();
                    waitLongPanel = (WaitLongPanel) dial.getGlassPane();
                }

                waitLongPanel.setCursor(Cursor.getDefaultCursor());
                waitLongPanel.setVisible(false);
                waitLongPanel.close();

                semafor.finishProcessOfBlinkig();
            } else {
                clearWorker();
            }
        } catch (Exception e) {
            SFLogger.error("Error by trying to hide wait panel", e);
        }
    }

    public static class WaitSemafor {
        private Lock lockSemafor = new ReentrantLock();

        private boolean startWaitProcess = false;
        private Semaphore semaphore = new Semaphore(1);
        private boolean startBlinking = false;

        public boolean isStartBlinkingAllowed() {
            lockSemafor.lock();
            try {
                if (startWaitProcess) {
                    if (semaphore.tryAcquire()) {
                        startBlinking = true;
                        return true;
                    }
                }
            } finally {
                lockSemafor.unlock();
            }
            return false;
        }

        /**
         * Invokes within flag from isStartBlinkingAllowed()
         */
        public void finishProcessOfBlinkig() {
            semaphore.release();
        }

        boolean isStopBlinkingAllowed() throws InterruptedException {
            lockSemafor.lock();
            try {
                if (startBlinking) {
                    semaphore.acquire();
                    startBlinking = false;
                    return true;
                } else {
                    startWaitProcess = false;
                }
            } finally {
                lockSemafor.unlock();
            }
            return false;
        }

        void startWaitProcess() {

            lockSemafor.lock();
            try {
                startWaitProcess = true;
                startBlinking = false;
            } finally {
                lockSemafor.unlock();
            }
        }
    }

}
