package ru.smartflex.djf.controller;

import java.util.List;

import javax.swing.SwingWorker;

import org.apache.commons.beanutils.MethodUtils;

import ru.smartflex.djf.FormAssistant;
import ru.smartflex.djf.FormStepEnum;
import ru.smartflex.djf.FrameHelper;
import ru.smartflex.djf.SFLogger;
import ru.smartflex.djf.controller.bean.BeanFormDef;
import ru.smartflex.djf.controller.bean.FormStepResult;
import ru.smartflex.djf.controller.bean.ModelLoadResult;
import ru.smartflex.djf.controller.bean.SFPair;
import ru.smartflex.djf.controller.bean.tree.TreeList;
import ru.smartflex.djf.controller.helper.ObjectCreator;
import ru.smartflex.djf.controller.helper.PrefixUtil;
import ru.smartflex.djf.model.gen.ModelType;
import ru.smartflex.djf.widget.TaskStatusLevelEnum;

public class FormManagerThread extends SwingWorker<ModelLoadResult, Void> {

    private FormManager fm;
    private boolean reLoading = false;
    private String welcomeMessageFromChildrenForm = null;

    public FormManagerThread(FormManager fm,
                             String welcomeMessageFromChildrenForm) {
        this.fm = fm;
        this.welcomeMessageFromChildrenForm = welcomeMessageFromChildrenForm;
    }

    public FormManagerThread(FormManager fm) {
        this.fm = fm;
    }

    @SuppressWarnings("RedundantThrows")
    @Override
    protected ModelLoadResult doInBackground() throws Exception {
        ModelLoadResult data = fm.getWidgetManager().getModelLoadResult();

        if (data != null) {
            reLoading = true;
        } else {
            data = new ModelLoadResult();
        }

        parseAndLoad(data);

        WidgetManagerHelper.checksGetters(fm, data);

        // winking
        // fm.getWidgetManager().enableItems(); // enables forms items for
        // following initial filling

        // disable (temporarily) from TGRID
        // WidgetManagerHelper.initialFilling(fm, data);

        return data;
    }

    private void delay(ModelType.Load load) {
        int delay = 0;

        if (load.getDelay() != null) {
            //noinspection CatchMayIgnoreException
            try {
                delay = Integer.parseInt(load.getDelay());
            } catch (Exception e) {
            }
            if (delay == 0) {
                //noinspection CatchMayIgnoreException
                try {
                    delay = (Integer) PrefixUtil
                            .getFormParameter(load.getDelay(),
                                    fm.getWidgetManager());
                } catch (Exception e) {
                }
            }
        }
        if (delay > 0) {
            //noinspection CatchMayIgnoreException
            try {
                Thread.sleep(delay);
            } catch (Exception e) {
            }
        }
    }

    private void parseAndLoad(ModelLoadResult data) {
        BeanFormDef beanDef = fm.getFormBag().getBeanFormDef();

        FormAssistant assistantLoading = fm.getFormBag().getAssistant();
        if (assistantLoading != null) {
            FormStepResult res = assistantLoading.step(
                    FormStepEnum.DATA_INIT_LOADING_PROCESS, fm.getFormBag()
                            .getFormSession());
            if (res != null) {
                if (!res.isStepOk()) {
                    data.setWasLoadError(true);
                    data.setException(res.getException());
                    SFLogger.error(
                            "Error by load bean in step \"DATA_INIT_LOADING_PROCESS\"",
                            res.getException());
                }
            }
        }

        List<ModelType> modelList = fm.getForm().getModels().getModel();
        for (ModelType mt : modelList) {
            if (data.isWasLoadError()) {
                break;
            }

            if (reLoading) {
                if (fm.getFormBag().isModelCanNotBeSaved(mt.getId())
                        || (!fm.getFormBag().isModelCanBeChanged(mt.getId()))) {
                    // data was loading through first iteration
                    continue;
                }
            }

            Object obj = null;
            String method = null;
            ModelType.Load load = mt.getLoad();
            boolean newSelf = beanDef.getModelBean(mt.getId()).isSelfCreated();

            if (newSelf) {
                // create new object
                if (!reLoading) {
                    // self created bean instantiated only once
                    try {
                        obj = ObjectCreator.createObjectWithoutParameter(beanDef
                                .getModelBean(mt.getId()).getClazz());
                    } catch (Exception e) {
                        data.setWasLoadError(true);
                        data.setException(e);
                        SFLogger.error("Error by bean creation", e);
                    }
                }
            } else {
                // load object or list objects
                delay(load);

                @SuppressWarnings("rawtypes")
                SFPair<Class, Object>[] pars = null;
                //noinspection ConstantConditions
                if (load != null) {
                    pars = ObjectCreator.createObjectParameters(
                            load.getParam(), mt.getId(), fm.getWidgetManager());
                    method = load.getMethod();
                }
                SFPair<String, Object> pair = PrefixUtil
                        .getMethodDefinition(method);
                if (pair != null) {
                    try {
                        Object[] objs = ObjectCreator.extractObject(pars);
                        @SuppressWarnings("rawtypes")
                        Class[] clss = ObjectCreator.extractClass(pars);
                        obj = MethodUtils.invokeMethod(pair.getValue(),
                                pair.getName(), objs, clss);
                    } catch (Exception e) {
                        data.setWasLoadError(true);
                        data.setException(e);
                        SFLogger.error("Error by load bean by method: \""
                                + method + "\"", e);
                    }
                }
            }

            if (obj != null) {
                if (!data.isWasLoadError()) {

                    TreeList treeList = new TreeList(fm.getFormBag()
                            .getWrapperFactory());

                    treeList.setRootDefProperties(beanDef.getModelBean(mt
                            .getId()));

                    fillTreeSetDefinition(beanDef.getModelBean(mt.getId()),
                            treeList);
                    treeList.fillTreeList(obj, beanDef.getModelBean(mt.getId())
                            .getClazz());

                    data.setTreeList(mt.getId(), treeList);

                    fm.getFormBag().getFormSession().addBean(mt.getId(), obj);
                }
            }
        } // end cycle of loading

        if (!data.isWasLoadError()) {
            if (!reLoading) {
                FormAssistant assistant = fm.getFormBag().getAssistant();
                if (assistant != null) {
                    assistant.step(FormStepEnum.AFTER_DATA_INIT_LOADING, fm
                            .getFormBag().getFormSession());
                }
            }
        }
    }

    private void fillTreeSetDefinition(BeanFormDef beanDef, TreeList treeList) {
        for (BeanFormDef bfd : beanDef.getLinearListOfSet()) {
            treeList.addSetDefinition(bfd);
        }
    }

    @Override
    public void done() {
        try {
            ModelLoadResult data = get();
            if (data.isWasLoadError()) {

                FrameHelper.showStatusMessage(TaskStatusLevelEnum.ERROR,
                        PrefixUtil.getMsg("${djf.message.error.form.creation}",
                                null));
                SFLogger.debug(FormManagerThread.class,
                        "Form was created with error");
                fm.getFormBag().registerErrorOnForm();
                fm.getWidgetManager().disableItems();
            } else {
                // fm.getWidgetManager().enableItems();
                fm.getFormBag().enableFrameButtons();

                fm.getWidgetManager().disallowRegisterFocusLastComponent();

                WidgetManagerHelper.initialFilling(fm, data);

                fm.getWidgetManager().getFormBag().setFormReady(true);

                // by this there is off invocation focus.moveUp and
                // focus.moveDown
                // because there is bug when last text widget completely filled,
                // then SFLengthFilter.replace invokes
                // wm.moveDown(textComponent.getName())
                // then it request focus on the first widget(grid), then all
                // components are refreshed
                // then on the last widget fired filter and
                // SFLengthFilter.replace invokes
                // wm.moveDown(textComponent.getName()) again
                // we have got cycle and java.lang.StackOverflowError
                fm.getWidgetManager().setAllowFocusMovement(false);
                WidgetManagerHelper.restoreSelectionOnScrollableWidgets(fm,
                        data, !reLoading);
                fm.getWidgetManager().setAllowFocusMovement(true);

                if (reLoading) {
                    fm.getWidgetManager()
                            .requestFocusOnLastSelectedFieldWrapper();

                    String msg = "Data form was reloaded successfully";
                    SFLogger.debug(FormManagerThread.class,
                            msg);
                    SFLogger.activityInfo("*** Activity *** ", msg, ": ", fm.getFormBag().getFormXml());
                } else {
                    // fm.getWidgetManager().goFirstWidget();

                    String msg = "Form was created successfully";
                    SFLogger.debug(FormManagerThread.class,
                            msg);
                    SFLogger.activityInfo("*** Activity *** ", msg, ": ", fm.getFormBag().getFormXml());
                }

                if (welcomeMessageFromChildrenForm != null) {
                    FrameHelper.showStatusMessage(TaskStatusLevelEnum.OK,
                            welcomeMessageFromChildrenForm);
                } else {
                    FrameHelper.showStatusMessage(TaskStatusLevelEnum.OK, fm
                            .getFormBag().getWelcomeMessage());
                }

                fm.getWidgetManager().allowRegisterFocusLastComponent();

            }
        } catch (Exception e) {
            SFLogger.error("Error by load and parsing data", e);
            FrameHelper.showStatusMessage(TaskStatusLevelEnum.ERROR, PrefixUtil
                    .getMsg("${djf.message.error.form.creation}", null));
            SFLogger.debug(FormManagerThread.class,
                    "Form was created with error");
            fm.getFormBag().registerErrorOnForm();
            fm.getWidgetManager().disableItems();
        }

        FrameHelper.hideWaitLongPanel();
    }

}
