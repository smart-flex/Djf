package ru.smartflex.djf.widget.grid;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class SFGridMouseMotionListener implements MouseMotionListener {

    private SFTableWrapper table;

    SFGridMouseMotionListener(SFTableWrapper table) {
        super();
        this.table = table;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // nothing yet
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        table.determineCurrentPointMouse(e.getPoint());
    }

}
