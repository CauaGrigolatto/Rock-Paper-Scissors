package br.edu.ifsp.dmo.game.model

class Player(val name: String) {
    var points: Int = 0
        private set

    fun addPoint() {
        points += 1
    }
}