import 'dart:io';
import 'package:jopen/_src/_android/jOpen_android.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

abstract class JOpenPlatform extends PlatformInterface {
  /// Constructs a JOpenPlatform.
  JOpenPlatform() : super(token: _token);

  static final Object _token = Object();

  static JOpenPlatform _instance = JOpenPlatform._setPlatform();

  /// The default instance of [JOpenPlatform] to use.
  ///
  /// Defaults to [MethodChannelJOpen].
  static JOpenPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [JOpenPlatform] when
  /// they register themselves.
  static set instance(JOpenPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  factory JOpenPlatform._setPlatform() {
    if (Platform.isAndroid) {
      return JOpenAndroid();
    } else {
      throw UnimplementedError(
        'The current platform "${Platform.operatingSystem}" is not supported by this plugin.',
      );
    }
  }
  Future<String> open({required String path, String type = ''}) {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }
}
