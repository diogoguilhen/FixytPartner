package fixyt.fixytMotor;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;




public class Auxilio extends FragmentActivity implements  View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private FirebaseAuth firebaseAuth;

    private Switch statusOnOff;
    private LocationManager locationManager;

    public String userKey;
    private TextView pontoReferencia;
    private TextView tempoEstimado;
    private TextView nomeMotorista;
    public String online = "";
    private String servicoString;
    private ArrayList<String> atendimentos;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    LocationRequest mLocationRequest;
    Context context;
    boolean GpsStatus ;
    public String codChamado = "";
    public String emAtendimento = "0";
    private Button endService;
    private String finalizar = "";
    private String motoristasIndesejados = " ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auxilio);

        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, PreLogin.class));
        }

        //Inicio Codigo
        //POLITICA DE FUNCIONAMENTO NÃO PODE TIRAR ESSA MERDA DE CODIGO DE MERDA FILHA DA PUTA
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            checkLocationPermission();
        }

        pontoReferencia = (TextView) findViewById(R.id.pontoRef);
        tempoEstimado = (TextView) findViewById(R.id.tempoETA);
        statusOnOff = (Switch) findViewById(R.id.onOff);
        endService = (Button) findViewById(R.id.botFimAtendimento);

        endService.setOnClickListener(this);


        statusOnOff.setChecked(true);

        procurarMotoristas();




        context = getApplicationContext();

        CheckGpsStatus();

        if (GpsStatus == true) {
            //
        } else {
            //
            Toast.makeText(Auxilio.this, "Ative seu GPS para utilizar o serviço!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);

        }
        buildGoogleApiClient();

        // INICIO DE PEGAR OS SERVICOS DO BANCO
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference servicos = database.getReference();
        //Query para captar os servicos do Partner
        Query query1 = servicos.child("/Partner/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/servicos");

        query1.addListenerForSingleValueEvent(new ValueEventListener() {

            public void onDataChange(DataSnapshot dataSnapshot) {
                //Passar os dados para a interface grafica
                servicoString = (String) dataSnapshot.getValue();
            }

            public void onCancelled(DatabaseError databaseError) {
                //Se ocorrer um erro
                databaseError.getMessage();
            }

        });

    }

    private void procurarMotoristas() {
        if(statusOnOff.isChecked()){
            statusOnOff.setText("Na espera de chamados");
            online = "1";
            //Começo da leitura child (em atendimento)
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference servicos = database.getReference();
            //Query para captar os servicos do Partner
            Query query2 = servicos.child("EmAtendimento");

            query2.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot snapshot, String s) {
                    for(DataSnapshot alert : snapshot.getChildren()){
                        if(snapshot.getKey().toString().contains(FirebaseAuth.getInstance().getCurrentUser().getUid()) && emAtendimento == "0" && !(motoristasIndesejados.contains(snapshot.getKey().toString()))){
                            online = "0";
                            emAtendimento = "1";
                            finalizar = snapshot.getKey().toString();
                            codChamado = snapshot.getKey().toString();
                            pontoReferencia.setText("Ponto de Referencia:" + snapshot.child("pontoDeReferencia").getValue().toString());
                            tempoEstimado.setText(snapshot.child("tempoEstimado").getValue().toString() + " Minutos até o seu cliente" );
                            final String latMot = snapshot.child("latitudeMotorista").getValue().toString();
                            final String longMot = snapshot.child("longitudeMotorista").getValue().toString();

                            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which){
                                        case DialogInterface.BUTTON_POSITIVE:
                                            //Clicou Sim vai para o Waze!
                                            //String uri = "waze://?ll=" + latMot + "," + longMot +"&z=10";
                                            //startActivity(new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri)));
                                            endService.setVisibility(View.VISIBLE);
                                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                                            DatabaseReference aceitacao = database.getReference("EmAtendimento/" + codChamado);

                                            userKey = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                            String key = userKey;

                                            //Atualização
                                            HashMap<String, Object> statusAccept = new HashMap<>();
                                            statusAccept.put("statusAceitacao", "1");
                                            aceitacao.updateChildren(statusAccept);
                                            break;

                                        case DialogInterface.BUTTON_NEGATIVE:
                                            //Clicou Não
                                            emAtendimento = "0";
                                            online = "1";
                                            Toast.makeText(Auxilio.this, "Novamente aguardando chamados...", Toast.LENGTH_SHORT).show();
                                            pontoReferencia.setText("");
                                            tempoEstimado.setText("");
                                            motoristasIndesejados = motoristasIndesejados + " " + finalizar;
                                            procurarMotoristas();
                                            break;
                                    }
                                }
                            };

                            AlertDialog.Builder builder = new AlertDialog.Builder(Auxilio.this);
                            builder.setMessage("Cliente encontrado! Que deseja fazer? \n" + pontoReferencia.getText().toString() + "\nTempo Estimado de viagem: " + tempoEstimado.getText().toString()).setPositiveButton("Aceitar Chamado", dialogClickListener)
                                    .setNegativeButton("Recusar Chamado", dialogClickListener).show();

                        }

                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });




        }else{
            statusOnOff.setText("Offline");
            online = "0";
            pontoReferencia.setText("");
            tempoEstimado.setText("");
            Toast.makeText(Auxilio.this, "Status : Offline", Toast.LENGTH_SHORT).show();
        }

        statusOnOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    statusOnOff.setText("Na espera de chamados");
                    online = "1";
                    //Começo da leitura child (em atendimento)
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference servicos = database.getReference();
                    //Query para captar os servicos do Partner
                    Query query2 = servicos.child("EmAtendimento");

                    query2.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot snapshot, String s) {
                            for(DataSnapshot alert : snapshot.getChildren()){
                                if(snapshot.getKey().toString().contains(FirebaseAuth.getInstance().getCurrentUser().getUid()) && emAtendimento == "0" && !(motoristasIndesejados.contains(finalizar))){
                                    online = "0";
                                    emAtendimento = "1";
                                    finalizar = snapshot.getKey().toString();
                                    codChamado = snapshot.getKey().toString();
                                    pontoReferencia.setText("Ponto de Referencia:" + snapshot.child("pontoDeReferencia").getValue().toString());
                                    tempoEstimado.setText(snapshot.child("tempoEstimado").getValue().toString() + " Minutos até o seu cliente" );
                                    final String latMot = snapshot.child("latitudeMotorista").getValue().toString();
                                    final String longMot = snapshot.child("longitudeMotorista").getValue().toString();

                                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which){
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    //Clicou Sim vai para o Waze!
                                                    //String uri = "waze://?ll=" + latMot + "," + longMot +"&z=10";
                                                    //startActivity(new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri)));
                                                    endService.setVisibility(View.VISIBLE);
                                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                                    DatabaseReference aceitacao = database.getReference("EmAtendimento/" + codChamado);

                                                    userKey = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                                    String key = userKey;

                                                    //Atualização
                                                    HashMap<String, Object> statusAccept = new HashMap<>();
                                                    statusAccept.put("statusAceitacao", "1");
                                                    aceitacao.updateChildren(statusAccept);
                                                    break;

                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    //Clicou Não
                                                    emAtendimento = "0";
                                                    online = "1";
                                                    Toast.makeText(Auxilio.this, "Novamente aguardando chamados...", Toast.LENGTH_SHORT).show();
                                                    pontoReferencia.setText("");
                                                    tempoEstimado.setText("");
                                                    motoristasIndesejados = motoristasIndesejados + " " + finalizar;
                                                    procurarMotoristas();
                                                    break;
                                            }
                                        }
                                    };

                                    AlertDialog.Builder builder = new AlertDialog.Builder(Auxilio.this);
                                    builder.setMessage("Cliente encontrado! Que deseja fazer? \n" + pontoReferencia.getText().toString() + "\nTempo Estimado de viagem: " + tempoEstimado.getText().toString()).setPositiveButton("Aceitar Chamado", dialogClickListener)
                                            .setNegativeButton("Recusar Chamado", dialogClickListener).show();

                                }

                            }
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });




                }else{
                    statusOnOff.setText("Offline");
                    online = "0";
                    pontoReferencia.setText("");
                    tempoEstimado.setText("");
                    Toast.makeText(Auxilio.this, "Status : Offline", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    private void CheckGpsStatus() {
        locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);

        GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }



    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onClick(View v) {
        if(v == endService){
            finalizarChamado();
            endService.setVisibility(View.INVISIBLE);
            Toast.makeText(Auxilio.this, "Serviço Finalizado", Toast.LENGTH_SHORT).show();
            procurarMotoristas();
        }
    }

    private void finalizarChamado() {


        FirebaseDatabase databaseName = FirebaseDatabase.getInstance();
        DatabaseReference noAtendimento = databaseName.getReference("AtendimentoFinalizado/");

        CadastroAuxilio endingAtendimento = new CadastroAuxilio("1");

        noAtendimento.child(finalizar).setValue(endingAtendimento);
        pontoReferencia.setText("");
        tempoEstimado.setText("");
        emAtendimento = "0";

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(10000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest,this);
        }
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference localizacao = database.getReference("Localizacoes/Partner");

        userKey = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String key = userKey;

        String vLatitude = String.valueOf(location.getLatitude());
        String vLongitude = String.valueOf(location.getLongitude());
        String vOnline = online;
        String vServico = servicoString;
        String vEmAtendimento = emAtendimento;



        //CadastroAuxilio diogoLindo = new CadastroAuxilio(vLatitude, vLongitude, vOnline, vServico, vEmAtendimento);
        //Latitude
        HashMap<String, Object> latitude = new HashMap<>();
        latitude.put("vLatitude", location.getLatitude());
        localizacao.child(key).updateChildren(latitude);
        //Longitude
        HashMap<String, Object> longitude = new HashMap<>();
        longitude.put("vLongitude", location.getLongitude());
        localizacao.child(key).updateChildren(longitude);
        //localizacao.child(key).setValue(diogoLindo);
        }
}




