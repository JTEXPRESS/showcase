package com.jnt.showcase;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.jnt.guide.GuideData;
import com.jnt.guide.GuideType;
import com.jnt.guide.GuideView;

public class MainActivity extends AppCompatActivity {
    private Button myButton;
    private Button myButton1;
    private Button myButton2;
    private Button myButton3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myButton    = findViewById(R.id.button);
        myButton1   = findViewById(R.id.button1);
        myButton2   = findViewById(R.id.button2);
        myButton3   = findViewById(R.id.button3);

        myButton.setOnClickListener(v-> tooltip());
        myButton1.setOnClickListener(v-> showcase());
        myButton2.setOnClickListener(v-> showcase());
        myButton3.setOnClickListener(v-> tooltip1());
    }

    private void showcase() {
        GuideData guideData = new GuideData();

        guideData.add(myButton1, "SHOWCASE", "where ‘library’ is the name of your library module.", true);
        guideData.add(myButton2, null, "If everything went well in the previous step, your library is ready to be released! Create a GitHub release or add a git tag and you’re done!");
        guideData.add(myButton3, "", "If you add a sample app to the same repo then your app needs to depend on the library. To do this in your app/build.gradle add a dependency in the form");

        new GuideView.Builder(this)
                .recent("new")
                .data(guideData)
                .type(GuideType.POPOVER)
                .previous("Back")
                .previousTextColor(Color.WHITE)
                .next("Selanjutnya")
                .nextBackgroundColor(Color.BLACK)
                .nextTextColor(Color.WHITE)
                .backgroundColor(Color.WHITE).build();
    }

    private void tooltip() {
        GuideData guideData = new GuideData();

        guideData.add(myButton, "Example projects");

        new GuideView.Builder(this)
                .data(guideData)
                .type(GuideType.TOOLTIP)
                .contentTextColor(Color.WHITE)
                .backgroundColor(Color.BLACK).build();
    }

    private void tooltip1() {
        GuideData guideData = new GuideData();

        guideData.add(myButton3, "Android projects");

        new GuideView.Builder(this)
                .data(guideData)
                .type(GuideType.TOOLTIP)
                .contentTextColor(Color.WHITE)
                .backgroundColor(Color.BLACK).build();
    }
}
