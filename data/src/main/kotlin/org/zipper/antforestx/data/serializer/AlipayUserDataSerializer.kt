package org.zipper.antforestx.data.serializer

import kotlinx.serialization.serializer
import org.zipper.antforestx.data.bean.AlipayUserData

class AlipayUserDataSerializer : BaseDataStoreSerializer<AlipayUserData>(
    serializer()
) {
    override val defaultValue: AlipayUserData
        get() = AlipayUserData()
}