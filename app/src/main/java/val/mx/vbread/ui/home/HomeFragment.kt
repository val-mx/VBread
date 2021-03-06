package `val`.mx.vbread.ui.home

import `val`.mx.vbread.R
import `val`.mx.vbread.adapters.AttributeAdapter
import `val`.mx.vbread.containers.Dimension
import `val`.mx.vbread.ui.popups.ColorPickerBottomSheet
import `val`.mx.vbread.ui.popups.FractalPickerPopUp
import `val`.mx.vbread.views.FractalView
import `val`.mx.vbread.views.FractalView.adapter
import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SeekBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_home.*
import java.math.BigDecimal
import java.util.*

class HomeFragment : Fragment(), SeekBar.OnSeekBarChangeListener {


    // DONE : 01.11.2020 FRAKTALAUSWAHL VERBESSERN done

    // DIese Klasse dient ausschließlich dem Zweck die Interaktiven Komponenten zu managen

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    private lateinit var seekBar: SeekBar
    private lateinit var editablesInflater: ImageView
    lateinit var editables: ConstraintLayout

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fractalView1 = view.findViewById(R.id.fractalView)
        seekBar = view.findViewById(R.id.seekBar)
        editables = view.findViewById(R.id.editables)
        editablesInflater = view.findViewById(R.id.value_editor_inflater)

        // Nicht hinterfragen

//        adView.loadAd(AdRequest.Builder().build())

        editablesInflater.setOnClickListener {

            if (editables.visibility == VISIBLE) {
                editablesInflater.setImageDrawable(resources.getDrawable(R.drawable.ic_baseline_build_24))
                editables.visibility = GONE
            } else {
                editables.visibility = VISIBLE
                editablesInflater.setImageDrawable(resources.getDrawable(R.drawable.ic_baseline_fullscreen_24))
            }
            fractalView.adapter = adapter
        }

        palette_picker.setOnClickListener {

            ColorPickerBottomSheet(fractalView).show(childFragmentManager, "picker")

        }

        button.setOnClickListener {
            try {
                onResult(
                    BigDecimal(posXEditText.text.toString()),
                    BigDecimal(posYEditText.text.toString()),
                    BigDecimal(posAusschnittEditText.text.toString()),
                    Integer.parseInt(iterations.text.toString())
                )
            } catch (ex: Exception) {
                Snackbar.make(requireView(), "Fehler bei Eingabe. Bitte wiederholen.", 1)
                ex.printStackTrace()
            }
        }

        reset_button.setOnClickListener {
            fractalView.adapter.dimension = null
            fractalView.adapter = adapter
            updateUI()
        }

        icon_save.setOnClickListener {
            if (fractalView.bitmap != null) {

                val imgName = UUID.randomUUID().toString()

                MediaStore.Images.Media.insertImage(
                    view.context.contentResolver,
                    fractalView.bitmap,
                    imgName,
                    ""
                )

                Snackbar.make(view, "Bild als $imgName.png gespeichert!", Snackbar.LENGTH_LONG)
                    .show()

            }
        }

        fractal_picker_icon.setOnClickListener {
            FractalPickerPopUp(fractalView).show(childFragmentManager, "ok")
        }


        seekBar.setOnSeekBarChangeListener(this)

        fractalView.homeFragment = this
    }

    companion object Companion {
        private lateinit var fractalView1: FractalView

        fun onResult(
            xs: BigDecimal,
            ys: BigDecimal,
            ye: BigDecimal,
            iteras: Int,
        ) {
            if (adapter == null) return

            val d = Dimension(
                (xs),
                (xs.subtract(ye)),
                ys,
                (ys.subtract(ye))
            )


            adapter!!.run {
                setItera(iteras)
                dimension = d
            }

            fractalView1.adapter = adapter

        }
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {}

    override fun onStartTrackingTouch(seekBar: SeekBar?) {}

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        if (adapter == null) return
        adapter!!.itera = seekBar!!.progress * 3
        fractalView.adapter = adapter!!
    }

    fun initParamSliders() {

        attributeRecycler.layoutManager = LinearLayoutManager(context)
        attributeRecycler.adapter = AttributeAdapter(context,fractalView);
    }



    fun updateUI() {
        posYEditText.setText(adapter!!.dimension.top.toDouble().toString())
        posXEditText.setText(adapter!!.dimension.left.toDouble().toString())
        iterations.setText(adapter.itera.toString())
        posAusschnittEditText.setText(
            adapter!!.dimension.left.subtract(adapter!!.dimension.right).abs().toFloat().toString()
        )
    }
}



