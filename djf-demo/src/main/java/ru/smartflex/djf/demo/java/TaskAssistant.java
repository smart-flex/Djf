package ru.smartflex.djf.demo.java;

import java.util.HashMap;
import java.util.Map;

import ru.smartflex.djf.Djf;
import ru.smartflex.djf.FormAssistant;
import ru.smartflex.djf.FormStepEnum;
import ru.smartflex.djf.IParameterMaker;
import ru.smartflex.djf.controller.bean.FormStepResult;
import ru.smartflex.djf.controller.bean.IFormSession;

public class TaskAssistant extends FormAssistant implements IParameterMaker {

    @Override
    public FormStepResult step(FormStepEnum step, IFormSession formSess) {
        // No need anything
        return null;
    }

    @Override
    public Map<String, Object> makeParametersMap() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("itemsAccess", "sfSpecialService");

        TaskDemo td = (TaskDemo) Djf.getCurrentObject();

        if (td.getIdTask() == null) {
            return map;
        }

        switch (td.getIdTask()) {
            case 4:
                map.put("makeNetworkError", Boolean.TRUE);
                map.put("loadDelay", 2000);
                break;
            case 5:
            case 8:
                // restrict access to some items
                map.remove("itemsAccess");
                break;
            case 9:
                map.put("equipWorkHist",
                        "ru/smartflex/djf/demo/xml/EquipWorkHist.pnl.xml");
                map.put("itemsAccessBank", "sfBank");
                break;
        }

        return map;
    }

}
