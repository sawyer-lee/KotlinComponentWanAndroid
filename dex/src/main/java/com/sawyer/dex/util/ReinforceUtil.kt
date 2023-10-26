package com.sawyer.dex.util

import java.io.File
import java.io.FileOutputStream

/**
 * 加固apk工具类，步骤：
 *  1.解压apk，提取源dex文件，对其进行加密
 *  2.创建壳dex文件，作用：程序运行时，利用此壳对加密过的源dex文件解密，然后才能运行
 *  3.将壳dex和源dex合并，并进行新的签名，生成新的apk
 */
object ReinforceUtil {

    //源Dex文件
    private lateinit var mainDexData: ByteArray
    //壳Dex文件
    private lateinit var aarDexData: ByteArray
    //合并的Dex文件
    private lateinit var mergeDex: ByteArray

    fun makeNewApk(){
        val tempFileApk = File("source/apk/temp")
        if(tempFileApk.exists()){
            val files = tempFileApk.listFiles()
            files?.forEach { file ->
                if (file.isFile) file.delete()
            }
        }

        val tempFileAar = File("source/aar/temp")
        if(tempFileAar.exists()){
            val files = tempFileAar.listFiles()
            files?.forEach { file ->
                if (file.isFile) file.delete()
            }
        }

        //Step 1: 处理原始apk，加密源dex
        AES.init(AES.DEFAULT_PWD)
        val apkFile = File("source/apk/app-debug.apk")
        val newApkFile = File(apkFile.parent + File.separator + "temp")
        if (!newApkFile.exists()) newApkFile.mkdirs()
        //解压apk存储在temp目录下
        AES.encryptAPKFile(apkFile, newApkFile)
        //对源dex文件进行重命名，以便区分壳dex和源dex
        if (newApkFile.isDirectory){
            val listFiles = newApkFile.listFiles()
            listFiles?.forEach { file ->
                if (file.isFile){
                    if (file.name.endsWith(".dex")){
                        val name = file.name
                        val cursor = name.indexOf(".dex")
                        val newName = file?.parent+File.separator+name.substring(0,cursor)+"-"+".dex"
                        file.renameTo(File(newName))
                    }
                }
            }
        }

        //Step 2: 处理aar,获取壳dex
        val aarFile = File("source/aar/library-debug.aar")
        val aarDex = Dx.jar2Dex(aarFile)
        val tempMainDex = File(newApkFile.path + File.separator + "classes.dex")
        if (!tempMainDex.exists()) tempMainDex.createNewFile()
        val fos = FileOutputStream(tempMainDex)
        val bytes = Utils.getBytes(aarDex)
        fos.write(bytes)
        //只有BufferedOutputStream这种带有byte[]的流需要flush，因为flush的作用为强制进行IO进行写操作
        //fos.flush()
        fos.close()

        //Step 3: 合并，并打包签名
        val unsignedApk = File("result/apk-unsigned.apk")
        unsignedApk.parentFile?.mkdirs()
        Zip.zip(newApkFile, unsignedApk)
        //不用插件就不能自动使用原apk的签名
        val signedApk = File("result/apk-signed.apk")
        Signature.signature(unsignedApk, signedApk)
    }

}