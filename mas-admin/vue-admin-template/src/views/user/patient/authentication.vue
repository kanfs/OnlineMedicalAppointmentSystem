<template>
    <el-dialog
        :close-on-click-modal="false"
        :close-on-press-escape="true"
        title="用户认证"
        :type="type"
        :visible.sync="isVisible"
        width="600px"
        top="150px"
    >
        <el-form
            ref="form"
            :model="userInfo"
            :rules="rules"
            label-position="right"
            label-width="120px"
        >
            <el-form-item label="用户状态" prop="userStatus">
                <el-switch
                    v-model="userInfo.userStatus"
                    active-color="#13ce66"
                    inactive-color="#ff4949"
                    active-text="启用"
                    inactive-text="禁用"
                    :active-value="1"
                    :inactive-value="0"
                >
                </el-switch>
            </el-form-item>
            <el-form-item label="用户状态认证结果" prop="authStatusResult">
                <el-input
                    type="textarea"
                    :rows="2"
                    v-model="userInfo.authStatusResult"
                    placeholder="输入用户认证状态结果"
                />
            </el-form-item>
            <el-form-item label="认证状态" prop="authStatus">
                <el-switch
                    v-model="userInfo.authStatus"
                    active-color="#13ce66"
                    inactive-color="#ff4949"
                    active-text="通过"
                    inactive-text="不通过"
                    :active-value="1"
                    :inactive-value="0"
                >
                </el-switch>
            </el-form-item>
            <el-form-item label="用户认证结果" prop="authResult">
                <el-input
                    type="textarea"
                    :rows="2"
                    v-model="userInfo.authResult"
                    placeholder="输入用户认证状态结果"
                />
            </el-form-item>
        </el-form>

        <div slot="footer" class="dialog-footer">
            <el-button plain type="warning" @click="isVisible = false"
                >取消</el-button
            >
            <el-button plain type="primary" @click="submitData">确定</el-button>
        </div>
    </el-dialog>
</template>
<script>

import authenticationApi from '@/api/user/authentication'

export default {
    components: {},
    props: {
        dialogVisible: {
            type: Boolean,
            default: false
        },
        type: {
            type: String,
            default: 'add'
        }
    },
    data() {
        //这里存放数据
        return {
            userInfo: {},

            //表单校验规则
            rules: {
                authStatusResult: [
                    { required: true, message: '用户状态认证结果不能为空', trigger: 'blur' }
                ],
                authResult: [
                    { required: true, message: '用户认证结果不能为空', trigger: 'blur' }
                ]
            }
        };
    },
    //计算属性 类似于 data 概念
    computed: {
        isVisible: {
            get() {
                return this.dialogVisible
            },
            set() {
                this.close()
                this.reset()
            }
        },
        title() {
            return this.type === 'add'
                ? "添加"
                : "修改"
        }
    },
    //监控 data 中的数据变化
    watch: {},
    //方法集合
    methods: {
        setUserInfo(data) {
            this.userInfo = data
        },
        close() {
            this.$emit('close')
        },
        reset() {
            // 先清除校验，再清除表单，不然有奇怪的bug
            this.$refs.form.clearValidate()
            this.$refs.form.resetFields()
            this.userInfo = {}
        },
        //提交表单数据
        submitData() {
            this.$refs.form.validate(valid => {
                if (valid) {
                    this.editSubmit()
                    this.userInfo = {}
                } else {
                    return false
                }
            })
        },
        editSubmit() {
            userInfoApi.userAuth(this.userInfo).then(response => {
                if (response.code = 200) {
                    this.isVisible = false
                    this.$message({
                        message: "认证成功",
                        type: 'success'
                    })
                    this.userInfo = {}
                    this.$emit('success')
                } else {
                    this.$message.error('认证失败！！！');
                }
            })
        },
    },
    //生命周期 - 创建完成（可以访问当前 this 实例）
    created() {

    },
    //生命周期 - 挂载完成（可以访问 DOM 元素）
    mounted() {


    }
}
</script>
<style lang="scss" scoped>
//@import url(); 引入公共 css 类
</style>
