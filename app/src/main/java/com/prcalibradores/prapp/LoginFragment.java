package com.prcalibradores.prapp;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.prcalibradores.prapp.model.User;
import com.prcalibradores.prapp.model.UsersLab;
import com.prcalibradores.prapp.networking.RestClient;
import com.prcalibradores.prapp.networking.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginFragment extends Fragment {

    private static final String DIALOG_BAD_CREDENTIALS = "dialog_bad_credentials";
    private TextInputEditText mUsernameEditText;
    private TextInputEditText mPasswordEditText;
    private RestClient mRestClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRestClient = new RestClient(getActivity().getApplicationContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        mUsernameEditText = view.findViewById(R.id.login_username_edit_text);
        mPasswordEditText = view.findViewById(R.id.login_password_edit_text);

        final Switch switchRememberMe = view.findViewById(R.id.login_switch_remember_me);
        final ConstraintLayout progressLayout = view.findViewById(R.id.progress_layout);

        MaterialButton loginButton = view.findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Editable usernameInput = mUsernameEditText.getText();
                Editable passwordInput = mPasswordEditText.getText();
                final boolean saveSession = switchRememberMe.isChecked();
                progressLayout.setVisibility(View.VISIBLE);

                mRestClient.validateUserCredentials(usernameInput.toString(), passwordInput.toString(),
                        new RestClient.Callback() {
                    @Override
                    public void onSuccess(JSONArray result) throws JSONException {
                        progressLayout.setVisibility(View.INVISIBLE);

                        JSONObject resultObject = result.getJSONObject(0);

                        saveSession(resultObject, saveSession);

                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        getActivity().startActivity(intent);
                        getActivity().finish();
                    }

                    @Override
                    public void onFailure(String response) {
                        progressLayout.setVisibility(View.INVISIBLE);
                        DialogBadCredentials dialog = new DialogBadCredentials();
                        dialog.show(getFragmentManager(), DIALOG_BAD_CREDENTIALS);
                    }
                });
            }
        });

        return view;
    }

    private void saveSession(JSONObject result, boolean saveSession) {
        User user = Utils.getUserFromJson(result);
        if (user != null) {
            user.setSaveSession(saveSession);
        }
        UsersLab.get(getActivity().getApplicationContext()).saveUser(user);
        mRestClient.addUserCookies(user);
    }

    public static class DialogBadCredentials extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.login_bad_credentials_title)
                    .setMessage(R.string.login_bad_credentials_message)
                    .setCancelable(true)
                    .setPositiveButton(android.R.string.ok,null)
                    .create();
        }
    }

}