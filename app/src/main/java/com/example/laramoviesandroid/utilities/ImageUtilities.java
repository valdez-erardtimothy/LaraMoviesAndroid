package com.example.laramoviesandroid.utilities;

import android.graphics.Bitmap;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class ImageUtilities {

    public static String bmpToBase64(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage =Base64.encodeToString(imageBytes, Base64.NO_WRAP);
        return encodedImage;
    }
}
