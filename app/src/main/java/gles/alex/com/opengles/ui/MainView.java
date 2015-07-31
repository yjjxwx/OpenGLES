package gles.alex.com.opengles.ui;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

/**
 * Created by alex on 15-6-25.
 */
public class MainView extends GLSurfaceView {

    public MainView(Context context) {
        super(context);
        setEGLContextClientVersion(2); // This is the important line
    }

    public MainView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setEGLContextClientVersion(2); // This is the important line
    }

}
