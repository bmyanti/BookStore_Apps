package activity;

import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.zxcvbn.bookstore.R;

public class activity_start extends AppCompatActivity {
    Intent intent1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_start);


        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        Button button = (Button) findViewById(R.id.button);

        /*Intent intent = new Intent(this, activity_end.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(this, imageView, "taylor");
            startActivity(intent, options.toBundle());
        }
        else {
            startActivity(intent);
        }*/
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
                View imageView = findViewById(R.id.imageView);
                View textView = findViewById(R.id.textView);
                View button = findViewById(R.id.button);

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    textView.setTransitionName("textView");
                    button.setTransitionName("button");

                    intent1 = new Intent(activity_start.this, activity_end.class);
                    Pair<View, String> pair1 = Pair.create(imageView, imageView.getTransitionName());
                    Pair<View, String> pair2 = Pair.create(textView, textView.getTransitionName());
                    Pair<View, String> pair3 = Pair.create(button, button.getTransitionName());
                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation(activity_start.this, pair1, pair2, pair3);
                    startActivity(intent1, options.toBundle());
                }
                else {
                    startActivity(intent1);
                }
            }
        });
    }
}
