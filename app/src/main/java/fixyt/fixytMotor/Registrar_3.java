package fixyt.fixytMotor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Registrar_3 extends AppCompatActivity implements View.OnClickListener {

    private Button botaoProximo3;
    private ImageView iconeGuincho, iconeCarroMoto;
    private String campoPerfilTipo;
    private CheckBox checkbox;
    private LinearLayout Checkboxes_Guincho, Checkboxes_CarroMoto;

    private ProgressDialog dialogoProgresso;



    // Declarar API Firabase Auth
    private FirebaseAuth firebasAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_3);

        //Chamando FIrebase Auth
        firebasAuth = FirebaseAuth.getInstance();
        dialogoProgresso = new ProgressDialog(this);



        Checkboxes_CarroMoto = (LinearLayout) findViewById(R.id.linearCheckCarroMoto);
        Checkboxes_Guincho = (LinearLayout) findViewById(R.id.linearCheckGuincho);




        iconeCarroMoto = (ImageView) findViewById(R.id.perfilCarroMoto);
        iconeGuincho = (ImageView) findViewById(R.id.perfilGuincho);

        botaoProximo3 = (Button) findViewById(R.id.botProximo3);

        //Preparando os botões para receber clicks
        botaoProximo3.setOnClickListener(this);
        iconeCarroMoto.setOnClickListener(this);
        iconeGuincho.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == botaoProximo3){
            //Finalizar Cadastro, salvar no banco de dados associando o User aos dados e ir para Main
            registrar3();
        }
        if (v == iconeCarroMoto){
            // Mostrar os checkboxes de Carro moto
            Toast.makeText(this, "Perfil de Carro/Moto Selecionado", Toast.LENGTH_SHORT).show();
            String[] arrayCarroMoto = getResources().getStringArray(R.array.CarroMotoChecks);
            List<String> aCarroMoto = new ArrayList<>();
            Collections.addAll(aCarroMoto, arrayCarroMoto);
            Checkboxes_CarroMoto.removeAllViewsInLayout();
            Checkboxes_Guincho.removeAllViewsInLayout();

            for (int i = 0; i < aCarroMoto.size(); i++) {
                checkbox = new CheckBox(this);
                checkbox.setId(i);
                checkbox.setText(aCarroMoto.get(i));
                //checkbox.setOnClickListener(captarCheckbox(checkbox));
                Checkboxes_CarroMoto.addView(checkbox);
            }
        }
        if (v == iconeGuincho) {
            // Mostrar os checkboxes de guincho
            Toast.makeText(this, "Perfil de Guincho Selecionado", Toast.LENGTH_SHORT).show();

            String[] arrayGuincho = getResources().getStringArray(R.array.GuinchoChecks);
            List<String> aGuincho = new ArrayList<>();
            Collections.addAll(aGuincho, arrayGuincho);

            Checkboxes_CarroMoto.removeAllViewsInLayout();
            Checkboxes_Guincho.removeAllViewsInLayout();

            for (int i = 0; i < aGuincho.size(); i++) {
                checkbox = new CheckBox(this);
                checkbox.setId(i);
                checkbox.setText(aGuincho.get(i));
                //checkbox.setOnClickListener(captarCheckbox(checkbox));
                Checkboxes_Guincho.addView(checkbox);

            }
        }

    }



    private void registrar3(){

        //Recebendo cadastro da tela Registrar_2
        CadastroMecanico cadastroMecanico =(CadastroMecanico)getIntent().getParcelableExtra("cadastro");

        //Apropriando os valores aos campos seguintes.

        //String perfilTipo = campoPerfilTipo.getSelectedItem().toString().trim();



        /*if(TextUtils.isEmpty(modeloVeiculo)){
            //Modelo de Veiculo vazio
            Toast.makeText(this, "Ingresse um Modelo de Carro!", Toast.LENGTH_SHORT).show();
            //parar a execução do código
            return;
        }*/

        // Após validar que cadastro está OK um dialogo de progresso é mostrada

        dialogoProgresso.setMessage("Tela 1: ");
        dialogoProgresso.show();


    }

}
