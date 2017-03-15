package fixyt.fixytMotor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;


public class Registrar_2_1 extends AppCompatActivity implements View.OnClickListener  {

    public static final int REQUEST_CAPTURE = 1;
    private Button butNext, butPhoto;
    private ImageView displayPhoto;

    private ProgressDialog dialogoProgresso;

    private StorageReference fireStorage;
    private FirebaseAuth firebasAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_2_1);


        firebasAuth = FirebaseAuth.getInstance();
     //  if(firebasAuth.getCurrentUser() != null){
     //      //ir para tela main ou perfil
     //      finish();
     //      //inicializar tela principal
     //      startActivity(new Intent(getApplicationContext(), Main.class));
     //  }

        fireStorage = FirebaseStorage.getInstance().getReference();


        displayPhoto = (ImageView) findViewById(R.id.imgCamera);
        butPhoto = (Button) findViewById(R.id.botFoto);
        butNext = (Button) findViewById(R.id.botProximo2_1);

        butPhoto.setOnClickListener(this);
        butNext.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        if (v == butNext){
            //completa o segundo passo do cadastro.
            if(displayPhoto.getDrawable() == null){
                Toast.makeText(this, "Tire foto da sua CNH antes de prosseguir!", Toast.LENGTH_SHORT).show();
            }
            else{
                registrar2_1();
            }
        }
        if (v == butPhoto){
            // Carrega foto e guarda no storage (mostra na tela)
            photoLoad();
        }

    }

    private void photoLoad() {
        Intent capture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(capture, REQUEST_CAPTURE);



    }
    String key = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CAPTURE && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap photo = (Bitmap) extras.get("data");
            displayPhoto.setImageBitmap(photo);




            StorageReference fixytRef = fireStorage.child("Mecanicos/Documentos/"+ key.toString() +"/cnhmecanico.jpg");

            displayPhoto.setDrawingCacheEnabled(true);
            displayPhoto.buildDrawingCache();
            Bitmap bitmap = displayPhoto.getDrawingCache();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data2 = baos.toByteArray();

            UploadTask uploadTask = fixytRef.putBytes(data2);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    // Uri downloadUrl = taskSnapshot.getDownloadUrl();
                }
            });


        }
    }



    private void registrar2_1() {

        //Recebendo cadastro da tela Registrar_1
        CadastroMecanico cadastroMecanico = getIntent().getParcelableExtra("cadastro");


        //Passando dados para a tela REGISTRAR 3
        Intent intentReg2_1 = new Intent(Registrar_2_1.this, Registrar_3.class);
        intentReg2_1.putExtra("cadastro", cadastroMecanico);
        startActivity(intentReg2_1);


    }
}
