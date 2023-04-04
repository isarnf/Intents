package com.example.intents

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.intents.MainActivity.Constantes.PARAMETRO_EXTRA
import com.example.intents.databinding.ActivityParametroBinding

class ParametroActivity : AppCompatActivity() {

    private val apb: ActivityParametroBinding by lazy {
        ActivityParametroBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(apb.root)

        intent.getStringExtra(PARAMETRO_EXTRA)?.let { parametro: String ->
            apb.parametroEt.setText(parametro)
        } //funcao let que recebe um lambda e pode devolver um tipo diferente de String, evite ter um if checando nulidade, pois ela só acontece se o que está anteriormente (intent) não for nulo. Se for nulo, ela não é executada. Caso contrário, deveríamos fazer da forma abaixo comentada:

//        val parametro: String? = intent.getStringExtra(PARAMETRO_EXTRA)
//        if (parametro != null){
//            apb.parametroEt.setText(parametro)
//        }

        apb.enviarParametroBt.setOnClickListener{
            val parametro: String = apb.parametroEt.text.toString()
            val retornoIntent: Intent = Intent()

            retornoIntent.putExtra(PARAMETRO_EXTRA, parametro)
            setResult(RESULT_OK, retornoIntent)
            finish()

        }

    }
}