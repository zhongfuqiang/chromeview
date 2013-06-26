// Copyright (c) 2013 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.chromium.android_webview;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Picture;

import org.chromium.base.CalledByNative;
import org.chromium.base.JNINamespace;
import org.chromium.content.common.TraceEvent;

/**
 * Provides auxiliary methods related to Picture objects and native SkPictures.
 */
@JNINamespace("android_webview")
public class JavaBrowserViewRendererHelper {

    
    private static Bitmap bitmap = null;
    private static int last_width = 0;
    private static int last_height = 0;
    
    /**
     * Provides a Bitmap object with a given width and height used for auxiliary rasterization.
     */
    @CalledByNative
    private static Bitmap createBitmap(int width, int height) {
        
      if(last_width != width || last_height != height) {
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        last_width = width;
        last_height = height;
      }
      return bitmap;
    }

    /**
     * Draws a provided bitmap into a canvas.
     * Used for convenience from the native side and other static helper methods.
     */
    @CalledByNative
    private static void drawBitmapIntoCanvas(Bitmap bitmap, Canvas canvas, int x, int y) {
        canvas.drawBitmap(bitmap, x, y, null);
    }

    /**
     * Creates a new Picture that records drawing a provided bitmap.
     * Will return an empty Picture if the Bitmap is null.
     */
    @CalledByNative
    private static Picture recordBitmapIntoPicture(Bitmap bitmap) {
        Picture picture = new Picture();
        if (bitmap != null) {
            Canvas recordingCanvas = picture.beginRecording(bitmap.getWidth(), bitmap.getHeight());
            drawBitmapIntoCanvas(bitmap, recordingCanvas, 0, 0);
            picture.endRecording();
        }
        return picture;
    }

    // Should never be instantiated.
    private JavaBrowserViewRendererHelper() {
    }
}
