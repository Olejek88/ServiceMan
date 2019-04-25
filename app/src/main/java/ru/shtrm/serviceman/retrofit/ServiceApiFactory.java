package ru.shtrm.serviceman.retrofit;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.shtrm.serviceman.BuildConfig;
import ru.shtrm.serviceman.retrofit.deserial.DateTypeDeserializer;
import ru.shtrm.serviceman.retrofit.iface.IPingService;
import ru.shtrm.serviceman.retrofit.iface.IUsersService;

public class ServiceApiFactory {
    private static final int CONNECT_TIMEOUT = 15;
    private static final int WRITE_TIMEOUT = 60;
    private static final int TIMEOUT = 60;
    private static String token;
    private static String oid;
    private static String secret;

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

                    String message = new Date().toString();
                    String toHash = message + ":" + oid + ":" + secret;
                    String hash;
                    try {
                        MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
                        digest.update(toHash.getBytes());
                        byte messageDigest[] = digest.digest();

                        // Create Hex String
                        StringBuilder hexString = new StringBuilder();
                        for (byte b : messageDigest) {
                            String h = Integer.toHexString(0xFF & b);
                            if (h.length() < 2) {
                                h = "0" + h;
                            }

                            hexString.append(h);
                        }

                        hash = hexString.toString();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                        hash = "";
                    }

                    Headers newHeaders = origHeaders.newBuilder()
                            .add("Authorization", "Bearer " + token)
                            .add("X-Data", message)
                            .add("X-Hash", hash)
                            .build();

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
    public static IUsersService getUsersService() {
        return getRetrofit().create(IUsersService.class);
    }

    @NonNull
    public static IPingService getPingService() {
        return getRetrofit().create(IPingService.class);
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
        builder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Gson gson = builder.create();
        return new Retrofit.Builder()
                .baseUrl(Api.API_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(CLIENT)
                .build();
    }

    public static void setToken(String t) {
        token = t;
    }

    public static void setOid(String o) {
        oid = o;
    }

    public static void setSecret(String s) {
        secret = s;
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
