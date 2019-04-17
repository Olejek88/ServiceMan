package ru.shtrm.serviceman.rfid.driver;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.shtrm.serviceman.rfid.RfidDriverBase;

/**
 * @author Dmitriy Logachev
 *         <p>
 *         Драйвер считывателя RFID который ни чего не делает.
 *         </p>
 */
public class RfidDriverDummy extends RfidDriverBase {
    public static final String DRIVER_NAME;

    static {
        DRIVER_NAME = "Dummy";
    }

    @Override
    public boolean init() {
        return true;
    }

    @Override
    public void readTagId() {
        // всегда "считываем" ид метки
        sHandler.obtainMessage(RESULT_RFID_SUCCESS, "").sendToTarget();

    }

    @Override
    public void readMultiplyTagId(final String[] tagIds) {
        // всегда "считываем" ид меток
        sHandler.obtainMessage(RESULT_RFID_SUCCESS, new String[]{""}).sendToTarget();
    }

    @Override
    public void close() {
    }

    @Override
    public View getView(LayoutInflater inflater, ViewGroup viewGroup) {
        return null;
    }

    @Override
    public void readTagData(String password, int memoryBank, int address,
                            int count) {
        sHandler.obtainMessage(RESULT_RFID_READ_ERROR).sendToTarget();
    }

    @Override
    public void readTagData(String password, String tagId, int memoryBank,
                            int address, int count) {
        sHandler.obtainMessage(RESULT_RFID_READ_ERROR).sendToTarget();
    }

    @Override
    public void writeTagData(String password, int memoryBank, int address, String data) {
        sHandler.obtainMessage(RESULT_RFID_WRITE_ERROR).sendToTarget();
    }

    @Override
    public void writeTagData(String password, String tagId, int memoryBank,
                             int address, String data) {
        sHandler.obtainMessage(RESULT_RFID_WRITE_ERROR).sendToTarget();
    }
}
