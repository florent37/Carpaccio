package com.github.florent37.badview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import static com.github.florent37.badview.BadViewHelper.construct;

/**
 * Created by florentchampigny on 21/07/15.
 */
public class BadView extends FrameLayout {

    List<View> badViews = new ArrayList<>();
    List<Object> registerObjects = new ArrayList<>();

    protected void findBadViews(View view){
        if(view.getTag() != null && !view.getTag().toString().isEmpty())
            badViews.add(view);

        if(view instanceof ViewGroup) {
            ViewGroup viewGroup = ViewGroup.class.cast(view);
            for (int i = 0; i < viewGroup.getChildCount(); ++i)
                findBadViews(viewGroup.getChildAt(i));
        }
    }

    protected void handleAttributes(Context context, AttributeSet attrs){
        if (isInEditMode()) {
            return;
        }
        TypedArray styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.BadView);

        {
            String register = styledAttrs.getString(R.styleable.BadView_register);
            if(register != null){
                String[] registers = register.split(";");
                for(String s : registers) {
                    String reg = s.trim().replace(";","");
                    registerObjects.add(construct(reg));
                }
            }

        }

        styledAttrs.recycle();

    }

    public BadView(Context context) {
        super(context);
    }

    public BadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        handleAttributes(context, attrs);
    }

    public BadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        handleAttributes(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        findBadViews(this);

        for(View view : badViews){
            String tag = view.getTag().toString().trim();
            String[] calls = tag.split(";");
            for(String call : calls) {
                String function = BadViewHelper.getFunctionName(call);
                String[] args = BadViewHelper.getAttributes(tag);
                for (Object registerObject : registerObjects)
                    BadViewHelper.callFunction(registerObject, function, view, args);
            }
        }
    }
}
