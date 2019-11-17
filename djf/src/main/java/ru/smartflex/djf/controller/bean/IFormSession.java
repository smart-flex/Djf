package ru.smartflex.djf.controller.bean;

import java.util.Map;

/**
 * Allows dynamically manage state of form as minimum before form creation. See method <code>step</code> for class: FormAssistant.
 *
 * @author Gali.Shaimardanov
 * @see ru.smartflex.djf.FormAssistant
 */
public interface IFormSession {

    /**
     * Returns id form that allows to determine accessible this form or not
     */
    @SuppressWarnings("unused")
    String getIdForm();

    @SuppressWarnings("unused")
    void setReadOnly(String idModel);

    Boolean isReadOnly(String idModel);

    @SuppressWarnings("unused")
    void setNoAppend(String idModel);

    @SuppressWarnings("unused")
    void setNoDelete(String idModel);

    Boolean isNoAppend(String idModel);

    @SuppressWarnings("unused")
    void setNoSave(String idModel);

    Boolean isNoSave(String idModel);

    Boolean isNoDelete(String idModel);

    @SuppressWarnings("unused")
    Map<String, Object> getFormParameters();

    void addBean(String idModel, Object bean);

    @SuppressWarnings("unused")
    Object getBean(String idModel);

}
