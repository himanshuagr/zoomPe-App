package com.example.zoompe;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

public class home extends AppCompatActivity {

    androidx.appcompat.widget.Toolbar toolbar;
    BottomNavigationView navigationView;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();
        toolbar =findViewById(R.id.mytoolbar);
        navigationView = findViewById(R.id.bottom_navigation);
        toolbar.inflateMenu(R.menu.main_menu);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new homeFragment()).commit();
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId()==R.id.logout)
                {
                    SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("Mypref",MODE_PRIVATE);
                    SharedPreferences.Editor edit=sharedPreferences.edit();
                    edit.clear();
                    edit.commit();
                    Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                }
                return false;
            }
        });
       navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
           @Override
           public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
               Fragment fragment = null;
                switch (item.getItemId())
                {
                    case R.id.home:
                        fragment = new homeFragment();
                        toolbar.setTitle("Dashboard");
                        toolbar.setLogo(R.drawable.ic_baseline_home_24_w);
                        break;
                    case R.id.wallet:
                        fragment = new walletFragment();
                        toolbar.setTitle("Wallet");
                        toolbar.setLogo(R.drawable.ic_baseline_account_balance_wallet_24_w);
                        break;
                    case R.id.qr:
                         fragment = new qrcodeFragment();
                         toolbar.setTitle("Scan and Pay");
                         toolbar.setLogo(R.drawable.ic_baseline_arrow_back_24);
                         break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
                return true;

           }
       });

    }
    int backButtonCount=0;
    public void onBackPressed()
    {
        if(backButtonCount >= 1)
        {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this, "Press the back button once again to close the application.", Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }
    }

}