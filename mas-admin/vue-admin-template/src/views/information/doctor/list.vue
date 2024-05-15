<template>
    <div class="app-container">
        <div class="search-div">
            <el-form label-width="70px" size="small">
                <el-row>
                    <el-col :span="6">
                        <el-form-item label="医院名称">
                            <el-input
                                style="width: 95%"
                                v-model="searchObj.hospitalName"
                                placeholder="所属医院名称"
                            ></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="部门名称">
                            <el-input
                                style="width: 95%"
                                v-model="searchObj.departmentName"
                                placeholder="所属部门名称"
                            ></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="医生姓名">
                            <el-input
                                style="width: 95%"
                                v-model="searchObj.name"
                                placeholder="医生姓名"
                            ></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="5">
                        <el-form-item label="等级" prop="grade">
                            <el-select v-model="searchObj.grade" placeholder="医生级别">
                                <el-option
                                v-for="item in grades"
                                :key="item.value"
                                :label="item.label"
                                :value="item.value">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="5">
                        <el-form-item label="职称" prop="title">
                            <el-select v-model="searchObj.title" placeholder="医生职称">
                                <el-option
                                v-for="item in titles"
                                :key="item.value"
                                :label="item.label"
                                :value="item.value">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="推荐度">
                            <el-input
                                style="width: 95%"
                                v-model="searchObj.score"
                                placeholder="患者推荐度"
                            ></el-input>
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
                                :disabled="$hasBP('bnt.doctor.list') === false"
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
                                :disabled="$hasBP('bnt.doctor.add') === false"
                                >添 加
                            </el-button>
                        </div>
                        <div>
                            <a
                                :disabled="
                                    $hasBP('bnt.doctor.export') === false
                                "
                                href="http://localhost:8888/doctor/system/doctor/excel/export"
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
                                    $hasBP('bnt.doctor.export') === false
                                "
                                href="http://localhost:8888/doctor/system/doctor/excel/export_user"
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
                                    $hasBP('bnt.doctor.import') === false
                                "
                                type="success"
                                icon="el-icon-upload"
                                size="mini"
                                style="margin-left: 10px"
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
                                        :on-change="onChange"
                                        :action="'http://localhost:8888/doctor/system/doctor/excel/import_doctor'"
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
            <el-table-column label="序号" width="50" align="center">
                <template slot-scope="scope">
                    {{ (page - 1) * limit + scope.$index + 1 }}
                </template>
            </el-table-column>
            <el-table-column
                align="center"
                prop="hospitalName"
                label="医院名称"
                width="140"
            />
            <el-table-column
                align="center"
                prop="departmentName"
                label="部门名称"
                width="120"
            />
            <el-table-column
                align="center"
                prop="doctorName"
                label="名称"
                width="120"
            />
            <el-table-column
                align="center"
                prop="grade"
                label="级别"
                width="60"
            />
            <el-table-column
                align="center"
                prop="title"
                label="职称"
                width="60"
            />
            <el-table-column
                align="center"
                prop="score"
                label="患者推荐度"
                width="60"
            />
            <el-table-column
                align="center"
                prop="goodat"
                label="擅长"
                width="500"
            />
            <el-table-column label="操作" align="center" fixed="right">
                <template slot-scope="scope">
                    <el-button
                        type="primary"
                        icon="el-icon-edit"
                        size="mini"
                        @click="edit(scope.row.id)"
                        :disabled="$hasBP('bnt.doctor.update') === false"
                        title="修改"
                    />

                    <el-button
                        type="danger"
                        icon="el-icon-delete"
                        size="mini"
                        @click="removeDataById(scope.row.id)"
                        :disabled="$hasBP('bnt.doctor.remove') === false"
                        title="删除"
                    />
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
                :model="doctor"
                label-width="100px"
                size="small"
                style="padding-right: 40px"
            >
                <el-form-item label="所属医院" prop="hospitalName">
                    <el-input v-model="doctor.hospitalName" :disabled="doctor.hospitalId!=null"/>
                </el-form-item>
                <el-form-item label="所属科室" prop="departmentName" v-if="doctor.hospitalId==null">
                    <el-input v-model="doctor.departmentName" />
                </el-form-item>
                <el-form-item label="所属科室" prop="departmentName" v-if="doctor.hospitalId!=null">
                    <el-select v-model="doctor.departmentName" placeholder="科室">
                        <el-option
                        v-for="item in departments"
                        :key="item"
                        :label="item"
                        :value="item">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="名称" prop="doctorName">
                    <el-input v-model="doctor.doctorName" />
                </el-form-item>
                <el-form-item label="级别" prop="grade">
                    <el-select v-model="doctor.grade" placeholder="级别">
                        <el-option
                        v-for="item in grades"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="职称" prop="title">
                    <el-select v-model="doctor.title" placeholder="职称">
                        <el-option
                        v-for="item in titles"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="患者推荐度" prop="score">
                    <el-input v-model="doctor.score" />
                </el-form-item>
                <el-form-item label="擅长" prop="goodat">
                    <el-input v-model="doctor.goodat" />
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

    </div>
</template>

<script>
import doctorApi from '@/api/information/doctor';
import hospitalApi from '@/api/information/hospital';
import departmentApi from '@/api/information/department';
import store from '@/store';

const defaultForm = {
    id: '',
    departmentId: 0,
    name:'',
    grade:'',
    title:'',
    score: 0,
    goodat: '',
    hospitalId: 0,
    departmentName:'',
    hospitalName:'',
}
export default {
    data() {
        return {
            // 主任医师 副主任医师 主治医师 主任技师 暂无 心理咨询师 医师 副主任医师  副主任技师 主管技师 高级按摩师 技师 副主任药师 主任药师 主任检验师 主任康复师 主任护师 副主任检验师 主管检验师 副主任护师 主管康复师 药师 营养师 副主任检验医师 检验技师 主任检验医师 护师 心理治疗师 主管药师 医士 康复师 主管护师 副主任康复师 护士 主治检验医师 验光技师 高级验光技师 主诊医师 检验医师 住院医师 初级验光员 技士 高级验光员 康复士 药士 助产士 乡村医生 检验技士 中级验光员
            
            listLoading: true, // 数据是否正在加载
            list: null, // banner列表
            total: 0, // 数据库中的总记录数
            page: 1, // 默认页码
            limit: 10, // 每页记录数
            searchObj: {
                hospitalId: store.getters.hospitalId,
            }, // 查询表单对象

            grades : [{value:'主任医师',label:'主任医师'}, {value:'副主任医师',label:'副主任医师'}, {value:'主治医师',label:'主治医师'}, {value:'主任技师',label:'主任技师'}, {value:'暂无',label:'暂无'}, {value:'心理咨询师',label:'心理咨询师'}, {value:'医师',label:'医师'}, {value:'副主任技师',label:'副主任技师'}, {value:'主管技师',label:'主管技师'}, {value:'高级按摩师',label:'高级按摩师'}, {value:'技师',label:'技师'}, {value:'副主任药师',label:'副主任药师'}, {value:'主任药师',label:'主任药师'}, {value:'主任检验师',label:'主任检验师'}, {value:'主任康复师',label:'主任康复师'}, {value:'主任护师',label:'主任护师'}, {value:'副主任检验师',label:'副主任检验师'}, {value:'主管检验师',label:'主管检验师'}, {value:'副主任护师',label:'副主任护师'}, {value:'主管康复师',label:'主管康复师'}, {value:'药师',label:'药师'}, {value:'营养师',label:'营养师'}, {value:'副主任检验医师',label:'副主任检验医师'}, {value:'检验技师',label:'检验技师'}, {value:'主任检验医师',label:'主任检验医师'}, {value:'护师',label:'护师'}, {value:'心理治疗师',label:'心理治疗师'}, {value:'主管药师',label:'主管药师'}, {value:'医士',label:'医士'}, {value:'康复师',label:'康复师'}, {value:'主管护师',label:'主管护师'}, {value:'副主任康复师',label:'副主任康复师'}, {value:'护士',label:'护士'}, {value:'主治检验医师',label:'主治检验医师'}, {value:'验光技师',label:'验光技师'}, {value:'高级验光技师',label:'高级验光技师'}, {value:'主诊医师',label:'主诊医师'}, {value:'检验医师',label:'检验医师'}, {value:'住院医师',label:'住院医师'}, {value:'初级验光员',label:'初级验光员'}, {value:'技士',label:'技士'}, {value:'高级验光员',label:'高级验光员'}, {value:'康复士',label:'康复士'}, {value:'药士',label:'药士'}, {value:'助产士',label:'助产士'}, {value:'乡村医生',label:'乡村医生'}, {value:'检验技士',label:'检验技士'}, {value:'中级验光员',label:'中级验光员'},],
            titles : [{value:'副教授',label:'副教授'}, {value:'教授',label:'教授'}, {value:'暂无',label:'暂无'}, {value:'副研究员',label:'副研究员'}, {value:'讲师',label:'讲师'}, {value:'研究员',label:'研究员'}, {value:'助教',label:'助教'}, ],
            departments: [ ],

            dialogVisible: false,
            doctor: defaultForm,
            saveBtnDisabled: false,

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
            doctorApi.getPageList(this.page, this.limit, this.searchObj).then(
                response => {
                    this.list = response.data.records
                    this.total = response.data.total
                    // 数据加载并绑定成功
                    this.listLoading = false
                }
            )
        },

        // 重置查询表单
        resetData() {
            console.log('重置查询表单')
            this.searchObj = {
                hospitalId: store.getters.hospitalId,
            }
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
                return doctorApi.removeById(id)
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
            this.doctor = {
                hospitalId: store.getters.hospitalId,
                hospitalName: "",
            }
            if ( this.doctor.hospitalId != null && this.doctor.hospitalId != 0){
                hospitalApi.getById(store.getters.hospitalId).then(response => {
                    this.doctor.hospitalName = response.data.name
                    departmentApi.getDepartmentNames(this.doctor.hospitalName).then(res => {
                        this.departments = res.data
                    })
                })
                
            }
            
            
            this.fileList = []
        },
        //编辑
        edit(id) {
            this.dialogVisible = true
            doctorApi.getById(id).then(response => {
                this.doctor = response.data
            })
        },

        //添加或更新
        saveOrUpdate() {
            if (!this.doctor.id) {
                this.save()
            } else {
                this.update()
            }
        },

        //添加
        save() {
            doctorApi.save(this.doctor).then(response => {
                this.$message.success('操作成功')
                this.dialogVisible = false
                this.fetchData(this.page)
            })
        },

        //更新
        update() {
            doctorApi.updateById(this.doctor).then(response => {
                this.$message.success(response.message || '操作成功')
                this.dialogVisible = false
                this.fetchData(this.page)
            })
        },

        handleSizeChange(value) {
            this.limit = value
            this.fetchData()
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
