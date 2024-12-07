package org.zipper.ant.forest.xposed.ui.state

data class AlipayHomeUiState(
    val displayUser: String,
    val friendCount: Int = 0,
    val dayCollectEnergy: Int = 0,
    val dayVitality: Int = 0,
    val dayWatering: Int = 0,
    val dayHelpEnergy: Int = 0,
    val dayStepNum: Int = 0
)