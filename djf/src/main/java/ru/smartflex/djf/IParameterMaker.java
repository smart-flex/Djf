package ru.smartflex.djf;

import java.util.Map;

/**
 * Describes method that makes map of parameters for passing to the next form. Assistant class may implements that interfaces.
 * This method is invoked in special run button listener.
 */
public interface IParameterMaker {

    Map<String, Object> makeParametersMap();
}
