package fixyt.fixytMotor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class Main extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth firebaseAuth;
    private Button logOut;
    private Button perfilUser;
    private Button atendimentosEmergenciais;
    private FirebaseDatabase database;
    private DatabaseReference dbReference;
    private DatabaseReference refBolada;
    private DatabaseReference dbReferencecu;
    private String userKey;
    private Double notaMediaInicial = 0.0;
    private Double notaMedia = 0.0;
    private Double nota = 0.0;
    private int divisorBoladao = 0;
    private int contDivisao = 0;
    private ArrayList<Double> notas;
    private int i = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();


        if(firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, Login.class));
        }



        leituraDeNota();





        logOut = (Button) findViewById(R.id.botaoLogout);
        perfilUser = (Button) findViewById(R.id.botaoPerfil);
        atendimentosEmergenciais = (Button) findViewById(R.id.botaoAuxilio);


        logOut.setOnClickListener(this);
        perfilUser.setOnClickListener(this);
        atendimentosEmergenciais.setOnClickListener(this);

    }

    private void salvarPontuacaoMedia() {
        userKey = FirebaseAuth.getInstance().getCurrentUser().getUid();

        database = FirebaseDatabase.getInstance();
        dbReference = database.getReference("Avaliacoes/" + userKey);
        notas = new ArrayList<>();

        dbReferencecu = database.getReference("Avaliacoes/" + userKey);

        Query queryPont = dbReference;

        queryPont.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot alert : dataSnapshot.getChildren()){
                    if(Integer.valueOf(alert.child("flProcessado").getValue().toString()) == 0){
                        nota = Double.valueOf(alert.child("nota").getValue().toString());
                        notas.add(nota);
                        HashMap<String, Object> processado = new HashMap<>();
                        processado.put("flProcessado", "1");
                        dbReferencecu.child(alert.getKey()).updateChildren(processado);
                    }
                }
                if(notas.size() > 0){
                    calcularMedia();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void calcularMedia() {

        refBolada = database.getReference("Partner/" + userKey);

        for(i=0; i<notas.size(); i++){
           notaMedia = notaMedia + notas.get(i);
        }
        notaMedia = (notaMediaInicial + notaMedia);
        divisorBoladao = notas.size() + contDivisao;
        System.out.println(notaMedia);
        HashMap<String, Object> notaMid = new HashMap<>();
        notaMid.put("notaMedia", notaMedia.toString());
        refBolada.child("Nota").updateChildren(notaMid);

        HashMap<String, Object> fatDiv = new HashMap<>();
        fatDiv.put("fatorDivisao", String.valueOf(divisorBoladao));
        refBolada.child("Nota").updateChildren(fatDiv);
    }

    private void leituraDeNota() {

        userKey = FirebaseAuth.getInstance().getCurrentUser().getUid();

        database = FirebaseDatabase.getInstance();
        dbReference = database.getReference("Partner/" + userKey);

        Query query1 = dbReference.child("/Nota/");

        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Passar os dados para a interface grafica
                notaMediaInicial = Double.parseDouble(dataSnapshot.child("notaMedia").getValue().toString());
                contDivisao = Integer.parseInt(dataSnapshot.child("fatorDivisao").getValue().toString());
                System.out.println(notaMediaInicial + " " + contDivisao);
                salvarPontuacaoMedia();
            }

            public void onCancelled(DatabaseError databaseError) {
                //Se ocorrer um erro
                databaseError.getMessage();
            }

        });


    }

    @Override
    public void onClick(View v) {
        if(v == logOut){
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, Login.class));
        }
        if(v == perfilUser){
            startActivity(new Intent(this, Perfil.class));
        }
        if(v == atendimentosEmergenciais){
            startActivity(new Intent(this, Auxilio.class));
        }

    }
}
