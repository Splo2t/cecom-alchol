package com.splo2t.alchol.activity;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.util.Consumer;
import androidx.window.java.layout.WindowInfoTrackerCallbackAdapter;
import androidx.window.layout.DisplayFeature;
import androidx.window.layout.FoldingFeature;
import androidx.window.layout.WindowInfoTracker;
import androidx.window.layout.WindowLayoutInfo;
import androidx.window.layout.WindowMetrics;
import androidx.window.layout.WindowMetricsCalculator;

import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;

import java.util.List;
import java.util.concurrent.Executor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.internal.ContextUtils;
import com.splo2t.alchol.model.RedButton;
import com.splo2t.alchol.R;

/*
* https://developer.android.com/jetpack/androidx/releases/window#window-1.0.0-beta02
* */

public class RatioActivity extends AppCompatActivity {
    String TAG = "JWM";
    LayoutStateChangeCallback layoutStateChangeCallback = new LayoutStateChangeCallback();
    WindowInfoTrackerCallbackAdapter wit;
    WindowMetrics wm;
    ConstraintLayout root;
    TextView outputText;
    WindowMetricsCalculator wmc;
    RedButton redButton;
    Button btn;
    Button secondBtn;
    EditText editTextM;
    EditText editTextN;
    float m_2(Rect[] rects, int m, int n, RedButton redButton){
        float alpha = Math.abs((rects[1].centerY()-rects[3].centerY()));
        float a = Math.abs(rects[0].centerX()-rects[1].centerX());
        float b = Math.abs(rects[2].centerX()-rects[3].centerX());
        float c = (float) Math.cbrt(n*1.0/(m+n)*a*a*a + m*1.0/(m+n)*b*b*b);
        float h = (a-c)/(a-b)*alpha;
        float a_m = Math.abs(rects[0].centerX()-rects[1].centerX());
        float b_m = Math.abs(rects[2].centerX()-rects[3].centerX());
        a_m = 0.262f * h * (c * c + c * a + a * a);
        b_m = 0.262f * (alpha - h) * (c * c + c * b + b * b);
        Log.d("alpha: ", String.valueOf(alpha));
        Log.d("h: ", String.valueOf(h));
        Log.d("c: ", String.valueOf(c));
        Log.d("a_m: ", String.valueOf(a_m));
        Log.d("b)m: ", String.valueOf(b_m));
        redButton.drawMyLine((540.0f)-c/2, rects[0].centerY()+h, (540.0f)+c/2, rects[0].centerY()+h);
        redButton.invalidate();

        return 0.0f;
    }
    /*
    float[] m_2(Rect[] rects, int m, int n, RedButton redButton){
        float tanTheta = (Math.abs(rects[0].centerY()-rects[1].centerY()))/((Math.abs(rects[1].centerX()-rects[0].centerX())-Math.abs(rects[3].centerX()-rects[2].centerX()))/2);
        float alpha = Math.abs((rects[1].centerY()-rects[3].centerY()));
        float h = alpha/2;
        float a = Math.abs(rects[0].centerX()-rects[1].centerX());
        float b = Math.abs(rects[2].centerX()-rects[3].centerX());
        float c = 0;
        float a_m;
        float b_m;

        Log.d("alpha: ", String.valueOf(alpha));
        Log.d("h: ", String.valueOf(h));
        Log.d("tanTheta: ", String.valueOf(tanTheta));
        Log.d("a: ", String.valueOf(alpha));
        Log.d("b: ", String.valueOf(b));

        for(int i = 0; i < 10000; i++) {

            if(a > b) {
                c = a - 2 * h / tanTheta;
                Log.d("c: ",String.valueOf(c));
                a_m = 0.262f * h * (c * c + c * a + a * a);
                b_m = 0.262f * (alpha - h) * (c * c + c * b + b * b);
                if (Math.abs(n / m - b_m / a_m) < 0.017) {
                    break;
                } else if (n / m > b_m / a_m) {
                    h = h - 0.1f;
                } else {
                    h = h + 0.1f;
                }

                redButton.drawMyLine((540.0f)-200, rects[0].centerY()-h, (540.0f)+200, rects[0].centerY()-h);
                redButton.invalidate();
            }
        }
        float[] returnFloat = new float[2];
        returnFloat[0] = rects[0].centerY()-h;
        returnFloat[1] = c;
        return returnFloat;
    }

     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ratio);
        root = findViewById(R.id.root);
        outputText = findViewById(R.id.outputText);
        outputText.setVisibility(View.GONE);
        redButton = new RedButton(this);
        root.addView(redButton);
        btn = findViewById(R.id.button);
        secondBtn = findViewById(R.id.button2);
        editTextM = findViewById(R.id.editText_m);
        editTextN = findViewById(R.id.editText_n);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redButton.reset();
                redButton.invalidate();
                Toast.makeText(getApplicationContext(),"초기화",Toast.LENGTH_SHORT).show();
            }
        });

        secondBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    redButton.m = Integer.parseInt(editTextM.getText().toString());
                    redButton.n = Integer.parseInt(editTextN.getText().toString());
                    redButton.refresh();
                    redButton.invalidate();
                }catch (NumberFormatException e) {
                    Toast.makeText(getApplicationContext(),"비율 값에 숫자를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });
        ConstraintLayout.LayoutParams layoutParams =
                new ConstraintLayout.LayoutParams(
                        ConstraintLayout.LayoutParams.MATCH_CONSTRAINT, ConstraintLayout.LayoutParams.MATCH_CONSTRAINT
                );

        layoutParams.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
        layoutParams.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID;
        layoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        layoutParams.bottomToTop = R.id.guideline2;

        redButton.setLayoutParams(layoutParams);

        Log.d(TAG, "onCreate callback adapter");
        wit = new WindowInfoTrackerCallbackAdapter(
                WindowInfoTracker.Companion.getOrCreate(
                        this
                )
        );
        wmc = WindowMetricsCalculator.Companion.getOrCreate();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // WindowInfoRepository listener
        Log.d(TAG, "onStart add listener");
        wit.addWindowLayoutInfoListener(
                this,
                runOnUiThreadExecutor(),
                layoutStateChangeCallback);
    }

    @Override
    protected void onStop() {
        super.onStop();
        wit.removeWindowLayoutInfoListener(layoutStateChangeCallback);
    }

    void updateLayout(WindowLayoutInfo windowLayoutInfo)
    {
        // WindowMetrics synchronous call
        wm = wmc.computeCurrentWindowMetrics(this);
        String currentMetrics = wm.getBounds().toString();
        wm = wmc.computeMaximumWindowMetrics(this);
        String maxMetrics = wm.getBounds().toString();
        String outMetrics = "\n\n";
        outMetrics += "Current Window Metrics " + currentMetrics;
        outMetrics += "\n";
        outMetrics += "Maximum Window Metrics " + maxMetrics;

        // WindowInfoRepository DisplayFeatures
        List<DisplayFeature> displayFeatures = windowLayoutInfo.getDisplayFeatures();
        if (displayFeatures.isEmpty()) {
            String out = getString(R.string.no_features);
            out += outMetrics;
            outputText.setText(out);
            return;
        }
        else {
            // update screen
            Log.d(TAG, "window layout contains display feature/s");
            String finalOutMetrics = outMetrics;
            displayFeatures.forEach(displayFeature -> {
                FoldingFeature foldingFeature = (FoldingFeature)displayFeature;
                if (foldingFeature != null)
                {   // only set if it's a fold, not other feature type. only works for single-fold devices.
                    String out = "";
                    if (foldingFeature.getOrientation() == FoldingFeature.Orientation.HORIZONTAL) {
                        out += getString(R.string.hinge_is_horizontal);
                    } else {
                        out += getString(R.string.hinge_is_vertical);
                    }
                    out += "\n";
                    out += "State is " + foldingFeature.getState().toString();
                    out += "\n";
                    out += "OcclusionType is " + foldingFeature.getOcclusionType().toString();
                    out += "\n";
                    out += "isSeparating is " + foldingFeature.isSeparating();
                    out += "\n";
                    out += "Bounds are " + foldingFeature.getBounds().toString();
                    out += finalOutMetrics;

                    ConstraintLayout.LayoutParams layoutParams =
                            new ConstraintLayout.LayoutParams(
                                    ConstraintLayout.LayoutParams.MATCH_CONSTRAINT, ConstraintLayout.LayoutParams.MATCH_CONSTRAINT
                            );

                    if(foldingFeature.getState().toString() == "FLAT"){
                        layoutParams.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
                        layoutParams.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID;
                        layoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
                        layoutParams.bottomToTop = R.id.ButtonLayout;
                    }
                    else{
                        layoutParams.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
                        layoutParams.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID;
                        layoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
                        layoutParams.bottomToBottom = R.id.guideline2;



                    }

                    redButton.setLayoutParams(layoutParams);
                    outputText.setText(out);



                    return; // only works for one hinge/fold!
                }
            });
        }
    }

    class LayoutStateChangeCallback implements Consumer<WindowLayoutInfo> {
        @Override
        public void accept(WindowLayoutInfo windowLayoutInfo) {
            updateLayout(windowLayoutInfo);
        }
    }

    Executor runOnUiThreadExecutor()
    {
        return new MyExecutor();
    }
    class MyExecutor implements Executor
    {
        Handler handler = new Handler(Looper.getMainLooper());
        @Override
        public void execute(Runnable command) {
            handler.post(command);
        }
    }
}