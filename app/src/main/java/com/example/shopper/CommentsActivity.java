package com.example.shopper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentsActivity extends AppCompatActivity {
    EditText addComment;
    ImageView post;
    CircleImageView imageProfile;
    private RecyclerView recyclerView;
    private CommentAdapter commentAdapter;
    private List<String> commentList;
    String postid;
    String publisherid;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        Toolbar toolbar = findViewById(R.id.commenttoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Comment");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        addComment = findViewById(R.id.add_comment);
        imageProfile = findViewById(R.id.image_profile);
        post = findViewById(R.id.post);
        recyclerView = findViewById(R.id.comments_recyclerview);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearlayoutManager = new LinearLayoutManager(this);
        linearlayoutManager.setOrientation(RecyclerView.VERTICAL);

        if (DBqueries.commentmodelList.size() ==0) {
            DBqueries.clearData();
            DBqueries.loadComments(CommentsActivity.this, postid,publisherid);
        }

        recyclerView.setLayoutManager(linearlayoutManager);
        commentList = new ArrayList<>();
        commentAdapter = new CommentAdapter(DBqueries.commentmodelList);
        recyclerView.setAdapter(commentAdapter);
        commentAdapter.notifyDataSetChanged();


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Intent intent = getIntent();
        postid = intent.getStringExtra("productId");
        publisherid = intent.getStringExtra("publisherId");
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addComment.getText().toString().equals("")) {
                    Toast.makeText(CommentsActivity.this, "You can't send empty comment!", Toast.LENGTH_SHORT).show();
                } else {
                    addcomment();
                }
            }
        });
//        readComments();
        getImage();

    }

    private void addcomment() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("Comment", addComment.getText().toString());
        hashMap.put("publisher", firebaseUser.getUid());

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Comments").document(postid).set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(CommentsActivity.this, "Commented!", Toast.LENGTH_SHORT).show();
            }
        });
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Comments").child(postid);
//
//        HashMap<String, Object> hashMap = new HashMap<>();
//        hashMap.put("Comment", addComment.getText().toString());
//        hashMap.put("publisher", firebaseUser.getUid());

//        reference.push().setValue(hashMap);
        addComment.setText("");
    }

    private void getImage() {
        FirebaseFirestore.getInstance().collection("USERS").document(publisherid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    String user = task.getResult().getString("profile");
                    Glide.with(getApplicationContext()).load(user).apply(new RequestOptions().placeholder(R.mipmap.ic_launcher_round)).into(imageProfile);
                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(CommentsActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

//    private void readComments() {
////        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Comments").child(postid);
//        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
//        firebaseFirestore.collection("Comments").document(postid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//
//                if (task.isSuccessful()) {
//                    if (commentList.size() > 0){
//                        String comment  = task.getResult().getString("Comment");
//                        commentList.add(comment);
//                    }
//
//
//                } else {
//                    String error = task.getException().getMessage();
//                    Toast.makeText(CommentsActivity.this, error, Toast.LENGTH_SHORT).show();
//
//                }
//            }
//        });

//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                commentList.clear();
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    Comment comment = snapshot.getValue(Comment.class);
//                    commentList.add(comment);
//                }
//                commentAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(CommentsActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
}