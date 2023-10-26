package com.sawyer.dex.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;

public class Dx {

    public static File jar2Dex(File aarFile) throws IOException, InterruptedException {
        File fakeDex = new File(aarFile.getParent() + File.separator + "temp");
        System.out.println("jar2Dex: aarFile.getParent(): " + aarFile.getParent());
        //解压aar到fakeDex目录下
        Zip.unZip(aarFile, fakeDex);
        //查找fakeDex下的classes.jar
        File[] files = fakeDex.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
                return s.equals("classes.jar");
            }
        });
        if (files == null || files.length <= 0) {
            throw new RuntimeException("the aar is invalidate");
        }
        File classes_jar = files[0];
        //将classes.jar转换为classes.dex
        File aarDex = new File(classes_jar.getParentFile(), "classes.dex");
        //使用Android tools下的工具dx.bat结合java调用windows的cmd命令进行转换
        Dx.dxCommand(aarDex, classes_jar);
        return aarDex;
    }

    public static void dxCommand(File aarDex, File classes_jar) throws IOException, InterruptedException {
        Runtime runtime = Runtime.getRuntime();
        Process process = runtime.exec("cmd.exe /C dx --dex --output=" + aarDex.getAbsolutePath() + " " +
                classes_jar.getAbsolutePath());

        try {
            process.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw e;
        }
        if (process.exitValue() != 0) {
        	InputStream inputStream = process.getErrorStream();
        	int len;
        	byte[] buffer = new byte[2048];
        	ByteArrayOutputStream bos = new ByteArrayOutputStream();
        	while((len=inputStream.read(buffer)) != -1){
        		bos.write(buffer,0,len);
        	}
        	System.out.println(bos.toString("GBK"));
            throw new RuntimeException("dx run failed");
        }
        process.destroy();
    }
}
