package com.xp.common.tools;

import android.icu.text.SimpleDateFormat;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Date;

@RequiresApi(api = Build.VERSION_CODES.N)
public class DataTool {
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//24小时制

    /**
     * 日期格式化
     * @param date
     * @return
     */
    public static String dataFormat(Date date){
        return  simpleDateFormat.format(date);
    }

    /**
     * 日期格式化
     * @param date
     * @param format
     * @return
     */
    private static String dataFormat(Date date, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
       return simpleDateFormat.format(date);
    }
}
