package ru.smartflex.djf.demo.java;

import ru.smartflex.djf.Djf;
import ru.smartflex.djf.FormAssistant;
import ru.smartflex.djf.FormStepEnum;
import ru.smartflex.djf.controller.bean.FormStepResult;
import ru.smartflex.djf.controller.bean.IFormSession;
import ru.smartflex.djf.controller.bean.tree.BeanStatusEnum;
import ru.smartflex.djf.controller.bean.tree.BeanWrapper;
import ru.smartflex.djf.controller.bean.tree.IBeanWrapper;

/**
 * This assistant is used in a few forms.
 *
 * @author gali.shaimardanov@gmail.com
 */
public class GridFieldsAssistant extends FormAssistant {

    @Override
    public FormStepResult step(FormStepEnum step, IFormSession formSess) {
        return null;
    }

    /**
     * Overriding this method allows to do status customization.
     */
    @Override
    public IBeanWrapper createBeanWrappper(Object data, BeanStatusEnum status) {
        IBeanWrapper bw = new BeanWrapper(data, status);
        if (status == BeanStatusEnum.PERSISTENT && data instanceof PojoAccount) {
            PojoAccount acc = (PojoAccount) data;
            if (!acc.isEmployed()) {
                bw.setupBeanLocked();
            }
        }
        return bw;
    }

    /**
     * This method will be invoked when user clicks on "Filter" button.
     */
    @SuppressWarnings("unused")
    public void filter() {
        // get filter object
        FilterAccount fa = (FilterAccount) Djf.getCurrentObject("fl");
        // save this object in session with key "filter".
        Djf.getSession().addBean("filter", fa);
        // under refreshing procedure this object will be passed into load method as parameter
        Djf.refreshFormForce();
    }

    /**
     * This method is invoked modal form for address filling.
     */
    @SuppressWarnings("unused")
    public void goAddress() {
        Djf.runForm("ru/smartflex/djf/demo/xml/Address.frm.xml");
    }

}
