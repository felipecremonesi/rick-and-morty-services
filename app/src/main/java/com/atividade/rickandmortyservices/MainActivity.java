package com.atividade.rickandmortyservices;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;

import com.atividade.rickandmortyservices.modelo.Personagem;

public class MainActivity extends AppCompatActivity {

    private EditText nomePersonagem;
    private TextView dadosPersonagem;
    private ImageView imgPer;
    private ProgressDialog load;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nomePersonagem = findViewById(R.id.edNomePersonagem);
        dadosPersonagem = findViewById(R.id.txtDadosPersonagem);
        imgPer = findViewById(R.id.ivPersonagem);
    }

    public void pesquisar(View v) {
        new FetchPersonagemTask().execute(nomePersonagem.getText().toString().trim());
    }

    public class FetchPersonagemTask extends AsyncTask<String, Void, Personagem> {
        private static final String TAG = "FetchPersonagemTask";

        @Override
        protected void onPreExecute() {
            load = ProgressDialog.show(MainActivity.this, "Por favor Aguarde ...", "Procurando Dados ...");
        }

        @Override
        protected Personagem doInBackground(String... params) {
            String nomePersonagemBusca = params[0].toLowerCase();
            String urlString = "https://rickandmortyapi.com/api/character/?name="+nomePersonagemBusca;
            try {
                String jsonString = HttpUtils.get(urlString);
                JSONObject jsonObject = new JSONObject(jsonString);
                return new Personagem(jsonObject);
            } catch (Exception e) {
                Log.e(TAG, "Erro na requisição: ", e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(Personagem personagem) {
            load.dismiss();

            if (personagem != null) {
                Toast.makeText(MainActivity.this, personagem.getNome(), Toast.LENGTH_LONG).show();
                dadosPersonagem.setText("  Name: "+personagem.getNome() + "\n" + "  Status: "+personagem.getStatus() + "\n" + "  Species: "+personagem.getEspecie());
                if (personagem.getImage() != null) {
                    new DownloadImageTask(imgPer).execute(personagem.getImage());
                }
            } else {
                Toast.makeText(MainActivity.this, "Nenhum personagem encontrado!", Toast.LENGTH_LONG).show();
                dadosPersonagem.setText("");
                imgPer.setImageDrawable(null);
            }
        }
    }

    private static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        private final ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}