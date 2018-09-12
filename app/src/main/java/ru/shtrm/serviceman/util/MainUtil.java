package ru.shtrm.serviceman.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ru.shtrm.serviceman.R;
import ru.shtrm.serviceman.app.App;
import ru.shtrm.serviceman.data.AuthorizedUser;
import ru.shtrm.serviceman.data.Equipment;
import ru.shtrm.serviceman.data.PhotoEquipment;
import ru.shtrm.serviceman.data.User;
import ru.shtrm.serviceman.data.source.local.GpsTrackLocalDataSource;
import ru.shtrm.serviceman.data.source.local.PhotoEquipmentLocalDataSource;
import ru.shtrm.serviceman.data.source.local.UsersLocalDataSource;

public class MainUtil {

    public static void setToolBarColor(Context context, Activity myActivityReference) {
        if (PreferenceManager.getDefaultSharedPreferences(context).
                getBoolean("navigation_bar_tint", true)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                myActivityReference.getWindow().setNavigationBarColor(
                        ContextCompat.getColor(context, R.color.colorPrimaryDark));
            }
        }
    }

    public static Bitmap getBitmapByPath(String path, String filename) {
        File imageFull = new File(path + filename);
        Bitmap bmp = BitmapFactory.decodeFile(imageFull.getAbsolutePath());
        if (bmp != null) {
            return bmp;
        } else return null;
    }

    public static String getPicturesDirectory(Context context) {
    String path = Environment.getExternalStorageDirectory()
            + "/Android/data/"
            + context.getPackageName()
            + "/Files"
            + File.separator;
        return path;
    }

    public static Bitmap storeNewImage(Bitmap image, Context context, int width, String image_name) {
        final String imageName = image_name;
        File mediaStorageDir = new File(getPicturesDirectory(context));

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                if (isExternalStorageWritable()) {
                    Toast.makeText(context, "Нет разрешений на запись данных",
                            Toast.LENGTH_LONG).show();
                    return null;
                }
            }
        }
        if (image_name==null || image_name.equals("")) {
            String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm", Locale.US).format(new Date());
            image_name = "file_" + timeStamp + ".jpg";
        }
        // Create a media file name
        File pictureFile;
        pictureFile = new File(mediaStorageDir.getPath() + File.separator + image_name);

        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            if (image != null) {
                int height = (int) (width * (float) image.getHeight() / (float) image.getWidth());
                if (height > 0) {
                    Bitmap myBitmap = Bitmap.createScaledBitmap(image, width, height, false);
                    myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.flush();
                    fos.close();

                    // TODO store to realm
                    return myBitmap;
                }
            }
        } catch (FileNotFoundException e) {
            Log.d("Util", "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d("Util", "Error accessing file: " + e.getMessage());
        }
        return null;
    }

    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public File createImageFile(String type, Context context) throws IOException {
        // Create an image file name
        String mCurrentPhotoPath;
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",Locale.US).format(new Date());
        String imageFileName = type + "_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public static void storePhotoEquipment (Equipment equipment) {
        PhotoEquipment photoEquipment = new PhotoEquipment();
        String uuid = java.util.UUID.randomUUID().toString();
        PhotoEquipmentLocalDataSource photoEquipmentRepository = PhotoEquipmentLocalDataSource.getInstance();
        GpsTrackLocalDataSource gpsTrackRepository = GpsTrackLocalDataSource.getInstance();
        User user = UsersLocalDataSource.getInstance().getUser(AuthorizedUser.getInstance().getId());
        photoEquipment.set_id(photoEquipmentRepository.getLastId());
        photoEquipment.setEquipment(equipment);
        photoEquipment.setUuid(uuid);
        photoEquipment.setCreatedAt(new Date());
        photoEquipment.setChangedAt(new Date());
        photoEquipment.setUser(user);
        if (gpsTrackRepository.getLastTrack() != null) {
            photoEquipment.setLattitude(gpsTrackRepository.getLastTrack().getLatitude());
            photoEquipment.setLongitude(gpsTrackRepository.getLastTrack().getLongitude());
        } else {
            photoEquipment.setLattitude(App.defaultLatitude);
            photoEquipment.setLongitude(App.defaultLongitude);
        }
        photoEquipmentRepository.savePhotoEquipment(photoEquipment);
    }
}
