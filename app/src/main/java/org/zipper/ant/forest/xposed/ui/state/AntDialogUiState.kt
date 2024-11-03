package org.zipper.ant.forest.xposed.ui.state

sealed interface AntDialogUiState

// 不显示
data object None : AntDialogUiState

// 收取间隔
data object CollectInterval : AntDialogUiState

// 收取限制
data object CollectLimit : AntDialogUiState

// 不收取好友弹窗
data object UnCollectFriend : AntDialogUiState

// 双击卡使用时间
data object DoublePropTime : AntDialogUiState

// 双击卡次数
data object DoublePropCount : AntDialogUiState

// 送好友道具弹窗
data object SendFriendProp : AntDialogUiState

// 送道具好友
data object SendPropToFriend : AntDialogUiState

data object ExchangeDoubleLimit : AntDialogUiState
data object ExchangeShieldLimit : AntDialogUiState

// 农场遣返弹窗
data object ManorRepatriateDialogState : AntDialogUiState

// 不遣返好友弹窗
data object ManorUnRepatriateFriendDialogState : AntDialogUiState

// 召回类型
data object ManorRecallTypeDialogState : AntDialogUiState

// 捐蛋数量
data object DonateEggDialogState : AntDialogUiState

// 喂鸡好友弹窗
data object ManorFeedFriendDialogState : AntDialogUiState

// 农场施肥次数弹窗
data object ManorFarmFertilizeDialogState : AntDialogUiState

data object AlipayCustomStep : AntDialogUiState