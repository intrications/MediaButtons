package com.github.mediabuttons;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.RemoteViews;

public class ZipImageSource extends ButtonImageSource {
    Bitmap[] mBitmaps = new Bitmap[Configure.NUM_ACTIONS];
    Bitmap mPauseBitmap;
    
    String[] sFilenames = { "play.png", "fastforward.png", "rewind.png", "next.png", "previous.png",
            "pause.png" };
    
    ZipImageSource(String source) {
        try {
            FileInputStream is = new FileInputStream(new File(source));
            ZipInputStream zis = new ZipInputStream(new BufferedInputStream(is));
            ZipEntry ze;
            while ((ze = zis.getNextEntry()) != null) {
                String filename = ze.getName();
                Bitmap bitmap = BitmapFactory.decodeStream(zis);
                
                boolean found = false;
                for (int i = 0; i < mBitmaps.length; ++i) {
                    if (filename.equals(sFilenames[i])) {
                        mBitmaps[i] = bitmap;
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    Log.e(Widget.TAG, "Didn't recognize filename " + filename);
                }
            }
        } catch (FileNotFoundException e) {
            Log.e(Widget.TAG, "Failed to find file " + source);
        } catch (IOException e) {
            Log.e(Widget.TAG, "Error while reading " + source);
        }
    }
    
    @Override
    Bitmap getIcon(Context context, int actionIndex) {
        return mBitmaps[actionIndex];
    }

    @Override
    void setButtonIcon(RemoteViews view, int actionIndex) {
        Log.i(Widget.TAG, "Setting icon (zip)");
        view.setImageViewBitmap(R.id.button, mBitmaps[actionIndex]);
    }

}
