import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

const router = new Router({
  mode: 'history',
  base: process.env.BASE_URL,
  routes: [
    {
      path: '/',
      name: 'Home',
      component: () => import(/* webpackChunkName: "home" */ '@/views/home'),
      meta: { title: 'HomePage-Web Cloud' },
    },
    {
      path: '/login',
      name: 'Login',
      component: () => import(/* webpackChunkName: "login" */ '@/views/Login'),
      meta: { title: 'Login' },
    },
    {
      path: '/register',
      name: 'Register',
      component: () => import(/* webpackChunkName: "register" */ '@/views/Register.vue'),
      meta: { title: 'Register - Cloud Files' },
    },
    {
      path: '/file',
      name: 'File',
      component: () => import(/* webpackChunkName: "file" */ '@/views/file/index.vue'),
      meta: {
        requireAuth: true, //  当前路由是否需要登录才可进入
        title: 'Cloud Space',
        content: {
          description: 'CS6650-final',
        },
        breadCrumbName: 'MySpace',
      },
    },
    {
      path: '/share/:shareBatchNum',
      name: 'Share',
      component: () => import(/* webpackChunkName: "share" */ '@/views/Share/SharePage.vue'),
      meta: {
        title: 'Cloud Space',
        breadCrumbName: 'Share',
      },
      props: true,
    },
    {
      path: '/myShare',
      name: 'myShare',
      component: () => import(/* webpackChunkName: "my_share" */ '@/views/MyShare/index.vue'),
      meta: {
        requireAuth: true,
        title: 'Cloud Space',
        breadCrumbName: 'MyShare',
      },
    },
    {
      path: '*',
      name: '404',
      component: () => import(/* webpackChunkName: "error_404" */ '@/views/404Page.vue'),
      meta: { title: '404 - Not Found' },
    },
  ],
})


const originalPush = Router.prototype.push
Router.prototype.push = function push(location) {
  return originalPush.call(this, location).catch((err) => err)
}

export default router
