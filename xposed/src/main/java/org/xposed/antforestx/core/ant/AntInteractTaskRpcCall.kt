package org.xposed.antforestx.core.ant

import org.json.JSONObject
import org.xposed.antforestx.core.util.toListJson

object AntInteractTaskRpcCall {
    // 查询任务
    const val TASK_QUERY = "alipay.content.interact.task.query"

    /**
     * 任务查询
     * 非首次：{"activityAtmosphere":"spring2024","ballList":[{"component":"liv://openapi?action=cube&component=LIFETAB_SignIn_Float&subComponent={subComponentScheme}"}],"newAudience":false,"newUser":true,"resultCode":200,"resultMsg":"成功","success":true,"tagList":[],"taskLimit":{},"taskList":[{"completed":false,"rewardParams":"{\"cp\":\"PLAY102975508\",\"er\":\"6f60b5378666114184848c66b102b6ee\",\"lti\":\"21848e5117269106535232647eb5d1\",\"osc\":0,\"ot\":\"signIn_radical\",\"s\":1,\"sc\":1,\"t\":6300002,\"ts\":1726910654,\"tt\":\"signIn\"}","taskActivityId":"PLAY102975508","taskData":{"popup":false,"taskProgress":[{"amount":"2","bigPrize":false,"completed":false,"progress":0.0,"title":"今天","today":true,"unit":"元"},{"amount":"2","bigPrize":false,"completed":false,"progress":0.0,"title":"明天","today":false,"unit":"元"},{"amount":"5","bigPrize":false,"completed":false,"progress":0.0,"title":"第3天","today":false,"unit":"元"},{"amount":"3","bigPrize":false,"completed":false,"progress":0.0,"title":"第4天","today":false,"unit":"元"},{"amount":"5","bigPrize":false,"completed":false,"progress":0.0,"title":"第5天","today":false,"unit":"元"},{"amount":"3","bigPrize":false,"completed":false,"progress":0.0,"title":"第6天","today":false,"unit":"元"},{"amount":"10","bigPrize":true,"completed":false,"progress":0.0,"title":"第7天","today":false,"unit":"元"}],"title":"https://mdn.alipayobjects.com/huamei_cf3u5z/afts/img/A*SulIQqZz_dYAAAAAAAAAAAAADseDAQ/original","totalStage":0},"taskType":"signIn","todayLimited":false},{"completed":false,"rewardParams":"{\"cp\":\"CP182326620\",\"er\":\"ba0994ed345906fba46b8947a8f24194\",\"lti\":\"21848e5117269106535232647eb5d1\",\"osc\":0,\"ot\":\"wfDayShare\",\"s\":1,\"sc\":3346,\"t\":2100001,\"ts\":1726910654,\"tt\":\"wfDayShare\"}","taskActivityId":"CP182326620","taskData":{"amount":"0.00","carry":"IdnXdJkJazTwoXKmmLff7k4JNCIzeKi668k+ytzcgeMOWWY+8MrgXVeVB17Zoq2X4MSQIQrPRxnrgA6XJEN/HQ==","ext":{"currentTime":"1726910654009","endTime":"1726934399999","newUserTag":"0"},"taskProgress":[],"totalStageCompletionCount":0,"totalStageCount":0},"taskType":"wfDayShare","todayLimited":false,"totalStage":3346},{"completed":false,"origTaskType":"radicalRed_0729new","rewardParams":"{\"cp\":\"PLAY100782501\",\"er\":\"d0e79ae9361bb6f00711878362ed158e\",\"lti\":\"21848e5117269106535232647eb5d1\",\"osc\":0,\"ot\":\"radicalRed_0729new\",\"s\":3,\"sc\":10,\"t\":7900003,\"ts\":1726910654,\"tt\":\"radicalRed\"}","taskActivityId":"PLAY100782501","taskData":{"availableAmount":"7.26","duration":10,"rewardType":"currency","stage":3,"taskProgress":[{"amount":"10.00","completed":false,"curAmount":"7.26","progress":0.72,"remainAmount":"2.74","today":false,"unit":"元"}],"unit":"元"},"taskType":"radicalRed","todayLimited":false}],"traceId":"21848e5117269106535232647eb5d1","ver":2}
     */
    suspend fun taskQuery(): Result<JSONObject> {
        return RpcUtil.requestV2("alipay.content.interact.task.query", """[{"action":"launch","pageType":"index","taskExt":"{}"}]""")
    }

    suspend fun start() : Result<JSONObject> {
        // [{"rewardParams":"{\"cp\":\"PLAY100782501\",\"er\":\"7fe99121e88218d815a1441d58d11212\",\"lti\":\"21848e5117269101778797685eb5d1\",\"osc\":0,\"ot\":\"radicalRed_0729new\",\"s\":0,\"sc\":10,\"t\":7900003,\"ts\":1726910178,\"tt\":\"radicalRed\"}","taskActivityId":"PLAY100782501","taskType":"radicalRed"}]
        val json = mapOf(
            "rewardParams" to "PLAY100782501",
            "taskActivityId" to "",
            "taskType" to "radicalRed"
        ).toListJson()
        return RpcUtil.requestV2("alipay.content.interact.task.largeshare.start", json)
    }

    /**
     * 领取奖励
     *
     */
    suspend fun reward(taskActivityId: String, rewardParams: String, taskType: String): Result<JSONObject> {
        // [{"rewardParams":"{\"cp\":\"PLAY102975508\",\"er\":\"6f60b5378666114184848c66b102b6ee\",\"lti\":\"21848e5117269106535232647eb5d1\",\"osc\":0,\"ot\":\"signIn_radical\",\"s\":1,\"sc\":1,\"t\":6300002,\"ts\":1726910654,\"tt\":\"signIn\"}","taskActivityId":"PLAY102975508","taskType":"signIn"}]
        val json = mapOf(
            "rewardParams" to rewardParams,
            "taskActivityId" to taskActivityId,
            "taskType" to taskType
        ).toListJson()
        return RpcUtil.requestV2("alipay.content.interact.task.reward", json)
    }

    suspend fun rewardVideo() :Result<JSONObject> {
        val json = """
            [
              {
                "completed": false,
                "contentId": "20240723OB020010032345695982",
                "ext": "{\"chInfo\":\"ch_life__chsub_Ndiscovery.featured\",\"contentInfo\":{\"_act_src\":\"zrtj\",\"_ad_sys\":\"contentlib\",\"_item_id\":\"20240723OB020010032345695982\",\"_item_src\":\"contentId\",\"algoTagIds\":\"A_AS2@I_BR1@I_XC1@B_CI1@B_FT1@C_LL1@Z_EX4@C_FT2@C_FT4@C_PT16@S_CS1@A_WT286@A_WT30@A_QS2@M_LS1@C_MG16@C_MH1@C_ML2@A_ZQ1@A_ZQ4@A_ZQ5@A_ZQ8@A_ZQ9@S_AS1@A_CQ33@F_CV2@C_AS2@A_EH2@A_DY247@A_DY262@A_DY263@A_DY285@G_FT1@H_DR1@H_VA5@V_PP1@A_KW54141@A_KW69276@L_SM1@A_TQ1@M_SV8@C_TN2@A_LZ000086@A_IP2@T_NE1@R_CQ11@R_CQ2@S_EE10@A_LG1@U_SV1@U_SV2@U_SV3@U_SV4@A_VQ82@A_VQ15@V_SR1@V_CL2\",\"allCateIds\":\"A_WT30@A_WT286\",\"authorId\":\"2030094058474239\",\"categoryIdL1\":\"A_WT30\",\"cityCode\":\"350100\",\"contentId\":\"20240723OB020010032345695982\",\"contentType\":\"video\",\"distributionType\":\"recommend\",\"ext\":{\"FirstTabType\":\"discovery\",\"SecondTabType\":\"discovery.featured\",\"_act_src\":\"zrtj\",\"canDisplay\":\"0\",\"canDownload\":\"1\",\"canSelectReply\":\"0\",\"canSmartCover\":\"1\",\"chInfo\":\"ch_life__chsub_Ndiscovery.featured\",\"curChInfo\":\"ch_life__chsub_Ndiscovery.featured\",\"env\":\"PROD\",\"infoSecResult\":\"null\",\"ip\":\"182.96.231.118\",\"ipLocation\":\"{\\\"areaCode\\\":\\\"30\\\",\\\"city\\\":\\\"南昌市\\\",\\\"cityCode\\\":\\\"360100\\\",\\\"country\\\":\\\"中国\\\",\\\"countryCode\\\":\\\"CN\\\",\\\"countryCode3\\\":\\\"CHN\\\",\\\"countryCodeNo\\\":\\\"156\\\",\\\"ip\\\":\\\"182.96.231.118\\\",\\\"province\\\":\\\"江西省\\\",\\\"provinceCode\\\":\\\"360000\\\"}\",\"isCache\":\"false\",\"liveTab\":\"false\",\"mcnPid\":\"2088741366717222\",\"needDoubleWriteOldContent\":\"true\",\"offerCardAtomicTMPLId\":\"LIFETAB_detail_content_offer_card_atomic\",\"offerCardAtomicTMPLVer\":\"260\",\"pageType\":\"index\",\"recResultExt\":\"{}\",\"refer\":\"discovery\",\"spmExt\":\"{\\\"_act_src\\\":\\\"zrtj\\\",\\\"_ad_sys\\\":\\\"contentlib\\\",\\\"_item_id\\\":\\\"20240723OB020010032345695982\\\",\\\"_item_src\\\":\\\"contentId\\\",\\\"distributionType\\\":\\\"recommend\\\",\\\"red_banner\\\":\\\"false\\\"}\",\"subTabType\":\"discovery.featured\",\"tabType\":\"discovery\",\"trafficSource\":\"Life\",\"viewToken\":\"f85d1b169e4c711abe6f2c993984c897\"},\"flowExpFlag\":\"exp\",\"flowFlag\":\"20TOTALITEM000_highbase@Default_highbase\",\"high_painting_style\":\"2\",\"isCollect\":\"false\",\"isLike\":\"false\",\"itemTypeDistribution\":\"5;0;0\",\"newRequestType\":\"next_screen\",\"oraScmRecmixer\":\"-|tab3_immersion_mix|-|-|-|tab3_immersion_mix_content_lives5_qianyi_v2@tab3_mix_goods_v2_cp3_zefu_15@mj_tab3_immersion_mix_lives_mj_v313\",\"red_banner\":\"false\",\"sceneAbExp\":\"adScene\",\"scm\":\"a1003.b133.video.20240723OB020010032345695982.21848e5117269101778507673eb5d1.34301.36600772.36600772.247919963+246718012+248618297+248619736+240221002.5101.-.01.immr\",\"subType\":\"3\",\"user_active\":\"1\"},\"nextContentInfo\":{\"_act_src\":\"zrtj\",\"_ad_sys\":\"contentlib\",\"_item_id\":\"20240723OB020010034045440784\",\"_item_src\":\"contentId\",\"algoTagIds\":\"A_AS2@A_BZ11@A_CQ35@A_DY466@A_DY472@A_IP2@A_KW53053@A_LG1@A_LZ000086@A_MR15@A_MR22@A_QS3@A_TQ1@A_VQ15@A_VQ18@A_WT202@A_WT25@A_ZQ1@A_ZQ4@A_ZQ5@A_ZQ8@A_ZQ9@B_CI1@B_FT1@C_AS1@C_MG5@C_ML2@C_PT13@C_TN1@F_CV2@G_FT1@H_DR1@I_BR1@I_XC1@M_LS2@S_AS1@T_NE1@V_PP1@V_SR1@A_EH2@H_VA3@S_EE14@V_CL4@U_SV1@U_SV3@U_SV4@C_FT2@C_FT4\",\"allCateIds\":\"A_WT25@A_WT202\",\"authorId\":\"2030093574742408\",\"canDisplay\":\"0\",\"canDownload\":\"1\",\"canSelectReply\":\"0\",\"canSmartCover\":\"1\",\"categoryIdL1\":\"A_WT25\",\"chInfo\":\"ch_life__chsub_Ndiscovery.featured\",\"cityCode\":\"350100\",\"contentId\":\"20240723OB020010034045440784\",\"contentType\":\"video\",\"curChInfo\":\"ch_life__chsub_Ndiscovery.featured\",\"distributionType\":\"recommend\",\"env\":\"PROD\",\"flowExpFlag\":\"exp\",\"flowFlag\":\"20TOTALITEM000_highbase@Default_highbase\",\"high_painting_style\":\"2\",\"infoSecResult\":\"null\",\"ip\":\"59.35.142.136\",\"ipLocation\":\"{\\\"areaCode\\\":\\\"80\\\",\\\"city\\\":\\\"揭阳市\\\",\\\"cityCode\\\":\\\"445200\\\",\\\"country\\\":\\\"中国\\\",\\\"countryCode\\\":\\\"CN\\\",\\\"countryCode3\\\":\\\"CHN\\\",\\\"countryCodeNo\\\":\\\"156\\\",\\\"county\\\":\\\"普宁市\\\",\\\"countyCode\\\":\\\"445281\\\",\\\"ip\\\":\\\"59.35.142.136\\\",\\\"province\\\":\\\"广东省\\\",\\\"provinceCode\\\":\\\"440000\\\"}\",\"itemTypeDistribution\":\"5;0;0\",\"mcnPid\":\"2088720007916400\",\"needDoubleWriteOldContent\":\"true\",\"newRequestType\":\"next_screen\",\"offerCardAtomicTMPLId\":\"LIFETAB_detail_content_offer_card_atomic\",\"offerCardAtomicTMPLVer\":\"260\",\"oraScmRecmixer\":\"-|tab3_immersion_mix|-|-|-|tab3_immersion_mix_content_lives5_qianyi_v2@tab3_mix_goods_v2_cp3_zefu_15@mj_tab3_immersion_mix_lives_mj_v313\",\"pageType\":\"index\",\"publicId\":\"2030093574742408\",\"recResultExt\":\"{}\",\"red_banner\":\"false\",\"refer\":\"discovery\",\"sceneAbExp\":\"adScene\",\"scm\":\"a1003.b133.video.20240723OB020010034045440784.21848e5117269101778507673eb5d1.34301.36600772.36600772.247919963+246718012+248618297+248619736+240221002.5101.-.01.immr\",\"source_type\":\"public\",\"subTabType\":\"discovery.featured\",\"subType\":\"3\",\"tabType\":\"discovery\",\"user_active\":\"1\",\"viewToken\":\"b3d159d1d6f78131f892057833552f8b\"}}",
                "hasTask": true,
                "loading": false,
                "rewardParams": "{\"cp\":\"PLAY100782501\",\"er\":\"1a4b336816c59019c916f33216b0eb33\",\"lti\":\"21848e5117269106978608355eb5d1\",\"osc\":0,\"ot\":\"radicalRed_0729new\",\"s\":3,\"sc\":10,\"t\":7900003,\"ts\":1726910698,\"tt\":\"radicalRed\"}",
                "taskActivityId": "PLAY100782501",
                "taskData": {
                  "availableAmount": "7.26",
                  "duration": 10,
                  "multiple": 0,
                  "popup": false,
                  "rewardType": "currency",
                  "stage": 3,
                  "taskProgress": [
                    {
                      "amount": "10.00",
                      "bigPrize": false,
                      "completed": false,
                      "curAmount": "7.26",
                      "remainAmount": "2.74",
                      "unit": "元"
                    }
                  ],
                  "totalStage": 0,
                  "unit": "元",
                  "vv": 0
                },
                "taskExt": "{}",
                "taskType": "radicalRed"
              }
            ]
        """.trimIndent()
        return RpcUtil.requestV2("alipay.content.interact.task.reward", json)
    }
}