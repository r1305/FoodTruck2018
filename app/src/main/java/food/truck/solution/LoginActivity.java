package food.truck.solution;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    String TAG = "console";
    public static int MY_PERMISSIONS_REQUEST_ACCESS= 1;
    DatabaseReference myRef;
    EditText email;
    String txt_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users");
        checkPermissions();

        email=findViewById(R.id.login_email);


        Button btn_login=findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_email=email.getText().toString();
                if(txt_email!=""){

                    addNewVUser(txt_email);
                }
                Intent i=new Intent(LoginActivity.this,HomeActivity.class);
                startActivity(i);
            }
        });
    }

    public void addNewVUser(String value){
        myRef.push().child("email").setValue(value);
    }

    private void checkPermissions(){
        if (
                ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED
                        &&
                        ActivityCompat.checkSelfPermission(this,
                                android.Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED
                )
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.INTERNET,
                            Manifest.permission.CAMERA
                    },
                    MY_PERMISSIONS_REQUEST_ACCESS);
        }else{
        }
    }
}
