package com.yan.xposeddemo;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;

public class TestView  extends View {
    public TestView(Context context) {
        super(context);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
//        getContext().getDrawable(R.drawable.post_4).draw(null);
    }
}
