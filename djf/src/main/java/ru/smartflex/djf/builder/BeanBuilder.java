package ru.smartflex.djf.builder;

import java.io.InputStream;
import java.util.List;

import ru.smartflex.djf.Djf;
import ru.smartflex.djf.controller.bean.BeanFormDef;
import ru.smartflex.djf.controller.bean.BeanFormDefProperty;
import ru.smartflex.djf.controller.bean.tree.ITreeConstants;
import ru.smartflex.djf.controller.exception.FormParseXMLException;
import ru.smartflex.djf.controller.exception.MissingException;
import ru.smartflex.djf.controller.helper.UnMarshalHelper;
import ru.smartflex.djf.model.gen.BeanPropertyType;
import ru.smartflex.djf.model.gen.BeanType;
import ru.smartflex.djf.model.gen.FormType;
import ru.smartflex.djf.model.gen.ModelType;
import ru.smartflex.djf.tool.OtherUtil;

public class BeanBuilder {

    private BeanBuilder() {
    }

    public static BeanFormDef build(FormType form) {
        BeanFormDef root = new BeanFormDef();

        if (form.getModels() != null) {
            List<ModelType> modelList = form.getModels().getModel();
            for (ModelType mt : modelList) {

                BeanType bt = mt.getBean();
                Boolean self = bt.isSelfCreated();
                if (bt.getInclude() != null) {
                    InputStream is = OtherUtil.getBodyAsStream(
                            Djf.getConfigurator().getPathToBean(), bt.getInclude());
                    bt = UnMarshalHelper.unmarshalBean(is);
                    // use selfCreated attribute from parent definition bean
                    bt.setSelfCreated(self);
                }

                validateBeanTypeRoot(bt);

                BeanFormDef beanDef = root.addBean(mt.getId(), bt.getClazz());
                if (bt.isSelfCreated() != null) {
                    beanDef.setSelfCreated(bt.isSelfCreated());
                }
                if (bt.getSelfSet() != null) {
                    beanDef.setPropertyNameSelf(bt.getSelfSet());
                }
                fillPropertyDefinition(mt.getId(), bt, beanDef, beanDef);

                fillSetDefinition(bt, mt.getId(), beanDef, beanDef);
            }
        }

        return root;
    }

    private static void fillPropertyDefinition(String parentPath, BeanType bt,
                                               BeanFormDef beanDef, BeanFormDef beanRootModelDef) {
        List<BeanPropertyType> list = bt.getProp();
        for (BeanPropertyType bpt : list) {
            if (bpt.getName() == null) {
                throw new MissingException("Bean property must have a name");
            }

            BeanFormDefProperty prop = new BeanFormDefProperty();

            prop.setName(bpt.getName());

            boolean hasAttribute = false;
            if (bpt.getLength() != null) {
                hasAttribute = true;
                prop.setLength(bpt.getLength());
            }
            if (bpt.isNotNull() != null) {
                hasAttribute = true;
                prop.setNotNull(bpt.isNotNull());
            }

            if (bpt.getFill() != null) {
                hasAttribute = true;
                prop.setFillExpression(bpt.getFill());
            }
            prop.setHumanName(bpt.getHumanName());

            hasAttribute = parseNumeric(bpt.getPreScale(), prop, hasAttribute);

            if (!hasAttribute) {
                throw new MissingException("Bean property: \"" + bpt.getName()
                        + "\" must have some filled attribute");
            }

            String treePath = parentPath
                    + ITreeConstants.PARENT_CHILD_DELIMITER + bpt.getName();

            beanDef.addProperty(treePath, prop);

            // just to avoid hierarchical search
            beanRootModelDef.addPropertyEntireModel(treePath, prop);
        }

    }

    private static boolean parseNumeric(String preScale,
                                        BeanFormDefProperty prop, boolean hasAttribute) {
        if (preScale == null) {
            return hasAttribute;
        }
        StringBuilder prec = new StringBuilder();
        StringBuilder scale = new StringBuilder();
        int pos = -1;
        char[] na = preScale.toCharArray();
        for (int i = 0; i < na.length; i++) {
            if (na[i] == '.') {
                if (pos != -1) {
                    throw new FormParseXMLException(
                            "For attribute \"pre-scale\" is not possible to use point more then one: "
                                    + preScale);
                }
                pos = i;
                continue;
            }
            if (!Character.isDigit(na[i])) {
                throw new FormParseXMLException(
                        "For attribute \"pre-scale\" is not possible to use value: "
                                + preScale);
            }
            if (pos == -1) {
                prec.append(na[i]);
            }
            if (pos != -1) {
                scale.append(na[i]);
            }
        }
        if (prec.length() == 0) {
            throw new FormParseXMLException(
                    "The attribute \"pre-scale\" has to be not empty");
        }
        int iPrec = Integer.parseInt(prec.toString());
        int iScale = 0;
        if (scale.length() > 0) {
            iScale = Integer.parseInt(scale.toString());
        }
        if ((iPrec - iScale) < 0) {
            throw new FormParseXMLException(
                    "The scale can not be more than precision for attribute \"pre-scale\": "
                            + preScale);
        }

        prop.setPrecision(iPrec);
        prop.setScale(iScale);

        return true;
    }

    private static void fillSetDefinition(BeanType bt, String path,
                                          BeanFormDef beanDef, BeanFormDef beanRootModelDef) {
        List<BeanType> listBt = bt.getSet();
        for (BeanType btset : listBt) {
            validateBeanTypeSet(btset);
            String nextPath = path + ITreeConstants.PARENT_CHILD_DELIMITER
                    + btset.getName();
            BeanFormDef beanDefSet = beanDef.addSet(btset.getName(),
                    btset.getClazz(), nextPath, btset.getParent());

            beanRootModelDef.getLinearListOfSet().add(beanDefSet);

            fillPropertyDefinition(nextPath, btset, beanDefSet,
                    beanRootModelDef);

            fillSetDefinition(btset, nextPath, beanDefSet, beanRootModelDef);
        }
    }

    private static void validateBeanTypeSet(BeanType bt) {
        if (bt.getName() == null) {
            throw new MissingException("For set tag must be defined a name");
        }
        if (bt.getClazz() == null) {
            throw new MissingException(
                    "For set tag must be defined a class name. See attribute \"clazz\"");
        }
    }

    private static void validateBeanTypeRoot(BeanType bt) {
        if (bt.getClazz() == null) {
            throw new MissingException(
                    "Model must has class name for bean definition");
        }
    }

}
