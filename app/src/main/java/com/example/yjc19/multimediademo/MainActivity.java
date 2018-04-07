package com.example.yjc19.multimediademo;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Context context=this;
    private Button show_notice;
    private Button take_photo;
    private static final int TAKE_PHOTO = 1;//打开相机请求码
    private ImageView myphoto;
    private static final int CHOOSE_PHOTO = 2;//打开系统相册请求码
    private static final int PHOTO_REQUEST_CUT = 3;//裁剪相片请求码
    private Button choose_photo;
    private ImageView album_photo;
    private Button play;
    private Button pause;
    private Button stop;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        show_notice.setOnClickListener(this);
        take_photo.setOnClickListener(this);
        choose_photo.setOnClickListener(this);
        play.setOnClickListener(this);
        pause.setOnClickListener(this);
        stop.setOnClickListener(this);


    }

    public void init()
    {
        show_notice=findViewById(R.id.show_notice);
        take_photo=findViewById(R.id.take_photo);
        myphoto=findViewById(R.id.my_photo);
        choose_photo = findViewById(R.id.choose_photo);
        album_photo = findViewById(R.id.album_photo);
        play = findViewById(R.id.play);
        pause = findViewById(R.id.pause);
        stop = findViewById(R.id.stop);
        mediaPlayer = new MediaPlayer();
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
                Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent1, TAKE_PHOTO);
                break;
            case R.id.choose_photo:
                //首先判断有没有打开系统相册的权限，如果没有则申请权限
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    open_album();
                }
                break;
            case R.id.play:

            default:
                 break;
        }
    }

    private void open_album() {
        //启动系统相册
        Intent intent2 = new Intent("android.intent.action.GET_CONTENT");
        intent2.setType("image/*");
        startActivityForResult(intent2, CHOOSE_PHOTO);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 2:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    open_album();
                } else {
                    Toast.makeText(this, "你拒绝了权限申请", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
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
                        System.out.println("------------- SD卡不可用 ---------------");
                        return;
                    }
                    String name = "photo" + System.currentTimeMillis() + ".jpg";
                    //获取拍照相片的数据，使用bundle是为了保证在activity被销毁的情况下还能获取到数据
                    Bundle bundle = data.getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");
                    File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/MultimediaDemo/");
                    file.mkdirs(); //创建文件夹
                    //将图片存储在本地(用绝对路径）
                    String fileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MultimediaDemo/" + name;
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
                    //将照片显示在界面上的imageview中
                  myphoto.setImageBitmap(bitmap);
                }
                break;
            case CHOOSE_PHOTO:
                if (requestCode == RESULT_OK) {
                    Uri uri = data.getData();
                    //判断手机系统版本，因为从安卓4.4之后开始选区系统相册的照片就不再返回图片的真实uri了，而是一个封装后的uri所以要对不同版本进行uri的解析
                    if (Build.VERSION.SDK_INT >= 19) {

                        handleimageafter(data);

                    } else {
                        handleimagebefore(data);

                    }
                }
            default:
                break;
        }
    }

    //4.4之前图片的处理
    private void handleimagebefore(Intent data) {
        Uri uri = data.getData();

        String imagePath = uri.getPath();
        show_choose_photo(imagePath);
    }

    //4.4之后图片处理
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void handleimageafter(Intent data) {
        String imagePath = "";
        Uri uri = data.getData();
        //如果返回的uri是document类型,通过document id处理
        if (DocumentsContract.isDocumentUri(this, uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];//通过字符串分割解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(uri, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://dowmloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(uri, null);
            }
            //如果返回的是content类型的uri
            else if ("content".equalsIgnoreCase(uri.getScheme())) {
                imagePath = getImagePath(uri, null);
            }
            //如果返回的是file类型的uri则直接通过uri获取
            else if ("file".equalsIgnoreCase(uri.getScheme())) {
                imagePath = uri.getPath();
            }
            //将图片显示在imageview中
            show_choose_photo(imagePath);

        }
    }

    private void show_choose_photo(String imagePath) {
        Bitmap compress_bitmap = null;
        FileOutputStream out = null;
        byte[] image_bytes = new byte[1024];
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            try {
                out = new FileOutputStream(imagePath);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.write(image_bytes);
                compress_bitmap = BitmapFactory.decodeByteArray(image_bytes, 0, image_bytes.length);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            album_photo.setImageBitmap(compress_bitmap);
        } else {
            Toast.makeText(MainActivity.this, "获取图片失败", Toast.LENGTH_SHORT).show();
        }
    }

    //获取图片真实路径的方法
    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

//    //剪切图片
//     private void crop(Uri uri) {
//                // 裁剪图片意图
//                 Intent intent = new Intent("com.android.camera.action.CROP");
//                 intent.setDataAndType(uri, "image/*");
//                 intent.putExtra("crop", "true");
//                // 裁剪框的比例，1：1
//                 intent.putExtra("aspectX", 1);
//                 intent.putExtra("aspectY", 1);
//               // 裁剪后输出图片的尺寸大小
//                 intent.putExtra("outputX", 250);
//                 intent.putExtra("outputY", 250);
//
//                 intent.putExtra("outputFormat", "JPEG");// 图片格式
//                 intent.putExtra("noFaceDetection", true);// 取消人脸识别
//                 intent.putExtra("return-data", true);
//                 // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CUT
//                 startActivityForResult(intent, PHOTO_REQUEST_CUT);
//             }

}
