package gles.alex.com.opengles.activity;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;

import gles.alex.com.opengles.R;
import gles.alex.com.opengles.ui.MainView;

/**
 * Created by alex on 15-9-19.
 */
public class SixPointedStarActivity extends Activity {
    private MainView mMainView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_six_pointed_star);
        mMainView = (MainView) findViewById(R.id.waving_view);
        mMainView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }
}
