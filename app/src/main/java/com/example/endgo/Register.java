package com.example.endgo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import java.io.ByteArrayOutputStream;

public class Register extends AppCompatActivity {

    EditText email, password, username;
    Button btnRegister;
    Button btnUpload;
    TextView tvLogin;
    FirebaseAuth fAuth;
    int PICK_IMAGE_REQUEST = 1;
    ImageView profileImage;
    StorageTask uploadTask;
    StorageReference storageRef;
    DatabaseReference databaseRef;
    FirebaseUser fUser;

    int CAMERA_PERMISSION = 1;
    int pictureOK;
    byte[] dataBaos;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        tvLogin = findViewById(R.id.tvLogin);
        btnRegister = findViewById(R.id.btnRegister);
        username = findViewById(R.id.username);
        fAuth = FirebaseAuth.getInstance();
        profileImage = findViewById(R.id.profileImage);
        btnUpload = findViewById(R.id.btnUpload);
        storageRef = FirebaseStorage.getInstance().getReference("uploads");
        databaseRef = FirebaseDatabase.getInstance().getReference();
        pictureOK = 0;

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askCameraPermission();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String e_mail =  email.getText().toString().trim();
                final String pwd = password.getText().toString().trim();
                final String usr = username.getText().toString().trim();

                if (TextUtils.isEmpty(e_mail)) {
                   email.setError("Email is required");
                } else if (TextUtils.isEmpty(usr)) {
                    username.setError("Username is required");
                } else if (usr.length() < 6) {
                    username.setError("Username must be at least 6 characters long");
                } else if (usr.length() > 24) {
                    username.setError("Username must be under 24 characters long");
                } else if (TextUtils.isEmpty(pwd)) {
                    password.setError("Password is required");
                } else if (pwd.length() < 6) {
                    password.setError("Password must be at least 6 characters long");
                } else if (pwd.length() > 24) {
                    password.setError("Password must be at most 24 characters long");
                } else if (pictureOK == 0) { //imgURI == null
                    Toast.makeText(Register.this, "No picture selected", Toast.LENGTH_SHORT).show();
                } else {

                    DatabaseReference usrnm = databaseRef.child("Users");
                    Query queries =  usrnm.orderByChild("name").equalTo(usr);
                    queries.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.exists()) {
                                fAuth.createUserWithEmailAndPassword(e_mail, pwd).addOnCompleteListener(
                                        new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(Register.this, "User Created",
                                                            Toast.LENGTH_SHORT).show();
                                                    fUser = FirebaseAuth.getInstance().getCurrentUser();

                                                    //photo storage
                                                    StorageReference fileReference = storageRef.child(fUser.getUid());

                                                    uploadTask = fileReference.putBytes(dataBaos)
                                                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                                @Override
                                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                                                    User user = new User(usr, e_mail,
                                                                            taskSnapshot.getMetadata().getReference().getDownloadUrl().toString(), 0);
                                                                    FirebaseDatabase.getInstance().getReference("Users")
                                                                            .child(FirebaseAuth.getInstance().getCurrentUser().
                                                                                    getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if (task.isSuccessful()) {
                                                                                Toast.makeText(Register.this, "DB working",
                                                                                        Toast.LENGTH_SHORT).show();
                                                                            } else {
                                                                                Toast.makeText(Register.this, "DB Error" +
                                                                                        task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        }
                                                                    });
                                                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                                                }
                                                            });
                                                } else {
                                                    Toast.makeText(Register.this, "Error" +
                                                            task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            } else {
                                username.setError("Username is already in use");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
             }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });

    }

    private void askCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, CAMERA_PERMISSION);
        } else {
            openCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(Register.this, "Camera permission required", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openCamera() {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST) {
            if (resultCode == RESULT_OK) {
                Bitmap image = (Bitmap) data.getExtras().get("data");
                profileImage.setImageBitmap(image);
                pictureOK = 1;
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                dataBaos = baos.toByteArray();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(Register.this, "Picture process cancelled", Toast.LENGTH_SHORT).show();
            }
        }
    }
}