package ru.smartflex.djf.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ru.smartflex.djf.SmartFlexException;
import ru.smartflex.djf.WidgetTypeEnum;
import ru.smartflex.djf.controller.bean.GridColumnInfo;
import ru.smartflex.djf.controller.bean.UIWrapper;
import ru.smartflex.djf.controller.bean.tree.WidgetTreeNode;
import ru.smartflex.djf.controller.exception.ObjectCreationException;
import ru.smartflex.djf.widget.grid.SFGrid;

/**
 * Help class for form creation
 *
 * @author gali.shaimardanov@gmail.com
 */
class FormManagerHelper {

    static void makeHierarchyUIItems(String idModel,
                                     List<UIWrapper> listUI,
                                     Map<String, WidgetTreeNode<UIWrapper>> mapUITreeNode) {

        List<UIWrapper> listUIBasedModel = new ArrayList<UIWrapper>();

        int maxLevel = -1;
        for (UIWrapper uiw : listUI) {
            if (uiw.isItemInModel(idModel)) {
                listUIBasedModel.add(uiw);
                if (uiw.getLevelUI(idModel) > maxLevel) {
                    maxLevel = uiw.getLevelUI(idModel);
                }
            }
        }

        WidgetTreeNode<UIWrapper> root = new WidgetTreeNode<UIWrapper>();

        fillTreeOfWidget(maxLevel, listUIBasedModel, root, idModel);

        mapUITreeNode.put(idModel, root);

    }

    private static void fillTreeOfWidget(int maxLevel,
                                         List<UIWrapper> listUIModel, WidgetTreeNode<UIWrapper> rootNode,
                                         String idModel) {

        // remark: enumerate list of level ONLY for base model
        for (int level = 0; level <= maxLevel; level++) {
            // search scroll widget and validate

            int cntScrollLevel = 0;
            Set<String> setTreePath = new HashSet<String>();
            Set<UIWrapper> scrollLevelWidget = new HashSet<UIWrapper>();
            for (UIWrapper uiw : listUIModel) {

                if (uiw.getLevelUI(idModel) != level) {
                    continue;
                }
                if (uiw.isScrollWidget()) {
                    cntScrollLevel++;
                    scrollLevelWidget.add(uiw);
                }

                if (cntScrollLevel >= 1) {
                    if (level == 0) {
                        if (cntScrollLevel > 1) {
                            throw new SmartFlexException(
                                    "Grid widget must be one for zero level.");
                        }
                    } else {
                        if (uiw.isScrollWidget()) {
                            if (setTreePath.contains(uiw.getTreePath(idModel))) {
                                throw new SmartFlexException(
                                        "Grid widget must contains different children. Treepath is: "
                                                + uiw.getTreePath(idModel));
                            } else {
                                setTreePath.add(uiw.getTreePath(idModel));
                            }
                        }
                    }
                }
            }
            // forms Tree
            if (scrollLevelWidget.size() > 0) {
                for (UIWrapper uiScroll : scrollLevelWidget) {

                    WidgetTreeNode<UIWrapper> node = rootNode;
                    if (level > 0) {
                        // search parent node which was added on previous step
                        // (index level)
                        node = rootNode.getParentScrollNodeByChildPath(
                                uiScroll.getLevelUI(idModel),
                                uiScroll.getTreePath(idModel), true);
                    }
                    WidgetTreeNode<UIWrapper> parentScroll = node.add(
                            new WidgetTreeNode<UIWrapper>(uiScroll, true,
                                    uiScroll.getTreePath(idModel)), true);
                    uiScroll.setTreeNode(parentScroll, idModel);
                    uiScroll.setIncludedIntoWidgetTree(true);

                    // grid columns
                    if (WidgetTypeEnum.isScrollAbleInMDStyle(uiScroll
                            .getWidgetType())) {

                        SFGrid grid = (SFGrid) uiScroll.getObjectUI();
                        List<GridColumnInfo> list = grid.getListColumnInfo();
                        for (GridColumnInfo gci : list) {
                            if (gci.getColumnDefinition() == null) {
                                // info column; skip it
                                continue;
                            }
                            if (uiScroll.equalsBaseTreePath(gci)) {
                                WidgetTreeNode<UIWrapper> childNode = parentScroll
                                        .add(new WidgetTreeNode<UIWrapper>(gci,
                                                        false, gci.getTreePath(idModel)),
                                                true);
                                gci.setTreeNode(childNode, idModel);

                                setAdditionalTreeNode(gci);
                            } else {
                                // here is treepath case: ts (in grid) in
                                // comparision ts.ts.idTask (in column of grid)
                                // may be in next generation of Djf it will
                                // obsolete but now we have to throw exception
                                String msg = "For grid tree paths have to be equals: "
                                        + uiScroll.getModelBase().getTreePath()
                                        + " != "
                                        + gci.getModelBase().getTreePath()
                                        + " ( bind is: " + gci.getBind() + " )";
                                throw new ObjectCreationException(msg);
                            }

                        }

                        handleLinkedComboBox(list);
                    }

                    // cycle for non-scroll widgets those depends on scroll
                    List<UIWrapper> lCmb = null;

                    for (UIWrapper uiw : listUIModel) {
                        if (uiw.getLevelUI(idModel) != level) {
                            continue;
                        }
                        if (!uiw.isScrollWidget()) {
                            if (uiScroll.equalsBaseTreePath(uiw)) {
                                WidgetTreeNode<UIWrapper> childNode = parentScroll
                                        .add(new WidgetTreeNode<UIWrapper>(uiw,
                                                        false, uiw.getTreePath(idModel)),
                                                true);
                                uiw.setTreeNode(childNode, idModel);
                                uiw.setIncludedIntoWidgetTree(true);

                                setAdditionalTreeNode(uiw);

                                if (lCmb == null) {
                                    lCmb = new ArrayList<UIWrapper>();
                                }
                                lCmb.add(uiw);
                            }
                        }
                    }
                    handleLinkedComboBox(lCmb);
                }
            }
        }

        // edit page
        for (int level = 0; level <= maxLevel; level++) {
            List<UIWrapper> lCmb = null;

            for (UIWrapper uiw : listUIModel) {

                // there can be such bind as acc.subtliving.id_tliving
                // this can be considered as the 0 level
                // therefore - comment the following
				/*if (uiw.getLevelUI(idModel) != 0) {
					continue;
				}*/
                if (uiw.isIncludedIntoWidgetTree()) {
                    continue;
                }

                if (uiw.getTreeNode(idModel) == null) {

                    WidgetTreeNode<UIWrapper> node = new WidgetTreeNode<UIWrapper>(
                            uiw, false, uiw.getTreePath(idModel));
                    rootNode.add(node, true);
                    uiw.setTreeNode(node, idModel);
                    uiw.setIncludedIntoWidgetTree(true);

                    setAdditionalTreeNode(uiw);

                    if (lCmb == null) {
                        lCmb = new ArrayList<UIWrapper>();
                    }
                    lCmb.add(uiw);
                }
            }
            handleLinkedComboBox(lCmb);


        }

        // checks on including in tree
        for (UIWrapper uiw : listUIModel) {
            if (!uiw.isIncludedIntoWidgetTree()) {
                String msg = "For model: \"" + idModel
                        + "\" there is orphan widget: " + uiw.getBind();
                throw new ObjectCreationException(msg);
            }
        }

    }

    private static void setAdditionalTreeNode(UIWrapper uiw) {
        if (uiw.getWidgetType() == WidgetTypeEnum.COMBOBOX) {

            WidgetTreeNode<UIWrapper> nodeFill = new WidgetTreeNode<UIWrapper>(
                    uiw, false, uiw.getModelFill().getTreePath());
            uiw.setTreeNode(nodeFill, uiw.getModelFill().getIdModel());

        }
    }

    private static void handleLinkedComboBox(List<?> list) {
        if (list == null) {
            return;
        }
        for (Object o1 : list) {
            UIWrapper uiw = (UIWrapper) o1;
            if (uiw.getWidgetType() == WidgetTypeEnum.COMBOBOX) {
                if (uiw.getParentPropertyName() != null) {
                    String pn = uiw.getParentPropertyName();
                    String childPropertyName = uiw.getModelBase().getProperty();
                    for (Object o2 : list) {
                        UIWrapper uiwParent = (UIWrapper) o2;
                        if (uiwParent.getWidgetType() == WidgetTypeEnum.COMBOBOX
                                && !uiwParent.getUiName()
                                .equals(uiw.getUiName())) {
                            if (uiwParent.getModelBase().getProperty()
                                    .equals(pn)) {
                                uiwParent
                                        .setChildPropertyName(childPropertyName);
                                uiwParent.setUiwChildCombobox(uiw);
                            }
                        }
                    }
                }
            }
        }
    }

}
