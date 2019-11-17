package ru.smartflex.djf.builder;

import org.apache.commons.beanutils.ConstructorUtils;
import org.apache.commons.lang3.ClassUtils;

import ru.smartflex.djf.SFLogger;
import ru.smartflex.djf.controller.WidgetManager;
import ru.smartflex.djf.controller.bean.UIWrapper;
import ru.smartflex.djf.controller.exception.MissingException;
import ru.smartflex.djf.controller.exception.ObjectCreationException;
import ru.smartflex.djf.controller.helper.ObjectCreator;
import ru.smartflex.djf.model.gen.LayoutType;

class LayoutBuilder {

    private LayoutBuilder() {
    }

    static UIWrapper build(LayoutType lt, WidgetManager wm) {
        UIWrapper wrapper = new UIWrapper();

        if (lt != null) {
            String className = lt.getClazz();
            if (className == null) {
                throw new MissingException(
                        "There is missed class name for layout tag");
            }

            Object[] pars;
            @SuppressWarnings("rawtypes")
            Class layputClazz;
            try {
                layputClazz = ClassUtils.getClass(className);
            } catch (ClassNotFoundException e1) {
                String err = "Class: " + className;
                SFLogger.error(err, e1);
                throw new ObjectCreationException(err, e1);
            }

            pars = ObjectCreator.extractObject(ObjectCreator
                    .createObjectParameters(lt.getParam(), null, wm));

            try {

                Object ui = ConstructorUtils.invokeConstructor(layputClazz,
                        pars);
                wrapper.setObjectUI(ui);
            } catch (Exception e) {
                String err = "Class: " + className;
                SFLogger.error(err, e);
                throw new ObjectCreationException(err, e);
            }
        }

        return wrapper;
    }
}
