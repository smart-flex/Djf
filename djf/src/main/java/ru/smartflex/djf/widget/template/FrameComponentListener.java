package ru.smartflex.djf.widget.template;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class FrameComponentListener extends ComponentAdapter {

    private FrameUI frameUI;

    FrameComponentListener(FrameUI frameUI) {
        this.frameUI = frameUI;
    }

    public void componentResized(ComponentEvent e) {
        frameUI.resizeToFit();
    }

}
