package ru.smartflex.djf.widget.grid;

import java.awt.event.KeyEvent;

import ru.smartflex.djf.Djf;
import ru.smartflex.djf.FrameHelper;
import ru.smartflex.djf.controller.DataManager;
import ru.smartflex.djf.controller.FormStack;
import ru.smartflex.djf.controller.WidgetManager;
import ru.smartflex.djf.controller.bean.UIWrapper;
import ru.smartflex.djf.controller.bean.tree.IBeanWrapper;
import ru.smartflex.djf.controller.helper.PrefixUtil;
import ru.smartflex.djf.widget.ISFHandler;
import ru.smartflex.djf.widget.TaskStatusLevelEnum;

public class GridKeyHandler extends java.awt.event.KeyAdapter implements
        ISFHandler {

    private UIWrapper uiw;
    private WidgetManager wm;

    GridKeyHandler(WidgetManager wm, UIWrapper uiw) {
        this.wm = wm;
        this.uiw = uiw;
    }

    public void keyPressed(KeyEvent e) {
        if (!wm.getFormBag().isFormReady()) {
            return;
        }

        switch (e.getKeyCode()) {
            case KeyEvent.VK_DELETE:
                // to delete
                e.consume();
                FormStack.getCurrentFormBag().markAsDelete();
                break;
            case KeyEvent.VK_INSERT:
                // select/deselect row
                e.consume(); // always consume. Otherwise grid cell will be started
                // for edit
                FormStack.getCurrentFormBag().markRowAsSelected(uiw);
                break;
            case KeyEvent.VK_MULTIPLY:
                // select/deselect all row
                e.consume();
                FormStack.getCurrentFormBag().markGridAsSelected(uiw);
                break;
            case KeyEvent.VK_ENTER:
                SFGrid grid = (SFGrid) uiw.getObjectUI();
                if (grid.getTable().isEditing()) {
                    // because after editing without F2 and Enter pressing we have auto scroll on JTable. It is strange for user Then this event will be consumed
                    e.consume();
                    if (grid.getTable().getCellEditor() != null) {
                        grid.getTable().getCellEditor().stopCellEditing();
                    }
                }
                break;

            case KeyEvent.VK_TAB:
                // go to next ui item (default foxpro behavior)
                IBeanWrapper bwSelect = wm.getSelectedBeanWrapper();
                if (!bwSelect.isBeanWrapperLocked()
                        && !bwSelect.isBeanWrapperSelected()
                        && !bwSelect.isBeanWrapperDeleted()) {
                    // if row is not locked(selected/deleted) then go to the next
                    // item. Because locked(selected/deleted) row disabled children widgets
                    e.consume();
                    wm.moveDown(uiw.getUiName());
                } else {
                    e.consume();
                    FrameHelper.showStatusMessage(TaskStatusLevelEnum.WARNING,
                            PrefixUtil.getMsg("${djf.grid.row_locked}", null));
                }
                break;

            case KeyEvent.VK_S:
                if (e.getModifiers() == KeyEvent.CTRL_MASK) {
                    // save by CTRL+S from menu
                    e.consume();
                    Djf.saveForm();
                }
                break;
            case KeyEvent.VK_DOWN:
                // add new row
                if (uiw.isAppendAble()) {
                    if (wm.isLastRowScrollWidget(uiw)) {
                        DataManager.createNewObject(wm);
                    }
                } else {
                    if (wm.isLastRowScrollWidget(uiw)) {
                        FrameHelper.showStatusMessage(TaskStatusLevelEnum.WARNING,
                                PrefixUtil.getMsg("${djf.grid.no_append}", null));
                    }
                }
                break;

        }
    }

    @Override
    public void closeHandler() {
        uiw = null;
        wm = null;
    }

}
