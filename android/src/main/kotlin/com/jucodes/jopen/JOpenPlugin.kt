package com.jucodes.jopen

import androidx.annotation.NonNull
import io.flutter.Log

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

/** JOpenPlugin */
class JOpenPlugin: FlutterPlugin, ActivityAware, MethodCallHandler {
    private var jFileProvider: JFileNew? = null
    private var activity: ActivityPluginBinding? = null
    private var pluginBinding: FlutterPlugin.FlutterPluginBinding? = null

    private var result: Result? = null
    private val tag: String = "JOpen"
    private lateinit var channel : MethodChannel

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
      pluginBinding = flutterPluginBinding
      channel = MethodChannel(flutterPluginBinding.binaryMessenger, "JOPEN")
      channel.setMethodCallHandler(this)
  }

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
        if (jFileProvider == null) {
            initJFileProvider()
        }
        try {
            this.result = result
            if (call.method == "OpenFile") {
                val fPathT = call.argument<String>("path")
                val fTypeM = call.argument<String>("type")
                jFileProvider!!.openFile(fPathT!!, fTypeM!!, result)
            } else {
                result.notImplemented()
            }
        }  catch (e: Exception) {
            Log.d(tag,  e.message.toString())
        }

    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        pluginBinding = null
        if(jFileProvider!=null) {
            activity?.removeActivityResultListener(jFileProvider!!)
            activity?.removeRequestPermissionsResultListener(jFileProvider!!)
            jFileProvider = null
        }
        channel.setMethodCallHandler(null)
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        this.activity = binding
    }

    override fun onDetachedFromActivityForConfigChanges() {
        if (jFileProvider != null) {
            activity?.removeActivityResultListener(jFileProvider!!)
            activity?.removeRequestPermissionsResultListener(jFileProvider!!)
            jFileProvider = null
        }
        activity = null
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {

        this.activity = binding
    }

    override fun onDetachedFromActivity() {
        if (jFileProvider != null) {
            activity?.removeActivityResultListener(jFileProvider!!)
            jFileProvider = null
        }
        activity = null
    }
    private fun initJFileProvider(): Boolean {
        var jProvider: JFileNew? = null
        if (activity != null) {
            jProvider = JFileNew(
                activity = activity!!.activity
            )
            activity!!.addActivityResultListener(jProvider)
            activity!!.addRequestPermissionsResultListener(jProvider)
        } else {
            if (result != null)
                result?.error("NullActivity", "Activity was Null", null)
        }
        this.jFileProvider = jProvider
        return jProvider != null
    }

}
