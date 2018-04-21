package com.posturl.posturl

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.util.Log

import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import org.androidannotations.annotations.Background
import java.io.IOException
import android.os.StrictMode
import com.facebook.stetho.okhttp3.StethoInterceptor


class MainActivity : AppCompatActivity() {

    internal var okHttpClient = OkHttpClient.Builder()
            .addNetworkInterceptor(StethoInterceptor())
            .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        btn.setOnClickListener{
            url()
        }

    }



    private fun updateResult(myResponse: String) {
        val msg = Message.obtain()
        msg.obj = myResponse
        Log.i("responce==>",myResponse)
        //Message msg = Message.obtain(mainThreadHandler);
        //msg.sendToTarget();
    }

    fun url(){
        val httpUrl = HttpUrl.Builder()
                .scheme("https")
                .host("ormbot-engine.herokuapp.com")
                .addPathSegment("signin")
                .build()

        val form = FormBody.Builder()
                .add("auth_param", "thisismy1@email.com")
                .add("password", "andthisismypassword")
                .build()


            try {
                updateResult(doSyncPost(okHttpClient, httpUrl, form))
            } catch (e: IOException) {
                e.printStackTrace()
                Log.i("Error==>",e.toString())
            }

           }
    @Throws(IOException::class)
    fun doSyncPost(client: OkHttpClient, url: HttpUrl, body: RequestBody): String {
        return doSyncPost(client, url.toString(), body)
    }
    @Throws(IOException::class)
    fun doSyncPost(client: OkHttpClient, url: String, body: RequestBody): String {
        val request = Request.Builder()
                .url(url)
                //.method("POST", body) already done in post()
                .post(body)
                .build()
//        if (android.os.Build.VERSION.SDK_INT > 9) {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
//        }
        val response = client.newCall(request).execute()
        return response.body()?.string().toString()
    }
}
