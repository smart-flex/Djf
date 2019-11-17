package ru.smartflex.djf.widget;

import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.JMenuBar;
import javax.swing.RootPaneContainer;

import ru.smartflex.djf.SizeFrameEnum;

/**
 * Assumed that object implemented this interface has to extends JFrame
 */
public interface IFrame extends RootPaneContainer {

    String PANEL_SOUTH_NAME = "pnlSouth";
    String PANEL_NORTH_NAME = "pnlNorth";
    String NAME_LABEL_INFO_FORM = "_NAME_LABEL_INFO_FORM";
    String NAME_LABEL_INFO_FORM_ADD = "_NAME_LABEL_INFO_FORM_ADD";

    void addForm(IForm iForm);

    void removeForm(IForm iForm);

    void showStatusMessage(TaskStatusLevelEnum status, String msg);

    void closeFrame();

    void expand(SizeFrameEnum size);

    Rectangle getFormUISCreenBound();

    void toFront();

    void showDescription(String msgMain, String msgAdd);

    @SuppressWarnings("unused")
    void setMenu(JMenuBar menu);

    void setMinimumSize(int width, int height);

    void setTitle(String title);

    @SuppressWarnings("unused")
    void setIconImage(Image image);
}
