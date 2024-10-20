package org.xposed.antforestx.core.ant

import kotlinx.serialization.json.JsonObject
import org.json.JSONObject
import org.xposed.antforestx.core.ant.RpcUtil
import org.xposed.antforestx.core.ant.RpcUtil.requestV2
import org.xposed.antforestx.core.util.DateUtils
import org.xposed.antforestx.core.util.RandomUtils
import org.xposed.antforestx.core.util.toListJson


object AntMemberRpcCall {
    private val uniqueId: String
        get() = System.currentTimeMillis().toString() + RandomUtils.nextLong()

    /* ant member point */
    suspend fun queryPointCert(page: Int, pageSize: Int): Result<JSONObject> {
        val args1 = "[{\"page\":$page,\"pageSize\":$pageSize}]"
        return requestV2("alipay.antmember.biz.rpc.member.h5.queryPointCert", args1)
    }

    /**
     * 收取待领取的积分
     */
    suspend fun receivePointByUser(certId: String): Result<JSONObject> {
        // 单个收取 [{"bizSource":"myTab","logicCertId":"N_11580910590","sourcePassMap":{"innerSource":"","passInfo":"{\"tc\":\"MEMBER_CLAIM_POINT\"}","source":"myTab","unid":""}}]
        val json = mapOf(
            "bizSource" to "myTab",
            "logicCertId" to certId,
            "sourcePassMap" to mapOf(
                "innerSource" to "",
                "passInfo" to "{\"tc\":\"MEMBER_CLAIM_POINT\"}",
                "source" to "myTab",
                "unid" to ""
            )
        ).toListJson()
        return requestV2("com.alipay.alipaymember.biz.rpc.pointcert.h5.receivePointByUser", json)
    }

    /**
     * 一键收取待领取的积分
     */
    suspend fun receiveAllPointByUser():Result<JSONObject> {
        // [{"bizSource":"myTab","sourcePassMap":{"innerSource":"","passInfo":"{\"tc\":\"MEMBER_CLAIM_POINT\"}","source":"myTab","unid":""}}]
        val json = mapOf(
            "bizSource" to "myTab",
            "sourcePassMap" to mapOf(
                "innerSource" to "",
                "passInfo" to "{\"tc\":\"MEMBER_CLAIM_POINT\"}",
                "source" to "myTab",
                "unid" to ""
            )
        ).toListJson()
        return requestV2("com.alipay.alipaymember.biz.rpc.pointcert.h5.receiveAllPointByUser", json)
    }

    /**
     * 补签卡信息
     */
    private suspend fun queryReSignInCardInfo(): Result<JSONObject> {
        return requestV2("com.alipay.amic.biz.rpc.signin.h5.queryReSignInCardInfo", "[{}]")
    }

    /**
     * 签到日历
     */
    suspend fun queryMemberSigninCalendar(): Result<JSONObject> {
        // [{"autoSignIn":true,"chInfo":"memberHomePage_myTab","invitorUserId":"","sceneCode":"QUERY"}]
        val json = mapOf(
            "autoSignIn" to true,
            "chInfo" to "memberHomePage_myTab",
            "invitorUserId" to "",
            "sceneCode" to "QUERY"
        ).toListJson()
        return requestV2("com.alipay.amic.biz.rpc.signin.h5.queryMemberSigninCalendar", json)
    }

    /**
     * 查询月签到日历
     * @param month 如: 202409
     */
    suspend fun queryMemberSigninCalendarMonth(month: String): Result<JSONObject> {
        // [{"autoSignIn":true,"chInfo":"memberHomePage_myTab","invitorUserId":"","month":"202409","sceneCode":"QUERY","sourcePassMap":{"innerSource":"","source":"myTab","unid":""}}]
        val json = mapOf(
            "autoSignIn" to true,
            "chInfo" to "memberHomePage_myTab",
            "invitorUserId" to "",
            "month" to month,
            "sceneCode" to "QUERY",
            "sourcePassMap" to mapOf(
                "innerSource" to "",
                "source" to "myTab",
                "unid" to ""
            )
        ).toListJson()
        return requestV2("com.alipay.amic.biz.rpc.signin.h5.queryMemberSigninCalendar", json)
    }

    /**
     * 会员签到
     */
    suspend fun memberSignin(): Result<JSONObject> {
        //  [{"sourcePassMap":{"innerSource":"","source":"myTab","unid":""}}]
        val json = mapOf(
            "sourcePassMap" to mapOf(
                "innerSource" to "",
                "source" to "myTab",
                "unid" to ""
            )
        ).toListJson()
        return requestV2("com.alipay.amic.biz.rpc.signin.h5.memberSignin", json)
    }

    /**
     * 查询节分收取
     */
    suspend fun queryPointCertV2(): Result<JSONObject> {
        // [{"dbId":0,"pageSize":20,"sourcePassMap":{"innerSource":"","source":"myTab","unid":""}}]
        val json = mapOf(
            "dbId" to 0,
            "pageSize" to 20,
            "sourcePassMap" to mapOf(
                "innerSource" to "",
                "source" to "myTab",
                "unid" to ""
            )
        ).toListJson()
        return requestV2("com.alipay.alipaymember.biz.rpc.pointcert.h5.queryPointCertV2", json)
    }

    /**
     * 所有任务完成状态
     */
    suspend fun queryAllStatusTaskList(): Result<JSONObject> {
        return requestV2("alipay.antmember.biz.rpc.membertask.h5.queryAllStatusTaskList", """[{"sourceBusiness":"signInAd"}]""")
    }

    /* 安心豆 */
    suspend fun pageRender(): Result<JSONObject> {
        return requestV2(
            "com.alipay.insplatformbff.common.insiopService.pageRender",
            "[\"INS_PLATFORM_BLUEBEAN\",{\"channelType\":\"insplatform_mobilesearch_anxindou\"}]"
        )
    }

    suspend fun taskProcess(appletId: String): Result<JSONObject> {
        return requestV2("com.alipay.insmarketingbff.task.taskProcess", "[{\"appletId\":\"$appletId\"}]")
    }

    suspend fun taskTrigger(appletId: String, scene: String): Result<JSONObject> {
        return requestV2(
            "com.alipay.insmarketingbff.task.taskTrigger",
            "[{\"appletId\":\"$appletId\",\"scene\":\"$scene\"}]"
        )
    }

    suspend fun queryUserAccountInfo(): Result<JSONObject> {
        return requestV2(
            "com.alipay.insmarketingbff.point.queryUserAccountInfo",
            "[{\"channel\":\"insplatform_mobilesearch_anxindou\",\"pointProdCode\":\"INS_BLUE_BEAN\",\"pointUnitType\":\"COUNT\"}]"
        )
    }

    suspend fun exchangeDetail(itemId: String): Result<JSONObject> {
        return requestV2(
            "com.alipay.insmarketingbff.onestop.planTrigger",
            "[{\"extParams\":{\"itemId\":\"" + itemId
                    + "\"},\"planCode\":\"bluebean_onestop\",\"planOperateCode\":\"exchangeDetail\"}]"
        )
    }

    suspend fun exchange(itemId: String, pointAmount: Int): Result<JSONObject> {
        return requestV2(
            "com.alipay.insmarketingbff.onestop.planTrigger",
            "[{\"extParams\":{\"itemId\":\"$itemId\",\"pointAmount\":\"$pointAmount\"},\"planCode\":\"bluebean_onestop\",\"planOperateCode\":\"exchange\"}]"
        )
    }

    /* 芝麻信用 */
    suspend fun queryHome(): Result<JSONObject> {
        return requestV2(
            "com.antgroup.zmxy.zmcustprod.biz.rpc.home.api.HomeV6RpcManager.queryHome",
            "[{\"miniZmGrayInside\":\"\"}]"
        )
    }

    suspend fun queryCreditFeedback(): Result<JSONObject> {
        return requestV2(
            "com.antgroup.zmxy.zmcustprod.biz.rpc.home.creditaccumulate.api.CreditAccumulateRpcManager.queryCreditFeedback",
            "[{\"queryPotential\":false,\"size\":20,\"status\":\"UNCLAIMED\"}]"
        )
    }

    suspend fun collectCreditFeedback(creditFeedbackId: String): Result<JSONObject> {
        return requestV2(
            "com.antgroup.zmxy.zmcustprod.biz.rpc.home.creditaccumulate.api.CreditAccumulateRpcManager.collectCreditFeedback",
            "[{\"collectAll\":false,\"creditFeedbackId\":\"$creditFeedbackId\",\"status\":\"UNCLAIMED\"}]"
        )
    }

    /* 商家服务 */

    suspend fun merchantQuery(): Result<JSONObject> {
        // [{"pageSource":"alipay_merchant_home","parameters":{"_cdp_extinfo":"","appId":"2021002117633826","bizScenario":"","chInfo":"ch_mrch_point","last_eventid":"no_eventid","mytabCreativeId":"","query":"","sourceEventId":"","tinyappVersion":"2024100101","token":"","utParams":""}}]
        val json = mapOf(
            "pageSource" to "alipay_merchant_home",
            "parameters" to mapOf(
                "_cdp_extinfo" to "",
                "appId" to "2021002117633826",
                "bizScenario" to "",
                "chInfo" to "ch_mrch_point",
                "last_eventid" to "no_eventid",
                "mytabCreativeId" to "",
                "query" to "",
                "sourceEventId" to "",
            )
        ).toListJson()
        return requestV2("alipay.merchant.artisan.component.query", json)
    }

    /**
     * 商家服务签到查询
     */
    suspend fun merchantSignInQuery(): Result<JSONObject> {
        return requestV2("alipay.mrchservbase.mrchpoint.ball.query.v1", "[{}]")
    }

    /**
     * 商家积分签到
     */
    suspend fun merchantSignInExecute(): Result<JSONObject> {
        return requestV2("alipay.mrchservbase.mrchpoint.sqyj.homepage.signin.v1", "[{}]")
    }

    /**
     * 商家积分任务
     */
    suspend fun merchantTaskListQuery(): Result<JSONObject> {
        return requestV2("alipay.mrchservbase.zcj.taskList.query.v2", """[{"taskItemCode":""}]""")
    }

    /**
     * 商家服务-接受任务
     */
    suspend fun merchantTaskReceive(taskCode: String): Result<JSONObject> {
        // [{"compId":"ZTS_TASK_RECEIVE","extInfo":{"taskCode":"BBNCLLRWX_TASK"}}]
        val json = mapOf(
            "compId" to "ZTS_TASK_RECEIVE",
            "extInfo" to mapOf(
                "taskCode" to taskCode
            )
        ).toListJson()
        return requestV2("alipay.mrchservbase.sqyj.task.receive", json)
    }

    /**
     * 商家服务-收取积分
     */
    suspend fun merchantBallReceive(ballIds: List<String>): Result<JSONObject> {
        // [{"ballIds":["20240928016660011204754109965402"],"channel":"MRCH_SELF","outBizNo":"9c3b4f6d-2763-469e-a6bd-64377f5bbfc8"}]
        val json = mapOf(
            "ballIds" to ballIds,
            "channel" to "MRCH_SELF",
            "outBizNo" to uniqueId
        ).toListJson()
        return requestV2("alipay.mrchservbase.mrchpoint.ball.receive", json)
    }


    suspend fun transcodeCheck(): Result<JSONObject> {
        return requestV2(
            "alipay.mrchservbase.mrchbusiness.sign.transcode.check",
            "[{}]"
        )
    }

    suspend fun zcjSignInQuery(): Result<JSONObject> {
        return requestV2(
            "alipay.mrchservbase.zcj.view.invoke",
            "[{\"compId\":\"ZCJ_SIGN_IN_QUERY\"}]"
        )
    }

    suspend fun zcjSignInExecute(): Result<JSONObject> {
        return requestV2(
            "alipay.mrchservbase.zcj.view.invoke",
            "[{\"compId\":\"ZCJ_SIGN_IN_EXECUTE\"}]"
        )
    }

    suspend fun taskListQuery(): Result<JSONObject> {
        return requestV2(
            "alipay.mrchservbase.zcj.taskList.query",
            "[{\"compId\":\"ZCJ_TASK_LIST\",\"params\":{\"activityCode\":\"ZCJ\",\"clientVersion\":\"10.3.36\",\"extInfo\":{},\"platform\":\"Android\",\"underTakeTaskCode\":\"\"}}]"
        )
    }

    suspend fun queryActivity(): Result<JSONObject> {
        return requestV2(
            "alipay.merchant.kmdk.query.activity",
            "[{\"scene\":\"activityCenter\"}]"
        )
    }

    suspend fun signIn(activityNo: String): Result<JSONObject> {
        return requestV2(
            "alipay.merchant.kmdk.signIn",
            "[{\"activityNo\":\"$activityNo\"}]"
        )
    }

    suspend fun signUp(activityNo: String): Result<JSONObject> {
        return requestV2(
            "alipay.merchant.kmdk.signUp",
            "[{\"activityNo\":\"$activityNo\"}]"
        )
    }

    /* 商家服务任务 */
    suspend fun taskFinish(bizId: String): Result<JSONObject> {
        return requestV2(
            "com.alipay.adtask.biz.mobilegw.service.task.finish",
            "[{\"bizId\":\"$bizId\"}]"
        )
    }

    suspend fun taskReceive(taskCode: String): Result<JSONObject> {
        return requestV2(
            "alipay.mrchservbase.sqyj.task.receive",
            "[{\"compId\":\"ZTS_TASK_RECEIVE\",\"extInfo\":{\"taskCode\":\"$taskCode\"}}]"
        )
    }

    suspend fun actioncode(actionCode: String): Result<JSONObject> {
        return requestV2(
            "alipay.mrchservbase.task.query.by.actioncode",
            "[{\"actionCode\":\"$actionCode\"}]"
        )
    }

    suspend fun produce(actionCode: String): Result<JSONObject> {
        return requestV2(
            "alipay.mrchservbase.biz.task.action.produce",
            "[{\"actionCode\":\"$actionCode\"}]"
        )
    }


    /**
     * 会员任务列表
     */
    suspend fun signPageTaskList(): Result<JSONObject> {
        return requestV2(
            "alipay.antmember.biz.rpc.membertask.h5.signPageTaskList",
            """[{"sourceBusiness":"antmember","spaceCode":"ant_member_xlight_task"}]"""
        )
    }

    /**
     * 接受任务
     * @param darwinName 任务名称
     * @param taskConfigId 任务ID
     */
    suspend fun applyTask(darwinName: String, oraScm: String, taskConfigId: Long): Result<JSONObject> {
        // [{"darwinExpParams":{"darwinName":"逛一逛蚂蚁森林"},"oraScm":"member_task_mix_sort|arec2_task_common_rec|-|TASK_BIZ_TYPE_EXPOSURE@member_task#TASK_BIZ_TYPE_CLICK@member_task#item_id@600202400067468#SINGLE_TASK_EXPOSURE@600202400067468#SINGLE_TASK_CLICK@600202400067468#SINGLE_TASK_COMPLETE@600202400067468#TASK_BIZ_TYPE_COMPLETE@member_task|1.0","sourcePassMap":{"innerSource":"","source":"myTab","unid":""},"taskConfigId":600202400067468}]
        // [{"darwinExpParams":{"darwinName":"逛一逛蚂蚁新村"},"sourcePassMap":{"innerSource":"","source":"myTab","unid":""},"taskConfigId":600202300025603}]
        val json = mapOf(
            "darwinExpParams" to mapOf("darwinName" to darwinName),
            "oraScm" to oraScm,
            "sourcePassMap" to mapOf("innerSource" to "", "source" to "myTab", "unid" to ""),
            "taskConfigId" to taskConfigId
        ).toListJson()
        return requestV2("alipay.antmember.biz.rpc.membertask.h5.applyTask", json)
    }

    suspend fun executeTask2(taskConfigId: String, targetBusiness: String): Result<JSONObject> {
        // [{"bizOutNo":"20240921","bizParam":"留真_签到feeds1阶段发积分","bizSubType":"15S","bizType":"BROWSE",
        // "sourcePassMap":{"innerSource":"","source":"myTab","unid":""},"syncProcess":true,"taskConfigId":"35500005"}]
        // BROWSE#15S#留真_签到feeds1阶段发积分
        val (bizType, bizSubType, bizParam) = parseBusiness(targetBusiness)

        val json = mapOf(
            "bizOutNo" to DateUtils.getYearMonthDay(),
            "bizParam" to bizParam,
            "bizSubType" to bizSubType,
            "bizType" to bizType,
            "sourcePassMap" to mapOf(
                "innerSource" to "",
                "source" to "myTab",
                "unid" to ""
            ),
            "syncProcess" to true,
            "taskConfigId" to taskConfigId
        ).toListJson()
        return requestV2("alipay.antmember.biz.rpc.membertask.h5.executeTask", json)
    }

    suspend fun executeTask(targetBusiness: String): Result<JSONObject> {
        // [{"bizOutNo":1726905590764,"bizParam":"wb-hxy912460-1714441811","bizSubType":"UNLIMITED","bizType":"BROWSE","sourcePassMap":{"innerSource":"","source":"myTab","unid":""}}]
        val (bizType, bizSubType, bizParam) = parseBusiness(targetBusiness)
        val json = mapOf(
            "bizOutNo" to (System.currentTimeMillis() - 16000L).toString(),
            "bizParam" to bizParam,
            "bizSubType" to bizSubType,
            "bizType" to bizType,
            "sourcePassMap" to mapOf(
                "innerSource" to "",
                "source" to "myTab",
                "unid" to ""
            )
        ).toListJson()

        return requestV2("alipay.antmember.biz.rpc.membertask.h5.executeTask", json)
    }

    private fun parseBusiness(targetBusiness: String): Array<String> {
        val result = Array(3) { "" }
        try {
            val splits = targetBusiness.split("#")
            result[0] = splits[0]
            result[1] = splits[1]
            result[2] = splits[2]
        } catch (ignored: Exception) {

        }
        return result
    }

    /**
     * 查询芝麻粒
     */
    suspend fun getUserPotential(): Result<JSONObject> {
        // com.antgroup.zmxy.zmmemberop.biz.rpc.creditaccumulate.CreditAccumulateRpcManager.getUserPotential
        // [{"includeCurrent":true,"includeFreeze":true,"includeHistory":true,"includeMonthClaimed":true,"includeMonthDisappear":true,"includeMonthUsed":true}]
        val json = mapOf(
            "includeCurrent" to true,
            "includeFreeze" to true,
            "includeHistory" to true,
            "includeMonthClaimed" to true,
            "includeMonthDisappear" to true,
            "includeMonthUsed" to true
        ).toListJson()
        return requestV2("com.antgroup.zmxy.zmmemberop.biz.rpc.creditaccumulate.CreditAccumulateRpcManager.getUserPotential", json)
    }

    /**
     * 芝麻粒任务列表
     */
    suspend fun queryListV3(): Result<JSONObject> {
        return requestV2(
            "com.antgroup.zmxy.zmmemberop.biz.rpc.creditaccumulate.CreditAccumulateStrategyRpcManager.queryListV3",
            "[{}]"
        )
    }

    /**
     * 芝麻粒任务点击去完成
     */
    suspend fun joinActivity(templateId: String): Result<JSONObject> {
        val json = """[{"chInfo":"seasameList","joinFromOuter":false,"templateId":"$templateId"}]"""
        return requestV2("com.antgroup.zmxy.zmmemberop.biz.rpc.promise.PromiseRpcManager.joinActivity", json)
    }

    /**
     * 芝麻粒-我的-守约记录-服务中
     */
    suspend fun getList(): Result<JSONObject> {
        // [{"bizScene":"","categoryCode":"","currentPage":1,"kaAppId":"","needCount":true,"nextQueryOffSet":"","nextQueryRange":"","pageSize":20,"statusCode":"INIT","targetId":"","version":2}]
        val json = mapOf(
            "bizScene" to "",
            "categoryCode" to "",
            "currentPage" to 1,
            "kaAppId" to "",
            "needCount" to true,
            "nextQueryOffSet" to "",
            "nextQueryRange" to "",
            "pageSize" to 20,
            "statusCode" to "INIT",
            "targetId" to "",
            "version" to 2
        ).toListJson()
        return requestV2("com.antgroup.zmxy.zmcustprod.biz.rpc.creditrecord.api.CreditRecordV5RpcManager.getList", json)
    }
}
