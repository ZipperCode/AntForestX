package org.xposed.antforestx.core.ant

import kotlinx.serialization.json.JsonObject
import org.json.JSONObject
import org.xposed.antforestx.core.ant.RpcUtil.request
import org.xposed.antforestx.core.util.RandomUtils


object AntMemberRpcCall {
    private val uniqueId: String
        get() = System.currentTimeMillis().toString() + RandomUtils.nextLong()

    /* ant member point */
    suspend fun queryPointCert(page: Int, pageSize: Int): Result<JSONObject> {
        val args1 = "[{\"page\":$page,\"pageSize\":$pageSize}]"
        return request("alipay.antmember.biz.rpc.member.h5.queryPointCert", args1)
    }

    suspend fun receivePointByUser(certId: String): Result<JSONObject> {
        val args1 = "[{\"certId\":$certId}]"
        return request("alipay.antmember.biz.rpc.member.h5.receivePointByUser", args1)
    }

    suspend fun queryMemberSigninCalendar(): Result<JSONObject> {
        return request(
            "com.alipay.amic.biz.rpc.signin.h5.queryMemberSigninCalendar",
            "[{\"autoSignIn\":true,\"invitorUserId\":\"\",\"sceneCode\":\"QUERY\"}]"
        )
    }

    /* 安心豆 */
    suspend fun pageRender(): Result<JSONObject> {
        return request(
            "com.alipay.insplatformbff.common.insiopService.pageRender",
            "[\"INS_PLATFORM_BLUEBEAN\",{\"channelType\":\"insplatform_mobilesearch_anxindou\"}]"
        )
    }

    suspend fun taskProcess(appletId: String): Result<JSONObject> {
        return request("com.alipay.insmarketingbff.task.taskProcess", "[{\"appletId\":\"$appletId\"}]")
    }

    suspend fun taskTrigger(appletId: String, scene: String): Result<JSONObject> {
        return request(
            "com.alipay.insmarketingbff.task.taskTrigger",
            "[{\"appletId\":\"$appletId\",\"scene\":\"$scene\"}]"
        )
    }

    suspend fun queryUserAccountInfo(): Result<JSONObject> {
        return request(
            "com.alipay.insmarketingbff.point.queryUserAccountInfo",
            "[{\"channel\":\"insplatform_mobilesearch_anxindou\",\"pointProdCode\":\"INS_BLUE_BEAN\",\"pointUnitType\":\"COUNT\"}]"
        )
    }

    suspend fun exchangeDetail(itemId: String): Result<JSONObject> {
        return request(
            "com.alipay.insmarketingbff.onestop.planTrigger",
            "[{\"extParams\":{\"itemId\":\"" + itemId
                    + "\"},\"planCode\":\"bluebean_onestop\",\"planOperateCode\":\"exchangeDetail\"}]"
        )
    }

    suspend fun exchange(itemId: String, pointAmount: Int): Result<JSONObject> {
        return request(
            "com.alipay.insmarketingbff.onestop.planTrigger",
            "[{\"extParams\":{\"itemId\":\"$itemId\",\"pointAmount\":\"$pointAmount\"},\"planCode\":\"bluebean_onestop\",\"planOperateCode\":\"exchange\"}]"
        )
    }

    /* 芝麻信用 */
    suspend fun queryHome(): Result<JSONObject> {
        return request(
            "com.antgroup.zmxy.zmcustprod.biz.rpc.home.api.HomeV6RpcManager.queryHome",
            "[{\"miniZmGrayInside\":\"\"}]"
        )
    }

    suspend fun queryCreditFeedback(): Result<JSONObject> {
        return request(
            "com.antgroup.zmxy.zmcustprod.biz.rpc.home.creditaccumulate.api.CreditAccumulateRpcManager.queryCreditFeedback",
            "[{\"queryPotential\":false,\"size\":20,\"status\":\"UNCLAIMED\"}]"
        )
    }

    suspend fun collectCreditFeedback(creditFeedbackId: String): Result<JSONObject> {
        return request(
            "com.antgroup.zmxy.zmcustprod.biz.rpc.home.creditaccumulate.api.CreditAccumulateRpcManager.collectCreditFeedback",
            "[{\"collectAll\":false,\"creditFeedbackId\":\"$creditFeedbackId\",\"status\":\"UNCLAIMED\"}]"
        )
    }

    /* 商家服务 */
    suspend fun transcodeCheck(): Result<JSONObject> {
        return request(
            "alipay.mrchservbase.mrchbusiness.sign.transcode.check",
            "[{}]"
        )
    }

    suspend fun zcjSignInQuery(): Result<JSONObject> {
        return request(
            "alipay.mrchservbase.zcj.view.invoke",
            "[{\"compId\":\"ZCJ_SIGN_IN_QUERY\"}]"
        )
    }

    suspend fun zcjSignInExecute(): Result<JSONObject> {
        return request(
            "alipay.mrchservbase.zcj.view.invoke",
            "[{\"compId\":\"ZCJ_SIGN_IN_EXECUTE\"}]"
        )
    }

    suspend fun taskListQuery(): Result<JSONObject> {
        return request(
            "alipay.mrchservbase.zcj.taskList.query",
            "[{\"compId\":\"ZCJ_TASK_LIST\",\"params\":{\"activityCode\":\"ZCJ\",\"clientVersion\":\"10.3.36\",\"extInfo\":{},\"platform\":\"Android\",\"underTakeTaskCode\":\"\"}}]"
        )
    }

    suspend fun queryActivity(): Result<JSONObject> {
        return request(
            "alipay.merchant.kmdk.query.activity",
            "[{\"scene\":\"activityCenter\"}]"
        )
    }

    suspend fun signIn(activityNo: String): Result<JSONObject> {
        return request(
            "alipay.merchant.kmdk.signIn",
            "[{\"activityNo\":\"$activityNo\"}]"
        )
    }

    suspend fun signUp(activityNo: String): Result<JSONObject> {
        return request(
            "alipay.merchant.kmdk.signUp",
            "[{\"activityNo\":\"$activityNo\"}]"
        )
    }

    /* 商家服务任务 */
    suspend fun taskFinish(bizId: String): Result<JSONObject> {
        return request(
            "com.alipay.adtask.biz.mobilegw.service.task.finish",
            "[{\"bizId\":\"$bizId\"}]"
        )
    }

    suspend fun taskReceive(taskCode: String): Result<JSONObject> {
        return request(
            "alipay.mrchservbase.sqyj.task.receive",
            "[{\"compId\":\"ZTS_TASK_RECEIVE\",\"extInfo\":{\"taskCode\":\"$taskCode\"}}]"
        )
    }

    suspend fun actioncode(actionCode: String): Result<JSONObject> {
        return request(
            "alipay.mrchservbase.task.query.by.actioncode",
            "[{\"actionCode\":\"$actionCode\"}]"
        )
    }

    suspend fun produce(actionCode: String): Result<JSONObject> {
        return request(
            "alipay.mrchservbase.biz.task.action.produce",
            "[{\"actionCode\":\"$actionCode\"}]"
        )
    }

    suspend fun ballReceive(ballIds: String): Result<JSONObject> {
        return request(
            "alipay.mrchservbase.mrchpoint.ball.receive",
            "[{\"ballIds\":[\"" + ballIds
                    + "\"],\"channel\":\"MRCH_SELF\",\"outBizNo\":\"" + uniqueId + "\"}]"
        )
    }

    /* 会员任务 */
    suspend fun signPageTaskList(): Result<JSONObject> {
        return request(
            "alipay.antmember.biz.rpc.membertask.h5.signPageTaskList",
            "[{\"sourceBusiness\":\"antmember\",\"spaceCode\":\"ant_member_xlight_task\"}]"
        )
    }

    suspend fun applyTask(darwinName: String, taskConfigId: Long): Result<JSONObject> {
        return request(
            "alipay.antmember.biz.rpc.membertask.h5.applyTask",
            "[{\"darwinExpParams\":{\"darwinName\":\"" + darwinName
                    + "\"},\"sourcePassMap\":{\"innerSource\":\"\",\"source\":\"myTab\",\"unid\":\"\"},\"taskConfigId\":"
                    + taskConfigId + "}]"
        )
    }

    suspend fun executeTask(bizParam: String, bizSubType: String): Result<JSONObject> {
        return request(
            "alipay.antmember.biz.rpc.membertask.h5.executeTask",
            "[{\"bizOutNo\":\"" + (System.currentTimeMillis() - 16000L).toString() + "\",\"bizParam\":\""
                    + bizParam + "\",\"bizSubType\":\"" + bizSubType + "\",\"bizType\":\"BROWSE\"}]"
        )
    }
}
