package ru.smartflex.djf.demo.java;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

class FactoryHelper {

    private FactoryHelper() {
        super();
    }

    @SuppressWarnings({"rawtypes", "CatchMayIgnoreException", "WeakerAccess"})
    public static List restoreFromStore(byte[] listStore) {
        List list = null;
        ByteArrayInputStream bis = new ByteArrayInputStream(listStore);
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(bis);
            list = (List) in.readObject();
        } catch (Exception e) {
        } finally {
            try {
                bis.close();
            } catch (IOException e) {
            }
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
            }
        }
        return list;
    }

    @SuppressWarnings({"CatchMayIgnoreException", "WeakerAccess"})
    public static byte[] saveToStore(@SuppressWarnings("rawtypes") List list) {
        byte[] listStore = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(list);
            listStore = bos.toByteArray();
        } catch (Exception e) {
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
            }
            try {
                bos.close();
            } catch (IOException e) {
            }
        }
        return listStore;
    }

}
