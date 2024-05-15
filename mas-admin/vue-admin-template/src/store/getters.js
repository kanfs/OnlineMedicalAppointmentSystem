import hospital from "@/api/information/hospital"

const getters = {
  sidebar: state => state.app.sidebar,
  device: state => state.app.device,
  token: state => state.user.token,
  avatar: state => state.user.avatar,
  name: state => state.user.name,
  //新增
  buttons: state => state.user.buttons,
  menus: state => state.user.menus,
  hospitalId : state => state.user.hospitalId,
  doctorId : state => state.user.doctorId,
}
export default getters
