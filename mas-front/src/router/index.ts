import {createRouter, createWebHistory} from "vue-router"
import Login from "../components/Login.vue"
import Register from "../components/Register.vue"
import Home from "../components/Home.vue";
import MainPage from "../components/MainPage.vue";
import Doctor from "../components/Doctor.vue";
import Hospital from "../components/Hospital.vue";
import Reservation from "../components/Reservation.vue";
import Department from "../components/Department.vue";
import Order from "../components/Order/Order.vue";
import OrderOverview from "../components/Order/OrderOverview.vue";
import OrderShow from "../components/Order/OrderShow.vue";
import User from "../components/User.vue";
import Patient from "../components/Patient/Patient.vue";
import PatientAdd from "../components/Patient/PatientAdd.vue";
import PatientOverview from "../components/Patient/PatientOverview.vue";
import PatientShow from "../components/Patient/PatientShow.vue";
import DepartmentChoose from "../components/Consultation/DepartmentChoose.vue";
import DoctorConsult from "../components/Consultation/DoctorConsult.vue";
import AITriage from "../components/Consultation/AITriage.vue";
import AIConsult from "../components/Consultation/AIConsult.vue";

const routes = [
  {path:"/",  redirect:"/mainpage"},
  {path:"/home", name:"首页", component:Home, redirect:"/mainpage",
    children:[
      {path:"/mainpage", component:MainPage},
      {path:"/doctor/:doctorId", component:Doctor},
      {path:"/hospital/:hospitalId", component:Hospital},
      {path:"/reservation/:hospitalId", component:Reservation},
      {path:"/department/:departmentId", component:Department},
      {path:"/order", component:Order, redirect:"/order/all",
      children:[
        {path:"/order/all", component:OrderOverview},
        {path:"/order/show/:orderId", component:OrderShow},
      ]},
      {path:"/user", component:User},
      {path:"/patient", component:Patient, redirect:"/patient/all", 
      children:[
        {path:"/patient/all", component:PatientOverview},
        {path:"/patient/add", component:PatientAdd},
        {path:"/patient/show", component:PatientShow},
      ]},
      {path:"/consultation/:hospitalId", component:DepartmentChoose,},
      {path:"/consultation/doctor/:doctorId", component:DoctorConsult,},
      {path:"/AITriage", component:AITriage,},
      {path:"/AIConsult", component:AIConsult,},
    ]},
  {path:"/register", name:"注册", component:Register},
  {path:"/login", name:"登录", component:Login},
  
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
});

export default router;