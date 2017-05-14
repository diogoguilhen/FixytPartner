package fixyt.fixytMotor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class Main extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth firebaseAuth;
    private Button logOut;
    private Button perfilUser;
    private Button atendimentosEmergenciais;
    private FirebaseDatabase database;
    private DatabaseReference dbReference;
    private String userKey;
    private Double notaMedia = 0.0;
    private Double nota = 0.0;
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

        salvarPontuacaoMedia();

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

        Query queryPont = dbReference.startAt(userKey);

        queryPont.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot alert : dataSnapshot.getChildren()){
                    nota = (Double) dataSnapshot.getValue();
                    notas.add(nota);
                    System.out.println(notas.get(i));
                    i++;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void leituraDeNota() {

        userKey = FirebaseAuth.getInstance().getCurrentUser().getUid();

        database = FirebaseDatabase.getInstance();
        dbReference = database.getReference("Partner/" + userKey);

        Query query1 = dbReference.child("/Nota/");

        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Passar os dados para a interface grafica
                notaMedia = Double.parseDouble(dataSnapshot.child("NotaMedia").getValue().toString());
                contDivisao = Integer.parseInt(dataSnapshot.child("FatorDivisao").getValue().toString());
                System.out.println(notaMedia + " " + contDivisao);
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
