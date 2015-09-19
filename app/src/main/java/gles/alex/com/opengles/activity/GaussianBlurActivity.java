package gles.alex.com.opengles.activity;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import gles.alex.com.opengles.R;
import gles.alex.com.opengles.renderer.GaussianBlurRenderer;
import gles.alex.com.opengles.ui.GaussianBlurView;
import gles.alex.com.opengles.ui.MainView;


public class GaussianBlurActivity extends ActionBarActivity {

    Button mButton = null;
    GaussianBlurView mMainView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gaussian_blur);
        mButton = (Button) findViewById(R.id.button_id);
        mMainView = (GaussianBlurView) findViewById(R.id.main_view);
        assert mMainView != null;
        String vs = getAssetsString("gaussianBlur/vertex_shader.fs");
        String fs = getAssetsString("gaussianBlur/fragment_shader.fs");
        assert vs != null;
        assert fs != null;
        mMainView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonClick();
            }
        });
    }

    private void onButtonClick() {
        ((GaussianBlurRenderer)mMainView.getRenderer()).changeControl();
    }

    private String getAssetsString(String fileName) {
        try {
            StringBuilder sb = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(getResources().getAssets().open(fileName)));
            String str;
            while ((str = br.readLine()) != null) {
                sb.append(str);
                sb.append("\n");
            }
            br.close();
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
