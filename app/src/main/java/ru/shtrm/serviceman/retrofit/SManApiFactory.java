package ru.shtrm.serviceman.retrofit;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.shtrm.serviceman.BuildConfig;
import ru.shtrm.serviceman.data.AuthorizedUser;
import ru.shtrm.serviceman.data.Equipment;
import ru.shtrm.serviceman.data.Measure;
import ru.shtrm.serviceman.retrofit.iface.IAlarmService;
import ru.shtrm.serviceman.retrofit.iface.IAlarmStatusService;
import ru.shtrm.serviceman.retrofit.iface.IAlarmTypeService;
import ru.shtrm.serviceman.retrofit.iface.ICityService;
import ru.shtrm.serviceman.retrofit.iface.IEquipmentService;
import ru.shtrm.serviceman.retrofit.iface.IEquipmentStatusService;
import ru.shtrm.serviceman.retrofit.iface.IEquipmentTypeService;
import ru.shtrm.serviceman.retrofit.iface.IFlatService;
import ru.shtrm.serviceman.retrofit.iface.IFlatStatusService;
import ru.shtrm.serviceman.retrofit.iface.IFlatTypeService;
import ru.shtrm.serviceman.retrofit.iface.IGpsTrackService;
import ru.shtrm.serviceman.retrofit.iface.IHouseService;
import ru.shtrm.serviceman.retrofit.iface.IHouseStatusService;
import ru.shtrm.serviceman.retrofit.iface.IHouseTypeService;
import ru.shtrm.serviceman.retrofit.iface.IJournalService;
import ru.shtrm.serviceman.retrofit.iface.IMeasureService;
import ru.shtrm.serviceman.retrofit.iface.IPhotoAlarmService;
import ru.shtrm.serviceman.retrofit.iface.IPhotoEquipmentService;
import ru.shtrm.serviceman.retrofit.iface.IPhotoFlatService;
import ru.shtrm.serviceman.retrofit.iface.IPhotoHouseService;
import ru.shtrm.serviceman.retrofit.iface.IPing;
import ru.shtrm.serviceman.retrofit.iface.IResidentService;
import ru.shtrm.serviceman.retrofit.iface.IStreetService;
import ru.shtrm.serviceman.retrofit.iface.ISubjectService;
import ru.shtrm.serviceman.retrofit.iface.ITokenService;
import ru.shtrm.serviceman.retrofit.iface.IUsersService;

public class SManApiFactory {
    private static final int CONNECT_TIMEOUT = 15;
    private static final int WRITE_TIMEOUT = 60;
    private static final int TIMEOUT = 60;

    private static final OkHttpClient CLIENT = new OkHttpClient()
            .newBuilder()
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request origRequest = chain.request();
                    Headers origHeaders = origRequest.headers();
                    AuthorizedUser user = AuthorizedUser.getInstance();
                    Headers newHeaders = origHeaders.newBuilder()
                            .add("Authorization", user.getBearer()).build();

                    Request.Builder requestBuilder = origRequest.newBuilder().headers(newHeaders);
                    Request newRequest = requestBuilder.build();
                    return chain.proceed(newRequest);
                }
            })
            .addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    if (BuildConfig.DEBUG) {
                        Request request = chain.request();
                        HttpUrl url = request.url()
                                .newBuilder()
                                .addQueryParameter("XDEBUG_SESSION_START", "PHPSTORM1")
                                .build();
                        Request.Builder requestBuilder = request.newBuilder().url(url);
                        Request newRequest = requestBuilder.build();
                        return chain.proceed(newRequest);
                    } else {
                        return chain.proceed(chain.request());
                    }
                }
            })
            .build();

    @NonNull
    public static IAlarmService getAlarmService() {
        return getRetrofit().create(IAlarmService.class);
    }

    @NonNull
    public static IAlarmStatusService getAlarmStatusService() {
        return getRetrofit().create(IAlarmStatusService.class);
    }

    @NonNull
    public static IAlarmTypeService getAlarmTypeService() {
        return getRetrofit().create(IAlarmTypeService.class);
    }

    @NonNull
    public static ICityService getCityService() {
        return getRetrofit().create(ICityService.class);
    }

    @NonNull
    public static IEquipmentService getEquipmentService() {
        return getRetrofit().create(IEquipmentService.class);
    }

    @NonNull
    public static IEquipmentStatusService getEquipmentStatusService() {
        return getRetrofit().create(IEquipmentStatusService.class);
    }

    @NonNull
    public static IEquipmentTypeService getEquipmentTypeService() {
        return getRetrofit().create(IEquipmentTypeService.class);
    }

    @NonNull
    public static IFlatService getFlatService() {
        return getRetrofit().create(IFlatService.class);
    }

    @NonNull
    public static IFlatStatusService getFlatStatusService() {
        return getRetrofit().create(IFlatStatusService.class);
    }

    @NonNull
    public static IGpsTrackService getGpsTrackService() {
        return getRetrofit().create(IGpsTrackService.class);
    }

    @NonNull
    public static IHouseService getHouseService() {
        return getRetrofit().create(IHouseService.class);
    }

    @NonNull
    public static IHouseStatusService getHouseStatusService() {
        return getRetrofit().create(IHouseStatusService.class);
    }

    @NonNull
    public static IJournalService getJournalService() {
        return getRetrofit().create(IJournalService.class);
    }

    @NonNull
    public static IMeasureService getMeasureService() {
        return getRetrofit().create(IMeasureService.class);
    }

    @NonNull
    public static IPhotoAlarmService getPhotoAlarmService() {
        return getRetrofit().create(IPhotoAlarmService.class);
    }

    @NonNull
    public static IPhotoEquipmentService getPhotoEquipmentService() {
        return getRetrofit().create(IPhotoEquipmentService.class);
    }

    @NonNull
    public static IPhotoFlatService getPhotoFlatService() {
        return getRetrofit().create(IPhotoFlatService.class);
    }

    @NonNull
    public static IPhotoHouseService getPhotoHouseService() {
        return getRetrofit().create(IPhotoHouseService.class);
    }

    @NonNull
    public static IResidentService getResidentService() {
        return getRetrofit().create(IResidentService.class);
    }

    @NonNull
    public static IStreetService getStreetService() {
        return getRetrofit().create(IStreetService.class);
    }

    @NonNull
    public static ISubjectService getSubjectService() {
        return getRetrofit().create(ISubjectService.class);
    }

    @NonNull
    public static ITokenService getTokenService() {
        return getRetrofit().create(ITokenService.class);
    }

    @NonNull
    public static IUsersService getUsersService() {
        return getRetrofit().create(IUsersService.class);
    }

    @NonNull
    public static IFlatTypeService getFlatTypeService() {
        return getRetrofit().create(IFlatTypeService.class);
    }

    @NonNull
    public static IHouseTypeService getHouseTypeService() {
        return getRetrofit().create(IHouseTypeService.class);
    }

    @NonNull
    public static IPing getPingService() {
        return getRetrofit().create(IPing.class);
    }

    @NonNull
    public static Retrofit getPrivateRetrofit() {
        return getRetrofit();
    }
    /**
     * Метод без указания специфической обработки элементов json.
     *
     * @return Retrofit
     */
    @NonNull
    private static Retrofit getRetrofit() {
        return getRetrofit(null);
    }

    /**
     * Метод для задания специфической обработки элементов json.
     *
     * @param list Список типов и десереализаторов.
     * @return Retrofit
     */
    @NonNull
    private static Retrofit getRetrofit(List<TypeAdapterParam> list) {
        GsonBuilder builder = new GsonBuilder();

        if (list != null) {
            for (TypeAdapterParam param : list) {
                builder.registerTypeAdapter(param.getTypeClass(), param.getDeserializer());
            }
        }

        builder.registerTypeAdapter(Date.class, new DateTypeDeserializer());
        builder.registerTypeAdapter(Equipment.class, new EquipmentSerializer());
        builder.registerTypeAdapter(Measure.class, new MeasureSerializer());
        builder.registerTypeAdapter(Measure.class, new AlarmSerializer());
        builder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Gson gson = builder.create();
        return new Retrofit.Builder()
                .baseUrl(Api.API_BASE)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(CLIENT)
                .build();
    }

    /**
     * Класс для хранения типа и десереализатора к этому типу.
     */
    private static class TypeAdapterParam {
        Class<?> typeClass;
        JsonDeserializer<?> deserializer;

        TypeAdapterParam(Class<?> c, JsonDeserializer<?> d) {
            typeClass = c;
            deserializer = d;
        }

        public Class<?> getTypeClass() {
            return typeClass;
        }

        public void setTypeClass(Class<?> typeClass) {
            this.typeClass = typeClass;
        }

        public JsonDeserializer<?> getDeserializer() {
            return deserializer;
        }

        public void setDeserializer(JsonDeserializer<?> deserializer) {
            this.deserializer = deserializer;
        }
    }

    public static boolean pingService() {
        Call<Void> call = getPingService().ping();
        try {
            return call.execute().isSuccessful();
        } catch (Exception e) {
            return false;
        }
    }
}
