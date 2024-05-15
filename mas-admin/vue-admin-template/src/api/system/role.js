/*
角色管理相关的API请求函数
*/
import request from '@/utils/request'

const api_name = '/admin/system/role'

export default {

    /**
     * 获取角色分页列表(带搜索)
     * @param {页码} page 
     * @param {每页条目数} limit 
     * @param {搜索条件} searchObj 
     * @returns 
     */
    getPageList(page, limit, searchObj) {
        return request({
            url: `${api_name}/${page}/${limit}`,
            method: 'get',
            params: searchObj
        })
    },

    /**
     * 删除角色
     * @param {角色Id} id 
     * @returns 
     */
    removeById(id) {
        return request({
            url: `${api_name}/remove/${id}`,
            method: 'delete'
        })
    },

    /**
     * 添加角色
     * @param {角色信息} role 
     * @returns 
     */
    save(role) {
        return request({
            url: `${api_name}/save`,
            method: 'post',
            data: role
        })
    },

    /**
     * 编辑
     * @param {角色主键Id} id 
     * @returns 
     */
    getById(id) {
        return request({
            url: `${api_name}/get/${id}`,
            method: 'get'
        })
    },

    /**
     * 更新
     * @param {角色信息} role 
     * @returns 
     */
    updateById(role) {
        return request({
            url: `${api_name}/update`,
            method: 'post',
            data: role
        })
    },

    /**
     * 批量删除
     * @param {批量删除Id} idList 
     * @returns 
     */
    batchRemove(idList) {
        return request({
            url: `${api_name}/batchRemove`,
            method: `delete`,
            data: idList
        })
    },

    //根据用户id查询用户已分配的角色
    getRolesByAdminId(adminId) {
        return request({
            url: `${api_name}/toAssign/${adminId}`,
            method: 'get'
        })
    },

    //分配角色
    assignRoles(params) {
        return request({
            url: `${api_name}/doAssign`,
            method: 'post',
            data: params
        })
    }
}