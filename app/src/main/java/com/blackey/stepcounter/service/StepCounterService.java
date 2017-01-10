package com.blackey.stepcounter.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;


import com.blackey.stepcounter.util.DateUtil;
import com.blackey.stepcounter.util.SharedPreferenceHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

//service负责后台的需要长期运行的任务
// 计步器服务
// 运行在后台的服务程序，完成了界面部分的开发后
// 就可以开发后台的服务类StepService
// 注册或注销传感器监听器，在手机屏幕状态栏显示通知，与StepActivity进行通信，走过的步数记到哪里了？？？
public class StepCounterService extends Service implements Runnable {
    public static int CUR_STEP = 0;

    public static Boolean FLAG = false;// 服务运行标志

    private SensorManager mSensorManager;// 传感器服务
    private StepDetector counter;// 传感器监听对象
    private Sensor sensor;

    private PowerManager mPowerManager;// 电源管理服务
    private WakeLock mWakeLock;// 屏幕灯
    String filename = "stepcounter";
    SharedPreferenceHelper sharedPreferenceHelper;
    SharedPreferenceHelper loginSPHelper;

    Date newDate;


    int originCounter, currentCounter, subStep;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        loginSPHelper = new SharedPreferenceHelper("LoginUser", this);
        sharedPreferenceHelper = new SharedPreferenceHelper("shared_file", this);

        flags = START_STICKY_COMPATIBILITY;

        /*if ((int) sharedPreferenceHelper.get("CURRENT_STEP", 0) > CUR_STEP) {
            CUR_STEP = (int) sharedPreferenceHelper.get("CURRENT_STEP", 0);
        } else {
            sharedPreferenceHelper.put("CURRENT_STEP", CUR_STEP);
        }*/
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        FLAG = false;// 服务停止
        if (counter != null) {
            mSensorManager.unregisterListener(counter);
        }
        if (mWakeLock != null) {
            mWakeLock.release();
        }
        Intent intent = new Intent("android.intent.action.MY_BROADCAST");
        intent.putExtra("type", 2);
        sendBroadcast(intent);
        super.onDestroy();
    }

    @Override
    public void run() {

        FLAG = true;// 标记为服务正在运行

        // 创建监听器类，实例化监听对象
        counter = new StepDetector(this);

        // 获取传感器的服务，初始化传感器
        mSensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);
        sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (sensor != null) {
            // 注册传感器，注册监听器
            mSensorManager.registerListener(counter, sensor, SensorManager.SENSOR_DELAY_FASTEST);
        } else {
            /*mSensorManager.registerListener(counter,
                    mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                    SensorManager.SENSOR_DELAY_FASTEST);*/
        }

        // 电源管理服务
        mPowerManager = (PowerManager) this
                .getSystemService(Context.POWER_SERVICE);
        mWakeLock = mPowerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK
                | PowerManager.ACQUIRE_CAUSES_WAKEUP, "S");
        mWakeLock.acquire();

        while (true) {
            try {
                Date date = new Date();//取时间
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                date = calendar.getTime();
                String curDate = formatter.format(date);
                int rhs = DateUtil.getDateSub(sharedPreferenceHelper.get("curDate", "1970-01-01").toString(), curDate);
                Log.i("curDate", curDate + "   " + sharedPreferenceHelper.get("curDate", "1970-01-01") + "   " + rhs);
                if (rhs != 0) {
                    sharedPreferenceHelper.put("CURRENT_STEP", 0);
                    CUR_STEP = 0;
                    sharedPreferenceHelper.put("ORIGIN_COUNTER", StepDetector.CURRENT_STEP);
                    sharedPreferenceHelper.put("curDate", curDate);
                } else {
                    if (StepDetector.CURRENT_STEP < originCounter) {
                        CUR_STEP += originCounter;
                        originCounter = StepDetector.CURRENT_STEP;
                        sharedPreferenceHelper.put("ORIGIN_COUNTER", originCounter);
                        Thread.sleep(10000);
                        continue;
                    }
                }

                if ((Integer) sharedPreferenceHelper.get("ORIGIN_COUNTER", 0) != 0) {
                    originCounter = (int) sharedPreferenceHelper.get("ORIGIN_COUNTER", 0);
                    Log.i("tt", "aaa");
                } else {
                    originCounter = StepDetector.CURRENT_STEP;
                    Log.i("tt","bbb");
                }

                newDate = new Date();
                if (newDate.getHours() == 0 && newDate.getMinutes() == 0) {
                    sharedPreferenceHelper.put("CURRENT_STEP", 0);
                    CUR_STEP = 0;
                    sharedPreferenceHelper.put("ORIGIN_COUNTER", StepDetector.CURRENT_STEP);
                    sharedPreferenceHelper.put("curDate", curDate);
                }

                Log.i("subStep", originCounter + "   " + currentCounter + "   " + subStep + "   " + CUR_STEP);

                currentCounter = StepDetector.CURRENT_STEP;
                subStep = currentCounter - originCounter;

                if (subStep > 0) {
                    CUR_STEP += subStep;
                }
                originCounter = currentCounter;

                sharedPreferenceHelper.put("ORIGIN_COUNTER", originCounter);
                //System.nanoTime();
                //SystemClock.elapsedRealtime();
                if ((int) sharedPreferenceHelper.get("CURRENT_STEP", 0) > CUR_STEP) {
                    CUR_STEP = (int) sharedPreferenceHelper.get("CURRENT_STEP", 0);
                } else {
                    sharedPreferenceHelper.put("CURRENT_STEP", CUR_STEP);
                }

                Thread.sleep(10000);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
