package ru.kostapo.myweather.utils;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

public class HttpClientUtil {

    private static CloseableHttpClient httpClient;

    public static CloseableHttpClient getHttpClient() {
        if(httpClient == null) {
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(3000) // Таймаут установки соединения
                    .setSocketTimeout(3000) // Таймаут ожидания данных
                    .build();
            httpClient = HttpClientBuilder.create()
                    .setDefaultRequestConfig(requestConfig)
                    .build();
        }
        return httpClient;
    }
}
