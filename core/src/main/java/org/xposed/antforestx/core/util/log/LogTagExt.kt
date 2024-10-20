package org.xposed.antforestx.core.util.log

import timber.log.Timber


val AntForestTag = Marker("蚂蚁森林", true)

val AntManorTag = Marker("蚂蚁庄园", true)

val AntMemberTag = Marker("蚂蚁会员", true)

fun Timber.Forest.ancient(): Timber.Tree = tag("保护古树🎐")

fun Timber.Forest.readBook(): Timber.Tree = tag("阅读任务📖")

fun Timber.Forest.waterCooperate(): Timber.Tree = tag("合种浇水🚿")

fun Timber.Forest.ocean(): Timber.Tree = tag("神奇海洋🐳")

fun Timber.Forest.manor(): Timber.Tree = tag(AntManorTag.value)

fun Timber.Forest.antForest(): Timber.Tree = tag(AntForestTag.value)