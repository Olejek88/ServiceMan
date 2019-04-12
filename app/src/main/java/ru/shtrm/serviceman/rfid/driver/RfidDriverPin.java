package ru.shtrm.serviceman.rfid.driver;

import android.support.design.widget.TextInputEditText;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ru.shtrm.serviceman.R;
import ru.shtrm.serviceman.rfid.IRfidDriver;
import ru.shtrm.serviceman.rfid.RfidDialog;
import ru.shtrm.serviceman.rfid.RfidDriverBase;


/**
 * @author Dmitriy Logachev
 *         <p>
 *         Драйвер считывателя RFID который "считывает" pin код в качестве tagId
 *         </p>
 */
@SuppressWarnings("unused")
public class RfidDriverPin extends RfidDriverBase implements IRfidDriver {
    public static final String DRIVER_NAME;

    static {
        DRIVER_NAME = "Ввод пин-кода";
    }

    private String TAG = "RfidDriverPin";
    private int command;

    @Override
    public boolean init() {
        return true;
    }

    @Override
    public void readTagId() {
        // В данном драйвере реального считывания не происходит.
        command = RfidDialog.READER_COMMAND_READ_ID;
    }

    @Override
    public void readMultiplyTagId(final String[] tagIds) {
        // В данном драйвере реального считывания не происходит.
        command = RfidDialog.READER_COMMAND_READ_MULTI_ID;
    }

    @Override
    public void readTagData(String password, int memoryBank, int address, int count) {
        // В данном режиме реального считывания не происходит.
        sHandler.obtainMessage(RESULT_RFID_READ_ERROR).sendToTarget();
    }

    @Override
    public void readTagData(String password, String tagId, int memoryBank, int address, int count) {
        // В данном режиме реального считывания не происходит.
        sHandler.obtainMessage(RESULT_RFID_READ_ERROR).sendToTarget();
    }

    @Override
    public void writeTagData(String password, int memoryBank, int address, String data) {
        // В данном режиме реальной записи не происходит.
        sHandler.obtainMessage(RESULT_RFID_WRITE_ERROR).sendToTarget();
    }

    @Override
    public void writeTagData(String password, String tagId, int memoryBank, int address, String data) {
        // В данном режиме реальной записи не происходит.
        sHandler.obtainMessage(RESULT_RFID_WRITE_ERROR).sendToTarget();
    }

    @Override
    public void close() {
    }

    @Override
    public View getView(LayoutInflater inflater, ViewGroup viewGroup) {

        View view = inflater.inflate(R.layout.rfid_dialog_pin, viewGroup);

        Button ok = view.findViewById(R.id.rfid_dialog_text_button_OK);
        ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d(TAG, "pressed OK");
                TextInputEditText pinText = v.getRootView().findViewById(R.id.rfid_dialog_input_pin);
                String tagId = "0000" + pinText.getText().toString();
                switch (command) {
                    case RfidDialog.READER_COMMAND_READ_ID:
                        sHandler.obtainMessage(RESULT_RFID_SUCCESS, tagId).sendToTarget();
                        break;
                    case RfidDialog.READER_COMMAND_READ_MULTI_ID:
                        sHandler.obtainMessage(RESULT_RFID_SUCCESS, new String[]{tagId}).sendToTarget();
                        break;
                    default:
                        sHandler.obtainMessage(RESULT_RFID_CANCEL).sendToTarget();
                }
            }
        });

        Button cancel = view.findViewById(R.id.rfid_dialog_text_button_CANCEL);
        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d(TAG, "pressed CANCEL");
                sHandler.obtainMessage(RESULT_RFID_CANCEL).sendToTarget();
            }
        });

        return view;
    }
}
