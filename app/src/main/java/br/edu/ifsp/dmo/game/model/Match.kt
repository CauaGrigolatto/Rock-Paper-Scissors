package br.edu.ifsp.dmo.game.model

class Match(buttles: Int, player1: String, player2: String) {
    private var buttle: Int = 0
    private var foughtButtle: Int = 0
    val player1: Player = Player(player1)
    val player2: Player = Player(player2)

    init {
        buttle = (if (buttles > 0) buttles else 1)
    }

    fun getWinner(): Player {
        if (hasButtles()) {
            throw MatchNotFinishedException("The match has not finishe yet.")
        } else {
            return if (player1.points > player2.points) {
                player1
            } else {
                player2
            }
        }
    }

    fun toPlay(weaponPlayer1: Weapon, weaponPlayer2: Weapon): Player? {
        var winner: Player? = null
        if (hasButtles()) {
            if (weaponPlayer1 != weaponPlayer2) {
                winner = when {
                    weaponPlayer1 is Rock && weaponPlayer2 is Scissors -> player1
                    weaponPlayer1 is Scissors && weaponPlayer2 is Paper -> player1
                    weaponPlayer1 is Paper && weaponPlayer2 is Rock -> player1
                    else -> player2
                }

                winner.addPoint()
                foughtButtle += 1
            } else {
                winner = null
            }
        }
        return winner
    }

    fun hasButtles() = foughtButtle < buttle
}

class MatchNotFinishedException(msg: String) : Exception(msg)
