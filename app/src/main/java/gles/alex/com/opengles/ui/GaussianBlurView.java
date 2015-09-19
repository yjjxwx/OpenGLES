package gles.alex.com.opengles.ui;

import android.content.Context;
import android.util.AttributeSet;

import gles.alex.com.opengles.renderer.GaussianBlurRenderer;
import gles.alex.com.opengles.util.ShaderUtil;

/**
 * Created by alex on 15-9-19.
 */
public class GaussianBlurView extends MainView {

    GaussianBlurRenderer mMainViewRenderer;
    public GaussianBlurView(Context context) {
        super(context);
    }

    public GaussianBlurView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public Renderer getRenderer() {
        if (mMainViewRenderer == null) {
            String vs = ShaderUtil.loadFromAssetsFile("gaussianBlur/vertex_shader.fs", getContext().getResources());
            String fs = ShaderUtil.loadFromAssetsFile("gaussianBlur/fragment_shader.fs", getContext().getResources());
            mMainViewRenderer = new GaussianBlurRenderer(getContext(), vs, fs);
        }
        return mMainViewRenderer;
    }
}
