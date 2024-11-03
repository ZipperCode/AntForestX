package org.zipper.ant.forest.xposed.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.zipper.antforestx.data.bean.AlipayUser
import org.zipper.antforestx.data.bean.AlipayUserData
import org.zipper.antforestx.data.bean.AntForestPropData
import org.zipper.antforestx.data.repository.IAntDataRepository

class AntDataViewModel : ViewModel(), KoinComponent {
    private val antDataRepository: IAntDataRepository by inject<IAntDataRepository>()

    val alipayUsers: StateFlow<AlipayUserData> = antDataRepository.alipayUserDataFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = AlipayUserData()
    )

    val alipayUserList: StateFlow<List<AlipayUser>> = antDataRepository.alipayUserDataFlow.map {
        it.values.toList()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = emptyList()
    )

    val forestPropList: StateFlow<AntForestPropData> = antDataRepository.forestPropDataFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = AntForestPropData()
    )

    fun test() {
        viewModelScope.launch {
            antDataRepository.updateAlipayUserData {
                it
            }
        }
    }
}