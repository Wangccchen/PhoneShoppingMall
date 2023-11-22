import Vue from 'vue'
import VueRouter from 'vue-router'

const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push(location) {
return originalPush.call(this, location).catch(err => err)
}

Vue.use(VueRouter)

const routes = [
  {
    path: '/backsystem',
    component: () => import('../views/HomePage.vue'),
    children: [
      {
        path: '/backsystem/products',
        component: () => import('../views/ProductView.vue')
      },
      {
        path: '/backsystem/users',
        component: () => import('../views/UserView.vue')
      }
    ]
  },
  {
    path: '/back/login',
    name: 'backLogin',
    component: () => import('../views/LoginView.vue')
  },
  {
    path: '/back/register',
    name:'backRegister',
    component: () => import('../views/RegisterView.vue')
  },
  {
    path:'/',
    redirect:'/backsystem/products'
  }
];

const router = new VueRouter({
  routes
})

export default router
