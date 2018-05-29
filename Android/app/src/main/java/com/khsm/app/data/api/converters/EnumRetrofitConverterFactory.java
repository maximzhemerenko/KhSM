package com.khsm.app.data.api.converters;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import retrofit2.Converter;
import retrofit2.Retrofit;

public class EnumRetrofitConverterFactory extends Converter.Factory {
    @Override
    public Converter<?, String> stringConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        Converter<?, String> converter = null;
        if (type instanceof Class && ((Class<?>)type).isEnum()) {
            converter = value -> EnumUtils.GetSerializedNameValue((Enum) value);
        }
        return converter;
    }

    public static class EnumUtils {
        @Nullable
        static <E extends Enum<E>> String GetSerializedNameValue(E e) {
            String value = null;
            try {
                value = e.getClass().getField(e.name()).getAnnotation(SerializedName.class).value();
            } catch (NoSuchFieldException exception) {
                exception.printStackTrace();
            }
            return value;
        }
    }
}