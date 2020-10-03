package betharley.catempregos.com.helper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class SetupFirebase {

    private static DatabaseReference firebase;
    private static FirebaseAuth autenticacao;
    private static StorageReference storage;

    //RETORNA O ID DO USUARIO AUTENTICADO
    public static String getIdUsuario(){
        FirebaseAuth autenticar = getAutenticacao();
        return autenticar.getCurrentUser().getUid();
    }

    //retorna a instancia do FirebaseAuth
    public static FirebaseAuth getAutenticacao(){
        if( autenticacao == null){
            autenticacao = FirebaseAuth.getInstance();
        }
        return autenticacao;
    }

    //retorna a refencia para o banco de dados
    public static DatabaseReference getFirebase(){
        if( firebase == null ){
            firebase = FirebaseDatabase.getInstance().getReference();
        }
        return firebase;
    }

    //Retorna a referencia para o Storage
    public static StorageReference getStorage(){
        if(storage == null){
            storage = FirebaseStorage.getInstance().getReference();
        }
        return storage;
    }
}
