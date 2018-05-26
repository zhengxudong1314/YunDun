package com.dahua.searchandwarn.utils;

import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public final class Base64FileUtils {
    /**
     * 文件转base64字符串
     */
    public static String fileToBase64(String path) {
        //File file = new File(path);

        String base64 = null;
        InputStream in = null;
        try {
            in = new FileInputStream(path);
            byte[] bytes = new byte[in.available()];
            int length = in.read(bytes);

            base64 = Base64.encodeToString(bytes, 0, length, Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return base64;
    }


    /**
     * base64字符串转文件
     *
     * @param base64   base64字符串
     * @param filename 文件名
     * @param fileDir  文件夹路径
     * @return file
     */
    public static File base64ToFile(String base64, String filename, String fileDir) {
        File file = null;
        FileOutputStream out = null;
        try {
            // 解码，然后将字节转换为文件
            file = new File(fileDir, filename);
            if (!file.exists())
                file.createNewFile();
            byte[] bytes = Base64.decode(base64, Base64.DEFAULT);// 将字符串转换为byte数组
            ByteArrayInputStream in = new ByteArrayInputStream(bytes);
            byte[] buffer = new byte[1024];
            out = new FileOutputStream(file);
            int byteSum = 0;
            int byteRead = 0;
            while ((byteRead = in.read(buffer)) != -1) {
                byteSum += byteRead;
                out.write(buffer, 0, byteRead); // 文件写操作
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

}
