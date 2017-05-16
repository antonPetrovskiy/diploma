package tk.bugnotwolf.sharejack;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static long back_pressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public void toClient(View view) {
        Intent intent = new Intent(this, ClientActivity.class);
        startActivity(intent);
    }

    public void toServer(View view) {
        Intent intent = new Intent(this, ServerActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (back_pressed + 2000 > System.currentTimeMillis())
            this.finish();
        else
            Toast.makeText(getBaseContext(), "Press once again to exit!",
                Toast.LENGTH_SHORT).show();
        back_pressed = System.currentTimeMillis();
    }

}
