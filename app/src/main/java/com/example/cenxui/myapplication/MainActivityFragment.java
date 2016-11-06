package com.example.cenxui.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    private int mScreenDensity;
    private MediaProjectionManager mMediaProjectionManager;

    private static final int REQUEST_MEDIA_PROJECTION = 1;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        final View text = view.findViewById(R.id.text2);
        view.findViewById(R.id.text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bitmap bitmap = loadBitmap();

                File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);

                File file = new File(path.getPath(),"mail.png");
                if (file.exists() == false) {
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                try {
                    FileOutputStream fO = new FileOutputStream(file);
                    ByteArrayOutputStream bO = new ByteArrayOutputStream();

                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, bO);
                    fO.write(bO.toByteArray());
                    fO.flush();
                    fO.close();

                    text.setBackground(new BitmapDrawable(getResources(),
                            BitmapFactory.decodeStream(new FileInputStream(file))));

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                intent.setType("image/png");
                startActivity(Intent.createChooser(intent,"test"));
            }
        });
        return view;
    }

    private Bitmap loadBitmap() {
        View v = this.getView().findViewById(R.id.text);
        Bitmap b = Bitmap.createBitmap(v.getLayoutParams().width, v.getLayoutParams().height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);

        return b;
    }
}
