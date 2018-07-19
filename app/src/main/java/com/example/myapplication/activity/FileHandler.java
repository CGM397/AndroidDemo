package com.example.myapplication.activity;

import java.io.*;

import android.app.Activity;
import com.example.myapplication.entity.Music;

public class FileHandler extends Activity{

    public void writeString(File fileName, String message){

        try {
            FileOutputStream fos = new FileOutputStream(fileName,true);//获得FileOutputStream对象
            byte[] bytes = message.getBytes();//将要写入的字符串转换为byte数组
            fos.write(bytes);//将byte数组写入文件
            fos.close();//关闭FileOutputStream对象
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readString(File fileName, int position){
        String result = "";
        try {
            FileInputStream fin = new FileInputStream(fileName);//获得FileInputStream对象
            InputStreamReader inputStreamReader = new InputStreamReader(fin);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line;
            int curPos = 1;
            while((line = reader.readLine()) != null){
                if(curPos == position) {
                    result = line;
                    break;
                }
                else
                    curPos++;
            }
            inputStreamReader.close();
            reader.close();
            //int length = fin.available();//获取文件长度
            //byte[] buffer = new byte[length];//创建byte数组用于读入数据
            //int byteCount = fin.read(buffer);
            //result = new String(buffer, 0, byteCount, "utf-8");
            fin.close();//关闭文件输入流
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
