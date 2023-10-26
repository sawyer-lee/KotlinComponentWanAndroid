package com.sawyer.dex.util;

import java.io.File;
import java.io.RandomAccessFile;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.Adler32;

public class Utils {

  public static byte[] int2Bytes(int value) {
      byte[] src = new byte[4];
      src[3] = (byte) ((value >> 24) & 0xFF);
      src[2] = (byte) ((value >> 16) & 0xFF);
      src[1] = (byte) ((value >> 8) & 0xFF);
      src[0] = (byte) (value & 0xFF);
      return src;
  }

  public static int bytes2Int(byte[] src) {
      int value;
      value = (int) ((src[0] & 0xFF)
              | ((src[1] & 0xFF)<<8)
              | ((src[2] & 0xFF)<<16)
              | ((src[3] & 0xFF)<<24));
      return value;
  }

  public static void changeSignature(byte[] newDex) throws NoSuchAlgorithmException {
      //更换dex文件签名信息
      MessageDigest md = MessageDigest.getInstance("SHA-1");
      //从第32个字节开始计算sha1值
      md.update(newDex, 32, newDex.length - 32);
      byte[] sha1 = md.digest();
      //从第12位开始拷贝20字节内容
      System.arraycopy(sha1, 0, newDex, 12, 20);
  }

  public static void changeCheckSum(byte[] newDex) {
      Adler32 adler = new Adler32();
      adler.update(newDex, 12, newDex.length - 12);
      int value = (int) adler.getValue();
      byte[] checkSum = Utils.int2Bytes(value);
      System.arraycopy(checkSum, 0, newDex, 8, 4);
  }

  public static byte[] getBytes(File dexFile) throws Exception {
      RandomAccessFile fis = new RandomAccessFile(dexFile, "r");
      byte[] buffer = new byte[(int)fis.length()];
      fis.readFully(buffer);
      fis.close();
      return buffer;
  }

  public static void changeFileSize(byte[] mainDexData, byte[] newDex, byte[] aarData ) {
      byte[] bytes = Utils.int2Bytes(mainDexData.length);
      //拷贝原来的dex到新的dex

      //更换dex文件头长度信息
      byte[] file_size = Utils.int2Bytes(newDex.length);
      System.arraycopy(file_size, 0, newDex, 32, 4);
  }

}
