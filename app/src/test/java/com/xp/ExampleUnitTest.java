package com.xp;

import com.xp.common.tools.OkHttpClientUtils;
import com.xp.common.tools.Photo;
import com.xp.zjd.po.ZJD;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void Test() {


        //Photo geson = new Gson().fromJson("{path:"+"d:/123.jpg,"+"name:"+"123.jpg","id:"+"123","zjd:"+"{"+"mDKBM:"+'1'," mDKMC:"+"2", "id:"+"0"+"}","isUpload:"+"false}",Photo.class);
    }



}