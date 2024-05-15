import request from '@/utils/request'

const api_name = '/admin/user/authentication'

export default {
    userInfoList(data) {
        return request({
            url: `${api_name}/list`,
            method: 'GET',
            params: data
        })
    },
    userAuth(data) {
        return request({
            url: `${api_name}/auth`,
            method: 'PUT',
            data: data
        })
    }
}