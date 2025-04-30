package com.example.firebaseapp.ui.theme

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firebaseapp.MainActivity
import com.example.firebaseapp.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        auth = FirebaseAuth.getInstance()

        // Configurar Google SignIn para el logout
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Configurar botón de cerrar sesión
        findViewById<Button>(R.id.logoutButton).setOnClickListener {
            signOut()
        }

        // Mostrar información del usuario
        val user = auth.currentUser
        user?.let {
            findViewById<TextView>(R.id.welcomeText).text = "¡Bienvenido, ${it.email}!"
            Toast.makeText(this, "¡Bienvenido, ${it.email}!", Toast.LENGTH_LONG).show()
        }
    }

    private fun signOut() {
        // Cerrar sesión en Firebase
        auth.signOut()

        // Cerrar sesión en Google
        googleSignInClient.signOut().addOnCompleteListener(this) {
            // Redirigir a MainActivity y limpiar el stack
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}