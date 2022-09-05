import 'package:flutter/services.dart';
import 'package:jopen/_src/jopen_platform_interface.dart';

/// [String] MethodChannel [_METHOD_CHANNEL]
const String _METHOD_CHANNEL = "JOPEN";

///[String] MethodName [_OPEN_FILE]
const String _OPEN_FILE = "OpenFile";

/// The method channel used to interact with the native platform.
const methodChannel = MethodChannel(_METHOD_CHANNEL);

///[String] MapKey [_PATH]
const String _PATH = "path";

///[String] MapKey [_TYPE]
const String _TYPE = "type";

class JOpenAndroid extends JOpenPlatform {
  ///[Future] method [open]
  ///takes the [String] path and [String] type
  ///Call [MethodChannel] and [invokeMethod] that has [String] return Value
  @override
  Future<String> open({required String path, String type = ''}) async {
    try {
      if (path.isNotEmpty) {
        final vPath = await methodChannel
            .invokeMethod<String>(_OPEN_FILE, {_PATH: path, _TYPE: type});
        if (vPath != null) {
          return vPath;
        } else {
          return "Check Permissions";
        }
      } else {
        return "Path Couldn't beEmpty";
      }
    } catch (e) {
      return e.toString();
    }
  }
}
