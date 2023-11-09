import Vue from 'vue'
import VueRouter from 'vue-router'


Vue.use(VueRouter)

const routes = [
  {
    path:'/products',
    name:'products',
    component:() => import('../views/ProductView.vue')
  },
  {
      path:'/users',
      name:'users',
      component:() => import('../views/UserView.vue')
  },
  {
    path: '/',
    redirect:'/products' //表示重定向到/emp即可
  },
]

const router = new VueRouter({
  routes
})

export default router
