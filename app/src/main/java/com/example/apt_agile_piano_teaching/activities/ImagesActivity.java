package com.example.apt_agile_piano_teaching.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.apt_agile_piano_teaching.R;
import com.example.apt_agile_piano_teaching.databinding.ActivityImagesBinding;
import com.example.apt_agile_piano_teaching.databinding.ActivityLessonsBinding;
import com.example.apt_agile_piano_teaching.models.ImageUpload;
import com.example.apt_agile_piano_teaching.utils.Preference;
import com.example.apt_agile_piano_teaching.utils.Settings;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

public class ImagesActivity extends AppCompatActivity {

    private final static int PICK_IMAGE_REQUEST = 1;
    private ActivityImagesBinding binding;
    private Uri mImageUri;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private FirebaseFirestore mFirestoreRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityImagesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mStorageRef = FirebaseStorage.getInstance().getReference("templates");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        mFirestoreRef = FirebaseFirestore.getInstance();

        Settings settings = new Settings(this, Preference.IMAGE_TEMPLATE_PREFERENCE);
        if (settings.getPreference()) {
            Glide.with(this).load(FirebaseStorage.getInstance()
                    .getReference("templates/" + FirebaseAuth.getInstance().getCurrentUser().getEmail() + "_template.jpg")).error(FirebaseStorage
                    .getInstance().getReference("templates/piano.jpg")).into(binding.imageView);
        } else {
            Glide.with(this).load(FirebaseStorage.getInstance().getReference("templates/piano.jpg")).into(binding.imageView);
        }

        binding.btnChooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        binding.btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK &&
                data != null && data.getData() != null) {

            mImageUri = data.getData();

            Picasso.get().load(mImageUri).into(binding.imageView);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = ImagesActivity.this.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadImage() {
        if (mImageUri != null) {
            StorageReference fileReference = mStorageRef.child(FirebaseAuth.getInstance().getCurrentUser().getEmail() + "_template." + getFileExtension(mImageUri));

            fileReference.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(ImagesActivity.this, "Upload successful", Toast.LENGTH_SHORT).show();
                    mStorageRef.getFile(new File(fileReference.toString())).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(ImagesActivity.this, "successo!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ImagesActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(ImagesActivity.this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

}