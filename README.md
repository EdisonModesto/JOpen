# jopen
JOpen is a Plugin that allows you to open any files in Android, JOpen provides a way to open files by file path

## Features
- Open File From Path.

## Usage
To Use `jopen`

First:
On Your `AndroidManifest.xml`
```xml
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
```

Second:

You Can Implement it to Your Code Like:
```dart
final _jOpenPlugin = JOpenPlatform.instance;
```
Or
```dart
final _jOpenPlugin = JOpen();
```
Finally:
```dart
Future<String> openFile() async {
  final v = await _jOpenPlugin.open(path: "/storage/emulated/0/JustC12.png");
  setState(() {
    _openedFile = v;
  });
  debugPrint(v.toString());
}

```

## Additional information

Provided By [Just Codes Developers](https://jucodes.com/)

