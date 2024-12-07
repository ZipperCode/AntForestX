package org.zipper.ant.forest.xposed.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.xposed.antforestx.core.util.log.FileLogcatProvider
import org.zipper.ant.forest.xposed.ui.state.AlipayHomeUiState
import org.zipper.antforestx.data.bean.AlipayUser
import org.zipper.antforestx.data.bean.AlipayUserData
import org.zipper.antforestx.data.bean.AntForestPropData
import org.zipper.antforestx.data.bean.FileBean
import org.zipper.antforestx.data.provider.StoreFileProvider
import org.zipper.antforestx.data.repository.IAntDataRepository
import org.zipper.antforestx.data.repository.IAntStatisticsRepository
import org.zipper.antforestx.data.repository.ILogcatRepository
import java.io.File

class AntDataViewModel : ViewModel(), KoinComponent {
    private val antDataRepository: IAntDataRepository by inject<IAntDataRepository>()
    private val antStatisticsRepository: IAntStatisticsRepository by inject<IAntStatisticsRepository>()
    private val logcatRepository: ILogcatRepository by inject<ILogcatRepository>()

    val alipayUsers: StateFlow<AlipayUserData> = antDataRepository.alipayUserDataFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = AlipayUserData()
    )

    val homeUiState: StateFlow<AlipayHomeUiState> =
        combine(alipayUsers, antStatisticsRepository.antForestStatisticsDayFlow) { userData, statistics ->
            AlipayHomeUiState(
                displayUser = userData.values.firstOrNull { it.isMine }?.displayName ?: "unknown",
                friendCount = userData.size - 1,
                dayCollectEnergy = statistics.collectedEnergy,
                dayVitality = statistics.vitality,
                dayWatering = statistics.watering,
                dayHelpEnergy = statistics.helpEnergy,
                dayStepNum = statistics.stepNum
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = AlipayHomeUiState("unknown")
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

    private val _fileList: MutableStateFlow<List<FileBean>> = MutableStateFlow(emptyList())
    val fileList: StateFlow<List<FileBean>> = _fileList

    fun loadFileList() {
        getFileList(StoreFileProvider.logcatRootDir)
    }

    fun getFileList(directory: File) {
        viewModelScope.launch {
            logcatRepository.getFileList(directory).collect {
                _fileList.value = it
            }
        }
    }

    private val _openFileState: MutableState<Boolean> = mutableStateOf(false)
    val openFileState: State<Boolean> = _openFileState

    private val _fileContent: MutableState<List<String>> = mutableStateOf(emptyList())
    val fileContent: State<List<String>> = _fileContent

    fun openFile(file: File) {
        viewModelScope.launch {
            _openFileState.value = true
            logcatRepository.openFile(file).collect {
                _fileContent.value = it
            }
        }
    }

    fun closeOpenFile(){
        _fileContent.value = emptyList()
        _openFileState.value = false
    }
}