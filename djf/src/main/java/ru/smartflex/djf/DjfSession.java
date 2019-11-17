package ru.smartflex.djf;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class DjfSession {

    private Map<String, Object> appBeans = new HashMap<String, Object>();
    private ReentrantLock lock = new ReentrantLock();

    public void addBean(String id, Object bean) {
        lock.lock();
        try {
            appBeans.put(id, bean);
        } finally {
            lock.unlock();
        }
    }

    @SuppressWarnings("unused")
    public void removeBean(String id) {
        lock.lock();
        try {
            appBeans.remove(id);
        } finally {
            lock.unlock();
        }
    }

    public Object getBean(String id) {
        lock.lock();
        try {
            return appBeans.get(id);
        } finally {
            lock.unlock();
        }
    }

}
