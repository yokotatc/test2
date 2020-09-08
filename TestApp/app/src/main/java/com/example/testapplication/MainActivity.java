package com.example.testapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.embedding.engine.FlutterEngineCache;
import io.flutter.embedding.engine.dart.DartExecutor;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FlutterEngine flutterEngine = new FlutterEngine(MainActivity.this);
        flutterEngine.getDartExecutor().executeDartEntrypoint(DartExecutor.DartEntrypoint.createDefault());
        FlutterEngineCache.getInstance().put("paper_comic_engine", flutterEngine);

        Button guestButton = this.findViewById(R.id.button2);
        guestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(MainActivity.this, ContentsActivity.class);
                //Intent intent = new Intent(MainActivity.this, FlutterPageViewActivity.class);
                //startActivity(intent);
                startActivity(FlutterActivity.createDefaultIntent(MainActivity.this));
                startActivity(FlutterActivity.withCachedEngine("paper_comic_engine").build(MainActivity.this));
            }
        });
    }
}