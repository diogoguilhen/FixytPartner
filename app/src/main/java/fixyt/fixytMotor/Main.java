package fixyt.fixytMotor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class Main extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth firebaseAuth;
    private Button logOut;
    private Button perfilUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, PreLogin.class));
        }

        logOut = (Button) findViewById(R.id.botaoLogout);
        perfilUser = (Button) findViewById(R.id.botaoPerfil);


        logOut.setOnClickListener(this);
        perfilUser.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v == logOut){
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, PreLogin.class));
        }
        if(v == perfilUser){
            finish();
            startActivity(new Intent(this, Perfil.class));
        }

    }
}
