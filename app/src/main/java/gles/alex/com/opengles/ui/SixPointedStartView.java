package gles.alex.com.opengles.ui;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import gles.alex.com.opengles.renderer.SixPointedStarRenderer;

/**
 * Created by alex on 15-9-19.
 */
public class SixPointedStartView extends MainView {

    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    private float mPreviousX;
    private float mPreviousY;
    private SixPointedStarRenderer mRenderer;
    public SixPointedStartView(Context context) {
        super(context);
    }

    public SixPointedStartView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    public Renderer getRenderer() {
        if (mRenderer == null) {
            mRenderer = new SixPointedStarRenderer(getContext());
        }
        return mRenderer;
    }
}
