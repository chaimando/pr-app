package com.prcalibradores.app.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.prcalibradores.app.R;
import com.prcalibradores.app.networking.RestClient;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class SearchPieceActivity extends AppCompatActivity implements OnErrorListener {

    private static final String EXTRA_MODEL_ID = "model_id";

    private PDFView mPDFView;
    private ProgressBar mProgressBar;

    public static Intent newIntent(Context packageContext, String modelId) {
        Intent intent = new Intent(packageContext, SearchPieceActivity.class);
        intent.putExtra(EXTRA_MODEL_ID, modelId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_piece);

        String modelId = getIntent().getStringExtra(EXTRA_MODEL_ID);

        mProgressBar = findViewById(R.id.progressBar2);
        mPDFView = findViewById(R.id.pdf);
        //mPDFView.fromAsset("CCA-Certificate-Java Level 1.pdf").load();
        //"http://server-die.alc.upv.es/asignaturas/PAEEES/2006-07/Tarjetas%20SD.pdf"
        new RetrievePDFStream().execute(RestClient.BASE_URL + "pdf.php?id=" + modelId);
    }

    @Override
    public void onError(Throwable t) {
        AlertDialog alertDialog = new AlertDialog.Builder(SearchPieceActivity.this).create();
        alertDialog.setTitle("PDF inexistente");
        alertDialog.setMessage("No se encontró ningún PDF con ese código QR.");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });
        alertDialog.show();
    }

    @SuppressLint("StaticFieldLeak")
    class RetrievePDFStream extends AsyncTask <String, Void, InputStream> {

        @Override
        protected InputStream doInBackground(String... strings) {
            InputStream inputStream = null;
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }
            } catch (IOException e) {
                return null;
            }
            return inputStream;
        }

        @Override
        protected void onPreExecute() {
        }
        @Override
        protected void onPostExecute(InputStream inputStream) {
            mPDFView.fromStream(inputStream).onError(SearchPieceActivity.this)
                    .load();
            mProgressBar.setVisibility(View.GONE);
        }
    }
}