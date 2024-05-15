<template>
    <!-- 展示医生详情 -->
    <div>{{ doctor.values }}</div>
    <div>hello</div>

</template>

<script>
import {ref, reactive, getCurrentInstance, onBeforeMount, watch} from 'vue';
import {useRouter,useRoute} from 'vue-router';
import Cookies from 'js-cookie';
export default {
    setup() {
        const doctorId = useRoute().params.doctorId;
        var token = Cookies.get('token');
        var doctor = reactive({});
        const ctx = getCurrentInstance().appContext.config.globalProperties;
        const getDoctorInfo = async () => {
            // 根据医生id获取医生详情
            const res = await ctx.$axios.get(`/doctor/info/${doctorId}`);
            if ( res.data.code === 1 ){
                // 获取失败
                ctx.$message.error(res.data.msg);
            }else{
                // 获取成功
                doctor.values = res.data.doctor.grade;
                console.log(doctor.values)
            }
        }
        onBeforeMount(async() => {
            // 根据Cookies中的token判断用户登录状态
            if ( token == null ){
                ctx.$message.error('请先登录');
                ctx.$router.push('/login');
            }else{
                getDoctorInfo();
            }
        });
        return { 
            doctorId, doctor,
         };
    }
}
</script>

<style>

</style>