<template>
    <div class="login-card-container">
        <div>
        <div class="login-card">
            <!--logo-->
            <div class="login-card-logo">
                <img src="../assets/image/logo.png" alt="logo">
            </div>
            <!--提示-->
            <div class="login-card-header">
                <h1>登录</h1>
                <div>进入后即可开启畅聊</div>
            </div>
            <!--表单-->
            <form class="login-card-form" method="post" action="checkPassword.php">
                <div class="form-item">
                    <span class="form-item-icon material-symbols-rounded">mail</span>
                    <input type="text" placeholder="邮箱/账户" name="email" :required="true" :autofocus="true">
                </div>
                <div class="form-item">
                    <span class="form-item-icon material-symbols-rounded">lock</span>
                    <input type="password" placeholder="密码" name="password" :required="true">
                </div>
                <div class="form-item-other">
                    <div class="checkbox">
                        <input type="checkbox" id="remeberMeCheckbox">
                        <label for="remeberMeCheckbox">记住我</label>
                    </div>
                    <a href="#">忘记密码？</a>
                </div>
                <button type="submit">登录</button>
            </form>
            <!--创建账户-->
            <div class="login-card-footer">
                还没有账户?<a href="./adduser.html">创建新账户</a>.
            </div>
        </div>
        <!--其他登录方式-->
        <!-- <div class="login-card-social">
            <div>其他快捷登录</div>
            <div class="login-card-social-btns">
                <a href="#">
                    <svg xmlns="http://www.w3.org/2000/svg" class="icon icon-tabler icon-tabler-brand-facebook" width="24" height="24" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" fill="none" stroke-linecap="round" stroke-linejoin="round">
                        <path stroke="none" d="M0 0h24v24H0z" fill="none"></path>
                        <path d="M7 10v4h3v7h4v-7h3l1 -4h-4v-2a1 1 0 0 1 1 -1h3v-4h-3a5 5 0 0 0 -5 5v2h-3"></path>
                    </svg>
                </a>
                <a href="#">
                    <svg xmlns="http://www.w3.org/2000/svg" class="icon icon-tabler icon-tabler-brand-google" width="24" height="24" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" fill="none" stroke-linecap="round" stroke-linejoin="round">
                        <path stroke="none" d="M0 0h24v24H0z" fill="none"></path>
                        <path d="M17.788 5.108a9 9 0 1 0 3.212 6.892h-8"></path>
                    </svg>
                </a>
            </div>
        </div> -->
    </div>
    </div>
</template>

<style scoped>
.login-card-container{
    font-family: 'Poppins',sans-serif;/*设置字体*/
    min-height: 100vh ;
    background: linear-gradient(to right,#333399,#ff00cc);
    display: flex;
    align-items: center;
    justify-content: center;
}
/* 登录透明卡片 */
.login-card{
    width: 450px;
    background: rgba(255, 255, 255, .5);
    padding: 4rem;
    border-radius: 10px;
    position: relative;
}
/* 把当前的盒子映射成一个盒子，然后形状是旋转的状态 */
.login-card::before{
    content: 'beij';
    position:absolute;
    background: rgba(255, 255, 255, .15);
    inset: 0;
    transform: rotate(-5deg);
    z-index: -1;
}
/* logo */
.login-card-logo{
    margin-bottom: 2rem;
}
.login-card-logo img{
    width: 60px;
}
.login-card-logo,
.login-card-header,
.login-card-footer{
    text-align: center;
}

/* a标签文字样式 */
.login-card a{
    text-decoration: none;
    color:#35339a;
}
.login-card a:hover{
    text-decoration: underline;
}

.login-card-header{
    margin-bottom: 2rem;
}
.login-card-header h1{
    font-size: 2rem;
    font-weight: 600;
    margin-bottom: .5rem;
}
.login-card-header h1 + div{
    font-size: calc(1rem * .8);
    opacity: .8;
}
/*表单*/
.login-card-form{
    display: flex;
    flex-direction: column;
    gap: 1.5rem;
}
/* 表单元素 */
.login-card-form .form-item{
    position: relative;
}
.login-card-form .form-item .form-item-icon{
    position: absolute;
    top: .82rem;
    left: 1.4rem;
    font-size: 1.3rem;
    opacity: .4;
}

/* 记住我 */
.login-card-form .form-item-other{
    display: flex;
    align-items: center;
    justify-content: space-between;
    font-size: calc(1rem * .8);
    margin-bottom: .5rem;
}
.login-card-form .checkbox{
    display: flex;
    align-items: center;
    gap: 7px;
}
/* 底部 */
.login-card-footer{
    margin-top: 1.5rem;
    font-size: calc(1rem * .8);
}

/* 表单元素 */
.login-card input[type="text"],
.login-card input[type="password"],
.login-card input[type="email"]{
    border: none;
    outline: none;
    background:rgba(255, 255, 255, .3);
    padding: 1rem 1.5rem;
    padding-left:calc(1rem * 3.5);
    border-radius: 100px;
    width: 100%;
    transition: background .5s;
}
/* 输入框聚焦 */
.login-card input:focus{
    background: white;
}
/* checkbox复选框 */
.login-card input[type="checkbox"]{
    width: 16px;
    height: 16px;
    accent-color: black;
}
/* 提交按钮 */
.login-card-form button{
    background: black;
    color:white;
    padding: 1rem;
    border-radius: 100px;
    text-transform: uppercase;
    letter-spacing: 2px;
    transition: background .5s;
}
.login-card-form button:hover{
    background-color: rgba(0, 0, 0,.85);
    cursor: pointer;
}
/* 其他登录方式 */
.login-card-social{
    display: flex;
    flex-direction: column;
    gap: 1.5rem;
    text-align: center;
    margin-top: 3rem;
}
.login-card-social > div{
    opacity: .8;
    text-transform: uppercase;
    letter-spacing: 1px;
    font-size: calc(1rem * .8);
}
.login-card-social-btns{
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 1rem;
}
.login-card-social-btns a{
    display: flex;
    align-items: center;
    justify-content: center;
    color:black;
    width: 50px;
    height: 50px;
    background: rgba(255, 255, 255, .6);
    border-radius: 100px;
    transition: all .5s;
}
.login-card-social-btns a:hover{
    background: white;
    transform: scale(1.1);/*缩放*/
}

/*适配 媒体查询*/
@media (max-width:768px) {
    body{
        padding:2rem 0;
    }
    .login-card{
        width: 280px;
        padding: 2rem;
    }
}
</style>