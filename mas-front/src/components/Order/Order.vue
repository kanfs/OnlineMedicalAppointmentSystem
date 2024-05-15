<template>
    <div class="nav-container">
        <!--左侧导航 #start -->
        <div class="left-nav">
            <div class="nav-item selected">
                <span class="v-link clickable dark" onclick="javascript:window.location='/order'">挂号订单</span>
            </div>
            <div class="nav-item">
                <span class="v-link clickable dark" onclick="javascript:window.location='/patient'">就诊人管理</span>
            </div>
            <div class="nav-item">
                <span class="v-link clickable dark" onclick="javascript:window.location='/user'">用户信息</span>
            </div>
        </div>
        <!-- 左侧导航 #end -->
        <!-- 右侧内容 -->
        <router-view/>
    </div>
</template>

<script>
export default {
    setup(){
        return {

        }
    }   
}
</script>