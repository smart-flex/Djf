package ru.smartflex.djf;

public interface IClosingClient {

    /**
     * Method that closes any client resources before UI will be destroyed
     */
    void closeClientResources();

}
