package br.edu.ifsp.dmo.game.view

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import br.edu.ifsp.dmo.game.R
import br.edu.ifsp.dmo.game.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        configToolBar()
        configSpinner()
        configListener()
    }

    private fun configListener() {
        binding.buttonStartMultiplayerGame.setOnClickListener { startMultiplayerGame() }
        binding.buttonStartSinglePlayerGame.setOnClickListener { startSinglePlayerGame() }
    }
    private fun configSpinner() {
        val adapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(R.array.game_types)
        )
        binding.spinnerButtles.adapter = adapter
    }
    private fun configToolBar() {
        // Esconder a ActionBar se ela existir no tema.
        supportActionBar?.hide()
    }
    private fun startMultiplayerGame() {
        val rounds = getRounds()
        val mIntent = Intent(this, MultiplayerMatchActivity::class.java)
        mIntent.putExtra(Constants.KEY_PLAYER_1, binding.edittextPlayer1.text.toString())
        mIntent.putExtra(Constants.KEY_PLAYER_2, binding.edittextPlayer2.text.toString())
        mIntent.putExtra(Constants.KEY_ROUNDS, rounds)
        startActivity(mIntent)
    }

    private fun startSinglePlayerGame() {
        val rounds = getRounds()
        val mIntent = Intent(this, SinglePlayerMatchActivity::class.java)
        mIntent.putExtra(Constants.KEY_ROUNDS, rounds)
        startActivity(mIntent)
    }

    private fun getRounds(): Int {
        return when (binding.spinnerButtles.selectedItemPosition) {
            0 -> 1
            1 -> 3
            else -> 5
        }
    }
}