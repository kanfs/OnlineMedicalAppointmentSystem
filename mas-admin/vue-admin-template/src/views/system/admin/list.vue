<template>
    <div class="app-container">
        <div class="search-div">
            <el-form label-width="70px" size="small">
                <el-row>
                    <el-col :span="8">
                        <el-form-item label="关 键 字">
                            <el-input
                                style="width: 95%"
                                v-model="searchObj.keyword"
                                placeholder="用户名/姓名/手机号码"
                            ></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="角色类别">
                            <el-select v-model="searchObj.role" placeholder="医院性质">
                                <el-option
                                v-for="item in roles"
                                :key="item.value"
                                :label="item.label"
                                :value="item.value">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="操作时间">
                            <el-date-picker
                                v-model="createTimes"
                                type="datetimerange"
                                range-separator="至"
                                start-placeholder="开始时间"
                                end-placeholder="结束时间"
                                value-format="yyyy-MM-dd HH:mm:ss"
                                style="margin-right: 10px; width: 100%"
                            />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <div style="display: flex; justify-content: space-between">
                        <div>
                            <el-button
                                type="primary"
                                icon="el-icon-search"
                                size="mini"
                                @click="fetchData()"
                                :disabled="$hasBP('bnt.admin.list') === false"
                                >搜索
                            </el-button>
                            <el-button
                                icon="el-icon-refresh"
                                size="mini"
                                @click="resetData"
                                >重置
                            </el-button>
                            <el-button
                                type="success"
                                icon="el-icon-plus"
                                size="mini"
                                @click="add"
                                :disabled="$hasBP('bnt.admin.add') === false"
                                >添 加
                            </el-button>
                        </div>
                        <div>
                            <a
                                :disabled="
                                    $hasBP('bnt.admin.export') === false
                                "
                                href="http://localhost:8160/admin/system/admin/excel/export_user_template"
                                target="_blank"
                            >
                                <el-button
                                    type="primary"
                                    icon="el-icon-download"
                                    size="mini"
                                    >下载模板
                                </el-button>
                            </a>
                            <a
                                :disabled="
                                    $hasBP('bnt.admin.export') === false
                                "
                                href="http://localhost:8160/admin/system/admin/excel/export_user"
                                target="_blank"
                                style="margin-left: 10px"
                            >
                                <el-button
                                    icon="el-icon-download"
                                    size="mini"
                                    type="primary"
                                    >导出信息
                                </el-button>
                            </a>
                            <el-button
                                :disabled="
                                    $hasBP('bnt.admin.import') === false
                                "
                                type="success"
                                icon="el-icon-upload"
                                size="mini"
                                style="margin-left: 10px"
                                @click="importUser"
                                >导入
                            </el-button>
                        </div>
                    </div>

                    <template>
                        <el-dialog
                            title="导入"
                            :visible.sync="dialogImportVisible"
                            width="400px"
                            v-loading="fullscreenLoading"
                            element-loading-text="拼命上传中"
                            element-loading-spinner="el-icon-loading"
                            element-loading-background="rgba(0, 0, 0, 0.8)"
                        >
                            <el-form label-position="right" label-width="110px">
                                <el-form-item label="文件">
                                    <el-upload
                                        :multiple="false"
                                        :on-success="onUploadSuccess"
                                        :on-error="onUploadError"
                                        :on-change="onChange"
                                        :action="'http://localhost:8160/admin/system/admin/excel/import_user'"
                                        class="upload-demo"
                                    >
                                        <el-button size="small" type="primary"
                                            >点击上传</el-button
                                        >
                                        <div slot="tip" class="el-upload_tip">
                                            只能上传excel文件,且不超过500kb<br />
                                            <span
                                                style="
                                                    color: red;
                                                    font-weight: bold;
                                                "
                                                >上传过程中不要随意取消操作，上传完成自动会关闭弹窗！</span
                                            >
                                        </div>
                                    </el-upload>
                                </el-form-item>
                            </el-form>
                            <div slot="footer" class="dialog-footer">
                                <el-button @click="dialogImportVisible = false">
                                    取消
                                </el-button>
                            </div>
                        </el-dialog>
                    </template>
                </el-row>
            </el-form>
        </div>

        <!-- 列表 -->
        <el-table
            v-loading="listLoading"
            :data="list"
            stripe
            border
            style="width: 100%; margin-top: 10px"
        >
            <el-table-column label="序号" width="70" align="center">
                <template slot-scope="scope">
                    {{ (page - 1) * limit + scope.$index + 1 }}
                </template>
            </el-table-column>
            <el-table-column
                align="center"
                prop="username"
                label="用户名"
                width="110"
            />
            <el-table-column
                align="center"
                prop="name"
                label="姓名"
                width="110"
            />
            <el-table-column
                align="center"
                prop="headUrl"
                label="头像"
                width="110"
            >
                <template slot-scope="scope">
                    <el-image
                        style="width: 105px; height: 105px"
                        :src="scope.row.headUrl"
                        fit="fit"
                    ></el-image>
                </template>
            </el-table-column>
            <el-table-column
                align="center"
                prop="phone"
                width="115"
                label="手机"
            />
            <el-table-column
                align="center"
                prop="role"
                label="当前角色"
                width="110"
            />
            <el-table-column align="center" label="状态" width="80">
                <template slot-scope="scope">
                    <el-switch
                        v-model="scope.row.status"
                        @change="switchStatus(scope.row)"
                        :disabled="$hasBP('bnt.admin.status') === false"
                    >
                    </el-switch>
                </template>
            </el-table-column>
            <el-table-column
                align="center"
                prop="createTime"
                label="创建时间"
            >
                <template slot-scope="scope">
                    {{ new Date(scope.row.createTime).toLocaleString() }}
                </template>
            </el-table-column>

            <el-table-column label="操作" align="center" fixed="right">
                <template slot-scope="scope">
                    <el-button
                        type="primary"
                        icon="el-icon-edit"
                        size="mini"
                        @click="edit(scope.row.id)"
                        :disabled="$hasBP('bnt.admin.update') === false"
                        title="修改"
                    />

                    <el-button
                        type="danger"
                        icon="el-icon-delete"
                        size="mini"
                        @click="removeDataById(scope.row.id)"
                        :disabled="$hasBP('bnt.admin.remove') === false"
                        title="删除"
                    />
                    <el-button
                        type="warning"
                        icon="el-icon-baseball"
                        size="mini"
                        @click="showAssignRole(scope.row)"
                        :disabled="$hasBP('bnt.admin.assignRole') === false"
                        title="分配角色"
                    ></el-button>
                </template>
            </el-table-column>
        </el-table>

        <!-- 分页组件 -->

        <el-pagination
            @size-change="handleSizeChange"
            @current-change="fetchData"
            :current-page="page"
            :page-sizes="[5, 10, 15, 20, 30, 40, 50, 100]"
            :page-size="limit"
            layout="total, sizes, prev, pager, next, jumper"
            :total="total"
            style="padding: 30px 0; text-align: center"
        >
        </el-pagination>

        <el-dialog title="添加/修改" :visible.sync="dialogVisible" width="40%">
            <el-form
                ref="dataForm"
                :model="admin"
                label-width="100px"
                size="small"
                style="padding-right: 40px"
            >
                <el-form-item label="用户名" prop="username">
                    <el-input v-model="admin.username" />
                </el-form-item>
                <el-form-item v-if="!admin.id" label="密码" prop="password">
                    <el-input v-model="admin.password" type="password" />
                </el-form-item>
                <el-form-item label="姓名" prop="name">
                    <el-input v-model="admin.name" />
                </el-form-item>
                <el-form-item label="手机" prop="phone">
                    <el-input v-model="admin.phone" />
                </el-form-item>
                <el-form-item label="头像" prop="headUrl">
                    <el-upload
                        class="upload-demo"
                        action="http://localhost:8888/mas/admin/file/img/avater"
                        :on-preview="handlePreview"
                        :on-remove="handleRemove"
                        :file-list="fileList"
                        :on-success="handleAvatarSuccess"
                        :before-upload="beforeAvatarUpload"
                        list-type="picture"
                    >
                        <el-button size="small" type="primary"
                            >点击上传</el-button
                        >
                        <div slot="tip" class="el-upload__tip">
                            只能上传jpg/png文件，且不超过2MB
                        </div>
                    </el-upload>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button
                    @click="dialogVisible = false"
                    size="small"
                    icon="el-icon-refresh-right"
                    >取 消</el-button
                >
                <el-button
                    type="primary"
                    icon="el-icon-check"
                    @click="saveOrUpdate()"
                    size="small"
                    >确 定</el-button
                >
            </span>
        </el-dialog>

        <el-dialog title="分配角色" :visible.sync="dialogRoleVisible">
            <el-form label-width="80px">
                <el-form-item label="用户名">
                    <el-input disabled :value="admin.username"></el-input>
                </el-form-item>

                <el-form-item label="角色列表">
                    <el-checkbox
                        :indeterminate="isIndeterminate"
                        v-model="checkAll"
                        @change="handleCheckAllChange"
                        >全选
                    </el-checkbox>
                    <div style="margin: 15px 0"></div>
                    <el-checkbox-group
                        v-model="adminRoleIds"
                        @change="handleCheckedChange"
                    >
                        <el-checkbox
                            v-for="role in allRoles"
                            :key="role.id"
                            :label="role.id"
                            >{{ role.name }}
                        </el-checkbox>
                    </el-checkbox-group>
                    <el-input v-model="hospitalId" v-if="adminRoleIds==2" placeholder="医院ID"/>
                    <el-input v-model="doctorId" v-if="adminRoleIds==3" placeholder="医生ID"/>
                    
                </el-form-item>
            </el-form>
            <div slot="footer">
                <el-button type="primary" @click="assignRole" size="small"
                    >保存</el-button
                >
                <el-button @click="dialogRoleVisible = false" size="small"
                    >取消</el-button
                >
            </div>
        </el-dialog>
    </div>
</template>

<script>
import adminApi from '@/api/system/admin'
import roleApi from '@/api/system/role'
import fileApi from '@/api/file/file'

const defaultForm = {
    id: '',
    username: '',
    password: '',
    name: '',
    phone: '',
    status: 1,
    headUrl: '',
}
export default {
    data() {
        return {
            roles :[{
                value: '系统管理员',
                label: '系统管理员',
            },{
                value:'医院管理员',
                label:'医院管理员',
            },{
                value:'医生',
                label:'医生',
            },{
                value:'用户',
                label:'用户',
            }],
            listLoading: true, // 数据是否正在加载
            list: null, // banner列表
            total: 0, // 数据库中的总记录数
            page: 1, // 默认页码
            limit: 10, // 每页记录数
            searchObj: {}, // 查询表单对象

            createTimes: [],

            dialogVisible: false,
            admin: defaultForm,
            saveBtnDisabled: false,

            dialogRoleVisible: false,
            allRoles: [], // 所有角色列表
            adminRoleIds: [], // 用户的角色ID的列表
            isIndeterminate: false, // 是否是不确定的
            checkAll: false, // 是否全选
            hospitalId: null,
            doctorId: null,

            fileList: [],

            dialogImportVisible: false,
            fullscreenLoading: false,
        }
    },

    // 生命周期函数：内存准备完毕，页面尚未渲染
    created() {
        console.log('list created......')
        this.fetchData()
    },

    // 生命周期函数：内存准备完毕，页面渲染成功
    mounted() {
        console.log('list mounted......')
    },

    methods: {
        // 加载banner列表数据
        fetchData(page = 1) {
            debugger
            this.page = page
            if (this.createTimes && this.createTimes.length == 2) {
                this.searchObj.createTimeBegin = this.createTimes[0]
                this.searchObj.createTimeEnd = this.createTimes[1]
            }

            adminApi.getPageList(this.page, this.limit, this.searchObj).then(
                response => {
                    this.list = response.data.records
                    console.log(response.data)
                    this.total = response.data.total
                    // 数据加载并绑定成功
                    this.listLoading = false
                }
            )
        },

        // 重置查询表单
        resetData() {
            console.log('重置查询表单')
            this.searchObj = {}
            this.createTimes = []
            this.fetchData()
        },

        // 根据id删除数据
        removeDataById(id) {
            // debugger
            this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => { // promise
                // 点击确定，远程调用ajax
                return adminApi.removeById(id)
            }).then((response) => {
                this.fetchData(this.page)
                this.$message.success(response.message || '删除成功')
            }).catch(() => {
                this.$message.info('取消删除')
            })
        },

        //弹出添加表单
        add() {
            this.dialogVisible = true
            this.admin = {}
            this.fileList = []
        },
        //编辑
        edit(id) {
            this.dialogVisible = true
            adminApi.getById(id).then(response => {
                this.admin = response.data
                if (this.admin.headUrl != '' || this.admin.headUrl != null) {
                    this.fileList = []
                    this.fileList.push({
                        name: this.admin.headUrl.substr(this.admin.headUrl.lastIndexOf('/') + 1),
                        url: this.admin.headUrl
                    })
                }
            })
        },

        //添加或更新
        saveOrUpdate() {
            if (!this.admin.id) {
                this.save()
            } else {
                this.update()
            }
        },

        //添加
        save() {
            adminApi.save(this.admin).then(response => {
                this.$message.success('操作成功')
                this.dialogVisible = false
                this.fetchData(this.page)
            })
        },

        //更新
        update() {
            adminApi.updateById(this.admin).then(response => {
                this.$message.success(response.message || '操作成功')
                this.dialogVisible = false
                this.fetchData(this.page)
            })
        },

        //切换用户状态
        switchStatus(row) {
            row.status = row.status === true ? true : false
            adminApi.updateStatus(row.id, row.status).then(response => {
                if (response.code) {
                    this.$message.success(response.message || '操作成功')
                    this.fetchData()
                }
            })
        },
        handleSizeChange(value) {
            this.limit = value
            this.fetchData()
        },

        //展示分配角色
        showAssignRole(row) {
            this.admin = row
            this.dialogRoleVisible = true
            roleApi.getRolesByAdminId(row.id).then(response => {
                this.allRoles = response.data.allRoles
                console.log(response.data)
                this.adminRoleIds = response.data.adminRoleIds
                this.checkAll = this.adminRoleIds.length === this.allRoles.length
                this.isIndeterminate = this.adminRoleIds.length > 0 && this.adminRoleIds.length < this.allRoles.length
            })
        },

        /*
        全选勾选状态发生改变的监听
        */
        handleCheckAllChange(value) {// value 当前勾选状态true/false
            // 如果当前全选, adminRoleIds就是所有角色id的数组, 否则是空数组
            this.adminRoleIds = value ? this.allRoles.map(item => item.id) : []
            // 如果当前不是全选也不全不选时, 指定为false
            this.isIndeterminate = false
        },

        /*
        角色列表选中项发生改变的监听
        */
        handleCheckedChange(value) {
            const { adminRoleIds, allRoles } = this
            this.checkAll = adminRoleIds.length === allRoles.length && allRoles.length > 0
            this.isIndeterminate = adminRoleIds.length > 0 && adminRoleIds.length < allRoles.length
        },

        //分配角色
        assignRole() {
            var assginRoleVo = {
                adminId: this.admin.id,
                roleIdList: this.adminRoleIds,
                hospitalId:this.hospitalId,
                doctorId:this.doctorId,
            }
            if(this.adminRoleIds==2 && (this.hospitalId==null || this.hospitalId<=0))
                this.$message.error('请选择医院')
            else if(this.adminRoleIds==3 && (this.doctorId==null || this.doctorId<=0))
                this.$message.error('请选择医生')
            else
                roleApi.assignRoles(assginRoleVo).then(response => {
                    this.$message.success(response.message || '分配角色成功')
                    this.dialogRoleVisible = false
                    this.fetchData(this.page)
                })
        },

        handleAvatarSuccess(res, file) {
            if (this.fileList.length > 0 && (this.admin.headUrl != null || this.admin.headUrl != '')) {
                // 先删除以前的图片
                this.deleteFile(this.admin.headUrl)
                this.fileList = []
            }
            this.fileList.push(
                {
                    name: file.name,
                    url: res.data
                })
            this.admin.headUrl = res.data

        },
        beforeAvatarUpload(file) {
            const isJPG = file.type === 'image/jpeg' || file.type === 'image/png'
            const isLt2M = file.size / 1024 / 1024 < 2

            if (!isJPG) {
                this.$message.error('上传头像图片只能是 JPG|PNG 格式!')
            }
            if (!isLt2M) {
                this.$message.error('上传头像图片大小不能超过 2MB!')
            }
            return isJPG && isLt2M
        },
        handleRemove(file, fileList) {
            this.deleteFile(file.url)
        },
        handlePreview(file) {
            window.open(file.url, '_blank')
        },
        deleteFile(fileUrl) {
            fileApi.delete({ fileUrl: fileUrl }).then(res => {
            })
        },
        importUser() {
            this.dialogImportVisible = true;
        },
        //文件上传成功执行的方法
        onUploadSuccess() {
            //关闭弹框
            this.dialogImportVisible = false;
            this.fullscreenLoading = false;
            this.$message({
                message: '上传成功！！！',
                type: 'success'
            });
            //刷新页面
            this.fetchData();
        },
        onUploadError() {
            this.fullscreenLoading = false;
            this.$message.error('上传失败！！！');
        },
        onChange() {
            this.fullscreenLoading = true;
        }
    }
}
</script>

<style>
.avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
}

.avatar-uploader .el-upload:hover {
    border-color: #409eff;
}

.avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;
}

.avatar {
    width: 178px;
    height: 178px;
    display: block;
}
</style>
