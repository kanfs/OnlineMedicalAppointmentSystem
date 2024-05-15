<template>
  <div class="login-container">
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
      <el-form
        ref="loginFormRef"
        :model="loginForm"
        :rules="loginFormRules"
        label-width="0px"
        class="login-form"
      >
        <!-- <el-row><text class="text">用户登录</text></el-row> -->

        <el-form-item prop="emailAddress" class="form-input" style="margin-top: 20px;">
          <el-input v-model="loginForm.emailAddress" placeholder="请输入邮箱地址">
            <template #prefix>
              <el-icon class="form-item-icon"><Message/></el-icon>
            </template>
          </el-input>
        </el-form-item>

        <el-form-item prop="password" class="form-input">
          <el-input type="password" v-model="loginForm.password" placeholder="请输入密码" show-password>
            <template #prefix>
              <el-icon class="form-item-icon"><Lock/></el-icon>
            </template>
          </el-input>
        </el-form-item>

        <el-form-item >
          <!-- <el-button type="primary" @click="register" :icon="Plus" class="login-btn" round>注册</el-button> -->
          <!-- <el-button type="info" @click="resetLoginForm" :icon="Refresh" class="login-btn" round>重置</el-button> --> 
          <button type="submit" @click="login" :icon="Check" class="login-btn" round>登录</button>
        </el-form-item>
        <el-form-item class="form-input" style="margin-left: 32%;">
          <!--创建账户-->
          <div class="login-card-footer">
                还没有账户?<a href="./register">创建新账户</a>.
          </div>
        </el-form-item>
      </el-form>
      <div>
        <el-button type="primary" @click="register" :icon="Plus" class="login-btn" round>注册</el-button>
      </div>
    </div>
    
  </div>
</template>


<script>
import { Plus, Refresh, Check, Lock, Message } from '@element-plus/icons-vue';
import { ref, reactive, getCurrentInstance, inject } from "vue";
import Cookies from 'js-cookie';
export default {
  setup() {
    const validateEmailAddress = (rule, value, callback) => {
      var emailRegExp = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(\.[a-zA-Z0-9_-])+/;
      var emailRegExp1 = /^([a-zA-Z]|[0-9])(\w|\-)+@[a-zA-Z0-9]+\.([a-zA-Z]{2,4})$/;
      if ((!emailRegExp.test(value) && value != '') || (!emailRegExp1.test(value) && value != '')) {
      callback(new Error('请输入有效邮箱格式！'));
      } else {
      callback();
      }
    };
    const ctx = getCurrentInstance().appContext.config.globalProperties;
    const loginForm = reactive({
      emailAddress: "483768977@qq.com",
      password: "123456",
    });
    const loginFormRef = ref(null);
    const loginFormRules = reactive({
      emailAddress: [{ required: true, validator: validateEmailAddress, trigger: "blur" }],
      password: [{ required: true, message: "请输入密码", trigger: "blur" }],
    });
    const login = async () => {
      loginFormRef.value.validate(async (valid) => {
        if (!valid) return;
        const { data: res } = await ctx.$axios.post("login", {
          emailAddress: loginForm.emailAddress,
          password: loginForm.password,
        });
        if (res.code == 1) ctx.$message.error("登录失败，密码错误");
        else if (res.code == 2) ctx.$message.error("登录失败，该邮箱尚未注册");
        else {
          ctx.$message.success("登录成功");
          Cookies.set("userId", res.userId, {domain: "localhost"});
          Cookies.set("token", res.token, {domain: "localhost"});
          Cookies.set("name", res.name, {domain: "localhost"});
          window.location.href = "http://localhost:8080/home";

        }
      });
    };
    const resetLoginForm = () => {
      loginFormRef.value.resetFields();
    };
    const register = () =>{
      ctx.$message.error("登录失败，密码错误");
      // ctx.$router.push("/register");
    }
    return {
      Plus, Refresh, Check, Lock, Message, 
      loginFormRef,
      loginForm,
      loginFormRules,
      login,
      resetLoginForm,
      register
    };
  },
};
</script>

<style lang="less" scoped>
.login-container {
  height: 100%;
  width: 100%;
  font-family: 'Poppins',sans-serif;/*设置字体*/
  min-height: 100vh ;
  background: linear-gradient(to right,#333399,#48dc92);
  display: flex;
  align-items: center;
  justify-content: center;
}
.login-card {
    height:550px;
    width: 420px;
    background: rgba(255, 255, 255, .5);
    padding: 4rem;
    border-radius: 10px;
    position: relative;
}
/* 把当前的盒子映射成一个盒子，然后形状是旋转的状态 */
.login-card::before{
  content: '';
  position:absolute;
  background: rgb(0, 255, 55);
  inset: 0;
  transform: rotate(-5deg);
  z-index: -1;
}
.text{
  font-size: 40px;
  font-family:"微软雅黑";
  color:#e4f497;
  padding:20px;
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
    margin-top: 1.5rem;
    font-size: calc(1rem * .8);
}
.login-form {
  display: flex;
    flex-direction: column;
    gap: 1.5rem;
  top: 10%;
  left: 0%;
  bottom: 20px;
  width: 100%;
  padding: 0 20px;
  box-sizing: border-box;
  text-align: -webkit-center;
}
.form-item-icon{
    svg{
      color: #ff00cc;
    }
}
.form-input{
  width:90%;
  height:45px;
  text-align: center;
  margin-top: 0px;
  margin-left:5%;
  .el-input{
    width:100%;
    height:100%;
    
  }
}
:deep(.el-input__inner){
  width:100%;
  height:100%;
  font-size:20px;
  font-family: 'Poppins',sans-serif;/*设置字体*/
  border-radius: 3px;
}
.login-btn{
  background: black;
  width:100%;
  color:white;
  padding: 1rem;
  border-radius: 100px;
  text-transform: uppercase;
  letter-spacing: 2px;
  transition: background .5s;
  cursor: pointer;
}
:deep(span) {
        color:#fff;
        font-size:26px;
        letter-spacing:1px;
}

</style>