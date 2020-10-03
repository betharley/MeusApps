package com.betharley.mobile.ecommerceonline;

import android.content.Intent;
import android.os.Bundle;

import com.betharley.mobile.ecommerceonline.fragment.CarrinhoFragment;
import com.betharley.mobile.ecommerceonline.fragment.PedidosFragment;
import com.betharley.mobile.ecommerceonline.fragment.PerfilFragment;
import com.betharley.mobile.ecommerceonline.fragment.PesquisarFragment;
import com.betharley.mobile.ecommerceonline.fragment.ProdutosFragment;
import com.betharley.mobile.ecommerceonline.helper.Firebase;
import com.betharley.mobile.ecommerceonline.helper.UsuarioFirebase;
import com.betharley.mobile.ecommerceonline.inicio.MainActivity;
import com.betharley.mobile.ecommerceonline.model.Usuario;
import com.betharley.mobile.ecommerceonline.vendedor.VendedorActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private GoogleSignInClient googleSignInClient;
    private AdView adView;

    private static Usuario usuario;
    private static TextView profile_name;
    private static TextView profile_email;
    private static CircleImageView profile_foto;
    private static String nome, email, foto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Home");

        //CONFIGURAÇÕES INICIAIS
        Paper.init(this);
        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd( adRequest );

        /*FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //      .setAction("Action", null).show();
                CarrinhoFragment carrinhoFragment = new CarrinhoFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace( R.id.frameLayout_conteiner, carrinhoFragment );
                fragmentTransaction.commit();
                getSupportActionBar().setTitle("Carrinho");
            }
        });
        */
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        //MOSTRAR OS DADOS DO PERFIL NO HEADER
        View headerView = navigationView.getHeaderView(0);
        profile_name = headerView.findViewById(R.id.profile_nome);
        profile_email = headerView.findViewById(R.id.profile_email);
        profile_foto = headerView.findViewById(R.id.profile_foto);

        //CARREGAR PRODUTOS NO HOME
        carregarProdutos();

    }

    public static void mostrarDadosHeader(){
        //BOOK
        //Usuario user = Paper.book().read("usuario");
        //profile_email.setText( user.getEmail() + " book");
        //profile_name.setText( user.getNome() + " book");
        //if( user.getFoto() != null ){
        //    Picasso.get().load( user.getFoto() ).into( profile_foto );
        //}

        DatabaseReference usuarioRef = Firebase.getDatabase()
                .child( "Usuarios" )
                .child( UsuarioFirebase.getIdUsuario() );
        usuarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                usuario = dataSnapshot.getValue( Usuario.class );

                profile_email.setText( usuario.getEmail() );
                profile_name.setText( usuario.getNome() );
                if( usuario.getFoto() != null && !usuario.getFoto().isEmpty() ){
                    Picasso.get().load( usuario.getFoto() ).into( profile_foto );
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Log.i("Usuario Logado", usuario.getNome() );
        /*
        nome = Paper.book().read( Prevalent.UserNameKey );
        celular = Paper.book().read( Prevalent.UserPhoneKey );
        foto = Paper.book().read( Prevalent.UserFotoKey );

        profile_name.setText( nome );
        profile_celular.setText( celular );
        if( foto != null && !foto.equals("") && foto.length() > 1){
            Picasso.get().load( foto ).into( profile_foto );
        }
        */

    }

    @Override
    protected void onStart() {
        super.onStart();
        HomeActivity.mostrarDadosHeader();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
            carregarProdutos();
        }else
        if (id == R.id.nav_carrinho) {
            // Handle the camera action
            CarrinhoFragment carrinhoFragment = new CarrinhoFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace( R.id.frameLayout_conteiner, carrinhoFragment );
            fragmentTransaction.commit();
            getSupportActionBar().setTitle("Carrinho");

        } else if (id == R.id.nav_pedidos) {
            PedidosFragment pedidosFragment = new PedidosFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace( R.id.frameLayout_conteiner, pedidosFragment );
            fragmentTransaction.commit();
            getSupportActionBar().setTitle("Pedidos");

        } else if (id == R.id.nav_pesquisar) {
            PesquisarFragment pesquisarFragment = new PesquisarFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace( R.id.frameLayout_conteiner, pesquisarFragment );
            fragmentTransaction.commit();
            getSupportActionBar().setTitle("Pesquisar");

        } else if (id == R.id.nav_perfil) {
            PerfilFragment perfilFragment = new PerfilFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frameLayout_conteiner, perfilFragment);
            fragmentTransaction.commit();
            getSupportActionBar().setTitle("Perfil");

        } else if (id == R.id.nav_logout) {
            deslogarUsuario();

        } else if (id == R.id.nav_publicar_produto) {
            usuarioPodePublicar();
            if( usuario != null && usuario.getFoto() != null && usuario.getEndereco() != null && usuario.getNome() != null && usuario.getTelefone() != null &&
                    !TextUtils.isEmpty(usuario.getFoto()) && !TextUtils.isEmpty(usuario.getEndereco()) && !TextUtils.isEmpty(usuario.getNome()) && !TextUtils.isEmpty(usuario.getTelefone()) ){

                Intent intentVendedor = new Intent(getApplicationContext(), VendedorActivity.class);
                startActivity( intentVendedor );
            }else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle( "Autorização" );
                builder.setMessage( "Sinto muito, você não Possui Autorização para Publicar Produtos no Aplicativo" );
                builder.setPositiveButton("OK", null);
                builder.show();
            }
        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void deslogarUsuario(){
        //DESLOGAR EMAIL E SENHA
        FirebaseAuth firebaseAuth = Firebase.getAutenticacao();
        firebaseAuth.signOut();

        //DESLOGAR GOOGLE
        GoogleSignInOptions gsm = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken( getString( R.string.default_web_client_id ) )
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient( this, gsm );
        googleSignInClient.signOut();

        Intent intentMain = new Intent(getApplicationContext(), MainActivity.class);
        intentMain.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
        startActivity( intentMain );

        finish();
    }

    private void carregarProdutos(){
        ProdutosFragment produtosFragment = new ProdutosFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace( R.id.frameLayout_conteiner, produtosFragment );
        fragmentTransaction.commit();
        getSupportActionBar().setTitle("Produtos");
    }

    private void usuarioPodePublicar(){
        DatabaseReference usuarioRef = Firebase.getDatabase()
                .child("Usuarios")
                .child( UsuarioFirebase.getIdUsuario() );

        usuarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if( dataSnapshot.exists() ){
                    usuario = dataSnapshot.getValue( Usuario.class );
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //if(usuario.getFoto() == null || usuario.getNome()  == null ||
                //usuario.getTelefone() == null || usuario.getEndereco()  == null ){
            //return false;
        //}else {
           // return true;
        //}
    }

    private void mensagem(String texto){
        Toast.makeText(getApplicationContext(), texto, Toast.LENGTH_SHORT).show();
    }
}
