package com.example.endgo;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.contentcapture.ContentCaptureManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    EditText email, password, username;
    Button btnRegister;
    Button btnUpload;
    TextView tvLogin;
    FirebaseAuth fAuth;
    int PICK_IMAGE_REQUEST = 1;
    Uri imgURI;
    ImageView profileImage;
    StorageTask uploadTask;
    StorageReference storageRef;
    DatabaseReference databaseRef;
    FirebaseUser fUser;

    //database variables
    String keyTitle = "title";
    String keyDescription = "description";
    FirebaseFirestore db = FirebaseFirestore.getInstance();

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
        databaseRef = FirebaseDatabase.getInstance().getReference("Users");


        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(i, PICK_IMAGE_REQUEST);
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
                } else if (TextUtils.isEmpty(pwd)) {
                    password.setError("Password is required");
                } else if (pwd.length() < 6) {
                    password.setError("Password must be at least 6 characters long");
                } else if (pwd.length() > 24) {
                    password.setError("Password must be under 24 characters long");
                } else if (imgURI == null) {
                    Toast.makeText(Register.this, "No picture selected", Toast.LENGTH_SHORT).show();
                } else {
                    fAuth.createUserWithEmailAndPassword(e_mail, pwd).addOnCompleteListener(
                            new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(Register.this, "User Created",
                                                Toast.LENGTH_SHORT).show();
                                        fUser = FirebaseAuth.getInstance().getCurrentUser();
                                        
                                        //photo storage
                                        StorageReference fileReference = storageRef.child(fUser.getUid()
                                                + "." + getFileExtension(imgURI));

                                        uploadTask = fileReference.putFile(imgURI)
                                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                    @Override
                                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                                        User user = new User(usr, e_mail,
                                                                taskSnapshot.getMetadata().getReference().getDownloadUrl().toString());
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            imgURI = data.getData();

            profileImage.setImageURI(imgURI);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
}