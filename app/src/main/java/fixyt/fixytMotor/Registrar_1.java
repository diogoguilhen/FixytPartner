package fixyt.fixytMotor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Registrar_1 extends AppCompatActivity implements View.OnClickListener{

    private Button botaoProximo1;
    private EditText nome;
    private EditText sobrenome;
    private EditText telefone;
    private EditText email;
    private EditText confirmaSenha;
    private EditText digSenha;
    private ProgressDialog dialogoProgresso;
    private CadastroMecanico cadastroMecanico;
    private static final String TAG = "Registrar_1";
    public String userKey;
    // Declarar API Firabase Auth
    private FirebaseAuth firebasAuth;

    // Banco de dados Firebase
    private Firebase mRef;
    //Emailbd
    private String emailBd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_1);

        //Chamando Firebase Auth
        firebasAuth = FirebaseAuth.getInstance();
        //Inicializando Base
        mRef = new Firebase("https://fixyt-20066.firebaseio.com/");


        dialogoProgresso = new ProgressDialog(this);

        botaoProximo1 = (Button) findViewById(R.id.botaoProximo1);
        nome = (EditText) findViewById(R.id.campoNome);
        sobrenome = (EditText) findViewById(R.id.campoSobrenome);
        telefone = (EditText) findViewById(R.id.campoTelefone);
        email = (EditText) findViewById(R.id.campoEmail);

        //Apropriando os valores dos EditText para os STRINGS do objeto CADASTRO.

        confirmaSenha = (EditText) findViewById(R.id.confirmaSenha);
        digSenha = (EditText) findViewById(R.id.campoSenha);

        //Utilizando mascaras para os campos devidos
        MaskEditTextChangedListener maskTEL = new MaskEditTextChangedListener("(##)#####-####", telefone);
        telefone.addTextChangedListener(maskTEL);

        botaoProximo1.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == botaoProximo1){
            //completa o primeiro passo do cadastro
            registrar1();
        }
    }

    private void registrar1(){
        cadastroMecanico = new CadastroMecanico();
        cadastroMecanico.setNome(nome.getText().toString().trim());
        cadastroMecanico.setSobrenome(sobrenome.getText().toString().trim());
        cadastroMecanico.setTelefone(telefone.getText().toString().trim());
        cadastroMecanico.setEmail(email.getText().toString().trim());
        cadastroMecanico.setSenha(digSenha.getText().toString().trim());

        String email = cadastroMecanico.getEmail().trim();
        String senha = cadastroMecanico.getSenha().trim();
        String nome = cadastroMecanico.getNome().trim();
        String sobrenome = cadastroMecanico.getSobrenome().trim();
        String telefone = cadastroMecanico.getTelefone().trim();
        String ConfSen = confirmaSenha.getText().toString().trim();
        String tSenha = digSenha.getText().toString().trim();

        if(TextUtils.isEmpty(nome)){
            //email vazio
            Toast.makeText(this, "Ingresse um nome!", Toast.LENGTH_SHORT).show();
            //parar a execução do código
            return;
        }
        if(TextUtils.isEmpty(sobrenome)){
            //email vazio
            Toast.makeText(this, "Ingresse um sobrenome!", Toast.LENGTH_SHORT).show();
            //parar a execução do código
            return;
        }
        if(TextUtils.isEmpty(telefone)){
            //email vazio
            Toast.makeText(this, "Ingresse um telefone!", Toast.LENGTH_SHORT).show();
            //parar a execução do código
            return;
        }

        if(TextUtils.isEmpty(email)){
            //email vazio
            Toast.makeText(this, "Ingresse um Email Válido!", Toast.LENGTH_SHORT).show();
            //parar a execução do código
            return;
        }
        if(TextUtils.isEmpty(senha)){
            //senha vazia
            Toast.makeText(this, "Ingresse uma Senha!", Toast.LENGTH_SHORT).show();
            //parar a execução do código
            return;
        }
        if(!ConfSen.equals(tSenha)){
            //senha não está igual em ambos os campos
            Toast.makeText(this, "Verifique a confirmação de senha novamente!" , Toast.LENGTH_SHORT).show();
            //parar a execução do código
            return;
        }
        // Após validar que cadastro está OK um dialogo de progresso é mostrada



        dialogoProgresso.setMessage("Aguarde...");
        dialogoProgresso.show();

        firebasAuth.createUserWithEmailAndPassword(cadastroMecanico.getEmail(), cadastroMecanico.getSenha())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //Se tarefa é completada
                        if (task.isSuccessful()) {
                            // CADASTRO NO FIREBASE E DEPOIS NO BANCO

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference criacaoPartner = database.getReference("Partner");

                            CadastroMecanico user = new CadastroMecanico (
                                    cadastroMecanico.getNome(),
                                    cadastroMecanico.getSobrenome(),
                                    cadastroMecanico.getTelefone(),
                                    cadastroMecanico.getEmail(),
                                    cadastroMecanico.getSenha(),
                              "null",     //cadastroMecanico.getCpf(),
                              "null",     //cadastroMecanico.getRg(),
                              "null",     //cadastroMecanico.getSexo(),
                              "null",     //cadastroMecanico.getTpLogradouro(),
                              "null",     //cadastroMecanico.getEndereco(),
                              "null",     //cadastroMecanico.getCep(),
                              "null",     //cadastroMecanico.getBairro(),
                              "null",     //cadastroMecanico.getUf(),
                              "null",     //cadastroMecanico.getCidade(),
                           // "null",     //cadastroMecanico.getPais(),
                              "null",     //cadastroMecanico.getDataNascimento(),
                              "null",     //cadastroMecanico.getPerfilTipo(),
                           // "null",     //cadastroMecanico.getTipoServicoEmergencial(),
                           // "null",     //cadastroMecanico.getTipoServicoAgendado(),
                              "null"      //cadastroMecanico.getServicos()
                            );

                            //Pegar o uid do usuario registrado
                            userKey =  task.getResult().getUser().getUid().toString();
                            String key = userKey;///FirebaseAuth.getInstance().getCurrentUser().getUid();
                            criacaoPartner.child(key).setValue(user);
                            //FIM CADASTRO NO BANCO


                            //usuario registrou corretamente
                            finish();
                            //inicializar cadastro de perfil
                            //startActivity(new Intent(getApplicationContext(), Main.class));

                            //Passar os dados para a tela 2
                            Intent intentReg1 = new Intent(Registrar_1.this, Registrar_2.class);
                            intentReg1.putExtra("cadastro", cadastroMecanico);
                            startActivity(intentReg1);

                            //mostrar mensagem para usuario indicando sucesso
                            Toast.makeText(Registrar_1.this, "Registrado com Sucesso.", Toast.LENGTH_SHORT).show();
                            dialogoProgresso.dismiss();
                        } else {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthWeakPasswordException e) {
                                Toast.makeText(Registrar_1.this, "A senha utilizada deve ter no mínimo 6 caracteres.", Toast.LENGTH_LONG).show();
                                dialogoProgresso.dismiss();
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                Toast.makeText(Registrar_1.this, "As credenciais utilizadas expiraram. Contate o administrador", Toast.LENGTH_LONG).show();
                                dialogoProgresso.dismiss();
                            } catch (FirebaseAuthUserCollisionException e) {
                                Toast.makeText(Registrar_1.this, "O usuário escolhido já está cadastrado. Escolha outro!", Toast.LENGTH_LONG).show();
                                dialogoProgresso.dismiss();
                            } catch (Exception e) {
                                Log.e(TAG,  e.getMessage());
                            }
                        }
                    }
                });


        //Passando dados para a tela REGISTRAR 2
        /*Intent intentReg1 = new Intent(Registrar_1.this, Registrar_2.class);
        intentReg1.putExtra("cadastro", cadastroMecanico);
        startActivity(intentReg1);
        dialogoProgresso.dismiss();*/
    }
}
