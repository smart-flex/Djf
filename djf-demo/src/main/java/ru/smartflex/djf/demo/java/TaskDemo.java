package ru.smartflex.djf.demo.java;

import java.util.ArrayList;
import java.util.List;

import ru.smartflex.djf.SFConstants;
import ru.smartflex.djf.controller.helper.PrefixUtil;
import ru.smartflex.djf.tool.OtherUtil;

public class TaskDemo {

    private Integer idTask = null;
    private String nameTask = null;
    private String formXML = null;

    private String formBody = null;
    private String panelBodies = null;
    private String beanBodies = null;
    private String formXMLShortName = null;
    private String javaHelperBodies = null;
    private String javaBeanBodies = null;
    private String formDescription = null;

    private List<TaskDemo> leafTaskDemo = new ArrayList<TaskDemo>();

    @SuppressWarnings("unused")
    public TaskDemo() {
        super();
    }

    @SuppressWarnings("WeakerAccess")
    public TaskDemo(Integer idTask, String nameTask, TaskDemo taskDemo) {
        super();
        this.idTask = idTask;
        this.nameTask = nameTask;

        this.formXML = taskDemo.formXML;
        this.formBody = taskDemo.formBody;
        this.panelBodies = taskDemo.panelBodies;
        this.beanBodies = taskDemo.beanBodies;
        this.formXMLShortName = taskDemo.formXMLShortName;
        this.javaHelperBodies = taskDemo.javaHelperBodies;
        this.javaBeanBodies = taskDemo.javaBeanBodies;
        this.formDescription = taskDemo.formDescription;

    }

    @SuppressWarnings("WeakerAccess")
    public TaskDemo(Integer idTask, String nameTask, String formXML,
                    String[] panels, String[] beans, String[] javaHelper, String[] javaBean) {
        super();
        this.idTask = idTask;
        this.nameTask = nameTask;
        this.formXML = formXML;

        init(panels, beans, javaHelper, javaBean);
    }

    private void init(String[] panels, String[] beans, String[] javaHelper, String[] javaBean) {
        formBody = OtherUtil.getBodyAsText(formXML);

        String formHTML = formXML.replace("xml", "html").replace(".frm.", ".");
        //noinspection CatchMayIgnoreException
        try {
            formDescription = OtherUtil.getBodyAsText(formHTML);
        } catch (Exception e) {
        }

        formXMLShortName = PrefixUtil.extractNameBySlash(formXML);

        panelBodies = concatenateSource(panels, "panel", true);
        beanBodies = concatenateSource(beans, "bean definition", true);
        javaHelperBodies = concatenateSource(javaHelper, "java", false);
        javaBeanBodies = concatenateSource(javaBean, "java", false);
    }

    private String concatenateSource(String[] files, String categoryType,
                                     boolean xml) {

        if (files != null && files.length > 0) {
            StringBuilder sb = new StringBuilder();
            for (String file : files) {
                if (sb.length() > 0) {
                    sb.append(SFConstants.NEW_LINE_CR);
                    sb.append(SFConstants.NEW_LINE_CR);
                }
                if (xml) {
                    sb.append("<!-- ");
                } else {
                    sb.append("/* ");
                }
                sb.append("******* Source of ").append(categoryType).append(": ");
                sb.append(file);
                sb.append(" *******");
                if (xml) {
                    sb.append(" -->");
                } else {
                    sb.append(" */");
                }
                sb.append(SFConstants.NEW_LINE_CR);
                sb.append(SFConstants.NEW_LINE_CR);

                String panelBody = OtherUtil.getBodyAsText(file);
                sb.append(panelBody);
            }
            return sb.toString();
        }

        return null;
    }

    @SuppressWarnings("WeakerAccess")
    public Integer getIdTask() {
        return idTask;
    }

    @SuppressWarnings("unused")
    public void setIdTask(Integer idTask) {
        this.idTask = idTask;
    }

    @SuppressWarnings("unused")
    public String getNameTask() {
        return nameTask;
    }

    @SuppressWarnings("unused")
    public void setNameTask(String nameTask) {
        this.nameTask = nameTask;
    }

    @SuppressWarnings("unused")
    public String getFormXML() {
        return formXML;
    }

    @SuppressWarnings("unused")
    public void setFormXML(String formXML) {
        this.formXML = formXML;
    }

    @SuppressWarnings("unused")
    public String getFormBody() {
        return formBody;
    }

    @SuppressWarnings("unused")
    public void setFormBody(String formBody) {
        this.formBody = formBody;
    }

    @SuppressWarnings("unused")
    public String getPanelBodies() {
        return panelBodies;
    }

    @SuppressWarnings("unused")
    public void setPanelBodies(String panelBodies) {
        this.panelBodies = panelBodies;
    }

    @SuppressWarnings("WeakerAccess")
    public List<TaskDemo> getLeafTaskDemo() {
        return leafTaskDemo;
    }

    @SuppressWarnings("unused")
    public void setLeafTaskDemo(List<TaskDemo> leafTaskDemo) {
        this.leafTaskDemo = leafTaskDemo;
    }

    @SuppressWarnings("unused")
    public String getFormXMLShortName() {
        return formXMLShortName;
    }

    @SuppressWarnings("unused")
    public void setFormXMLShortName(String formXMLShortName) {
        this.formXMLShortName = formXMLShortName;
    }

    @SuppressWarnings("unused")
    public String getJavaHelperBodies() {
        return javaHelperBodies;
    }

    @SuppressWarnings("unused")
    public void setJavaHelperBodies(String javaHelperBodies) {
        this.javaHelperBodies = javaHelperBodies;
    }

    @SuppressWarnings("unused")
    public String getJavaBeanBodies() {
        return javaBeanBodies;
    }

    @SuppressWarnings("unused")
    public void setJavaBeanBodies(String javaBeanBodies) {
        this.javaBeanBodies = javaBeanBodies;
    }

    @SuppressWarnings("unused")
    public String getFormDescription() {
        return formDescription;
    }

    @SuppressWarnings("unused")
    public void setFormDescription(String formDescription) {
        this.formDescription = formDescription;
    }

    @SuppressWarnings("unused")
    public String getBeanBodies() {
        return beanBodies;
    }

    @SuppressWarnings("unused")
    public void setBeanBodies(String beanBodies) {
        this.beanBodies = beanBodies;
    }

    @Override
    public String toString() {
        return "TaskDemo [idTask=" + idTask + ", nameTask=" + nameTask + "]";
    }

}
