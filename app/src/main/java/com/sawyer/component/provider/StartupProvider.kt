package com.sawyer.component.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import com.sawyer.component.manage.StartupManager

/**
 * 利用provider的执行在Application之前，先进行三方SDK的初始化
 */
class StartupProvider : ContentProvider() {
    override fun onCreate(): Boolean {
        val startupList = StartupInitializer.discoverAndInitialize(context!!,javaClass.name)
        StartupManager.Builder()
            .addAllStartup(startupList)
            .build(context!!)
            .start()
            .await()
        return true
    }

    override fun query(
        uri: Uri, projection: Array<out String>?,
        selection: String?, selectionArgs: Array<out String>?,
        sortOrder: String?
    ) = null

    override fun getType(uri: Uri) = null

    override fun insert(uri: Uri, values: ContentValues?) = null

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?) = 0

    override fun update(
        uri: Uri, values: ContentValues?,
        selection: String?, selectionArgs: Array<out String>?
    ) = 0
}