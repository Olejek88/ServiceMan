package ru.shtrm.serviceman.rfid;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Handler;
import android.preference.PreferenceScreen;

import java.util.ArrayList;
import java.util.List;

import ru.shtrm.serviceman.rfid.driver.RfidDriverC5;
import ru.shtrm.serviceman.rfid.driver.RfidDriverNfc;
import ru.shtrm.serviceman.rfid.driver.RfidDriverNull;
import ru.shtrm.serviceman.rfid.driver.RfidDriverP6300;
import ru.shtrm.serviceman.rfid.driver.RfidDriverQRcode;

/**
 * @author Dmitriy Logachev
 * 
 */
public abstract class RfidDriverBase implements IRfidDriver {

	public final static int MEMORY_BANK_RESERVED = 0;
	public final static int MEMORY_BANK_EPC = 1;
	public final static int MEMORY_BANK_TID = 2;
	public final static int MEMORY_BANK_USER = 3;
	public static final int RESULT_RFID_SUCCESS = 0;
	public static final int RESULT_RFID_READ_ERROR = 1;
	public static final int RESULT_RFID_INIT_ERROR = 2;
	public static final int RESULT_RFID_CLASS_NOT_FOUND = 3;
	public static final int RESULT_RFID_WRITE_ERROR = 4;
	public static final int RESULT_RFID_CANCEL = 5;
	public static final int RESULT_RFID_TIMEOUT = 6;
	public static final int RESULT_RFID_DISCONNECT = 7;
	public static final String RESULT_RFID_TAG_ID = "tagId";
    protected static final String TAG = "RfidDriverBase";
    // вместо ручного разбора dex файла создаём статический массив с именами классов драйверов
	private static final String[] uhfDriversClass;
	private static final String[] rfidDriversClass;
	// Handler который будет обрабатывать сообщение от драйвера
	protected static Handler sHandler;

    static {
        List<String> tmpList = new ArrayList<>();
		tmpList.add(RfidDriverNull.class.getCanonicalName());
		tmpList.add(RfidDriverP6300.class.getCanonicalName());
		tmpList.add(RfidDriverC5.class.getCanonicalName());
		uhfDriversClass = tmpList.toArray(new String[0]);

		tmpList.clear();
		tmpList.add(RfidDriverQRcode.class.getCanonicalName());
		tmpList.add(RfidDriverNfc.class.getCanonicalName());
		tmpList.add(RfidDriverP6300.class.getCanonicalName());
		tmpList.add(RfidDriverC5.class.getCanonicalName());
		rfidDriversClass = tmpList.toArray(new String[0]);

        tmpList = null;
    }

	protected Context mContext;
	protected Fragment mFragment;
	protected Activity mActivity;

    public static String getDriverName(String classPath) {
        String driverName;

        if (classPath == null || classPath.equals("")) {
            return null;
        }

        try {
            Class<?> driverClass;
            driverClass = Class.forName(classPath);
            driverName = (String) (driverClass.getDeclaredField("DRIVER_NAME").get(""));
        } catch (Exception e) {
            e.printStackTrace();
            driverName = null;
        }

        return driverName;
    }

	public static String[] getUhfDriversClass() {
		return uhfDriversClass;
	}

	public static String[] getRfidDriversClass() {
		return rfidDriversClass;
	}

	public void setHandler(Handler handler) {
		sHandler = handler;
	}

	public void setContext(Context context) {
		mContext = context;
	}

	@Override
	public void setIntegration(Activity activity) {
		mActivity = activity;
	}

	@Override
	public void setIntegration(Fragment fragment) {
		mFragment = fragment;
	}

	@Override
	public PreferenceScreen getSettingsScreen(PreferenceScreen screen) {
		return null;
	}
}
