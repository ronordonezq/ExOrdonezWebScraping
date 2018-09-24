package com.example.ademuz.exordonezwebscraping;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    static ArrayList<Noticias> noticias;
    static ArrayList<String> contentURL;
    private ProgressBar pbNoticias;

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM", Locale.getDefault());
    Date date = new Date();

    String fecha = dateFormat.format(date);

    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Perú21: NOTICIAS " + fecha);

        pbNoticias = (ProgressBar)findViewById(R.id.pb_Noticias);

        noticias = new ArrayList<>();
        contentURL = new ArrayList<>();

        getNoticias();


        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.srlRefrescar);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getNoticias();
                swipeRefreshLayout.setRefreshing(true);
            }
        });

    }

    private void getNoticias() {

        new getNoticiasATask().execute();

    }

    private class getNoticiasATask extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {

            try{
                String url = "https://peru21.pe/archivo";
                Document document = Jsoup.connect(url).get();

                Elements horas  = document.select(".column-flows .flow-detail .flow-data");
                String[] mhoras = new String[200];
                int count = 0;

                for(Element hora : horas){
                    mhoras[count]=hora.text();
                    count++;
                }

                Elements titulos = document.select(".column-flows .flow-detail .flow-title");
                String[] mTitulo = new String[200];
                count = 0;
                for(Element titulo : titulos){
                    mTitulo[count]=titulo.text();
                    count++;
                }

                Elements contenidos  = document.select(".column-flows .flow-detail .flow-summary");
                String[] mContenido = new String[200];
                count = 0;
                for (Element contenido : contenidos){
                    mContenido[count]=contenido.text();
                    count++;
                }

                Elements contenidosLinks = document.select(".column-flows .flow-detail .flow-title .page-link");
                for(Element contenidosLink : contenidosLinks){
                    String contenidoUrl = "https://peru21.pe";
                    contenidoUrl+=contenidosLink.attr("href");
                    contentURL.add(contenidoUrl);
                }

                Elements imagenesLinks = document.select(".column-flows .flow-image picture source");
                ArrayList<Bitmap> bitmaps = new ArrayList<>();
                for(Element imagenesLink : imagenesLinks){
                    String imagenStringUrl = "https://peru21.pe";
                    imagenStringUrl+=imagenesLink.attr("srcset");
                    URL imagenUrl = new URL(imagenStringUrl);
                    HttpURLConnection connection = (HttpURLConnection) imagenUrl.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    bitmaps.add(BitmapFactory.decodeStream(input));
                }
                for(int i=0;i<count;i++){
                    noticias.add(new Noticias(mhoras[i],mTitulo[i],bitmaps.get(i),mContenido[i]));
                }
            }
            catch (Exception ex){
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void voids){
            RecyclerView rv = (RecyclerView)findViewById(R.id.rvNoticias);
            rv.setHasFixedSize(true);

            LinearLayoutManager llm = new LinearLayoutManager(getBaseContext());
            rv.setLayoutManager(llm);

            RVAdapter adapter = new RVAdapter(noticias, getBaseContext());
            rv.setAdapter(adapter);

            pbNoticias.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(false);
        }
    }
}
