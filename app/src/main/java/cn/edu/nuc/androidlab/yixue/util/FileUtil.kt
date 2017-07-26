package cn.edu.nuc.androidlab.yixue.util

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore

/**
 * Created by MurphySL on 2017/7/26.
 */
class FileUtil{

    companion object {
        @JvmStatic
        fun getFilePahtFromUri(context : Context, uri : Uri) : String?{
            var data : String? = null
            val cusor : Cursor? = context.contentResolver.query(uri, arrayOf(MediaStore.Images.ImageColumns.DATA), null, null, null)
            cusor?.let {
                if(cusor.moveToFirst()){
                    val index = cusor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                    if(index > -1){
                        data = cusor.getString(index)
                    }
                }
                cusor.close()
            }

            return data
        }

        @JvmStatic
        fun getFileName(filePath : String) : String = filePath.substringAfterLast("/")
    }
}