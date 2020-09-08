package com.example.testapplication;

import android.app.Activity;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class FilePathFinder {
    // 指定したdirectryNameの場所から画像ファイルのパスをリスト化してフルパスで取得する
    // ※取得するファイルの拡張子をしていする場合は引数extensuionsに設定(全ファイル取得の場合はnull指定)
    public static ArrayList<String> getFilePathList(Activity activity, String directoryName, String[] extensions){
        String fullPath = "";
        String fileName = "";
        String fileExtension = "";
        int lastDotPosition = 0;
        ArrayList<String> refFilePathList = new ArrayList<>();
        File dir = activity.getExternalFilesDir(directoryName);
        File[] files = dir.listFiles();
        if( files == null ){
            return null;
        }

        for(int fileCnt = 0; fileCnt < files.length; fileCnt++){
            fullPath = String.valueOf(files[fileCnt]);
            fileName = String.valueOf(files[fileCnt].getName());

            if(files[fileCnt].isDirectory()){
                // ディレクトリの為対象外
                continue;
            }

            // 取得するファイルの拡張子指定があればファイルパスから拡張子を見て処理する
            if(extensions != null) {
                // 拡張子取得
                fileExtension = "";
                lastDotPosition = fileName.lastIndexOf(".");
                if (lastDotPosition != -1) {
                    fileExtension = fileName.substring(lastDotPosition + 1);
                }

                // 拡張子がextensionsに含まれてればリストに追加する
                if (Arrays.asList(extensions).contains(fileExtension)) {
                    refFilePathList.add(fullPath);
                }
            }else{
                // 全ファイル取得するので無条件でパスをリストに追加
                refFilePathList.add(fullPath);
            }
        }

        return refFilePathList;
    }
}
