package com.example.myprojetsynthese2

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.FrameLayout
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myprojetsynthese2.fragment.FragmentIndiceDeMasseCorporelle
import com.example.myprojetsynthese2.fragment.FragmentPoidsIdeal
import com.example.myprojetsynthese2.fragment.FragmentPoucentageDeMatieresGrasseas
import com.example.myprojetsynthese2.fragment.FragmentSommeilSain
import java.util.Locale

class MainActivity : AppCompatActivity() {

    // Déclaration des vues sans initialisation immédiate
    lateinit var btnSommeil: Button
    lateinit var btnPoucentageMatieresGrasses: Button
    lateinit var btnPoidsIdeal: Button
    lateinit var btnPointCorporelle: Button
    lateinit var fragment: FrameLayout
    lateinit var sLangue: Spinner

    // Liste des langues
    val listOfLanguage = listOf<String>("العربية", "English")

    // recuper la langue selecte
    val longueSelecte = intent?.getStringExtra("langue") ?: "en"

    @SuppressLint("CommitTransaction")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        // Initialisation des vues après setContentView
        btnSommeil = findViewById(R.id.btnSommeil)
        btnPoucentageMatieresGrasses = findViewById(R.id.btnPoucentageMatieresGrasses)
        btnPoidsIdeal = findViewById(R.id.btnPoidsIdeal)
        btnPointCorporelle = findViewById(R.id.btnPointCorporelle)
        fragment = findViewById(R.id.fl)
        sLangue = findViewById(R.id.sLangue)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Spinner des langues
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, listOfLanguage)
        sLangue.adapter = adapter
//        Toast.makeText(this, sLangue.selectedItem.toString() , Toast.LENGTH_SHORT).show()

        // Afficher fragment sommeil sain par défaut
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl, FragmentSommeilSain())
                .commit()
        }

        // Appel à la fonction pour changer la couleur du bouton sélectionné
        changerCouleurBoutonSelectionne(btnSommeil)

        // btnSommeil
        btnSommeil.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fl, FragmentSommeilSain())
                    .commit()
            }
            changerCouleurBoutonSelectionne(btnSommeil)
        }

        // btnPoucentageMatieresGrasses
        btnPoucentageMatieresGrasses.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fl, FragmentPoucentageDeMatieresGrasseas())
                    .commit()
            }
            changerCouleurBoutonSelectionne(btnPoucentageMatieresGrasses)
        }

        // btnPoidsIdeal
        btnPoidsIdeal.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fl, FragmentPoidsIdeal())
                    .commit()
            }
            changerCouleurBoutonSelectionne(btnPoidsIdeal)
        }

        // btnPointCorporelle
        btnPointCorporelle.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fl, FragmentIndiceDeMasseCorporelle())
                    .commit()
            }
            changerCouleurBoutonSelectionne(btnPointCorporelle)
        }

        // Initialiser le Spinner avec la langue actuelle
//        sLangue.setSelection(if (Locale.getDefault().language == "ar") 0 else 1)
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

//        Toast.makeText(this, intent.getStringExtra("selectedLangCode"), Toast.LENGTH_SHORT).show()
    }



    // Fonction pour changer dynamiquement la couleur du bouton sélectionné
    fun changerCouleurBoutonSelectionne(boutonClique: Button) {
        val boutons = listOf(btnSommeil, btnPoucentageMatieresGrasses, btnPoidsIdeal, btnPointCorporelle)

        for (bouton in boutons) {
            if (bouton == boutonClique) {
                bouton.setBackgroundColor(Color.WHITE)
                bouton.setTextColor(Color.BLACK)
            } else {
                bouton.setBackgroundColor(Color.parseColor("#136D64"))
                bouton.setTextColor(Color.WHITE)
            }
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
