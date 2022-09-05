import 'package:flutter_test/flutter_test.dart';
import 'package:jopen/jOpen.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockJopenPlatform
    with MockPlatformInterfaceMixin
    implements JOpenPlatform {
  @override
  Future<String> open({required String path, String type = ''}) {
    throw UnimplementedError();
  }
}

void main() {
  test('getPlatformVersion', () async {
    JOpen jopenPlugin = JOpen();
    MockJopenPlatform fakePlatform = MockJopenPlatform();
    JOpenPlatform.instance = fakePlatform;

    expect(await jopenPlugin.open(path: ""), '42');
  });
}
