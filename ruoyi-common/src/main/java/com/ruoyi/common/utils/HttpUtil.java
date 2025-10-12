package com.ruoyi.common.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import okhttp3.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
public class HttpUtil {
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private volatile static okhttp3.OkHttpClient client;

    private static final int MAX_IDLE_CONNECTION = 50;

    private static final long KEEP_ALIVE_DURATION = 50;

    private static final long CONNECT_TIMEOUT = 2000;

    private static final long READ_TIMEOUT = 2000;

    /**
     * 单例模式(双重检查模式)
     */
    private static OkHttpClient getInstance() {
        if (client == null) {
            synchronized (okhttp3.OkHttpClient.class) {
                if (client == null) {
                    client = new okhttp3.OkHttpClient.Builder()
                            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                            .connectionPool(new ConnectionPool(MAX_IDLE_CONNECTION, KEEP_ALIVE_DURATION, TimeUnit.MINUTES))
                            .sslSocketFactory(getSSLSocketFactory(),  getX509TrustManager())
                            .build();
                }
            }
        }
        return client;
    }

    public static String doPost(String url, String json) {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try {
            Response response = getInstance().newCall(request).execute();
            if (response.isSuccessful()) {
                String result = response.body().string();
                log.info("syncPost response = {}, responseBody= {}", response, result);
                return result;
            }
            String result = response.body().string();
            log.info("syncPost response = {}, responseBody= {}", response, result);
            throw new IOException("三方接口返回http状态码为" + response.code());
        } catch (Exception e) {
            log.error("syncPost() url:{} have a ecxeption {}", url, e);
            throw new RuntimeException("syncPost() have a ecxeption {}" + e.getMessage());
        }
    }


    public static String doGet(String url){
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        try {
            Response response = getInstance().newCall(request).execute();
            if (response.isSuccessful()) {
                String result = response.body().string();
                log.info("syncPost response = {}, responseBody= {}", response, result);
                return result;
            }
            String result = response.body().string();
            log.info("syncPost response = {}, responseBody= {}", response, result);
            throw new IOException("三方接口返回http状态码为" + response.code());
        } catch (Exception e) {
            log.error("syncPost() url:{} have a ecxeption {}", url, e);
            throw new RuntimeException("syncPost() have a ecxeption {}" + e.getMessage());
        }
    }




    /**
     * description 忽略https证书验证
     */
    private static HostnameVerifier getHostnameVerifier() {
        HostnameVerifier hostnameVerifier = new HostnameVerifier() {
            @Override
            public boolean verify(String s, SSLSession sslSession) {
                return true;
            }
        };
        return hostnameVerifier;
    }
    /**
     * description 忽略https证书验证
     */
    private static SSLSocketFactory getSSLSocketFactory() {
        try {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, getTrustManager(), new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static X509TrustManager getX509TrustManager() {
        X509TrustManager trustManager = null;
        try {
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init((KeyStore) null);
            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                throw new IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers));
            }
            trustManager = (X509TrustManager) trustManagers[0];
        } catch (Exception e) {
            e.printStackTrace();
        }

        return trustManager;
    }

    private static TrustManager[] getTrustManager() {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[]{};
                    }
                }
        };
        return trustAllCerts;
    }


    /**
     * 获取0点aimm价格 https://web3.okx.com/zh-hans/build/docs/waas/walletapi-api-get-historical-price
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    public static String getAimmPrice() {
        String domain = "https://www.okx.com";
        // 今天0点
        LocalDateTime localDateTime = LocalDate.now().atStartOfDay();
        long timestamp = localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

        // 当前时间
        long time = new Date().getTime();
        String path = "/api/v5/wallet/token/historical-price";
        // tokenAddress 地址 chainIndex sol
        String param = "?tokenAddress=FbUbGr5N2f6Z7a4SVPYfNmEERBy5xXaT5vt7LBPCQMVQ&chainIndex=501&begin="+timestamp;
        String secret = "D296C4695B3B34EA4E58504ADFE188E6";
        String sign = hmacSHA256(time + "GET" + path + param, secret);
        if (Objects.isNull(sign)) {
            return null;
        }
        Request request = new Request.Builder()
                .url(domain+path+param)
                .addHeader("OK-ACCESS-KEY","0fc2fcd4-3c36-4f68-9a2a-4adf39d6b6fe")
                .addHeader("OK-ACCESS-PASSPHRASE","Aiu@admin123")
                .addHeader("OK-ACCESS-PROJECT","2ab4badd15260e26b3487623d58bbc93")
                .addHeader("OK-ACCESS-TIMESTAMP", String.valueOf(time))
                .addHeader("OK-ACCESS-SIGN",sign)
                .get()
                .build();

        try (Response response = getInstance().newCall(request).execute()){
            if (response.isSuccessful()) {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode root = objectMapper.readTree(response.body().string());
                return root.path("data").get(0)
                        .path("prices").get(0)
                        .path("price").asText();
            }
            log.error("doGetAddHeader,response{}",response.body().string());
            return null;
        } catch (Exception e) {
            log.error("doGetAddHeader", e);
            return null;
        }
    }

    public static String hmacSHA256(String data, String secret) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(secretKeySpec);
            byte[] hmacSha256Bytes = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hmacSha256Bytes);
        } catch (Exception e) {
            log.error("hmacSHA256 error",e);
            return null;
        }
    }
}
