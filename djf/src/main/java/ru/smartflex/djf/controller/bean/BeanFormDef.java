package ru.smartflex.djf.controller.bean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.smartflex.djf.SmartFlexException;
import ru.smartflex.djf.controller.bean.tree.TreeListUtils;
import ru.smartflex.djf.controller.exception.MissingException;

/**
 * Contains bean definition of form.
 *
 * @author gali.shaimardanov@gmail.com
 */
public class BeanFormDef {

    private String name = null;
    private String clazz = null;
    private String treePath = null;
    private boolean selfCreated = false;
    private String propertyNameSelf = null;
    private String parent = null;
    private String selectablePropertyOfRootModel = null;
    private Map<String, String> selectableMapOfSetModel = new HashMap<String, String>();

    private List<BeanFormDef> linearListOfSetBean = new ArrayList<BeanFormDef>();

    private Map<String, BeanFormDef> modelBean = new HashMap<String, BeanFormDef>();

    private Map<String, BeanFormDefProperty> propMap = new HashMap<String, BeanFormDefProperty>();
    private Map<String, BeanFormDefProperty> propertyEntireModelMap = new HashMap<String, BeanFormDefProperty>();
    private List<String> bindList = new ArrayList<String>();

    public BeanFormDef() {
    }

    private BeanFormDef(String clazz) {
        this.clazz = clazz;
    }

    private BeanFormDef(String name, String clazz, String treePath,
                        String parent) {
        super();
        this.name = name;
        this.clazz = clazz;
        this.treePath = treePath;
        this.parent = parent;
    }

    public void addSelectableProperty(String bindPref, String bind) {
        String idModel = bindPref;
        String[] parts = TreeListUtils.getPartProperties(bindPref);
        if (parts.length > 0) {
            idModel = parts[0];
        }
        BeanFormDef beanOfModel = getModelBean(idModel);
        if (beanOfModel == null) {
            throw new MissingException(
                    "There is no model for such bindPref: \"" + bindPref + "\"");
        }
        if (parts.length == 1) {
            if (beanOfModel.getSelectablePropertyOfRootModel() == null) {
                beanOfModel.selectablePropertyOfRootModel = bind;
            } else {
                throw new SmartFlexException(
                        "There is already filled bind selectable property: \""
                                + beanOfModel.selectablePropertyOfRootModel
                                + "\"");
            }
        } else if (parts.length > 1) {
            if (beanOfModel.selectableMapOfSetModel.get(bindPref) == null) {
                beanOfModel.selectableMapOfSetModel.put(bindPref, bind);
            } else {
                throw new SmartFlexException(
                        "There is already filled bind selectable property: \""
                                + beanOfModel.selectableMapOfSetModel
                                .get(bindPref) + "\"");
            }
        }
    }

    public String getSelectableSetProperty(String treePath) {
        return selectableMapOfSetModel.get(treePath);
    }

    public void addProperty(String treePath, BeanFormDefProperty prop) {
        propMap.put(treePath, prop);
    }

    public void addPropertyEntireModel(String treePath, BeanFormDefProperty prop) {
        propertyEntireModelMap.put(treePath, prop);
    }

    void bindRegisterFromWidget(String bind) {
        if (bind == null) {
            // bind == null is allowed for grid widget
            return;
        }
        bindList.add(bind);
    }

    public boolean isPropertyExistsInBindsDefinition(String clazzName,
                                                     String prop) {
        for (String idModel : modelBean.keySet()) {
            BeanFormDef bDef = modelBean.get(idModel);

            if (bDef.getClazz().equals(clazzName)) {
                for (String bnd : bindList) {
                    if (bnd.startsWith(idModel + ".")
                            && bnd.endsWith("." + prop)) {
                        return true;
                    }
                }
                break;
            } else {
                for (BeanFormDef bf : bDef.getLinearListOfSet()) {
                    if (bf.getClazz().equals(clazzName)) {
                        for (String bnd : bindList) {
                            if (bnd.startsWith(idModel + ".")
                                    && bnd.endsWith("." + prop)) {
                                return true;
                            }
                        }
                        break;
                    }
                }
            }
        }
        return false;
    }

    public Collection<BeanFormDefProperty> getProperties() {
        return propMap.values();
    }

    public BeanFormDef addBean(String idModel, String clazz) {
        BeanFormDef beanOfModel = new BeanFormDef(clazz);
        modelBean.put(idModel, beanOfModel);
        return beanOfModel;
    }

    public BeanFormDef getModelBean(String idModel) {
        return modelBean.get(idModel);
    }

    public BeanFormDef addSet(String name, String clazz, String treePath,
                              String parent) {
        return new BeanFormDef(name, clazz, treePath, parent);
    }

    /**
     * Returns linear list for set definition (non-tree)
     */
    public List<BeanFormDef> getLinearListOfSet() {
        return linearListOfSetBean;
    }

    /**
     * Returns bean property from linear property map (no-tree)
     */
    public BeanFormDefProperty getPropertyFromLinearMap(String bind) {
        BeanFormDefProperty prop = null;
        if (bind != null) {
            String[] parts = TreeListUtils.getPartProperties(bind);
            if (parts.length > 1) {
                BeanFormDef rootModelBean = modelBean.get(parts[0]);
                prop = rootModelBean.propertyEntireModelMap.get(bind);
            }
        }
        return prop;
    }

    public BeanNameProperty[] getNotNulProperties() {
        BeanNameProperty[] arr = null;

        int iNN = 0;
        for (BeanFormDefProperty bfd : getProperties()) {
            if (bfd.getNotNull() != null && bfd.getNotNull()) {
                iNN++;
            }
        }
        if (iNN > 0) {
            arr = new BeanNameProperty[iNN];
            int i = 0;
            for (BeanFormDefProperty bfd : getProperties()) {
                if (bfd.getNotNull() != null && bfd.getNotNull()) {
                    arr[i++] = new BeanNameProperty(bfd.getName(),
                            bfd.getHumanName());
                }
            }
        }

        return arr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String getTreePath() {
        return treePath;
    }

    public boolean isSelfCreated() {
        return selfCreated;
    }

    public void setSelfCreated(boolean newSelf) {
        this.selfCreated = newSelf;
    }

    public String getPropertyNameSelf() {
        return propertyNameSelf;
    }

    public void setPropertyNameSelf(String propertyNameSelf) {
        this.propertyNameSelf = propertyNameSelf;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        return "BeanFormDef [name=" + name + ", clazz=" + clazz + ", treePath="
                + treePath + ", parent=" + parent + "]";
    }

    public String getSelectablePropertyOfRootModel() {
        return selectablePropertyOfRootModel;
    }

}
