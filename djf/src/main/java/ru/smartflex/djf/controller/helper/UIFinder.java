package ru.smartflex.djf.controller.helper;

import java.awt.Component;
import java.awt.Container;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JViewport;

public class UIFinder {

    private UIFinder() {
        super();
    }

    @SuppressWarnings("ConstantConditions")
    public static JComponent getControlByName(Object container,
                                              String nameComponent) {
        JComponent uiControl = null;

        if (container == null || nameComponent == null) {
            return null;
        }

        if (container instanceof JFrame) {
            Container cntp = ((JFrame) container).getContentPane();
            for (int i = 0; i < cntp.getComponentCount(); i++) {
                Component ct = cntp.getComponent(i);
                uiControl = getControlByName(ct, nameComponent);
                if (uiControl != null) {
                    break;
                }
            }
        } else if (container instanceof JDialog) {
            uiControl = getControlByName(((JDialog) container).getRootPane(),
                    nameComponent);
        } else if (container instanceof JPanel
                || container instanceof JScrollPane
                || container instanceof JViewport
                || container instanceof JTabbedPane
                || container instanceof JInternalFrame
                || container instanceof JRootPane
                || container instanceof JLayeredPane) {

            if (((JComponent) container).getName() != null
                    && ((JComponent) container).getName().equals(nameComponent)) {
                uiControl = (JComponent) container;
            } else {
                for (int i = 0; i < ((JComponent) container)
                        .getComponentCount(); i++) {
                    Component component = ((JComponent) container)
                            .getComponent(i);
                    uiControl = getControlByName(component,
                            nameComponent);
                    if (uiControl != null) {
                        break;
                    }
                }
            }
        } else if (container instanceof JTextField
                || container instanceof JFormattedTextField
                || container instanceof JPasswordField
                || container instanceof JComboBox
                || container instanceof JRadioButton
                || container instanceof JCheckBox
                || container instanceof JTextArea
                || container instanceof JTable
                || container instanceof JButton
                || container instanceof JLabel) {
            if (((Component) container).getName() != null
                    && ((Component) container).getName().equals(nameComponent)) {
                uiControl = (JComponent) container;
            }
        } else {
            if (((Component) container).getName() != null
                    && ((Component) container).getName().equals(nameComponent)) {
                uiControl = (JComponent) container;
            }
        }
        return uiControl;
    }

    public static void getUpAndTabSelection(Component comp, Component prevComp) {
        // workaround swing feature: focus request doesn't work if component
        // located in non-selected tab
        if (comp instanceof JTabbedPane) {
            if (prevComp != null) {
                ((JTabbedPane) comp).setSelectedComponent(prevComp);
            }
        }
        if (comp.getParent() != null) {
            getUpAndTabSelection(comp.getParent(), comp);
        }
    }

}
