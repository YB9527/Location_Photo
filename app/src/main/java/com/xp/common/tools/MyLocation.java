package com.xp.common.tools;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import com.xp.common.po.MyCallback;
import com.xp.common.po.ResultData;
import com.xp.common.po.Status;

import java.util.List;
import java.util.Locale;

public class MyLocation {
    private MyCallback myCallback;
    public MyLocation(MyCallback myCallback){
        this.myCallback = myCallback;
    }
    public void start(){
        //检查是否开启权限！
        if (ActivityCompat.checkSelfPermission(AndroidTool.getMainActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(AndroidTool.getMainActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(AndroidTool.getMainActivity(), "权限不够", Toast.LENGTH_LONG).show();
            return;
        }

//获取一个地址管理者，获取的方法比较特殊，不是直接new出来的
        LocationManager locationManager = (LocationManager) AndroidTool.getMainActivity().getSystemService(AndroidTool.getMainActivity().LOCATION_SERVICE);

//使用GPS获取上一次的地址，这样获取到的信息需要多次，才能够显示出来，所以后面有动态的判断
        Location location = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
//判断是否用户打开了GPS开关，这个和获取权限没关系
        GPSisopen(locationManager);
//显示信息，可以根据自己的传入对应的location！！！
        ResultData resultData = new ResultData(Status.Success,"");
        resultData.setObject(location);
        //upLoadInfor(location);//实时的显示信息
        myCallback.call(resultData);

//获取时时更新，第一个是Provider,第二个参数是更新时间1000ms，第三个参数是更新半径，第四个是监听器
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 8, new LocationListener() {

            @Override
            /*当地理位置发生改变的时候调用*/
            public void onLocationChanged(Location location) {

                ResultData resultData = new ResultData(Status.Success,"");
                resultData.setObject(location);
                //upLoadInfor(location);//实时的显示信息
                myCallback.call(resultData);
            }

            /* 当状态发生改变的时候调用*/
            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
                Log.d("GPS_SERVICES", "状态信息发生改变");

            }

            /*当定位者启用的时候调用*/
            @Override
            public void onProviderEnabled(String s) {
                Log.d("TAG", "onProviderEnabled: ");

            }

            @Override
            public void onProviderDisabled(String s) {
                Log.d("TAG", "onProviderDisabled: ");
            }
        });
    }
    //判断是否用户打开GPS开关，并作指导性操作！
    private void GPSisopen(LocationManager locationManager) {
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(AndroidTool.getMainActivity(), "请打开GPS", Toast.LENGTH_SHORT);
            final AlertDialog.Builder dialog = new AlertDialog.Builder(AndroidTool.getMainActivity());
            dialog.setTitle("请打开GPS连接");
            dialog.setMessage("为了获取定位服务，请先打开GPS");
            dialog.setPositiveButton("设置", new android.content.DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //界面跳转
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    AndroidTool.getMainActivity().startActivityForResult(intent, 0);
                }
            });
            dialog.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            //调用显示方法！
            dialog.show();
        }
    }
    //同时获取到的只是location如果想根据location获取具体地址，可以通过Android提供的API获取具体的地点！

    //传进来一个location返回一个Address列表，这个是耗时的操作所以需要在子线程中进行！！！
//传进来一个location返回一个Address列表，这个是耗时的操作所以需要在子线程中进行！！！
//传进来一个location返回一个Address列表，这个是耗时的操作所以需要在子线程中进行！！！
    private List<Address> getAddress(Location location) {
        List<Address> result = null;
        try {
            if (location != null) {
                Geocoder gc = new Geocoder(AndroidTool.getMainActivity(), Locale.getDefault());
                result = gc.getFromLocation(location.getLatitude(),
                        location.getLongitude(), 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }



}
