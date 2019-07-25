package com.zzhou.entrance.guard.module;

import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.view.SurfaceHolder;

import com.zzhou.entrance.guard.util.LogUtils;

import java.io.IOException;

public class SurfaceCallback  implements SurfaceHolder.Callback{

    // Variables
    private Camera mCamera = null;
    private PreviewCallback previewCallback;
    private boolean mPreviewRunning = false;
    private boolean mProcessing = false;
    private int mWidth = 0;
    private int mHeight = 0;

    SurfaceCallback(Camera camera,PreviewCallback previewCb){
        mCamera = camera;
        previewCallback = previewCb;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){
        LogUtils.d(">>>>>>>测试修改相机预览方式>>>>>>surfaceChanged");
        if(mPreviewRunning ){
            mCamera.stopPreview();
        }

        // Store width and height
        mWidth = width;
        mHeight = height;

        // Set camera parameters
        Camera.Parameters p = mCamera.getParameters();
        mCamera.setParameters(p);

        if(android.os.Build.VERSION.SDK_INT >= 8){
            // If API >= 8 -> rotate display...
            //mCamera.setDisplayOrientation(90);
        }

        try{
            mCamera.setPreviewCallback(previewCallback);
            mCamera.setPreviewDisplay(holder);
        } catch(IOException e){
            e.printStackTrace();
        }

        mCamera.startPreview();
        mPreviewRunning = true;

    }

    @Override
    public void surfaceCreated(final SurfaceHolder holder){
        LogUtils.d(">>>>>>>测试修改相机预览方式>>>>>>surfaceCreated");
        try {
            mCamera.setPreviewCallback(previewCallback);
            mCamera.setPreviewDisplay(holder);
            Camera.Parameters params = mCamera.getParameters();
            params.setPreviewSize(300,300);
            mCamera.setParameters(params);
        } catch (IOException e){
            mCamera.release();
            mCamera = null;
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder){
        LogUtils.d(">>>>>>>测试修改相机预览方式>>>>>>surfaceDestroyed");
        if(mCamera != null){
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
            mPreviewRunning = false;
            mCamera.release();
            mCamera = null;
        }
    }
}
