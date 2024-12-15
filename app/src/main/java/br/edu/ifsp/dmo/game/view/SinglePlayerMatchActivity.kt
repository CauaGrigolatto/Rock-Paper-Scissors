package br.edu.ifsp.dmo.game.view

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import br.edu.ifsp.dmo.game.R
import br.edu.ifsp.dmo.game.databinding.ActivityMultiplayerMatchBinding
import br.edu.ifsp.dmo.game.databinding.ActivitySinglePlayerMatchBinding
import br.edu.ifsp.dmo.game.model.Match
import br.edu.ifsp.dmo.game.model.Player
import br.edu.ifsp.dmo.game.model.Scissors
import br.edu.ifsp.dmo.game.model.Weapon
import br.edu.ifsp.dmo.game.model.WeaponGenerator

class SinglePlayerMatchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySinglePlayerMatchBinding
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private lateinit var match: Match
    private var playerWeapon: Weapon? = null
    private var botWeapon: Weapon? = null

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySinglePlayerMatchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        openBundle()
        updateUI()
        configListener()
        configResultLauncher()
    }
    private fun battle() {
        setBotWeapon()

        val winner: Player?

        if (playerWeapon != null) {
            winner = match.toPlay(playerWeapon!!, botWeapon!!)

            if (winner != null) {
                Toast.makeText(
                    this,
                    "${getString(R.string.winner)} ${winner.name}",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                Toast.makeText(
                    this,
                    getString(R.string.draw),
                    Toast.LENGTH_LONG
                ).show()
            }

            playerWeapon = null
            botWeapon = null
            updateScoreBoard()

            if (!match.hasButtles()) {
                proclaimWinner()
            }
        } else {
            val name = match.player1.name

            Toast.makeText(
                this,
                "$name${getString(R.string.choose_gun_player)}",
                Toast.LENGTH_LONG
            ).show()
        }
    }
    private fun configListener() {
        binding.buttonWeapon.setOnClickListener { startSelectionActivity(1) }
        binding.buttonFight.setOnClickListener { battle() }
        binding.buttonClose.setOnClickListener { finish() }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun configResultLauncher() {
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())  {
                result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val extras = result.data?.extras
                    if (extras != null) {
                        val number = extras.getInt(Constants.KEY_PLAYER_NUMBER)

                        val chosenWeapon: Weapon = extras.getSerializable(
                            Constants.KEY_WEAPON,
                            Weapon::class.java
                        ) as Weapon

                        if (number == 1)  playerWeapon = chosenWeapon
                    }
                }
            }
    }
    private fun openBundle() {
        val extras = intent.extras
        if (extras != null) {
            val player = Constants.PLAYER
            val bot = Constants.BOT
            val rounds = extras.getInt(Constants.KEY_ROUNDS)
            match = Match(rounds, player, bot)
        }
    }
    private fun proclaimWinner() {
        val str = "${match.getWinner().name} ${getString(R.string.won_the_match)}"
        binding.buttonWeapon.visibility = View.GONE
        binding.buttonFight.visibility = View.GONE
        binding.buttonClose.visibility = View.VISIBLE
        binding.textviewReport.visibility = View.VISIBLE
        binding.textviewReport.text = str
    }
    private fun startSelectionActivity(number: Int) {
        val name = match.player1.name
        val mIntent = Intent(this, SelectionActivity::class.java)
        mIntent.putExtra(Constants.KEY_PLAYER_NUMBER, number)
        mIntent.putExtra(Constants.KEY_PLAYER_NAME, name)
        resultLauncher.launch(mIntent)
    }
    private fun updateScoreBoard() {
        binding.textviewScore1.text = "${match.player1.points}"
        binding.textviewScore2.text = "${match.player2.points}"
    }
    private fun updateUI() {
        val str = "${match.player1.name} X ${match.player2.name}"
        actionBar?.setTitle(str)

        binding.labelPlayer.text = match.player1.name
        binding.labelBot.text = match.player2.name
        updateScoreBoard()

        binding.buttonWeapon.text = "${match.player1.name} ${getString(R.string.gun_selection)}"
    }

    private fun setBotWeapon() {
        botWeapon = WeaponGenerator.generate()
    }
}