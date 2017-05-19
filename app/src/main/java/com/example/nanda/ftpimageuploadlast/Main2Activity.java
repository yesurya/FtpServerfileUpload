package com.example.nanda.ftpimageuploadlast;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import android.os.Handler;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

//import org.apache.commons.net.ftp.FTPClient;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Main2Activity extends Activity {

    protected static final int SELECT_PICTURE = 0;
    private static final String SERVER = "ftp.pec.edu.np";
    private static final String USERNAME = "tryagn@pec.edu.np";
    private static final String PASSWORD = "th3br0gramm3rs125";
    private static final String PATH = null;
    public String var;
    TextView textTargetUri;
    Handler progressHandler;
    String img;
    //public FTPClient mFTPClient = null;
    boolean status = false;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Button buttonLoadImage = (Button)findViewById(R.id.loadimage);
        textTargetUri = (TextView)findViewById(R.id.targeturi);

        buttonLoadImage.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View arg0) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                        "Click Picture To Export"), SELECT_PICTURE);

            }});
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            Uri targetUri = data.getData();
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = managedQuery(targetUri, projection, null, null, null);
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            //return cursor.getString(column_index);
            textTargetUri.setText(cursor.getString(column_index));
             var = cursor.getString(column_index);
            // textTargetUri.setText("connected"+var);
             img = var.substring(8);

            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    //     buffer();

                    FTPClient ftpClient = new FTPClient();

                    try {

                        ftpClient.connect(InetAddress.getByName(SERVER));
                        ftpClient.login(USERNAME, PASSWORD);
                        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                        ftpClient.enterLocalPassiveMode();

                        // Prepare file to be uploaded to FTP Server
                        File file = new File(var);
                        FileInputStream ifile = new FileInputStream(file);

                        // Upload file to FTP Server
                        ftpClient.storeFile("asdf.jpg",ifile);
                        Log.d("-----", "uploaded");
                        ftpClient.disconnect();


                    } catch (SocketException e) {
                        e.printStackTrace();
                        Log.e("-----", e.getStackTrace().toString());
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                        Log.e("---------------", e.getStackTrace().toString());
                    } catch (IOException e) {
                        Log.e("----------------", e.getStackTrace().toString());
                    }




                }
            });
            t.start();
            textTargetUri.setText(var);

        }
    }

}