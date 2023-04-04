package com.example.intents

import android.Manifest.permission.CALL_PHONE
import android.content.Intent
import android.content.Intent.*
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import com.example.intents.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    companion object Constantes { //singleton de constantes
        const val PARAMETRO_EXTRA: String = "PARAMETRO_EXTRA"
//        const val PARAMETRO_ACTIVITY_REQUEST_CODE = 0

    }

    private lateinit var parl : ActivityResultLauncher<Intent> //parametro activity result launcher
    private lateinit var permissaoChamadaArl: ActivityResultLauncher<String> //as permissoes sao do tipo string
    private lateinit var pegarImagemArl: ActivityResultLauncher<Intent> // para pegar imagem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)

        parl = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                    if (result?.resultCode == RESULT_OK){
                        val retorno: String = result.data?.getStringExtra(PARAMETRO_EXTRA) ?: ""
                        amb.parametroTv.text = retorno
                    }
        }


//        parl = registerForActivityResult(ActivityResultContracts.StartActivityForResult(),
//            object: ActivityResultCallback<ActivityResult>{
//                override fun onActivityResult(result: ActivityResult?) {
//                    if (result?.resultCode == RESULT_OK){
//                        val retorno: String = result.data?.getStringExtra(PARAMETRO_EXTRA) ?: ""
//                        amb.parametroTv.text = retorno
//
//                    }
//                }
//            })

//        amb.entrarParametroBt.setOnClickListener {
//            val parametroIntent: Intent = Intent(this, ParametroActivity::class.java)
//            parametroIntent.putExtra(PARAMETRO_EXTRA, amb.parametroTv.text.toString())
//            parl.launch(parametroIntent)
//            //startActivityForResult(parametroIntent, PARAMETRO_ACTIVITY_REQUEST_CODE)
//        }
//    }

        amb.entrarParametroBt.setOnClickListener {
            val parametroIntent: Intent = Intent("BOLO_CHOCOLATE_ACTION")
            parametroIntent.putExtra(PARAMETRO_EXTRA, amb.parametroTv.text.toString())
            parl.launch(parametroIntent)
        }

        permissaoChamadaArl = registerForActivityResult(ActivityResultContracts.RequestPermission()){permissaoConcedida ->
            if (permissaoConcedida){
                //chamar o numero
                chamarNumero(true)

            }else{
                Toast.makeText(this, "Permissão necessária para continuar", Toast.LENGTH_SHORT).show()
                finish()
            }

        }

        pegarImagemArl = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ resultado ->
            if(resultado.resultCode == RESULT_OK) {
                //pega a imagem retornada
                val imagemUri = resultado.data?.data
                imagemUri?.let{// let = só é executada quando for diferente de nulo
                    amb.parametroTv.text = it.toString()
                    //abrindo imagem para visualização
                    val visualizarImagemIntent = Intent(ACTION_VIEW, it)
                    startActivity(visualizarImagemIntent)
                }
            }

        }

    }



//    override fun onActivityResult(requestCode:Int, resultCode: Int, data: Intent?){
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == PARAMETRO_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) { //result_ok só vai acontecer se clicar no botão; se clicar no botão de voltar o resultado será result_cancelled
//            val retorno: String = data?.getStringExtra(PARAMETRO_EXTRA) ?: ""
//            amb.parametroTv.text = retorno
//        }
//    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean { //faz aparecer (cria) o menu na tela
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean { //chamado sempre que uma opcao do menu for clicado; true = selecionou a opcao, false = desprezou a opcao
        return when(item.itemId) {
            R.id.viewMi -> { //nao funciona com www, precisa ter o http
                val url: Uri = Uri.parse(amb.parametroTv.text.toString())
                val navegadorIntent: Intent = Intent(Intent.ACTION_VIEW, url)
                startActivity(navegadorIntent)
                true
            }
            R.id.callMi -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    //checar a permissao
                    if (checkSelfPermission(CALL_PHONE) == PERMISSION_GRANTED){//retorna se a permissão foi dada ou não
                        //chamar o número
                        chamarNumero(true)
                    }else{
                        //solicitar permissão
                        permissaoChamadaArl.launch(CALL_PHONE)

                    }
                }else{
                    //chamar o numero
                    chamarNumero(chamar = true)
                }
                true
            } //call faz a chamada sem interacao com o usuario
            R.id.dialMi-> {
                chamarNumero(chamar = false)
                true
            }//dial abre o discador e o usuario que permite a chamada
            R.id.pickMi -> {
                val pegarImagemIntent: Intent = Intent(ACTION_PICK)
                val diretorioImagens = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).path //retorna o diretório de armazenamento público de imagens
                pegarImagemIntent.setDataAndType(Uri.parse(diretorioImagens), "image/*")
                pegarImagemArl.launch(pegarImagemIntent)

                true
            } //selecionar uma imagem da galeria
            R.id.chooserMi -> {
                val url: Uri = Uri.parse(amb.parametroTv.text.toString())
                val navegadorIntent: Intent = Intent(Intent.ACTION_VIEW, url)
                val escolherAppIntent: Intent = Intent(Intent.ACTION_CHOOSER)
                escolherAppIntent.putExtra(EXTRA_TITLE, "Escolha seu navegador preferido")
                escolherAppIntent.putExtra(EXTRA_INTENT, navegadorIntent)
                startActivity(escolherAppIntent)
                true
            }
            else -> false
        }
    }

    private fun chamarNumero(chamar: Boolean){
        val numeroUri: Uri = Uri.parse("tel: ${amb.parametroTv.text}")
        val chamarIntent: Intent = Intent(if(chamar) ACTION_CALL else ACTION_DIAL)
        chamarIntent.data = numeroUri
        startActivity(chamarIntent)
    }



}




















