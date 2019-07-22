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
import ru.shtrm.serviceman.data.Alarm;
import ru.shtrm.serviceman.data.AuthorizedUser;
import ru.shtrm.serviceman.data.City;
import ru.shtrm.serviceman.data.Defect;
import ru.shtrm.serviceman.data.Documentation;
import ru.shtrm.serviceman.data.Equipment;
import ru.shtrm.serviceman.data.EquipmentType;
import ru.shtrm.serviceman.data.House;
import ru.shtrm.serviceman.data.HouseType;
import ru.shtrm.serviceman.data.Measure;
import ru.shtrm.serviceman.data.Message;
import ru.shtrm.serviceman.data.Operation;
import ru.shtrm.serviceman.data.OperationTemplate;
import ru.shtrm.serviceman.data.Street;
import ru.shtrm.serviceman.data.Task;
import ru.shtrm.serviceman.data.TaskTemplate;
import ru.shtrm.serviceman.data.ZhObject;
import ru.shtrm.serviceman.retrofit.deserial.CityDeserializer;
import ru.shtrm.serviceman.retrofit.deserial.DateTypeDeserializer;
import ru.shtrm.serviceman.retrofit.deserial.DefectDeserializer;
import ru.shtrm.serviceman.retrofit.deserial.DocumentationDeserializer;
import ru.shtrm.serviceman.retrofit.deserial.EquipmentDeserializer;
import ru.shtrm.serviceman.retrofit.deserial.EquipmentTypeDeserializer;
import ru.shtrm.serviceman.retrofit.deserial.HouseDeserializer;
import ru.shtrm.serviceman.retrofit.deserial.HouseTypeDeserializer;
import ru.shtrm.serviceman.retrofit.deserial.MessageDeserializer;
import ru.shtrm.serviceman.retrofit.deserial.ObjectDeserializer;
import ru.shtrm.serviceman.retrofit.deserial.OperationDeserializer;
import ru.shtrm.serviceman.retrofit.deserial.OperationTemplateDeserializer;
import ru.shtrm.serviceman.retrofit.deserial.StreetDeserializer;
import ru.shtrm.serviceman.retrofit.deserial.TaskDeserializer;
import ru.shtrm.serviceman.retrofit.deserial.TaskTemplateDeserializer;
import ru.shtrm.serviceman.retrofit.iface.IAlarmService;
import ru.shtrm.serviceman.retrofit.iface.IAlarmStatusService;
import ru.shtrm.serviceman.retrofit.iface.IAlarmTypeService;
import ru.shtrm.serviceman.retrofit.iface.ICityService;
import ru.shtrm.serviceman.retrofit.iface.IDefectService;
import ru.shtrm.serviceman.retrofit.iface.IDefectTypeService;
import ru.shtrm.serviceman.retrofit.iface.IDocumentationService;
import ru.shtrm.serviceman.retrofit.iface.IDocumentationTypeService;
import ru.shtrm.serviceman.retrofit.iface.IEquipmentService;
import ru.shtrm.serviceman.retrofit.iface.IEquipmentStatusService;
import ru.shtrm.serviceman.retrofit.iface.IEquipmentSystemService;
import ru.shtrm.serviceman.retrofit.iface.IEquipmentTypeService;
import ru.shtrm.serviceman.retrofit.iface.IGpsTrackService;
import ru.shtrm.serviceman.retrofit.iface.IHouseService;
import ru.shtrm.serviceman.retrofit.iface.IHouseStatusService;
import ru.shtrm.serviceman.retrofit.iface.IHouseTypeService;
import ru.shtrm.serviceman.retrofit.iface.IJournalService;
import ru.shtrm.serviceman.retrofit.iface.IMeasureService;
import ru.shtrm.serviceman.retrofit.iface.IMeasureTypeService;
import ru.shtrm.serviceman.retrofit.iface.IMessageService;
import ru.shtrm.serviceman.retrofit.iface.IOperationTemplateService;
import ru.shtrm.serviceman.retrofit.iface.IPingService;
import ru.shtrm.serviceman.retrofit.iface.IStreetService;
import ru.shtrm.serviceman.retrofit.iface.ITaskService;
import ru.shtrm.serviceman.retrofit.iface.ITaskTemplateService;
import ru.shtrm.serviceman.retrofit.iface.ITaskTypeService;
import ru.shtrm.serviceman.retrofit.iface.ITaskVerdictService;
import ru.shtrm.serviceman.retrofit.iface.ITokenService;
import ru.shtrm.serviceman.retrofit.iface.IUsersService;
import ru.shtrm.serviceman.retrofit.iface.IWorkStatusService;
import ru.shtrm.serviceman.retrofit.iface.IZhObjectService;
import ru.shtrm.serviceman.retrofit.iface.IZhObjectStatusService;
import ru.shtrm.serviceman.retrofit.iface.IZhObjectTypeService;
import ru.shtrm.serviceman.retrofit.serial.AlarmSerializer;
import ru.shtrm.serviceman.retrofit.serial.EquipmentSerializer;
import ru.shtrm.serviceman.retrofit.serial.MeasureSerializer;
import ru.shtrm.serviceman.retrofit.serial.MessageSerializer;

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
                                .addQueryParameter("XDEBUG_SESSION_START", "xdebug")
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
    public static IStreetService getStreetService() {
        return getRetrofit().create(IStreetService.class);
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
    public static IHouseTypeService getHouseTypeService() {
        return getRetrofit().create(IHouseTypeService.class);
    }

    @NonNull
    public static IPingService getPingService() {
        return getRetrofit().create(IPingService.class);
    }

    @NonNull
    public static IMessageService getMessageService() {
        return getRetrofit().create(IMessageService.class);
    }

    @NonNull
    public static IDefectTypeService getDefectTypeService() {
        return getRetrofit().create(IDefectTypeService.class);
    }

    @NonNull
    public static IDocumentationTypeService getDocumentationTypeService() {
        return getRetrofit().create(IDocumentationTypeService.class);
    }

    @NonNull
    public static IMeasureTypeService getMeasureTypeService() {
        return getRetrofit().create(IMeasureTypeService.class);
    }

    @NonNull
    public static IZhObjectStatusService getZhObjectStatusService() {
        return getRetrofit().create(IZhObjectStatusService.class);
    }

    @NonNull
    public static IZhObjectTypeService getZhObjectTypeService() {
        return getRetrofit().create(IZhObjectTypeService.class);
    }

    @NonNull
    public static ITaskTypeService getTaskTypeService() {
        return getRetrofit().create(ITaskTypeService.class);
    }

    @NonNull
    public static ITaskVerdictService getTaskVerdictService() {
        return getRetrofit().create(ITaskVerdictService.class);
    }

    @NonNull
    public static ITaskService getTaskService() {
        return getRetrofit().create(ITaskService.class);
    }

    @NonNull
    public static IWorkStatusService getWorkStatusService() {
        return getRetrofit().create(IWorkStatusService.class);
    }

    @NonNull
    public static IEquipmentSystemService getEquipmentSystemService() {
        return getRetrofit().create(IEquipmentSystemService.class);
    }

    @NonNull
    public static IDefectService getDefectService() {
        return getRetrofit().create(IDefectService.class);
    }

    @NonNull
    public static IDocumentationService getDocumentationService() {
        return getRetrofit().create(IDocumentationService.class);
    }

    @NonNull
    public static IOperationTemplateService getOperationTemplateService() {
        return getRetrofit().create(IOperationTemplateService.class);
    }

    @NonNull
    public static ITaskTemplateService getTaskTemplateService() {
        return getRetrofit().create(ITaskTemplateService.class);
    }

    @NonNull
    public static IZhObjectService getObjectService() {
        return getRetrofit().create(IZhObjectService.class);
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

        // Deserializers
        builder.registerTypeAdapter(City.class, new CityDeserializer());
        builder.registerTypeAdapter(Date.class, new DateTypeDeserializer());
        builder.registerTypeAdapter(Defect.class, new DefectDeserializer());
        builder.registerTypeAdapter(Documentation.class, new DocumentationDeserializer());
        builder.registerTypeAdapter(Equipment.class, new EquipmentDeserializer());
        builder.registerTypeAdapter(EquipmentType.class, new EquipmentTypeDeserializer());
        builder.registerTypeAdapter(House.class, new HouseDeserializer());
        builder.registerTypeAdapter(HouseType.class, new HouseTypeDeserializer());
        builder.registerTypeAdapter(Message.class, new MessageDeserializer());
        builder.registerTypeAdapter(ZhObject.class, new ObjectDeserializer());
        builder.registerTypeAdapter(Operation.class, new OperationDeserializer());
        builder.registerTypeAdapter(OperationTemplate.class, new OperationTemplateDeserializer());
        builder.registerTypeAdapter(Street.class, new StreetDeserializer());
        builder.registerTypeAdapter(Task.class, new TaskDeserializer());
        builder.registerTypeAdapter(TaskTemplate.class, new TaskTemplateDeserializer());
//        builder.registerTypeAdapter(UserHouse.class, new UserHouseDeserializer());

        // Serializers
        builder.registerTypeAdapter(Equipment.class, new EquipmentSerializer());
        builder.registerTypeAdapter(Measure.class, new MeasureSerializer());
        builder.registerTypeAdapter(Alarm.class, new AlarmSerializer());
        builder.registerTypeAdapter(Message.class, new MessageSerializer());

        builder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Gson gson = builder.create();
        return new Retrofit.Builder()
                .baseUrl(Api.API_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(CLIENT)
                .build();
    }

    public static boolean pingService() {
        Call<Void> call = getPingService().ping();
        try {
            return call.execute().isSuccessful();
        } catch (Exception e) {
            return false;
        }
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
}
