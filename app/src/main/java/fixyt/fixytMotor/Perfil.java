package fixyt.fixytMotor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Perfil extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;
    private TextView emailUsuario;
    private Button logOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, Login.class));
        }

        FirebaseUser usuario = firebaseAuth.getCurrentUser();

        emailUsuario = (TextView) findViewById(R.id.textUsuarioEmail);
        emailUsuario.setText("Bem-Vindo " + usuario.getEmail());
        logOut = (Button) findViewById(R.id.botaoLogout);

        logOut.setOnClickListener(this);



      //raiz.child("usuarios/8").addValueEventListener(new ValueEventListener() {
      //    @Override
      //    public void onDataChange(DataSnapshot dataSnapshot)
      //    {
      //        Utilizador user = dataSnapshot.getValue(Utilizador.class);
      //    }
      //    @Override
      //    public void onCancelled(DatabaseError databaseError) {
      //        //Se ocorrer um erro}
      //    });
    }




    @Override
    public void onClick(View v) {
        if(v == logOut){
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, Login.class));
        }
    }
}
