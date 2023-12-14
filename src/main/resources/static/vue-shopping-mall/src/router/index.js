import Vue from 'vue'
import VueRouter from 'vue-router'

const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push(location) {
return originalPush.call(this, location).catch(err => err)
}


Vue.use(VueRouter)

const routes = [
  {
    path:'/mall',
    name:'mall',
    redirect:'/products',
    component:()=> import('../views/Mall.vue'),
    children:[
      {
        path:'/products',
        component:()=> import('../views/Products.vue')
      }
    ]
  },
  {
    path:'/checkout',
    name:'checkout',
    component:()=>import('../views/CheckOutView.vue')
  },
  {
    path: '/backsystem',
    redirect:'/backsystem/reports',
    component: () => import('../views/BackHomePage.vue'),
    children: [
      {
        path: '/backsystem/products',
        component: () => import('../views/BackProductView.vue')
      },
      {
        path: '/backsystem/users',
        component: () => import('../views/UserView.vue')
      },
      {
        path: '/backsystem/logs',
        component: () => import('../views/LogView.vue')
      },{
        path: '/backsystem/reports',
        component: () => import('../views/BackReportView.vue')
      }
    ]
  },
  {
    path: '/back/login',
    name: 'backLogin',
    component: () => import('../views/BackLoginView.vue')
  },
  {
    path: '/login',
    name: 'login',
    component: () => import('../views/LoginPage.vue')
  },
  {
    path: '/register',
    name:'register',
    component: () => import('../views/RegisterView.vue')
  },
  {
    path:'/',
    redirect:'/mall'
  }
];

const router = new VueRouter({
  routes
})

export default router
