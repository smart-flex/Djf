package ru.smartflex.djf.widget.grid;

import java.util.concurrent.atomic.AtomicBoolean;

public class SFGridMouseMotionFlag {

    private static final ThreadLocal<AtomicBoolean> thread = new ThreadLocal<AtomicBoolean>();

    public static void setMotionFlag(boolean mouseMotion) {
        AtomicBoolean ab = thread.get();
        if (ab == null) {
            ab = new AtomicBoolean(mouseMotion);
            thread.set(ab);
        } else {
            ab.set(mouseMotion);
        }
    }

    static boolean isMotion() {
        AtomicBoolean ab = thread.get();
        if (ab == null) {
            return false;
        } else {
            return ab.get();
        }
    }
}
