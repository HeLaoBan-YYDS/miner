package com.ruoyi.common.utils;

import java.util.Random;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

public class InvitationCodeGenerator {
    private static final String CHARACTERS = "0123456789";

    private static final String CHARACTERSInvice = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    private static final int LENGTH = 6;

    public static String generateInvitationCode() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(index);
            sb.append(randomChar);
        }
//        return "1234";
        return sb.toString();
    }


    public static String generateInvitationCodeSup() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < LENGTH; i++) {
            int index = random.nextInt(CHARACTERSInvice.length());
            char randomChar = CHARACTERSInvice.charAt(index);
            sb.append(randomChar);
        }
        return sb.toString();
    }


    public static Long generateUserCode(Long userId) {
        // 将用户ID转换为字符串
        String userIdStr = String.valueOf(userId);

        // 使用CRC32作为可逆哈希函数
        Checksum crc32 = new CRC32();
        crc32.update(userIdStr.getBytes(), 0, userIdStr.length());
        long checksum = crc32.getValue();

        // 转换为7位数，这里简单起见直接取模
        // 注意：这可能不是完全可逆的，取决于哈希函数和取模基数
        long code = checksum % 10000000;

        // 如果需要确保是7位数，可以补零
        return Long.valueOf(String.format("%07d", code));
    }

    public static String generateInvitationCode(String username) {
        return username;
    }
}
