package ru.smartflex.djf.widget;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.plaf.basic.BasicComboPopup;
import java.awt.*;

public class SFComboBoxPopupMenuListener implements PopupMenuListener, ISFHandler {
    private JScrollPane scrollPane;

    @Override
    public void popupMenuWillBecomeVisible(PopupMenuEvent popupMenuEvent) {
        JComboBox comboBox = (JComboBox) popupMenuEvent.getSource();

        final Object child = comboBox.getAccessibleContext().getAccessibleChild(0);

        if (child instanceof BasicComboPopup) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    customizePopup((BasicComboPopup) child);
                }
            });
        }

    }

    private void customizePopup(BasicComboPopup popup) {
        scrollPane = getScrollPane(popup);

        popupWider(popup);

        //  For some reason in JDK7 the popup will not display at its preferred
        //  width unless its location has been changed from its default
        //  (ie. for normal "pop down" shift the popup and reset)

        Component comboBox = popup.getInvoker();
        Point location = comboBox.getLocationOnScreen();

        int height = comboBox.getPreferredSize().height;
        popup.setLocation(location.x, location.y + height - 1);
        popup.setLocation(location.x, location.y + height);
    }

    /*
     *  Adjust the width of the scrollpane used by the popup
     */
    private void popupWider(BasicComboPopup popup) {
        JList list = popup.getList();

        //  Determine the maximimum width to use:
        //  a) determine the popup preferred width
        //  b) limit width to the maximum if specified
        //  c) ensure width is not less than the scroll pane width

        int popupWidth = list.getPreferredSize().width
                + 5  // make sure horizontal scrollbar doesn't appear
                + getScrollBarWidth(popup, scrollPane);

        Dimension scrollPaneSize = scrollPane.getPreferredSize();
        popupWidth = Math.max(popupWidth, scrollPaneSize.width);

        //  Adjust the width
        scrollPaneSize.width = popupWidth;
        scrollPane.setPreferredSize(scrollPaneSize);
        scrollPane.setMaximumSize(scrollPaneSize);
    }

    /*
     *  Get the scroll pane used by the popup so its bounds can be adjusted
     */
    private JScrollPane getScrollPane(BasicComboPopup popup) {
        JList list = popup.getList();
        Container c = SwingUtilities.getAncestorOfClass(JScrollPane.class, list);

        return (JScrollPane) c;
    }

    /*
     *  I can't find any property on the scrollBar to determine if it will be
     *  displayed or not so use brute force to determine this.
     */
    private int getScrollBarWidth(BasicComboPopup popup, JScrollPane scrollPane) {
        int scrollBarWidth = 0;
        JComboBox comboBox = (JComboBox) popup.getInvoker();

        if (comboBox.getItemCount() > comboBox.getMaximumRowCount()) {
            JScrollBar vertical = scrollPane.getVerticalScrollBar();
            scrollBarWidth = vertical.getPreferredSize().width;
        }

        return scrollBarWidth;
    }

    @Override
    public void popupMenuWillBecomeInvisible(PopupMenuEvent popupMenuEvent) {

    }

    @Override
    public void popupMenuCanceled(PopupMenuEvent popupMenuEvent) {

    }

    @Override
    public void closeHandler() {
        scrollPane = null;
    }
}
