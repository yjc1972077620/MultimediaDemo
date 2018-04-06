package com.example.yjc19.multimediademo;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Context context=this;
    private Button show_notice;
    private Button take_photo;
    private ImageView myphoto;
    private Uri imageUri;
    private static final int TAKE_PHOTO=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        show_notice.setOnClickListener(this);
        take_photo.setOnClickListener(this);
    }

    public void init()
    {
        show_notice=findViewById(R.id.show_notice);
        take_photo=findViewById(R.id.take_photo);
        myphoto=findViewById(R.id.my_photo);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.show_notice:
                Intent intent=new Intent(this,ClickNoticeActivity.class);
                PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intent,0);
                //获得一个NotificationManager对象来对通知进行管理
                NotificationManager manager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                //设置点击通知后通知自动消失,将notify中传入的消息id传给cancel即可
                manager.cancel(1);
                //建立通知对象
                Notification notification=new NotificationCompat.Builder(context,"test")
                        .setContentTitle("消息通知测试")
                        .setContentText("这是一条对于手机通知进行测试的消息aSBHDjkhbfkhjzbdckjlzbdfhjkbszjkcbhjzkcvbjkhsavdfhjabsdjhkavbfhavhfvkajhsfbakfbhsdfbsjhdvfgbhsvdbkjhsjdfbnsjkldbfgnshgbksjhbdfvkljsdhbnfkjl;sbnfjkl")
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.drawable.messageicon)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)//设置点击通知后通知自动消失
                        //.setSound(Uri.fromFile(new File("/system/media/audio/ringtones/Luna.ogg")))//为消息设置通知铃声
                        .setVibrate(new long[]{0,1000,1000,1000})//设置通知震动（数组中第奇数元素表示静止时长，偶数表示震动时长，通过数组设置来使手机间歇性震动）
                        .setLights(Color.GREEN,1000,1000)//设置呼吸灯的显示（第一个参数表示颜色，第二个参数表示亮起时间，第三个表示熄灭时间）
                        .setDefaults(NotificationCompat.DEFAULT_ALL)//如果不想进行复杂的设置。可以直接使用系统默认效果，系统根据消息类别和手机当前环境决定实现效果
                        //将大段的文字完全显示
                       //.setStyle(new NotificationCompat.BigTextStyle().bigText("这是一条对于手机通知进行测试的消息aSBHDjkhbfkhjzbdckjlzbdfhjkbszjkcbhjzkcvbjkhsavdfhjabsdjhkavbfhavhfvkajhsfbakfbhsdfbsjhdvfgbhsvdbkjhsjdfbnsjkldbfgnshgbksjhbdfvkljsdhbnfkjl"))
//                        .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(BitmapFactory.decodeResource(getResources(),R.drawable.bg)))//在通知中显示大图片
                        .setPriority(NotificationCompat.PRIORITY_MAX)//设置消息的优先级程度，系统根据优先级对消息进行显示
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.messageicon))
                        .build();
                manager.notify(1,notification);
                break;
            case R.id.take_photo:
//                //创建File对象，用于存储拍摄后的照片
//                File file=new File(getExternalCacheDir(),"output_image.jpg");
//
//                try {
//                    if(file.exists())
//                    file.delete();
//                    file.createNewFile();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                if(Build.VERSION.SDK_INT>=24)
//                {
//                    imageUri=FileProvider.getUriForFile(MainActivity.this,"com.example.yjc19.multimediademo",file);
//                }
//                else
//                {
//                    imageUri=Uri.fromFile(file);
//                }

                Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent1, 1);
            default:
                 break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode)
        {
            case TAKE_PHOTO:
                if (resultCode == Activity.RESULT_OK){
                    String sdStatus = Environment.getExternalStorageState();
                    //首先判断sd卡是否可用
                    if(!sdStatus.equals(Environment.MEDIA_MOUNTED)){
                        System.out.println(" ------------- sd card is not avaiable ---------------");
                        return;
                    }


                    String name = "photo.jpg";

                    Bundle bundle = data.getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");

                    File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/");
                    file.mkdirs(); //创建文件夹

                    String fileName = Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+name;

                    FileOutputStream b =null;

                    try {
                        b=new FileOutputStream(fileName);
                        bitmap.compress(Bitmap.CompressFormat.JPEG,100,b);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }finally {
                        try {
                            b.flush();
                            b.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                  myphoto.setImageBitmap(bitmap);

                }
                break;
            default:
                break;
        }
    }
}
