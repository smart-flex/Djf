package ru.smartflex.djf.controller;

import ru.smartflex.djf.Djf;
import ru.smartflex.djf.WidgetTypeEnum;
import ru.smartflex.djf.controller.bean.SFPair;
import ru.smartflex.djf.controller.bean.UIWrapper;
import ru.smartflex.djf.controller.bean.tree.BeanStatusEnum;

public class DataManager {

    private DataManager() {
        super();
    }

    public static void createNewObject(WidgetManager wm) {

        boolean fok = true;
        UIWrapper uiw = wm.getSelectedScrollWrapper();
        if (uiw == null) {
            Djf.showStatusWarnMessage("${djf.message.warn.no_selected_for_append}");
            fok = false;
        } else {
            String idModel = uiw.getModelBase().getIdModel();
            if (!wm.getFormBag().isModelCanBeAppend(idModel)) {
                Djf.showStatusWarnMessage("${djf.message.warn.append_is_not_allowed}");
                fok = false;
            }
        }

        if (fok) {
            if (uiw.getWidgetType() != WidgetTypeEnum.TGRID) {
                // checks if exists new unchanged row
                SFPair<BeanStatusEnum, BeanStatusEnum> statusLastRow = wm
                        .getStatusLastRowScrollWidget(uiw);
                if (statusLastRow.getName() == BeanStatusEnum.NEW) {
                    if (statusLastRow.getValue() != BeanStatusEnum.CHANGED) {
                        Djf.showStatusWarnMessage("${djf.message.warn.have_to_fill_new_row}");
                        fok = false;
                    }
                }
            }
        }
        if (fok) {
            wm.createNewObject(uiw);
        }
    }
}
