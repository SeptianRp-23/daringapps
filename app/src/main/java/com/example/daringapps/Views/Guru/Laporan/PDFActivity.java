package com.example.daringapps.Views.Guru.Laporan;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.daringapps.R;

import java.io.File;
import java.io.IOException;

public class PDFActivity extends AppCompatActivity {

    WebView webView;
    private ProgressBar progressBar;
    @SuppressLint("SetJavaScriptEnabled")
    String GET, NAME;
    private long enqueue;
    private DownloadManager dm;
    TextView txtUrl, txtName;
    Button Download;
    private static final int PERMISSION_STORAGE_CODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("PDF VIEWER");
        setContentView(R.layout.activity_p_d_f);

        webView = findViewById(R.id.WV);
        progressBar = findViewById(R.id.pb);
        progressBar.setVisibility(View.VISIBLE);
        txtUrl = findViewById(R.id.txt_url);
        txtName = findViewById(R.id.txt_name);
        Download=findViewById(R.id.bt_download);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.setWebChromeClient(new WebChromeClient());
        Intent intent = getIntent();
        final int position = intent.getIntExtra("position", 0);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                webView.loadUrl("javascript:(function() { " +
                        "document.querySelector('[role=\"toolbar\"]').remove();})()");
                progressBar.setVisibility(View.GONE);
            }
        });
        //https://docs.google.com/viewerng/viewer?embedded=true&url=
        webView.loadUrl("https://docs.google.com/gview?embedded=true&url=" + GuruLaporanActivity.list.get(position).getPdfUrl());
        GET = "https://docs.google.com/gview?embedded=true&url="+GuruLaporanActivity.list.get(position).getPdfUrl();
        NAME = GuruLaporanActivity.list.get(position).getPdfName();
        txtUrl.setText(GET);
        txtName.setText(NAME);

        Download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = GET;
                String title = txtName.getText().toString();
                final ProgressDialog progressDialog = new ProgressDialog(PDFActivity.this);
                progressDialog.setMessage("Tunggu Sebentar . . .");
                progressDialog.show();
                final Handler handler = new Handler();
                if (title.equals("Data_Pengguna")){
                    DownloadBooks("https://mydbskripsi.000webhostapp.com/daring-pdf/files/dataPengguna.pdf","Data_Pengguna");
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            startActivity(new Intent(PDFActivity.this, GuruLaporanActivity.class));
                            Toast.makeText(PDFActivity.this, "Downloading...", Toast.LENGTH_SHORT).show();
                        }
                    }, 2000);
                }
                else if (title.equals("Data_Siswa")){
                    DownloadBooks("https://mydbskripsi.000webhostapp.com/daring-pdf/files/dataSiswa.pdf","Data_Siswa");
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            startActivity(new Intent(PDFActivity.this, GuruLaporanActivity.class));
                            Toast.makeText(PDFActivity.this, "Downloading...", Toast.LENGTH_SHORT).show();
                        }
                    }, 2000);
                }
                else if (title.equals("Tugas_Harian")){
                    DownloadBooks("https://mydbskripsi.000webhostapp.com/daring-pdf/files/tugasHarian.pdf","Tugas_Harian");
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            startActivity(new Intent(PDFActivity.this, GuruLaporanActivity.class));
                            Toast.makeText(PDFActivity.this, "Downloading...", Toast.LENGTH_SHORT).show();
                        }
                    }, 2000);
                }
                else if (title.equals("Absensi_Harian")){
                    DownloadBooks("https://mydbskripsi.000webhostapp.com/daring-pdf/files/absenHarian.pdf","Absensi_Harian");
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            startActivity(new Intent(PDFActivity.this, GuruLaporanActivity.class));
                            Toast.makeText(PDFActivity.this, "Downloading...", Toast.LENGTH_SHORT).show();
                        }
                    }, 2000);
                }
                else if (title.equals("Absensi_All")){
                    DownloadBooks("https://mydbskripsi.000webhostapp.com/daring-pdf/files/absenAll.pdf","Absensi_All");
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            startActivity(new Intent(PDFActivity.this, GuruLaporanActivity.class));
                            Toast.makeText(PDFActivity.this, "Downloading...", Toast.LENGTH_SHORT).show();
                        }
                    }, 2000);
                }
                else if (title.equals("Data_Tugas")){
                    DownloadBooks("https://mydbskripsi.000webhostapp.com/daring-pdf/files/dataTugas.pdf","Data_Tugas");
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            startActivity(new Intent(PDFActivity.this, GuruLaporanActivity.class));
                            Toast.makeText(PDFActivity.this, "Downloading...", Toast.LENGTH_SHORT).show();
                        }
                    }, 2000);
                }
            }
        });
    }

    public void DownloadBooks(String url, String title){
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        String tempTitle = title.replace("", "");
        request.setTitle(tempTitle);
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, tempTitle+".pdf");
        DownloadManager downloadManager = (DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
        request.setMimeType("application/pdf");
        request.allowScanningByMediaScanner();
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        downloadManager.enqueue(request);
    }

    public void download(View view) {
        String url = GET;
        String title = NAME;
        if (title.equals("Penggunaan_Barang")){
            DownloadBooks("https://mydbskripsi.000webhostapp.com/files/Pengguna_Barang.pdf","Penggunaan_Barang");
        }
        else if (title.equals("Data_Barang")){
            DownloadBooks("https://mydbskripsi.000webhostapp.com/files/Data_Barang.pdf","Data_Barang");
        }
        else if (title.equals("Pengajuan_Bmn")){
            DownloadBooks("https://mydbskripsi.000webhostapp.com/files/Pengajuan_Bmn.pdf","Pengajuan_Bmn");
        }
        else if (title.equals("Pengajuan_Famum")){
            DownloadBooks("https://mydbskripsi.000webhostapp.com/files/Pengajuan_Famum.pdf","Pengajuan_Famum");
        }
        else if (title.equals("Pengguna_Aplikasi")){
            DownloadBooks("https://mydbskripsi.000webhostapp.com/files/Semua_Pengajuan.pdf","Pengguna_Aplikasi");
        }
        else if (title.equals("Pekerjaan_Selesai")){
            DownloadBooks("https://mydbskripsi.000webhostapp.com/files/Tugas_Selesai.pdf","Pekerjaan_Selesai");
        }
    }

    private class DownloadFile extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            String fileUrl = strings[0];   // -> http://maven.apache.org/maven-1.x/maven.pdf
            String fileName = strings[1];  // -> maven.pdf
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File folder = new File(extStorageDirectory, "testthreepdf");
            folder.mkdir();

            File pdfFile = new File(folder, fileName);

            try{
                pdfFile.createNewFile();
            }catch (IOException e){
                e.printStackTrace();
            }
            FileDownloader.downloadFile(fileUrl, pdfFile);
            return null;
        }
    }
}