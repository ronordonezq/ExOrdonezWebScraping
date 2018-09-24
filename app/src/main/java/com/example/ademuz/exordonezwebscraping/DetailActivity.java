package com.example.ademuz.exordonezwebscraping;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DetailActivity extends AppCompatActivity {

    TextView tvTitle, tvContent;
    ImageView ivImagen;
    ProgressBar pbDetalle;

    String title, content;
    Bitmap imagen;

    TextView tvCompartir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tvTitle=(TextView)findViewById(R.id.txt_title_detail);
        tvContent=(TextView)findViewById(R.id.txt_content_detail);
        ivImagen=(ImageView)findViewById(R.id.iv_image_detail);
        pbDetalle=(ProgressBar)findViewById(R.id.pb_detail);

        tvCompartir=(TextView)findViewById(R.id.txt_compartir);


        final int pos=getIntent().getIntExtra("EXTRA_POSITION",0);
        title=MainActivity.noticias.get(pos).titulo;
        imagen=MainActivity.noticias.get(pos).imagen;
        content="";
        String noticia="NOTICIA " +(pos+1);
        setTitle(noticia);

        String contentUrl=MainActivity.contentURL.get(pos);
        getNewsContent(contentUrl);

        tvCompartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, MainActivity.contentURL.get(pos));
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });
    }
    private void getNewsContent(String contentUrl) {
        new GetNewsContentATask().execute(contentUrl);
    }
    private class GetNewsContentATask extends AsyncTask<String,Void,Void>{

        @Override
        protected Void doInBackground(String... strings) {
            try {
                Document document = Jsoup.connect(strings[0]).get();
                Elements photos = document.select(".image img");
                for(Element photoDetail : photos){
                    String photoStringUrl = photoDetail.attr("src");
                    URL photoUrl = new URL(photoStringUrl);
                    HttpURLConnection connection = (HttpURLConnection) photoUrl.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    imagen= BitmapFactory.decodeStream(input);
                }
                Elements contenidos = document.select(".news-text-content p");
                for(Element contenido : contenidos) {
                    content += contenido.text() + "\n";
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void voids){
            tvTitle.setText(title);
            ivImagen.setImageBitmap(imagen);
            tvContent.setText(content);
            pbDetalle.setVisibility(View.GONE);
        }
    }


}
