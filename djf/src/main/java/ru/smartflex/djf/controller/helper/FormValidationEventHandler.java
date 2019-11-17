package ru.smartflex.djf.controller.helper;

import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.ValidationEventLocator;

import ru.smartflex.djf.SFLogger;

public class FormValidationEventHandler implements ValidationEventHandler {

    private boolean unmarshalReady = false;

    FormValidationEventHandler() {
        super();
    }

    void setUnmarshalReady() {
        unmarshalReady = true;
    }

    boolean isUnmarshalReady() {
        return unmarshalReady;
    }

    public boolean handleEvent(ValidationEvent ve) {

        if (ve.getSeverity() != ValidationEvent.WARNING) {
            ValidationEventLocator vel = ve.getLocator();
            SFLogger.error("Line:Col[", String.valueOf(vel.getLineNumber()),
                    ":", String.valueOf(vel.getColumnNumber()), "]:",
                    ve.getMessage());
            return false;
        }
        return true;
    }

}
