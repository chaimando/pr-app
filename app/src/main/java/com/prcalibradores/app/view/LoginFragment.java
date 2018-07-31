package com.prcalibradores.app.view;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.loopj.android.http.PersistentCookieStore;
import com.prcalibradores.app.R;
import com.prcalibradores.app.model.User;
import com.prcalibradores.app.model.UsersLab;
import com.prcalibradores.app.networking.RestClient;
import com.prcalibradores.app.networking.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import cz.msebera.android.httpclient.impl.cookie.BasicClientCookie;

public class LoginFragment extends Fragment {

    private static final String DIALOG_BAD_CREDENTIALS = "dialog_bad_credentials";
    private static final String TAG = "LoginFragment";
    private TextInputEditText mUsernameEditText;
    private TextInputEditText mPasswordEditText;
    private RestClient mRestClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRestClient = new RestClient();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        mUsernameEditText = view.findViewById(R.id.login_username_edit_text);
        mPasswordEditText = view.findViewById(R.id.login_password_edit_text);

        final ConstraintLayout progressLayout = view.findViewById(R.id.progress_layout);

        MaterialButton loginButton = view.findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            Editable usernameInput = mUsernameEditText.getText();
            Editable passwordInput = mPasswordEditText.getText();
            progressLayout.setVisibility(View.VISIBLE);

            mRestClient.validateUserCredentials(usernameInput.toString(), passwordInput.toString(),
                    new RestClient.Callback() {
                @Override
                public void onSuccess(JSONArray result) throws JSONException {
                    progressLayout.setVisibility(View.INVISIBLE);

                    JSONObject resultObject = result.getJSONObject(0);

                    saveSession(resultObject);

                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    Objects.requireNonNull(getActivity()).startActivity(intent);
                    getActivity().finish();
                }

                @Override
                public void onSuccess(JSONObject result) {
                    progressLayout.setVisibility(View.INVISIBLE);

                    saveSession(result);

                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    Objects.requireNonNull(getActivity()).startActivity(intent);
                    getActivity().finish();
                }

                @Override
                public void onFailure(String response) {
                    progressLayout.setVisibility(View.INVISIBLE);
                    DialogBadCredentials dialog = new DialogBadCredentials();
                    if (getFragmentManager() != null) {
                        dialog.show(getFragmentManager(), DIALOG_BAD_CREDENTIALS);
                    }
                }
            });
            }
        });

        return view;
    }

    private void saveSession(JSONObject result) {
        User user = Utils.getUserFromJson(result);
        if (user != null) {
            UsersLab.get(getActivity()).saveUser(user);
            PersistentCookieStore cookieStore = Utils.getCookieStore(getActivity());
            BasicClientCookie clientCookie = Utils.getNewCookie("local_id", user.getUUID().toString());
            cookieStore.addCookie(clientCookie);
            Log.d(TAG, "saveSession: User data saved: " + user.toString());
            Log.d(TAG, "saveSession: Cookie saved: " + cookieStore.getCookies().get(0));
        }
    }

    public static class DialogBadCredentials extends DialogFragment {
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new AlertDialog.Builder(Objects.requireNonNull(getActivity()))
                    .setTitle(R.string.login_bad_credentials_title)
                    .setMessage(R.string.login_bad_credentials_message)
                    .setCancelable(true)
                    .setPositiveButton(android.R.string.ok,null)
                    .create();
        }
    }

}
