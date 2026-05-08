package ui.main;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.movenow.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import util.notifications.NotificationScheduler;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_POST_NOTIFICATIONS = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browser_menu);

        // ✅ Configurar navegación inferior
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);

        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(bottomNav, navController);

        // ✅ Pedir permiso para notificaciones (Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        REQUEST_POST_NOTIFICATIONS
                );
            }
        }

        // ✅ Nuevo sistema de notificaciones
        initNotifications();

        // ✅ Si se abrió desde una notificación → ir al HomeFragment
        if (getIntent().getBooleanExtra("ask_goal", false)) {
            if (navHostFragment != null) {
                NavController navController2 = navHostFragment.getNavController();
                navController2.navigate(R.id.homeFragment);
            }
        }
    }

    private void initNotifications() {

        // ✅ Leer preferencia del usuario
        SharedPreferences prefs = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        boolean enabled = prefs.getBoolean("notifications_enabled", true);

        if (enabled) {
            // ✅ Iniciar sistema unificado
            NotificationScheduler.startNotifications(this);
        } else {
            NotificationScheduler.stopNotifications(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_POST_NOTIFICATIONS) {
            // Puedes manejar el resultado si quieres
        }
    }
}
