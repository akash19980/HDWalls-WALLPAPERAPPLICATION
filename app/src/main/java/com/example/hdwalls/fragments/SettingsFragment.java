package com.example.hdwalls.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.hdwalls.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class SettingsFragment extends Fragment {
    private static final int Google_signin=21;
    private GoogleSignInClient mGoogleSignin;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
   if(FirebaseAuth.getInstance().getCurrentUser()==null)
   {
       return inflater.inflate(R.layout.fragment_settings_default,container,false);
   }

        return inflater.inflate(R.layout.fragment_settings_logged_in,container,false);



    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        GoogleSignInOptions googleSignInOptions= new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .build();

        mGoogleSignin= GoogleSignIn.getClient(getActivity(),googleSignInOptions);
        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            ImageView userimage= view.findViewById(R.id.image_view);
            TextView name= view.findViewById(R.id.text_view_name);
            TextView email=view.findViewById(R.id.text_view_email);

            Glide.with(getActivity()).load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl())
                    .into(userimage);
            name.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
            email.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

            view.findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseAuth.getInstance().signOut();
                    mGoogleSignin.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            ;
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.content_area,new SettingsFragment()).commit();
                        }
                    });
                }
            });
        }
        else
        {
            view.findViewById(R.id.googlesignin).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent= mGoogleSignin.getSignInIntent();
                    startActivityForResult(intent,Google_signin);
                }
            });
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Google_signin) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthwithGoogle(account);
            }
            catch (ApiException e){
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthwithGoogle(GoogleSignInAccount account) {
        FirebaseAuth Auth= FirebaseAuth.getInstance();
        AuthCredential credential= GoogleAuthProvider.getCredential(account.getIdToken(),null);
        Auth.signInWithCredential(credential).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {

                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content_area,new SettingsFragment()).commit();
                    Toast.makeText(getActivity(), "Login Successful", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
