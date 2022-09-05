package com.jucodes.jopen

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.REQUEST_INSTALL_PACKAGES
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.PluginRegistry
import java.io.File
import java.io.IOException


private const val STRING_APK = "application/vnd.android.package-archive"
private const val REQ = 2022
private const val VIEW_CODE = 2021
private const val PERMISSION_CODE = 2020

class JFileNew (private val activity: Activity) : PluginRegistry.ActivityResultListener, PluginRegistry.RequestPermissionsResultListener  {
    private var result: MethodChannel.Result? = null
    private var fileTypeJ: String? = null
    private lateinit var filePathJ: String

    private val tagT = "JOpenActivity"



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        try {
            if (requestCode == PERMISSION_CODE) {
                if (canInstallApp()) {
                    startActivity()
                } else {
                    resultMessage("Permission denied: REQUEST_INSTALL_PACKAGES")
                    return false
                }
                return  true
            }else if (requestCode == VIEW_CODE &&  resultCode == Activity.RESULT_OK && data?.data != null){
                val zed = data.data
                resultMessage(zed.toString())
                return  true
            }else{
                return false
            }
        }catch (e:IllegalArgumentException){
            return  false
        } catch (e:SecurityException){
            return  false
        }catch (e:Exception){
            return  false
        }catch (e:UnsupportedOperationException){
           return false
        }

    }



fun openFile(
    filePathJJ: String,
    fileTypeJJ: String,
    result: MethodChannel.Result,
){
    this.result = result
    this.filePathJ = filePathJJ
    this.fileTypeJ = if(fileTypeJJ.isNotEmpty()){
        getFileType(fileTypeJJ)
    }else{
        getFileType(filePathJJ)
    }
    try{
        if(isFileExist()){
            if(hasAccessPermission(filePathJJ)){
                if (hasPermissionB(READ_EXTERNAL_STORAGE)){
                    if (STRING_APK == this.fileTypeJ){
                        openApkFile()
                    }else{
                        startActivity()
                    }
                }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        activity.requestPermissions(arrayOf(READ_EXTERNAL_STORAGE), REQ)
                    }else{
                        startActivity()
                    }

            }else{
                startActivity()
            }
        }else{
            resultMessage("File does not exists")
        }
    }catch (e:Exception){
        resultMessage(e.message.toString())
    } catch (e:UnsupportedOperationException){
        resultMessage(e.message.toString())
    }
}

    private fun getFileType(filePath: String): String {
        return when (filePath.substring(filePath.lastIndexOf(".") +1)) {
            "3gp" -> "video/3gpp"
            "torrent" -> "application/x-bittorrent"
            "kml" -> "application/vnd.google-earth.kml+xml"
            "gpx" -> "application/gpx+xml"
            "apk" -> "application/vnd.android.package-archive"
            "asf" -> "video/x-ms-asf"
            "avi" -> "video/x-msvideo"
            "bin", "class", "exe" -> "application/octet-stream"
            "bmp" -> "image/bmp"
            "c" -> "text/plain"
            "conf" -> "text/plain"
            "cpp" -> "text/plain"
            "doc" -> "application/msword"
            "docx" -> "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
            "xls", "csv" -> "application/vnd.ms-excel"
            "xlsx" -> "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
            "gif" -> "image/gif"
            "gtar" -> "application/x-gtar"
            "gz" -> "application/x-gzip"
            "h" -> "text/plain"
            "htm" -> "text/html"
            "html" -> "text/html"
            "jar" -> "application/java-archive"
            "java" -> "text/plain"
            "jpeg" -> "image/jpeg"
            "jpg" -> "image/jpeg"
            "js" -> "application/x-javascript"
            "log" -> "text/plain"
            "m3u" -> "audio/x-mpegurl"
            "m4a" -> "audio/mp4a-latm"
            "m4b" -> "audio/mp4a-latm"
            "m4p" -> "audio/mp4a-latm"
            "m4u" -> "video/vnd.mpegurl"
            "m4v" -> "video/x-m4v"
            "mov" -> "video/quicktime"
            "mp2" -> "audio/x-mpeg"
            "mp3" -> "audio/x-mpeg"
            "mp4" -> "video/mp4"
            "mpc" -> "application/vnd.mpohun.certificate"
            "mpe" -> "video/mpeg"
            "mpeg" -> "video/mpeg"
            "mpg" -> "video/mpeg"
            "mpg4" -> "video/mp4"
            "mpga" -> "audio/mpeg"
            "msg" -> "application/vnd.ms-outlook"
            "ogg" -> "audio/ogg"
            "pdf" -> "application/pdf"
            "png" -> "image/png"
            "pps" -> "application/vnd.ms-powerpoint"
            "ppt" -> "application/vnd.ms-powerpoint"
            "pptx" -> "application/vnd.openxmlformats-officedocument.presentationml.presentation"
            "prop" -> "text/plain"
            "rc" -> "text/plain"
            "rmvb" -> "audio/x-pn-realaudio"
            "rtf" -> "application/rtf"
            "sh" -> "text/plain"
            "tar" -> "application/x-tar"
            "tgz" -> "application/x-compressed"
            "txt" -> "text/plain"
            "wav" -> "audio/x-wav"
            "wma" -> "audio/x-ms-wma"
            "wmv" -> "audio/x-ms-wmv"
            "wps" -> "application/vnd.ms-works"
            "xml" -> "text/plain"
            "z" -> "application/x-compress"
            "zip" -> "application/x-zip-compressed"
            else -> "*/*"
        }
    }
    private fun hasAccessPermission(filePath: String): Boolean {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            false
        } else try {
            val appDirFilePath: String = activity.getExternalFilesDir(null)!!.canonicalPath
            val appDirCachePath: String = activity.externalCacheDir!!.canonicalPath
            val fileCanonicalPath: String = File(filePath).canonicalPath
            if (fileCanonicalPath.startsWith(appDirFilePath)) {
                false
            } else !fileCanonicalPath.startsWith(appDirCachePath)
        } catch (e: IOException) {
            e.printStackTrace()
            true
        }
    }
    private fun isFileExist(): Boolean {
        return if(filePathJ.isNotEmpty()){
            val file = File(filePathJ)
            file.exists()
        }else{
            false
        }
    }
    private fun startActivity() {
        try {
            Log.d(tagT, "Starting $filePathJ")
            if (isFileExist()) {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                 intent.action = Intent.ACTION_VIEW
                if (STRING_APK == fileTypeJ){
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }else{
                    intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                }

                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                intent.addCategory(Intent.CATEGORY_DEFAULT)
                val packageName: String = activity.packageName
                intent.putExtra(Intent.EXTRA_RETURN_RESULT, true)
                val myFile = File(filePathJ)
                val uri = FileProvider.getUriForFile(
                    activity,
                    "$packageName.jprovider",
                    myFile
                )
                intent.setDataAndType(uri, fileTypeJ)
                activity.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                activity.startActivityForResult(intent,VIEW_CODE)
            }else{
                resultMessage("File doesn't exist")
            }
        } catch (e: ActivityNotFoundException) {
            resultMessage(e.message.toString())
        } catch (e: java.lang.Exception) {
            resultMessage(e.message.toString())
        }catch (e: SecurityException){
            resultMessage(e.message.toString())
        }catch (e:UnsupportedOperationException){
            resultMessage(e.message.toString())
        }catch (e:Exception){
            resultMessage(e.message.toString())
        }

    }


    private fun openApkFile() {
        if (!canInstallApp()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                installAppActivity()
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    activity.requestPermissions(arrayOf(REQUEST_INSTALL_PACKAGES),REQ)
                }else{
                    startActivity()
                }

        } else {
            startActivity()
        }
    }

    private fun canInstallApp(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            activity.packageManager.canRequestPackageInstalls()
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            hasPermissionB(REQUEST_INSTALL_PACKAGES)
        } else {
            true
        }
    }

    @Suppress("DEPRECATION")
    private fun installAppActivity() {
        val packageURI = Uri.parse("package:" + activity.packageName)
        val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageURI)
        } else {
           Intent(Intent.ACTION_INSTALL_PACKAGE)
        }
        activity.startActivityForResult(intent, PERMISSION_CODE)
    }

    private fun hasPermissionB(uriPerm: String): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.checkSelfPermission(uriPerm) == PackageManager.PERMISSION_GRANTED
        } else {
            ContextCompat.checkSelfPermission(activity, uriPerm) == PackageManager.PERMISSION_GRANTED
        }
    }
    private fun resultMessage(message: String) {
        if (result != null) {
            result!!.success(message)
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ): Boolean {
        return if(requestCode != REQ){
            false
        }else{
            if(hasPermissionB(READ_EXTERNAL_STORAGE) && STRING_APK == fileTypeJ ){
                openApkFile()
                true
            }else{
                startActivity()
                true
            }
        }
    }
}