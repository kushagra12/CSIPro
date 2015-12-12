package com.csi.kushagra.cosawaaridriver;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.neovisionaries.ws.client.WebSocketException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Login extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "com.csi.cosawaaridriver.MESSAGE";
    static final int REQUEST_IMAGE_CAPTURE = 1;
    ImageView mImageView;
    String imageEncoded = null;
    SocketsApp mSockets;
    EditText tvID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_login);

        try {
            mSockets = SocketsApp.getInstance(this);
        } catch (IOException | WebSocketException e) {
            e.printStackTrace();
        }

        Button btDone = (Button) findViewById(R.id.btDoneLogin);
        Button btPhoto = (Button) findViewById(R.id.btTakePhoto);
        Button btSignUp = (Button) findViewById(R.id.btSgnUp);
        tvID = (EditText) findViewById(R.id.editText);
        mImageView = (ImageView) findViewById(R.id.imageView);

        btSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadSignUp();
            }
        });

        btDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tvID.getText().toString().isEmpty()) {
                    StartIntent();
                    sendPhoto(tvID.getText().toString());
                } else {
                    Toast.makeText(getApplicationContext(), "Enter A valid Driver Id", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });
    }

    private void sendPhoto(String id) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("messageType", "receivePhoto");
            obj.put("messageData", "");
            obj.put("DRIVER_ID", id);
            obj.put("PHOTO", imageEncoded);
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            String s = obj.toString();

            Log.d("SENDING DATA", s);

            mSockets.send_Message(s);
        }
    }

    private void loadSignUp() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        View v = getLayoutInflater().inflate(R.layout.dialog_login, null);

        WebView wv = (WebView) v.findViewById(R.id.wvLogin);
        EditText edit = (EditText) v.findViewById(R.id.edit);
        edit.setFocusable(true);
        edit.requestFocus();

        wv.loadUrl("http://128.199.138.206:7000/register");
        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        alert.setView(v);


        AlertDialog dialog = alert.create();

        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        dialog.show();
    }

    private void StartIntent() {
        Intent i = new Intent(this, Main2Activity.class);

        String message = tvID.getText().toString();
        i.putExtra(EXTRA_MESSAGE, message);
        startActivity(i);
        //finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            assert imageBitmap != null;
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

            byte[] b = baos.toByteArray();
            imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

            mImageView.setImageBitmap(imageBitmap);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
