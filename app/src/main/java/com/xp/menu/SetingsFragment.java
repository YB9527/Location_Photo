package com.xp.menu;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.loopj.android.image.SmartImageView;
import com.xp.R;
import com.xp.common.tools.AndroidTool;
import com.xp.common.tools.Photo;
import com.xp.common.tools.Tool;

import org.apache.http.Header;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;

import java.io.UnsupportedEncodingException;

/**
 * 全局设置界面
 */
public class SetingsFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_settings,container,false);
        //sendPostWithEntity();

        Button btu = view.findViewById(R.id.aa);
        btu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SmartImageView imageView = view.findViewById(R.id.imageView);
                AsyncHttpClient client = new AsyncHttpClient();
                String urlPath = Tool.getHostAddress()+"pic";
                imageView.setImageUrl(urlPath,R.mipmap.ic_launcher);
               /* File file = new File("/aa.jpg");
                client.get(Tool.getHostAddress() + "downloadfile", new FileAsyncHttpResponseHandler(file) {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {

                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, File file) {

                    }

                });
*/
                //Bitmap bitmap = BitmapFactory.decodeStream()
                //imageView.setImageBitmap();
            }
        });
        return  view;
    }

    private void sendPostWithEntity(){
        AsyncHttpClient client = new AsyncHttpClient();
        client.setProxy("192.168.3.3",8080);
        String postBody = "this is post body";
        ByteArrayEntity entity = null;

        try {
            entity = new ByteArrayEntity(postBody.getBytes("UTF-8"));

            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String urlPath = Tool.getHostAddress()+"demo1";
        client.post(AndroidTool.getMainActivity(),urlPath,entity,"application/json",new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i,Header[] headers, byte[] bytes) {
                Log.d("hwj", "**AsyncHttpClientActivity onSuccess**");
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                Log.d("hwj", "**AsyncHttpClientActivity onFailure**");
            }
        });
    }
    public void postPhoot(){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        Photo photo = new Photo("d:/aa/123.jpg",false);
        String str = new Gson().toJson(photo);
        params.put("photo",str);

        String urlPath = Tool.getHostAddress()+"demo1";

        client.post(urlPath,params,new TextHttpResponseHandler(){
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

            }



        });


    }
}
