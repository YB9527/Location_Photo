package com.xp;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;
import com.loopj.android.http.TextHttpResponseHandler;
import com.xp.common.JSONUtils;
import com.xp.common.Photo;
import com.xp.common.Tool;
import com.xp.zjd.po.ZJD;

import org.apache.http.Header;
import org.json.JSONObject;
import org.junit.Test;

import java.io.File;
import java.nio.file.Path;
import java.security.spec.ECField;

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
        ZJD zjd = new ZJD();
        zjd.setId(0);
        zjd.setmDKBM("1");
        zjd.setmDKMC("2");
        Photo p = new Photo("d:/123.jpg",false);
        zjd.getPhotos().add(p);
        p.setId(123);
       p.setZjd(zjd);
       //String str =  new Gson().toJson(p);


        //Photo geson = new Gson().fromJson("{path:"+"d:/123.jpg,"+"name:"+"123.jpg","id:"+"123","zjd:"+"{"+"mDKBM:"+'1'," mDKMC:"+"2", "id:"+"0"+"}","isUpload:"+"false}",Photo.class);
    }



}