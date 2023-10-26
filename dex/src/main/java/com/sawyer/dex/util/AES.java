package com.sawyer.dex.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class AES {
    public static final String DEFAULT_PWD = "abcdefghijklmnop";
    private static final String algorithmStr = "AES/ECB/PKCS5Padding";
    private static Cipher encryptCipher;
    private static Cipher decryptCipher;

    public static void init(String password) {
        try {
        	//生成一个实现指定转换的Cipher对象
            encryptCipher = Cipher.getInstance(algorithmStr);
            decryptCipher = Cipher.getInstance(algorithmStr);// algorithmStr
            byte[] keyStr = password.getBytes();
            SecretKeySpec key = new SecretKeySpec(keyStr, "AES");
            encryptCipher.init(Cipher.ENCRYPT_MODE, key);
            decryptCipher.init(Cipher.DECRYPT_MODE, key);
        } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException e) {
            e.printStackTrace();
        }
    }

    /**
    * @param srcAPKFile  源apk文件
    * @param dstApkFile  目标apk文件
    * @return            加密后的新dex文件
    */
    public static File encryptAPKFile(File srcAPKFile, File dstApkFile) throws Exception {
        if (srcAPKFile == null) {
        	System.out.println("encryptAPKFile: srcAPKFile is null");
            return null;
        }
        Zip.unZip(srcAPKFile, dstApkFile);
        //获得所有的dex（需要处理分包情况）
        File[] dexFiles = dstApkFile.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
                return s.endsWith(".dex");
            }
        });

        File mainDexFile = null;
        byte[] mainDexData = null;

        for (File dexFile: dexFiles) {
        	//读数据
            byte[] buffer = Utils.getBytes(dexFile);
            //加密
            byte[] encryptBytes = AES.encrypt(buffer);

            if (dexFile.getName().endsWith("classes.dex")) {
                mainDexData = encryptBytes;
                mainDexFile = dexFile;
            }
           //替换原来的数据
            FileOutputStream fos = new FileOutputStream(dexFile);
            fos.write(encryptBytes);
//            fos.flush();
            fos.close();
        }
        return mainDexFile;
    }

    public static byte[] encrypt(byte[] content) {
        try {
            return encryptCipher.doFinal(content);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] decrypt(byte[] content) {
        try {
            return decryptCipher.doFinal(content);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
//        byte[] newfs = Utils.int2Bytes(2312312);
//        byte[] refs = new byte[4];
//        //高位在前，低位在前掉个个
//        for (int i = 0; i < 4; i++) {
//            refs[i] = newfs[newfs.length - 1 - i];
//        }
//        System.out.println(Arrays.toString(newfs));
//        System.out.println(Arrays.toString(refs));
//
//        ByteBuf byteBuf = Unpooled.buffer();
//
//        byteBuf.writeInt(2312312);
//        byte[] a = new byte[4];
//        byteBuf.order(ByteOrder.LITTLE_ENDIAN);
//        byteBuf.readBytes(a);
//        System.out.println(Arrays.toString(a));

//        AES.init(AES.DEFAULT_PWD);
//        String msg = Base64.encode(AES.encrypt(new byte[]{1, 2, 3, 4, 5}));
//        System.out.println(msg);
//        byte[] aes = AES.decrypt(Base64.decode(msg));
//        System.out.println(Arrays.toString(aes));

        File zip = new File("/Users/xiang/develop/source.apk");
        String absolutePath = zip.getAbsolutePath();
        File dir = new File(absolutePath.substring(0, absolutePath.lastIndexOf(".")));
        Zip.unZip(zip,dir);

        File zip2 = new File("/Users/xiang/develop/app-debug2.apk");
        Zip.zip(dir,zip2);

        String[] argv = {
                "jarsigner","-verbose", "-sigalg", "MD5withRSA",
                "-digestalg", "SHA1",
                "-keystore", "/Users/xiang/develop/debug.keystore",
                "-storepass","android",
                "-keypass", "android",
                "-signedjar", "/Users/xiang/develop/app-debug2-sign.apk",
                "/Users/xiang/develop/app-debug2.apk",
                "androiddebugkey"
        };
        Process pro = null;
        try {
            pro = Runtime.getRuntime().exec(argv);
            //destroy the stream
            try {
                pro.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } finally {
            if (pro != null) {
                pro.destroy();
            }
        }
    }

}
