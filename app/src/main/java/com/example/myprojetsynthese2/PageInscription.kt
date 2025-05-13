package com.example.myprojetsynthese2

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Locale

class PageInscription : AppCompatActivity() {

    // Déclaration des vues paresseuse
    lateinit var sLangue: Spinner
    lateinit var btnCreateAccount: Button
    lateinit var tvLogin: TextView


    // Liste des langues
    val listOfLanguage = listOf<String>("العربية", "English")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_page_inscription)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main2)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialisation des vues après setContentView
        sLangue = findViewById(R.id.sLangue)
        btnCreateAccount = findViewById(R.id.btnCreateAccount)
        tvLogin = findViewById(R.id.tvLogin)

        // Spinner des langues
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, listOfLanguage)
        sLangue.adapter = adapter

        // Initialiser le Spinner avec la langue actuelle
        sLangue.setSelection(if (intent.getStringExtra("selectedLangCode") == "ar") 0 else 1)

        // Listener du Spinner
        sLangue.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedLangCode = if (position == 0) "ar" else "en"
                // Vérifier si la langue sélectionnée est différente de la langue actuelle
                if (Locale.getDefault().language != selectedLangCode) {
                    changerLangue(selectedLangCode)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })

        // Aller a l'activite principale MainActivity et y ajouter la langue choisie
        btnCreateAccount.setOnClickListener {
            val selectedLangCode = if (sLangue.selectedItemPosition == 0) "ar" else "en"
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("langue", selectedLangCode)  // Passer la langue avec l'Intent
            startActivity(intent)

            finish()
        }

        // Aller a l'activite PageLogin
        tvLogin.setOnClickListener{
            val intent = Intent(this, PageLogin::class.java)
            startActivity(intent)
            //
            finish()
        }
    }


    // Fonction pour changer la langue
    fun changerLangue(codeLangue: String) {
        val locale = Locale(codeLangue)
        Locale.setDefault(locale)

        val config = Configuration()


        val localeWithLatinNumbers = Locale.Builder()
            .setLocale(locale)
            //.setUnicodeLocaleKeyword("nu", "latn")
            .build()

        config.setLocale(localeWithLatinNumbers)
        config.setLayoutDirection(localeWithLatinNumbers)

        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)
        recreate()
    }
}