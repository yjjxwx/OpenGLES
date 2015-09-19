package gles.alex.com.opengles;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.opengl.GLSurfaceView;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import gles.alex.com.opengles.renderer.MainViewRenderer;
import gles.alex.com.opengles.ui.MainView;


public class MainActivity extends ActionBarActivity {

    Button mButton = null;
    MainView mMainView;
    MainViewRenderer mMainViewRenderer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mButton = (Button) findViewById(R.id.button_id);
        mMainView = (MainView) findViewById(R.id.main_view);
        assert mMainView != null;
        String vs = getAssetsString("vertex_shader.fs");
        String fs = getAssetsString("fragment_shader.fs");
        assert vs != null;
        assert fs != null;
        mMainViewRenderer = new MainViewRenderer(this, vs, fs);
        mMainView.setRenderer(mMainViewRenderer);
        mMainView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonClick();
            }
        });
    }

    private void onButtonClick() {
        mMainViewRenderer.changeControl();
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
