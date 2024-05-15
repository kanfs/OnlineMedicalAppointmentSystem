<template>
        <!-- 右侧内容 #start -->
        <div class="page-container">
            <div class="order-detail">
                <div class="title">挂号详情</div>
                <div class="status-bar">
                    <div class="left-wrapper">
                        <div class="status-wrapper BOOKING_SUCCESS">
                            <img
                                src="../../assets/image/shield-check-line.png"
                                style="color:blue;margin-right: 18px;"
                            ></img>
                            <span v-if="order.status  == 0" >预约成功，待支付</span>
                            <span v-if="order.status  == 1" style="color: #67c23a">预约成功，已支付</span>
                            <span v-if="order.status  == 2" style="color: red">取消预约，已退订</span>
                            <span v-if="order.status  == 3" style="color: #485698">预约过期</span>
                        </div>
                    </div>
                    <div class="right-wrapper">
                        <img
                            src="../../assets/image/code_order_detail.png"
                            class="code-img"
                        />
                        <div class="content-wrapper">
                            <div>
                                微信<img src="../../assets/image/wechat-fill.png" style="color:green"></img>
                                关注“OMAS智能医疗预约平台”
                            </div>
                            <div class="watch-wrapper">快速挂号，轻松就医</div>
                        </div>
                    </div>
                </div>
                <div class="info-wrapper">
                    <div class="title-wrapper">
                        <div class="block"></div>
                        <div>挂号信息</div>
                    </div>
                    <div class="info-form">
                        <el-form ref="form" :model="order">
                            <el-form-item label="就诊人：">
                                <div class="content">
                                    <span>{{ order.patientName }}</span>
                                </div>
                            </el-form-item>
                            <el-form-item label="联系电话：">
                                <div class="content">
                                <span>{{ order.phone }}</span>
                                </div>
                            </el-form-item>
                            <el-form-item label="就诊日期：">
                                <div class="content">
                                    <span>{{ order.workDate }}</span>
                                </div>
                            </el-form-item>
                            <el-form-item label="就诊医院：">
                                <div class="content">
                                    <span>{{ order.hospitalName }} </span>
                                </div>
                            </el-form-item>
                            <el-form-item label="就诊科室：">
                                <div class="content">
                                    <span>{{ order.departmentName }} </span>
                                </div>
                            </el-form-item>
                          <!-- <el-form-item label="就诊门诊：">
                            <div class="content">
                              <span>{{ order.patientName }} </span>
                            </div>
                          </el-form-item> -->
                            <el-form-item label="医生姓名：">
                                <div class="content">
                                <span>{{ order.doctorName }} </span>
                                </div>
                            </el-form-item>
                            <!-- <el-form-item label="医生职称：">
                                <div class="content">
                                    <span>{{ order.position}} </span>
                                </div>
                            </el-form-item> -->
                            <el-form-item label="医事服务费：">
                                <div class="content">
                                    <div class="fee">
                                        ￥{{ order.price }}元
                                    </div>
                                </div>
                            </el-form-item>
                          <el-form-item label="预约号序：">
                            <div class="content">
                              <span>{{ order.number }} </span>
                            </div>
                          </el-form-item>
                          <!-- <el-form-item label="取号地点：">
                            <div class="content">
                              <span>{{ order.fetchAddress }} </span>
                            </div>
                          </el-form-item>
                            <el-form-item label="挂号单号：">
                                <div class="content">
                                    <span>{{ order.outTradeNo }} </span>
                                </div>
                            </el-form-item> -->
                            <el-form-item label="预约时间：">
                                <div class="content">
                                    <span>{{ order.workDate }}</span>
                                </div>
                            </el-form-item>
                            <!-- <el-form-item label="退号时间：">
                                <div class="content">
                                <span>{{ order.quitTime }}</span>
                                </div>
                            </el-form-item> -->
                        </el-form>
                    </div>
                </div>

              <!-- <div class="info-wrapper" style="margin-bottom: 40px">
                <div class="title-wrapper">
                  <div class="block"></div>
                  <div>就诊状态</div>
                </div>
                <div class="info-form">
                  <el-form ref="form" :model="visit">
                    <el-form-item label="就诊状态：">
                      <el-tag v-if="visit.visitStatus == 'INIT'"
                      >待就诊</el-tag
                      >
                      <el-tag
                        v-if="visit.visitStatus == 'SUCCESS'"
                        type="success"
                      >已就诊</el-tag
                      >
                      <el-tag
                        v-if="visit.visitStatus == 'BREAK'"
                        type="warning"
                      >爽约</el-tag
                      >
                      <el-tag
                        v-if="visit.visitStatus == 'UNSUBSCRIBE'"
                        type="danger"
                      >退订</el-tag
                      >
                    </el-form-item>
                  </el-form>
                </div>
              </div> -->

                <!-- <div class="rule-wrapper mt40">
                    <div class="rule-title">注意事项</div>
                    <div>
                        1、请确认就诊人信息是否准确，若填写错误将无法取号就诊，损失由本人承担；<br />
                        <span style="color: red"
                            >2、【取号】就诊当天需在{{
                                order.fetchTime
                            }}在医院取号，未取号视为爽约，该号不退不换；</span><br />
                        3、【退号】在就诊前一天挂号时间内可在线退号
                        ，逾期将不可办理退号退费；<br />
                        4、挂号支持自费患者使用身份证预约，同时支持地区社保卡在平台预约挂号。请于就诊当日，携带预约挂号所使用的有效身份证件到院取号；<br />
                        5、请注意医保患者在住院期间不能使用社保卡在门诊取号。
                    </div>
                </div> -->
                <div
                    class="bottom-wrapper mt60"
                    v-if=" order.status == 0 || order.status == 1"
                >
                    <div class="button-wrapper">
                        <div class="v-button white" style="width: 120px" @click="cancelOrder()">
                            取消预约
                        </div>
                    </div>
                    <div
                        class="button-wrapper ml20"
                        v-if="order.status  == 0"
                    >
                        <div class="v-button" style="width: 120px" @click="pay()">支付</div>
                    </div>
                </div>
            </div>
        </div>
        <!-- 右侧内容 #end -->
        <!-- 微信支付弹出框 -->
        <el-dialog
            v-model="dialogPayVisible"
            style="text-align: left"
            :append-to-body="true"
            width="500px"
            :before-close="cancelPay"
        >
            <div class="container">
                <div class="operate-view" style="height: 350px">
                    <div class="wrapper wechat">
                        <div>
                            <div ref="qrcode" style="margin-left:130px;"></div>
                            <!-- <img :src="qrcode" alt="" style="margin-left:130px;width:200px;height:200px;"/> -->
                            <div
                                style="
                                    text-align: center;
                                    line-height: 25px;
                                    margin-top:20px;
                                    margin-bottom: 20px;
                                "
                            >
                                请使用微信扫一扫<br />
                                扫描二维码支付
                            </div>
                        </div>
                        <el-button type="info" @click="cancelPay" style="margin-left:180px;"> 取消支付</el-button>
                        <!-- <el-button type="primary" @click="confirmPay"> 确认支付</el-button> -->
                    </div>
                </div>
            </div>
        </el-dialog>
    <!-- footer -->
</template>

<script>
import {ref, reactive, getCurrentInstance, onBeforeMount, onBeforeUnmount, nextTick} from 'vue';
import Cookies from 'js-cookie';
import QRCode from 'qrcodejs2';
export default{
    setup(){
        const ctx = getCurrentInstance().appContext.config.globalProperties;
        var order = reactive({});
        var dialogPayVisible = ref(false);
        var qrcode = ref();
        var timer = null;
        onBeforeMount(async() => {
            if( Cookies.get("token") == null ){
                ctx.$message.error("用户信息过时，请重新登录！")
                ctx.$router.push("/home")
            }else{
                order.id = ctx.$route.params.orderId;
                var userId = Cookies.get("userId");
                var flag = await checkRightUser(userId, order.id)
                if( flag ){
                    getOrderInfo(order.id);
                }
                    
                else{
                    ctx.$message.error("非法访问，请重新进入！")
                    ctx.$router.push("/home")
                }
            }
           
        });
        const checkRightUser = async (userId, orderId) =>{
            const res = await ctx.$axios.get("/order/right", {params:{userId:userId, orderId: orderId}})
            return res.data.data;
        }
        const getOrderInfo = async (id) =>{
            const res = await ctx.$axios.get("/order/get/"+id);
            // int id; private int patientId;  int scheduleId;  int status;  int number;  double price;  Date creationTime;  int doctorId;  Date workDate;  int userId;  String patientName;  String identifyCardNumber;  String phone;  String doctorName;  String departmentName;
            order.patientId = res.data.data.patientId;
            order.scheduleId = res.data.data.scheduleId;
            order.status = res.data.data.status;
            order.number = res.data.data.number;
            order.price = res.data.data.price;
            order.creationTime = new Date(res.data.data.creationTime).toLocaleString();
            order.doctorId = res.data.data.doctorId;
            order.workDate = new Date(res.data.data.workDate).toLocaleDateString();
            order.userId = res.data.data.userId;
            order.patientName = res.data.data.patientName;
            order.identifyCardNumber = res.data.data.identifyCardNumber;
            order.phone = res.data.data.phone;
            order.doctorName = res.data.data.doctorName;
            order.departmentName = res.data.data.departmentName;
            order.hospitalName  = res.data.data.hospitalName;
        }
        const cancelOrder = async () => {
            const res = await ctx.$axios.get("/order/cancel/"+order.id);
            if( res.data.code == 200 ){
                ctx.$message.success("取消成功")
                order.status = 2
            }
        };
        const pay = async () => {
            dialogPayVisible.value = true;
            const res = await ctx.$axios.get("/order/createNative", 
                {params:{orderId: order.id, departmentName:order.departmentName, workDate: order.workDate}}
            );
            if (res.data.data.code_url == ""){
                dialogPayVisible.value = false;
                ctx.$message.error("获取失败")
            }else{
                // document.getElementById('qrcode').innerHTML = '';
                var code_url = res.data.data.code_url
                new QRCode(qrcode.value, {width:200,height:200,text:code_url});
                timer = setInterval(async () => {
                    queryOrderStatus()
                }, 2500)   
            }
            
        }
        const queryOrderStatus = async () => {
            const res = await ctx.$axios.get("/order/queryOrderStatus/"+order.id);
            if( res.data.message == '支付中' ){
                return;
            }
            clearInterval(timer);
            window.location.reload();
        }
        const confirmPay = async () =>{
            dialogPayVisible.value = false;
            // const res = await ctx.$axios.get("/order/confirm/"+order.id);
            // if( res.data.code == 200 ){
            //     ctx.$message.success("支付成功")
            //     order.status = 1
            // }
            
        }
        const cancelPay = () => {
            dialogPayVisible.value = false;
            if(timer!=null) clearInterval(timer);
        }
        return {
            order, dialogPayVisible, qrcode, 
            cancelOrder, pay, confirmPay, cancelPay, 
        }
    }
}
</script>

<style scoped>
.page-container {
    padding-top: 38px;
    min-height: 500px;
    width: calc(100% - 200px);
    margin-left: 0px
}

.order-detail{
    width: 80%;
}

.order-detail .status-bar {
    margin-top: 20px;
    background-color: #f4f9ff;
    width: 1000px;
    height: 80px;
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    -webkit-box-pack: justify;
    -ms-flex-pack: justify;
    justify-content: space-between
}

.order-detail .status-bar .status-wrapper {
    color: #4490f1;
    font-size: 16px;
    font-weight: 700;
    letter-spacing: 1px;
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    -webkit-box-align: center;
    -ms-flex-align: center;
    align-items: center
}

.order-detail .status-bar .status-wrapper .iconfont {
    margin-right: 6px;
    font-size: 28px;
    font-weight: 400
}

.order-detail .status-bar .status-wrapper.CANCELLED, .order-detail .status-bar .status-wrapper.STOP_VISIT {
    color: #999
}

.order-detail .status-bar .left-wrapper {
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    -webkit-box-pack: justify;
    -ms-flex-pack: justify;
    justify-content: space-between;
    width: 636px;
    padding-left: 20px
}

.order-detail .status-bar .identify-wrapper {
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    -webkit-box-orient: vertical;
    -webkit-box-direction: normal;
    -ms-flex-direction: column;
    flex-direction: column;
    -webkit-box-pack: center;
    -ms-flex-pack: center;
    justify-content: center;
    text-align: right
}

.order-detail .status-bar .identify-wrapper .identify-text {
    font-size: 12px;
    color: #333
}

.order-detail .status-bar .identify-wrapper .identify-code {
    font-size: 24px;
    color: #4490f1
}

.order-detail .status-bar .right-wrapper {
    -webkit-box-flex: 1;
    -ms-flex: 1;
    flex: 1;
    padding-left: 40px;
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    -webkit-box-align: center;
    -ms-flex-align: center;
    align-items: center;
    color: #333;
    margin-left: -65px
}

.order-detail .status-bar .right-wrapper .content-wrapper {
    margin-top: -3px;
    margin-left: 12px
}

.order-detail .status-bar .right-wrapper .content-wrapper .text {
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    -webkit-box-align: center;
    -ms-flex-align: center;
    align-items: center
}

.order-detail .status-bar .right-wrapper .content-wrapper .iconfont {
    color: #00c25f;
    font-size: 24px
}

.order-detail .status-bar .right-wrapper .watch-wrapper {
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    margin-top: 3px
}

.order-detail .status-bar .right-wrapper .code-img {
    width: 61px;
    height: 61px
}

.order-detail .info-wrapper .title-wrapper {
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    -webkit-box-align: center;
    -ms-flex-align: center;
    align-items: center;
    color: #999;
    margin-top: 40px
}

.order-detail .info-wrapper .info-form {
    margin: 40px 0 0 140px
}

.order-detail .info-wrapper .info-form .fee {
    color: #4490f1;
    font-weight: 700
}

.order-detail .info-wrapper .info-form .patient-info {
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    -webkit-box-align: center;
    -ms-flex-align: center;
    align-items: center;
    font-size: 12px
}

.order-detail .info-wrapper .info-form .patient-info .patient-name {
    color: #333;
    font-weight: 700;
    font-size: 14px
}

.order-detail .info-wrapper .info-form .patient-info .split {
    margin: 0 20px;
    height: 14px
}

.order-detail .info-wrapper .info-form .patient-info .patient-no {
    margin-left: 10px
}

.order-detail .rule-wrapper {
    background-color: #fafafa;
    padding: 30px
}

.order-detail .rule-wrapper .rule-title {
    color: #333;
    font-weight: 700;
    margin-bottom: 10px;
}

.order-detail .bottom-wrapper {
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    -webkit-box-pack: center;
    -ms-flex-pack: center;
    justify-content: center
}

.order-detail .bottom-wrapper .button-wrapper {
    width: 140px
}

.order-detail .mt40 {
    margin-top: 40px
}

.order-detail .mt60 {
    margin-top: 60px
}

.order-detail .ml20 {
    margin-left: 20px
}

.order-detail .cancel-dialog .icon {
    font-size: 54px;
    color: #4490f1;
    display: block;
    text-align: center
}

.order-detail .cancel-dialog .dialog-footer {
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    -ms-flex-pack: distribute;
    justify-content: space-around
}

.order-detail .cancel-dialog .dialog-text {
    margin: 30px 0 28px;
    color: #333;
    font-size: 16px;
    letter-spacing: 1px;
    text-align: center
}

.order-detail .cancel-dialog .button-wrapper {
    width: 140px
}

.v-button {
    -webkit-box-pack: center;
    -ms-flex-pack: center;
    justify-content: center;
    -webkit-box-align: center;
    -ms-flex-align: center;
    align-items: center;
    background-color: #4490f1;
    -webkit-box-shadow: 0 10px 20px -10px rgba(73, 144, 241, .5);
    box-shadow: 0 10px 20px -10px rgba(73, 144, 241, .5);
    border-radius: 4px;
    display:flex;
    height: 44px;
    color: #fff;
    font-size: 16px;
    font-weight: 500;
    -webkit-transition: all .2s ease;
    transition: all .2s ease;
    -webkit-user-select: none;
    -moz-user-select: none;
    -ms-user-select: none;
    user-select: none;
    cursor: pointer
}

.v-button.white {
    background-color: #fff;
    color: #4490f1;
    -webkit-box-shadow: 0 10px 20px -10px rgba(73, 144, 241, .2);
    box-shadow: 0 10px 20px -10px rgba(73, 144, 241, .2);
    border: 1px solid #4990f1
}

.v-button.white:hover {
    background-color: #fff
}

.v-button.disable {
    background-color: #eee;
    border: 0;
    -webkit-box-shadow: initial;
    box-shadow: none;
    cursor: auto
}

.v-button.disable:hover {
    background-color: #eee
}

.v-button:hover {
    background-color: #2d81f7
}

.v-button.is-loading {
    pointer-events: none;
    opacity: .8
}
</style>