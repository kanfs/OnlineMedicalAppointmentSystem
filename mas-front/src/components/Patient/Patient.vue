<template>
    <div class="nav-container">
        <!--左侧导航 #start -->
        <div class="left-nav">
            <div class="nav-item">
                <span class="v-link clickable dark" onclick="javascript:window.location='/order'">挂号订单</span>
            </div>
            <div class="nav-item selected">
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
export default{
    setup(){
        return {

        }
    }
}
</script>

<style>
.nav-container {
    padding-top: 38px;
    min-height: 500px;
    margin-left: 200px;
    margin-top: -60px;
    display: flex;
}
.left-nav {
    position: -webkit-sticky;
    position: sticky;
    top: 14px;
    float: left
}
.nav-item {
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    -webkit-box-align: center;
    -ms-flex-align: center;
    align-items: center;
    width: 200px;
    height: 70px;
    position: relative;
    padding-left: 20px
}

.nav-item.selected {
    font-weight: 700;
    font-size: 16px
}

.nav-item.selected:before {
    content: "";
    position: absolute;
    left: 0;
    width: 10px;
    height: 10px;
    z-index: 1;
    background: #4490f1
}
.v-link {
    color: #666;
    -webkit-transition: all .2s ease;
    transition: all .2s ease;
    -webkit-user-select: none;
    -moz-user-select: none;
    -ms-user-select: none;
    user-select: none;
    letter-spacing: 1px;
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    -webkit-box-align: center;
    -ms-flex-align: center;
    align-items: center
}
.v-link.dark {
    color: #333
}
.v-link.clickable {
    cursor: pointer
}
.v-link.selected, .v-link:hover {
    color: #4490f1
}
.page-container {
    padding-top: 38px;
    min-height: 500px;
    width: calc(100% - 200px);
    margin-left: 200px
}
</style>