package mx.edukweb.tortuaak;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class Login extends Activity implements OnClickListener {

    private EditText user, pass;
    private Button mSubmit, mRegister;

    private ProgressDialog pDialog;

    // Clase JSONParser
    JSONParser jsonParser = new JSONParser();


    // si trabajan de manera local "localhost" :
    // En windows tienen que ir, run CMD > ipconfig
    // buscar su IP
    // y poner de la siguiente manera
    // "http://xxx.xxx.x.x:1234/cas/login.php";

   // private static final String LOGIN_URL = "http://sislinkit.com/users/login2.php";
//http://basededatosremotas.meximas.com/cas/login.php
   // http://edukwebmti.esy.es/login.php
private static final String LOGIN_URL = "http://chipvis.com/users/login2.php";
    //private static final String LOGIN_URL = "http://basededatosremotas.meximas.com/cas/login.php";

        // La respuesta del JSON es
        private static final String TAG_SUCCESS = "success";
        private static final String TAG_MESSAGE = "message";

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            // TODO Auto-generated method stub
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);

            // setup input fields
            user = (EditText) findViewById(R.id.username);
            pass = (EditText) findViewById(R.id.password);

            // setup buttons
            mSubmit = (Button) findViewById(R.id.login);
            //mRegister = (Button) findViewById(R.id.register);


            mSubmit.setOnClickListener(this);


            // register listeners

       // mRegister.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.login:

                if(user.getText().toString().equals("") || pass.getText().toString().equals(""))
                {

                    AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                    alertDialog.setTitle("Información");
                    alertDialog.setMessage("El Usuario y Contraseña no pueden estar en blanco");
                    alertDialog.setButton("Aceptar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
// aquí puedes añadir funciones
                        }
                    });
                    //alertDialog.setIcon(R.drawable.icon);
                    alertDialog.show();

                }
                else
                new AttemptLogin().execute();



                break;
      /*      case R.id.register:
                Intent i = new Intent(this, Register.class);
                startActivity(i);
                break;
    */
            default:
                break;
        }
    }

    class AttemptLogin extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Login.this);
            pDialog.setMessage("Enviando información de login...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            int success;
            String username = user.getText().toString();
            String password = pass.getText().toString();
           // cript(username);
           // username=getCifrado(username, "SHA1");

            password=getCifrado(password,"SHA1" );
            //cript(password);
            try {
                // Building Parameters
                List params = new ArrayList();
                params.add(new BasicNameValuePair("username", username));
                params.add(new BasicNameValuePair("password", password));
                Log.d("checar el cifrado!", password);
                Log.d("request!", "starting");
                // getting product details by making HTTP request
                JSONObject json = jsonParser.makeHttpRequest(LOGIN_URL, "POST",
                        params);

                // check your log for json response
                Log.d("datos del json que descarga", json.toString());

                // json success tag
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("Login Successful!", json.toString());
                    // save user data
                    SharedPreferences sp = PreferenceManager
                            .getDefaultSharedPreferences(Login.this);
                    Editor edit = sp.edit();
                    edit.putString("username", username);
                    edit.commit();

                    Intent i = new Intent(Login.this, menusliding.class); //("la clase nada más"), existe una clase especial que te da cuales procesos aun estan activadas
                    finish();
                    startActivity(i);
                    return json.getString(TAG_MESSAGE);
                } else {
                    Log.d("Login Failure!", json.getString(TAG_MESSAGE));
                    return json.getString(TAG_MESSAGE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted
            pDialog.dismiss();
            if (file_url != null) {
                Toast.makeText(Login.this, file_url, Toast.LENGTH_LONG).show();
            }
        }
    }


    public class SHA1 {
        private MessageDigest md;
        private byte[] buffer, digest;
        private String hash = "";

        public String getHash(String message) throws NoSuchAlgorithmException {
            buffer = message.getBytes();
            md = MessageDigest.getInstance("SHA1");
            md.update(buffer);
            digest = md.digest();

            for(byte aux : digest) {
                int b = aux & 0xff;
                if (Integer.toHexString(b).length() == 1) hash += "0";
                hash += Integer.toHexString(b);
            }
            return hash;}
    }

    public String cript(String pass){
        SHA1 s = new SHA1();
        //String basura="rwr24t5yt25y543td32ty6";
        try {
           // return s.getHash(s.getHash(pass+basura)+basura);
            return s.getHash(s.getHash(pass));
        }
        catch(NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return "0";}

        private String getCifrado(String texto, String hashType) {
            try {
                java.security.MessageDigest md = java.security.MessageDigest.getInstance(hashType);
                byte[] array = md.digest(texto.getBytes());
                StringBuilder sb = new StringBuilder();

                for (int i = 0; i < array.length; ++i) {
                    sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
                }
                return sb.toString();
            } catch (java.security.NoSuchAlgorithmException e) {
                System.err.println("Error "+e.getMessage());
            }
            return "";
        }






}//Fin de clase