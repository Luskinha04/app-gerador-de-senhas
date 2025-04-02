package com.lucas.app_gerador_de_senhas

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var senhaGerada: TextView
    private lateinit var switchUpper: Switch
    private lateinit var switchLower: Switch
    private lateinit var switchNumbers: Switch
    private lateinit var switchSymbols: Switch
    private lateinit var switchExcludeSimilar: Switch
    private lateinit var seekBar: SeekBar
    private lateinit var txtTamanho: TextView
    private lateinit var btnGenerate: Button
    private lateinit var btnRefresh: ImageButton
    private lateinit var btnCopy: ImageButton

    private val letrasMaiusculas = "ABCDEFGHJKLMNPQRSTUVWXYZ"
    private val letrasMinusculas = "abcdefghijkmnopqrstuvwxyz"
    private val numeros = "123456789"
    private val simbolos = "!@#\$%&*()-_=+[]{};:,.<>?/"
    private val similares = "iIlLoO0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        senhaGerada = findViewById(R.id.txtSenha)
        switchUpper = findViewById(R.id.switchUpper)
        switchLower = findViewById(R.id.switchLower)
        switchNumbers = findViewById(R.id.switchNumbers)
        switchSymbols = findViewById(R.id.switchSymbols)
        switchExcludeSimilar = findViewById(R.id.switchSimilar)
        seekBar = findViewById(R.id.seekBar)
        txtTamanho = findViewById(R.id.txtTamanho)
        btnGenerate = findViewById(R.id.btnGenerate)
        btnRefresh = findViewById(R.id.btnRefresh)
        btnCopy = findViewById(R.id.btnCopy)

        txtTamanho.text = seekBar.progress.toString()

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                txtTamanho.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        btnGenerate.setOnClickListener {
            val senha = gerarSenha()
            senhaGerada.text = senha
        }

        btnRefresh.setOnClickListener {
            val senha = gerarSenha()
            senhaGerada.text = senha
        }

        btnCopy.setOnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("senha", senhaGerada.text)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(this, "Senha copiada!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun gerarSenha(): String {
        var caracteres = ""

        if (switchUpper.isChecked) caracteres += letrasMaiusculas
        if (switchLower.isChecked) caracteres += letrasMinusculas
        if (switchNumbers.isChecked) caracteres += numeros
        if (switchSymbols.isChecked) caracteres += simbolos

        if (switchExcludeSimilar.isChecked) {
            caracteres = caracteres.filterNot { similares.contains(it) }
        }

        if (caracteres.isEmpty()) {
            Toast.makeText(this, "Selecione ao menos uma opção!", Toast.LENGTH_SHORT).show()
            return ""
        }

        val tamanho = seekBar.progress
        val senha = StringBuilder()
        val usado = mutableSetOf<Char>()

        repeat(tamanho) {
            var c: Char
            var tentativa = 0
            do {
                c = caracteres[Random.nextInt(caracteres.length)]
                tentativa++
            } while (c in usado && tentativa < 10)
            senha.append(c)
            usado.add(c)
        }

        return senha.toString()
    }
}
