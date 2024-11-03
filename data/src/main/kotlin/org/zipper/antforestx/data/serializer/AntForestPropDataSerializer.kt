package org.zipper.antforestx.data.serializer

import kotlinx.serialization.serializer
import org.zipper.antforestx.data.bean.AntForestPropData

class AntForestPropDataSerializer: BaseDataStoreSerializer<AntForestPropData>(
    serializer()
) {

    override val defaultValue: AntForestPropData
        get() = AntForestPropData()
}