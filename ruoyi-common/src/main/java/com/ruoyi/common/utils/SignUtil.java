package com.ruoyi.common.utils;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.*;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 签名工具
 */
public class SignUtil {

    public String genSign(Map<String, Object> data, String priKey) throws NoSuchAlgorithmException, InvalidKeySpecException, SignatureException, InvalidKeyException {
        Map<String, String> stringMap = toStringMap(new GsonBuilder().create().toJson(data));
        return this.sign(this.genSignString(stringMap), priKey);
    }

    public String genSignString(Map<String, String> params) {
        if (null == params || params.size() == 0) {
            return "";
        }

        List<String> sortedKeys = new ArrayList<>(params.keySet());
        sortedKeys.remove("sign");
        sortedKeys.sort(null);
        String sb = sortedKeys.stream().map(s -> s + "=" + params.get(s)).collect(Collectors.joining("&"));

        System.out.println("genSignString = " + sb);

        return sb;
    }

    public String sign(String str, String priKey) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] decodedKey = Base64.getDecoder().decode(priKey.getBytes(StandardCharsets.UTF_8));
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
        PrivateKey pk = keyFactory.generatePrivate(keySpec);

        Signature signature = Signature.getInstance("MD5withRSA");
        signature.initSign(pk);
        signature.update(str.getBytes(StandardCharsets.UTF_8));
        return new String(Base64.getEncoder().encode(signature.sign()), StandardCharsets.UTF_8);
    }

    public boolean verifySign(JsonElement data, String sign, String pubKey) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException {
        Map<String, String> resMap = toStringMap(data);
        String signString = this.genSignString(resMap);

        byte[] keyBytes = Base64.getDecoder().decode(pubKey.getBytes(StandardCharsets.UTF_8));
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey key = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance("MD5withRSA");
        signature.initVerify(key);
        signature.update(signString.getBytes(StandardCharsets.UTF_8));
        return signature.verify(Base64.getDecoder().decode(sign.getBytes(StandardCharsets.UTF_8)));
    }

    public static Map<String, String> toStringMap(String str) {
        JsonParser jp = new JsonParser();
        JsonElement jEle = jp.parse(str);
        return toStringMap(jEle);
    }

    public static Map<String, String> toStringMap(JsonElement jEle) {
        Map<String, String> resMap = new HashMap<>();
        if (null != jEle && jEle.isJsonObject()) {
            for (Map.Entry<String, JsonElement> entry : jEle.getAsJsonObject().entrySet()) {
                resMap.put(entry.getKey(), valueAsString(entry.getValue()));
            }
        }
        return resMap;
    }

    public static String valueAsString(JsonElement element) {
        if (element.isJsonNull()) {
            return "";
        } else if (element.isJsonPrimitive()) {
            return element.getAsJsonPrimitive().getAsString();
        } else if (element.isJsonObject()) {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, JsonElement> entry : element.getAsJsonObject().entrySet()) {
                sb.append(valueAsString(entry.getValue()));
            }
            return sb.toString();
        } else if (element.isJsonArray()) {
            StringBuilder sb = new StringBuilder();
            for (JsonElement vv : element.getAsJsonArray()) {
                sb.append(valueAsString(vv));
            }
            return sb.toString();
        }
        return "";
    }

    public static String decrypt(String encryptedData, String privateKeyStr) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(privateKeyStr);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(spec);

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));

        return new String(decryptedBytes);
    }
}
