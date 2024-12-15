package br.edu.ifsp.dmo.game.model

import kotlin.random.Random

object WeaponGenerator {
    private val weapons = listOf<Weapon>(
        Rock,
        Paper,
        Scissors
    )

    fun generate(): Weapon {
        val randomPosition: Int = Random.nextInt(0, weapons.size)
        return weapons.get(randomPosition)
    }

}