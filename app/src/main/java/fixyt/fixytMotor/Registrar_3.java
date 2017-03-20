package fixyt.fixytMotor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


public class Registrar_3 extends AppCompatActivity implements View.OnClickListener {

    private Button botaoProximo3;
    private ImageView iconeGuincho, iconeCarroMoto;
    private String campoPerfilTipo;
    private String[] campoServicos;
    private CheckBox checkbox;
    private LinearLayout Checkboxes_Guincho, Checkboxes_CarroMoto;
    public String userKey;
    private ProgressDialog dialogoProgresso;



    // Declarar API Firabase Auth
    private FirebaseAuth firebasAuth;

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Não é possivel voltar! Finalize o Cadastro!", Toast.LENGTH_SHORT).show();
    }


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
        if (v == botaoProximo3) {
            //Finalizar Cadastro, salvar no banco de dados associando o User aos dados e ir para Main
            if (campoPerfilTipo == "CarroEMoto") {
                ArrayList<String> servicosSelecionados = new ArrayList<>();
                for (int i = 0; i < Checkboxes_CarroMoto.getChildCount(); i++) {
                    View nextChild = Checkboxes_CarroMoto.getChildAt(i);
                    CheckBox check = (CheckBox) nextChild;
                    if (check.isChecked()) {
                        servicosSelecionados.add(check.getText().toString());
                    }
                }
                registrar3(servicosSelecionados);
            } else {
                ArrayList<String> servicosSelecionados = new ArrayList<>();
                for (int i = 0; i < Checkboxes_Guincho.getChildCount(); i++) {
                    View nextChild = Checkboxes_Guincho.getChildAt(i);
                    CheckBox check = (CheckBox) nextChild;
                    if (check.isChecked()) {
                        servicosSelecionados.add(check.getText().toString());
                    }
                }
                registrar3(servicosSelecionados);
            }
        }
        if (v == iconeCarroMoto){
            campoPerfilTipo = "CarroEMoto";
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
                Checkboxes_CarroMoto.addView(checkbox);
            }
        }
        if (v == iconeGuincho) {
            // Mostrar os checkboxes de guincho
            campoPerfilTipo = "Guincho";
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
                Checkboxes_Guincho.addView(checkbox);

            }
        }

    }


    private void registrar3(ArrayList<String> arrayList){

        //Recebendo cadastro da tela Registrar_2_1
        CadastroMecanico cadastroMecanico =(CadastroMecanico)getIntent().getParcelableExtra("cadastro");


        //Apropriando os valores aos campos seguintes.
        cadastroMecanico.setPerfilTipo(campoPerfilTipo.trim());
        //cadastroMecanico.setServicos(arrayList);


        // Após validar que cadastro está OK um dialogo de progresso é mostrada

        dialogoProgresso.show();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference criacaoPartner = database.getReference("Partner");


        String json = new Gson().toJson(arrayList);


        userKey =  firebasAuth.getCurrentUser().getUid().toString();    //getUser().getUid().toString();

        Gson gson = new GsonBuilder().create();

        JsonArray myCustomArray = gson.toJsonTree(arrayList).getAsJsonArray();

        JsonObject jsonObject = new JsonObject();

        jsonObject.add("Diferenciado", myCustomArray);

        String key = userKey;
        criacaoPartner.child(key).child(cadastroMecanico.getPerfilTipo().toString()).setValue(jsonObject.toString());


        //PerfilTipo
        HashMap<String, Object> perfilTipo = new HashMap<>();
        perfilTipo.put("perfilTipo",  cadastroMecanico.getPerfilTipo());
        criacaoPartner.child(key).updateChildren(perfilTipo);

        //PerfilTipo
        HashMap<String, Object> servicos = new HashMap<>();
        servicos.put("servicos",  json.toString());
        criacaoPartner.child(key).updateChildren(servicos);


        //só criar um objeto para esse cadastro de tipo de perfil...



        //dialogoProgresso.setMessage("Perfil Selecionado: " + cadastroMecanico.getPerfilTipo() + "\n" + "Servicos: " + arrayList.get(1) + ", " + arrayList.get(2));

        dialogoProgresso.dismiss();


        //Passando dados para a tela REGISTRAR 3
        Intent intentMain = new Intent(Registrar_3.this, Main.class);
        startActivity(intentMain);


    }


}
