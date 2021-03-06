package ru.shtrm.serviceman.rfid.driver;

import ru.shtrm.serviceman.rfid.RfidDriverBase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author Dmitriy Logachev
 *         <p>
 *         Драйвер считывателя RFID который ни чего не делает.
 *         </p>
 */
public class RfidDriverNull extends RfidDriverBase {
    public static final String DRIVER_NAME;

    static {
        DRIVER_NAME = "Не установлен";
    }

	@Override
	public boolean init() {
		return true;
	}

	@Override
	public void readTagId() {
		sHandler.obtainMessage(RESULT_RFID_READ_ERROR).sendToTarget();
	}

	@Override
    public void readMultiplyTagId(final String[] tagIds) {
        sHandler.obtainMessage(RESULT_RFID_READ_ERROR).sendToTarget();
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
	public void writeTagData(String password, int memoryBank, int address,
			String data) {
		sHandler.obtainMessage(RESULT_RFID_WRITE_ERROR).sendToTarget();
	}

	@Override
	public void writeTagData(String password, String tagId, int memoryBank,
			int address, String data) {
		sHandler.obtainMessage(RESULT_RFID_WRITE_ERROR).sendToTarget();
	}
}
