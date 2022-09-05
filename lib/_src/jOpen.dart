import 'package:jopen/_src/jopen_platform_interface.dart';

class JOpen {
  ///[Future] method [open]
  ///takes the [String] path and [String] type
  ///Call [JOpenPlatform] and [instance.open] that has [String] return Value
  Future<String> open({required String path, String type = ''}) {
    return JOpenPlatform.instance.open(path: path, type: type);
  }
}
