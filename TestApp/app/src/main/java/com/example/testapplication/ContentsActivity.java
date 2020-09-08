package com.example.testapplication;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class ContentsActivity extends AppCompatActivity {
    // 定数宣言
    final int GRID_COLUMNS = 3;                     // 表示する画像の列数
    private static final int REQUEST_CODE = 1000;   // 権限リクエスト時のハンドラーに登録するID
    private final static String[] PERMISSION_WRITE_EXTERNAL_STORAGE = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
    private final static String COMIC_DIRECTORY_NAME = "DownloadData";
    private final static String THUMBNAIL_EXTENSION = ".jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contents);

        // ストレージの権限確認
        if(PermissionChecker.isNeedSettingPermission(ContentsActivity.this,
                                                      Manifest.permission.READ_EXTERNAL_STORAGE)){
            requestPermissions(PERMISSION_WRITE_EXTERNAL_STORAGE, REQUEST_CODE);
        }else{
            MakeGridView();
        }
        //MakeGridView();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                          @NonNull String[] permissions,
                                          @NonNull int[] grantResults){
        if(requestCode == REQUEST_CODE && grantResults.length > 0){
            if(!PermissionChecker.checkGrantResults(grantResults)) {
                // 「今後は確認しない」にチェックされているかどうか
                if(PermissionChecker.shouldShowRequestPermissionRationale(ContentsActivity.this, PERMISSION_WRITE_EXTERNAL_STORAGE[0])){
                    Toast.makeText(ContentsActivity.this, "権限が無いためアプリを続行出来ません。", Toast.LENGTH_SHORT).show();
                }else{
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            PermissionChecker.showAlertDialog(getSupportFragmentManager(), "ストレージ");
                        }
                    });
                }
            }else{
                // 使用が許可された
                // リストの構築を開始する
                MakeGridView();
            }
        }
    }

    // GridViewの作成
    private void MakeGridView(){
        // 表示する画像を取得
        try {
            // ダウンロードデータのディレクトリにあるファイルパスを全て取得する
            ArrayList<String> lstDirectoryPass = DirectoryPathFinder.getDirectoryPathList(ContentsActivity.this,
                                                                            COMIC_DIRECTORY_NAME);
            if(lstDirectoryPass == null){
                // nullの場合パス取得先の設定に問題があるためリストを作成せず終了
                Toast.makeText(this, "参照先ディレクトリに問題があります。", Toast.LENGTH_SHORT).show();
                return;
            }

            ArrayList<Bitmap> lstBitmap = new ArrayList<>();
            for(int imageCnt = 0; imageCnt < lstDirectoryPass.size(); imageCnt++) {
                String fileName = DirectoryPathFinder.getLastDirectoryName(lstDirectoryPass.get(imageCnt)) + THUMBNAIL_EXTENSION;
                InputStream inputStream = new FileInputStream(lstDirectoryPass.get(imageCnt) + "/" + fileName);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                lstBitmap.add(bitmap);
            }

            if( lstBitmap.isEmpty() == false ) {
                // グリッドの作成
                GridView mGridView = (GridView) findViewById(R.id.gridView);
                // グリッド4列表示
                mGridView.setNumColumns(GRID_COLUMNS);

                // アダプター作成
                BitmapAdapter adapter = new BitmapAdapter(ContentsActivity.this.getApplicationContext(), lstBitmap);
                // グリッドにアダプタを設定
                mGridView.setAdapter(adapter);
            }
            else{
                // 利用中の端末に何もダウンロードされていないためリストを作成せず終了
                Toast.makeText(this, "ダウンロードされたコミックがありません。", Toast.LENGTH_SHORT).show();
            }
        }catch(Exception e){
            e.printStackTrace();
            Toast.makeText(this, "例外が発生しました。権限は許可されていますか？", Toast.LENGTH_SHORT).show();
        }
    }
}