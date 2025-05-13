package com.example.myprojetsynthese2.fragment

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.transition.Visibility
import com.example.myprojetsynthese2.R
import java.text.SimpleDateFormat
import java.util.*

class FragmentSommeilSain : Fragment() {

    private lateinit var clSommeil_2_fragment: ConstraintLayout
    private lateinit var champAge: EditText
    private lateinit var timePickerReveil: TimePicker
    private lateinit var boutonCalculer: Button
    private lateinit var texteDureeSommeil: TextView
    private lateinit var texteHeureCoucher: TextView
    private lateinit var cyclesTextes: List<TextView>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_sommeil_sain, container, false)

    @SuppressLint("DefaultLocale")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clSommeil_2_fragment = view.findViewById(R.id.clSommeil_2_fragment)
        champAge = view.findViewById(R.id.etAgeSommeil_fragment)
        timePickerReveil = view.findViewById(R.id.tPSommeil_fragment)
        boutonCalculer = view.findViewById(R.id.btnCalculateSommeil_fragment)
        texteDureeSommeil = view.findViewById(R.id.tvDureeSommeilRecommende_Fragment)
        texteHeureCoucher = view.findViewById(R.id.tvHeureCoucherConsecutive_Fragment)
        timePickerReveil.setIs24HourView(true)


        //
        cyclesTextes = listOf(
            view.findViewById(R.id.tvCoucher1_fragment),
            view.findViewById(R.id.tvCoucher2_fragment),
            view.findViewById(R.id.tvCoucher3_fragment),
            view.findViewById(R.id.tvCoucher4_fragment),
            view.findViewById(R.id.tvCoucher5_fragment),
            view.findViewById(R.id.tvCoucher6_fragment)
        )

        boutonCalculer.setOnClickListener {
            val ageStr = champAge.text.toString()
            val age = ageStr.toIntOrNull()

            // Vérifier si l'age est valide
            if (age == null ) {
                champAge.error = getString(R.string.entrez_age_valide)
                Toast.makeText(requireContext(), getString(R.string.entrez_age_valide), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val heureReveil = timePickerReveil.hour
            val minuteReveil = timePickerReveil.minute

            val heuresSommeil = when {
                age < 5 -> 11
                age < 13 -> 10
                age < 18 -> 9
                age < 65 -> 8
                else -> 7
            }

            val calendrier = Calendar.getInstance()
            calendrier.set(Calendar.HOUR_OF_DAY, heureReveil)
            calendrier.set(Calendar.MINUTE, minuteReveil)
            calendrier.add(Calendar.HOUR_OF_DAY, -heuresSommeil)

            val heureCoucher = String.format("%02d:%02d", calendrier.get(Calendar.HOUR_OF_DAY), calendrier.get(Calendar.MINUTE))

            texteDureeSommeil.text = getString(R.string.duree_sommeil_heures, heuresSommeil)

            texteHeureCoucher.text = heureCoucher

            //Toast.makeText(requireContext(), getString(R.string.toast_heure_coucher, heureCoucher), Toast.LENGTH_LONG).show()

            calculerCyclesSommeil(heureReveil, minuteReveil)

            //
            clSommeil_2_fragment.visibility = view.visibility
        }
    }

    @SuppressLint("SetTextI18n")
    private fun calculerCyclesSommeil(heureReveil: Int, minuteReveil: Int) {
        val format = SimpleDateFormat("HH:mm", Locale.getDefault())

        // Commence avec l'heure de réveil
        val calendrierBase = Calendar.getInstance()
        calendrierBase.set(Calendar.HOUR_OF_DAY, heureReveil)
        calendrierBase.set(Calendar.MINUTE, minuteReveil)

        // Affiche 6 cycles de 90 minutes
        for (i in 0 until 6) {
            // On clone le calendrier de base pour ne pas le modifier à chaque tour
            val calendrierCycle = calendrierBase.clone() as Calendar

            // On calcule combien de minutes on doit soustraire (1 cycle = 90 min)
            val minutesASoustraire = (6 - i) * 90
            calendrierCycle.add(Calendar.MINUTE, -minutesASoustraire)

            // Format de l'heure
            val heureFormattee = format.format(calendrierCycle.time)

            // Mise à jour du texte
            cyclesTextes[i].text = getString(R.string.cycle_sommeil_format, 6 - i, heureFormattee)
        }
    }
}
