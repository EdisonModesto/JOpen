import 'package:file_picker/file_picker.dart';
import 'package:flutter/material.dart';
import 'package:jopen/jOpen.dart';
import 'package:permission_handler/permission_handler.dart';

void main() {
  WidgetsFlutterBinding.ensureInitialized();
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _openedFile = 'Unknown';
  final _jOpenPlugin = JOpenPlatform.instance;

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Text('Path: $_openedFile\n'),
        ),
        floatingActionButton: Column(
          mainAxisAlignment: MainAxisAlignment.end,
          children: [
            FloatingActionButton(
              heroTag: '1',
              onPressed: () async {
                await openFile();
              },
              child: const Icon(Icons.android),
            ),
            FloatingActionButton(
              heroTag: '2',
              onPressed: () async {
                await openFile2();
              },
              child: const Icon(Icons.code),
            ),
            FloatingActionButton(
              heroTag: '2',
              onPressed: () async {
                await openFile3();
              },
              child: const Icon(Icons.storage),
            ),
          ],
        ),
      ),
    );
  }

  openFile() async {
    await Permission.storage.request();
    final val = await FilePicker.platform.pickFiles(allowMultiple: false);
    if (val != null) {
      if (val.files.isNotEmpty) {
        final file = val.files.first;
        if (file.path != null) {
          debugPrint(file.path!);

          final v = await _jOpenPlugin.open(path: file.path!);
          setState(() {
            _openedFile = v;
          });
          debugPrint(v.toString());
        }
      }
    }
  }

  openFile2() async {
    await Permission.storage.request();
    final v = await _jOpenPlugin.open(path: "/storage/079C-07F3/troll.mp4");
    setState(() {
      _openedFile = v;
    });
    debugPrint(v.toString());
  }

  openFile3() async {
    await Permission.storage.request();
    final v = await _jOpenPlugin.open(path: "/storage/emulated/0/JustC12.png");
    setState(() {
      _openedFile = v;
    });
    debugPrint(v.toString());
  }
}
