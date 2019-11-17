package ru.smartflex.djf.controller.helper;

import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import ru.smartflex.djf.SFConstants;
import ru.smartflex.djf.SFLogger;
import ru.smartflex.djf.SmartFlexException;
import ru.smartflex.djf.controller.exception.FormParseXMLException;
import ru.smartflex.djf.model.gen.BeanType;
import ru.smartflex.djf.model.gen.FormType;
import ru.smartflex.djf.model.gen.PanelType;

public class UnMarshalHelper {

    private static Unmarshaller unmarshallerPanel = null;
    private static FormValidationEventHandler panelEventHandler = new FormValidationEventHandler();

    private static Unmarshaller unmarshallerFrame = null;
    private static FormValidationEventHandler frameEventHandler = new FormValidationEventHandler();

    private static Unmarshaller unmarshallerBean = null;
    private static FormValidationEventHandler beanEventHandler = new FormValidationEventHandler();

    private UnMarshalHelper() {
        super();
    }

    public static FormType unmarshalForm(InputStream is)
            throws SmartFlexException {
        FormType form = null;
        if (!frameEventHandler.isUnmarshalReady()) {
            try {
                unmarshallerFrame = prepareUnmarshaling(
                        frameEventHandler);
            } catch (JAXBException e) {
                SFLogger.error("Unmarshall frame preparing error", e);
                throw new FormParseXMLException(
                        "Unmarshall frame preparing error", e);
            }
        }
        if (frameEventHandler.isUnmarshalReady()) {

            JAXBElement<FormType> root;
            try {
                root = unmarshallerFrame.unmarshal(new StreamSource(is),
                        FormType.class);
            } catch (JAXBException e) {
                SFLogger.error("Unmarshall frame error", e);
                throw new FormParseXMLException("Unmarshall frame error", e);
            } finally {
                //noinspection CatchMayIgnoreException
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (Exception e) {
                }
            }

            form = root.getValue();
        }

        return form;
    }

    public static PanelType unmarshalPanel(InputStream is)
            throws SmartFlexException {
        PanelType panel = null;
        if (!panelEventHandler.isUnmarshalReady()) {
            try {
                unmarshallerPanel = prepareUnmarshaling(
                        panelEventHandler);
            } catch (JAXBException e) {
                SFLogger.error("Unmarshall panel preparing error", e);
                throw new FormParseXMLException(
                        "Unmarshall panel preparing error", e);
            }
        }
        if (panelEventHandler.isUnmarshalReady()) {

            JAXBElement<PanelType> root;
            try {
                root = unmarshallerPanel.unmarshal(new StreamSource(is),
                        PanelType.class);
            } catch (JAXBException e) {
                SFLogger.error("Unmarshall panel error", e);
                throw new FormParseXMLException("Unmarshall panel error", e);
            } finally {
                //noinspection CatchMayIgnoreException
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (Exception e) {
                }
            }

            panel = root.getValue();
        }

        return panel;
    }

    public static BeanType unmarshalBean(InputStream is)
            throws SmartFlexException {
        BeanType bean = null;
        if (!beanEventHandler.isUnmarshalReady()) {
            try {
                unmarshallerBean = prepareUnmarshaling(
                        beanEventHandler);
            } catch (JAXBException e) {
                SFLogger.error("Unmarshall bean preparing error", e);
                throw new FormParseXMLException(
                        "Unmarshall bean preparing error", e);
            }
        }
        if (beanEventHandler.isUnmarshalReady()) {

            JAXBElement<BeanType> root;
            try {
                root = unmarshallerBean.unmarshal(new StreamSource(is),
                        BeanType.class);
            } catch (JAXBException e) {
                SFLogger.error("Unmarshall bean error", e);
                throw new FormParseXMLException("Unmarshall bean error", e);
            } finally {
                //noinspection CatchMayIgnoreException
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (Exception e) {
                }
            }

            bean = root.getValue();
        }

        return bean;
    }

    private static Unmarshaller prepareUnmarshaling(
            FormValidationEventHandler formValidationEventHandler)
            throws JAXBException {
        Unmarshaller unmarshaller;

        JAXBContext jc = JAXBContext.newInstance(SFConstants.MODEL_PACKAGE);

        unmarshaller = jc.createUnmarshaller();

        unmarshaller.setEventHandler(formValidationEventHandler);

        formValidationEventHandler.setUnmarshalReady();

        return unmarshaller;
    }

}
