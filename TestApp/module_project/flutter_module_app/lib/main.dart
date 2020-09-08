import 'package:flutter/material.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        // This is the theme of your application.
        //
        // Try running your application with "flutter run". You'll see the
        // application has a blue toolbar. Then, without quitting the app, try
        // changing the primarySwatch below to Colors.green and then invoke
        // "hot reload" (press "r" in the console where you ran "flutter run",
        // or press Run > Flutter Hot Reload in a Flutter IDE). Notice that the
        // counter didn't reset back to zero; the application is not restarted.
        primarySwatch: Colors.blue,
      ),
      home: MyHomePage(title: 'Flutter Demo Home Page'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  MyHomePage({Key key, this.title}) : super(key: key);

  // This widget is the home page of your application. It is stateful, meaning
  // that it has a State object (defined below) that contains fields that affect
  // how it looks.

  // This class is the configuration for the state. It holds the values (in this
  // case the title) provided by the parent (in this case the App widget) and
  // used by the build method of the State. Fields in a Widget subclass are
  // always marked "final".

  final String title;

  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  final _controller = PreloadPageController(
    initialPage: 0,
  );
  final _pages = List<Page>();
  bool _isFullScreen = false;
  int _currentIndex = 0;

  @override
  void initState() {
    super.initState();
    Iterable<int>.generate(20).forEach((index) {
      _pages.add(
        Page.manga(
          url: "https://placehold.jp/9fa0b0/ffffff/360x640.png?text=Page ${index + 1}",
        ),
      );
    });

    _pages.add(Page.end());
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      extendBodyBehindAppBar: true,
      appBar: _isFullScreen
          ? null
          : AppBar(
              title: Text(widget.title),
            ),
      body: Stack(
        children: <Widget>[
          Container(
            color: Colors.black,
          ),
          // 1. 左右にスワイプして、ページを切り替えられる
          PreloadPageView.builder(
            preloadPagesCount: 2,
            controller: _controller,
            onPageChanged: (index) {
              setState(() {
                _currentIndex = index;
              });
            },
            reverse: true,
            itemCount: _pages.length,
            itemBuilder: (context, index) {
              final page = _pages[index];
              return page.when(
                manga: (url) {
                  // 2. 画像を読み込んで表示できる
                  // 3. 画像の拡大縮小ができる
                  return PhotoView(
                    onTapUp: (context, details, controllerValue) {
                      setState(() {
                        // 5. 画面タップで全画面表示の切り替えができる
                        _isFullScreen = !_isFullScreen;
                      });
                    },
                    imageProvider: NetworkImage(url),
                  );
                },
                end: () {
                  // 7. ビューワーの最後のページには、マンガページとは違うページ（最終ページ）がある
                  return Container(
                    color: Colors.grey,
                    child: Center(
                      child: Text("End Page"),
                    ),
                  );
                },
              );
            },
          ),
          Align(
            alignment: Alignment.bottomCenter,
            child: Visibility(
              visible: !_isFullScreen,
              child: Column(
                mainAxisAlignment: MainAxisAlignment.end,
                children: <Widget>[
                  Visibility(
                    visible: !(_pages[_currentIndex] is EndPage),
                    child: Container(
                      decoration: BoxDecoration(
                        color: Colors.black45,
                        borderRadius: BorderRadius.all(
                          Radius.circular(8),
                        ),
                      ),
                      padding: EdgeInsets.fromLTRB(16, 8, 16, 8),
                      // 6. ページ番号の表示がある
                      child: Text(
                        "${_currentIndex + 1}/${_pages.mangaPageLength()}",
                        style: TextStyle(
                          color: Colors.white,
                          fontSize: 16,
                        ),
                      ),
                    ),
                  ),
                  SizedBox(
                    height: 16,
                  ),
                  Container(
                    height: 60,
                    color: Theme.of(context).primaryColor,
                    // 4. スライダーでページを移動できる
                    child: SliderTheme(
                      data: SliderTheme.of(context).copyWith(
                          activeTrackColor: Colors.white,
                          inactiveTrackColor: Colors.white,
                          thumbColor: Colors.white,
                          trackHeight: 3.0,
                          trackShape: MyRoundedRectSliderTrackShape()),
                      child: Slider(
                        onChanged: (value) {
                          setState(() {
                            _currentIndex = _pages.lengthForIndex() - value.floor();
                            _controller.jumpToPage(_currentIndex);
                          });
                        },
                        value: (_pages.lengthForIndex() - _currentIndex).toDouble(),
                        min: 0,
                        max: _pages.lengthForIndex().toDouble(),
                      ),
                    ),
                  ),
                ],
              ),
            ),
          ),
        ],
      ),
    );
  }
}
