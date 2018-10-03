package ru.shtrm.serviceman.util;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
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

import me.leolin.shortcutbadger.ShortcutBadger;
import ru.shtrm.serviceman.R;
import ru.shtrm.serviceman.app.App;
import ru.shtrm.serviceman.data.AuthorizedUser;
import ru.shtrm.serviceman.data.Equipment;
import ru.shtrm.serviceman.data.Flat;
import ru.shtrm.serviceman.data.House;
import ru.shtrm.serviceman.data.Message;
import ru.shtrm.serviceman.data.PhotoEquipment;
import ru.shtrm.serviceman.data.PhotoFlat;
import ru.shtrm.serviceman.data.PhotoHouse;
import ru.shtrm.serviceman.data.PhotoMessage;
import ru.shtrm.serviceman.data.User;
import ru.shtrm.serviceman.data.source.local.GpsTrackLocalDataSource;
import ru.shtrm.serviceman.data.source.local.MeasureLocalDataSource;
import ru.shtrm.serviceman.data.source.local.PhotoEquipmentLocalDataSource;
import ru.shtrm.serviceman.data.source.local.PhotoFlatLocalDataSource;
import ru.shtrm.serviceman.data.source.local.PhotoHouseLocalDataSource;
import ru.shtrm.serviceman.data.source.local.PhotoMessageLocalDataSource;
import ru.shtrm.serviceman.data.source.local.UsersLocalDataSource;

public class MainUtil {
    public static final int ACTIVITY_PHOTO_MESSAGE = 103;
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
        return Environment.getExternalStorageDirectory()
            + "/Android/data/"
            + context.getPackageName()
            + "/Files"
            + File.separator;
    }

    public static Bitmap storeNewImage(Bitmap image, Context context, int width, String image_name) {
        File mediaStorageDir = new File(getPicturesDirectory(context));
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

    private static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public static File createImageFile(String uuid, Context context) throws IOException {
        // Create an image file name
        File mediaStorageDir = new File(getPicturesDirectory(context));
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                if (isExternalStorageWritable()) {
                    Toast.makeText(context, "Нет разрешений на запись данных",
                            Toast.LENGTH_LONG).show();
                    return null;
                }
            }
        }
        return File.createTempFile(
                uuid,  /* prefix */
                ".jpg",         /* suffix */
                mediaStorageDir      /* directory */
        );
    }

    public static void storePhotoEquipment (Equipment equipment, String uuid) {
        PhotoEquipment photoEquipment = new PhotoEquipment();
        PhotoEquipmentLocalDataSource photoEquipmentRepository = PhotoEquipmentLocalDataSource.getInstance();
        GpsTrackLocalDataSource gpsTrackRepository = GpsTrackLocalDataSource.getInstance();
        User user = UsersLocalDataSource.getInstance().getUser(AuthorizedUser.getInstance().getUser().getUuid());
        photoEquipment.set_id(photoEquipmentRepository.getLastId()+1);
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

    public static void storePhotoHouse (House house, String uuid) {
        PhotoHouse photoHouse = new PhotoHouse();
        User user = UsersLocalDataSource.getInstance().getUser(AuthorizedUser.getInstance().getUser().getUuid());
        PhotoHouseLocalDataSource photoHouseRepository = PhotoHouseLocalDataSource.getInstance();
        GpsTrackLocalDataSource gpsTrackRepository = GpsTrackLocalDataSource.getInstance();
        photoHouse.set_id(photoHouseRepository.getLastId()+1);
        photoHouse.setHouse(house);
        photoHouse.setUuid(uuid);
        photoHouse.setCreatedAt(new Date());
        photoHouse.setChangedAt(new Date());
        photoHouse.setUser(user);
        if (gpsTrackRepository.getLastTrack() != null) {
            photoHouse.setLattitude(gpsTrackRepository.getLastTrack().getLatitude());
            photoHouse.setLongitude(gpsTrackRepository.getLastTrack().getLongitude());
        } else {
            photoHouse.setLattitude(App.defaultLatitude);
            photoHouse.setLongitude(App.defaultLongitude);
        }
        photoHouseRepository.savePhotoHouse(photoHouse);
    }

    public static void storePhotoFlat (Flat flat, String uuid) {
        PhotoFlat photoFlat = new PhotoFlat();
        PhotoFlatLocalDataSource photoFlatRepository = PhotoFlatLocalDataSource.getInstance();
        GpsTrackLocalDataSource gpsTrackRepository = GpsTrackLocalDataSource.getInstance();
        User user = UsersLocalDataSource.getInstance().getUser(AuthorizedUser.getInstance().getUser().getUuid());
        photoFlat.set_id(photoFlatRepository.getLastId()+1);
        photoFlat.setFlat(flat);
        photoFlat.setUuid(uuid);
        photoFlat.setCreatedAt(new Date());
        photoFlat.setChangedAt(new Date());
        photoFlat.setUser(user);
        if (gpsTrackRepository.getLastTrack() != null) {
            photoFlat.setLattitude(gpsTrackRepository.getLastTrack().getLatitude());
            photoFlat.setLongitude(gpsTrackRepository.getLastTrack().getLongitude());
        } else {
            photoFlat.setLattitude(App.defaultLatitude);
            photoFlat.setLongitude(App.defaultLongitude);
        }
        photoFlatRepository.savePhotoFlat(photoFlat);
    }

    public static void storePhotoMessage (Message message, String uuid) {
        PhotoMessage photoMessage = new PhotoMessage();
        PhotoMessageLocalDataSource photoMessageRepository = PhotoMessageLocalDataSource.getInstance();
        GpsTrackLocalDataSource gpsTrackRepository = GpsTrackLocalDataSource.getInstance();
        photoMessage.set_id(photoMessageRepository.getLastId()+1);
        photoMessage.setMessage(message);
        photoMessage.setUuid(uuid);
        photoMessage.setCreatedAt(new Date());
        photoMessage.setChangedAt(new Date());
        if (gpsTrackRepository.getLastTrack() != null) {
            photoMessage.setLattitude(gpsTrackRepository.getLastTrack().getLatitude());
            photoMessage.setLongitude(gpsTrackRepository.getLastTrack().getLongitude());
        } else {
            photoMessage.setLattitude(App.defaultLatitude);
            photoMessage.setLongitude(App.defaultLongitude);
        }
        photoMessageRepository.savePhotoMessage(photoMessage);
    }

    public static void setBadges(Context context) {
        long not_sended = MeasureLocalDataSource.getInstance().getUnsentMeasuresCount();
        if (not_sended > 0) {
            ShortcutBadger.applyCount(context, (int)not_sended);
        }
    }

    public static String MD5(String string) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(string.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;
    }
    
    public static String getLastPhotoFilePath(@NonNull Activity activity) {
        String[] projection = new String[]{
                MediaStore.Images.ImageColumns._ID,
                MediaStore.Images.ImageColumns.DATA,
                MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                MediaStore.Images.ImageColumns.DATE_TAKEN,
                MediaStore.Images.ImageColumns.MIME_TYPE
        };
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver resolver = activity.getContentResolver();
        String orderBy = android.provider.MediaStore.Video.Media.DATE_TAKEN + " DESC";
        Cursor cursor = resolver.query(uri, projection, null, null, orderBy);

        // TODO: реализовать удаление записи о фотке котрую мы "забрали"
        //resolver.delete(uri,);
        String result;
        if (cursor != null && cursor.moveToFirst()) {
            int column_index_data = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            result = cursor.getString(column_index_data);
            cursor.close();
        } else {
            result = null;
        }
        return result;
    }
}
