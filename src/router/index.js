import Vue from 'vue'
import VueRouter from 'vue-router'
import Helper from '@/components/Helper.vue'
import Login from '@/components/Login.vue'
import { Message } from 'element-ui'

Vue.use(VueRouter)

// , meta: { title: 'Helper' }自定义标题
const routes = [
  { path: '/', redirect: '/login' },
  { path: '/helper', component: Helper, meta: { title: 'helper' } },
  { path: '/login', component: Login, meta: { title: 'login' } },
]

const router = new VueRouter({
  mode: 'hash',
  routes
})


// 获取特定的cookie
function getCookie(name) {
  var nameEQ = name + "=";
  var cookies = document.cookie.split(';');
  for (var i = 0; i < cookies.length; i++) {
    var cookie = cookies[i];
    while (cookie.charAt(0) == ' ') cookie = cookie.substring(1, cookie.length);
    if (cookie.indexOf(nameEQ) == 0) return cookie.substring(nameEQ.length, cookie.length);
  }
  return null;
}

router.beforeEach((to, from, next) => {

  /*------------自定义标题-------------- */
  if (to.meta.title) {
    document.title = to.meta.title;
  }
  /*------------自定义标题-------------- */

  //用户转跳路径不是login并且token为空,则转login
  const token = getCookie('token');
  if (to.path !== '/login' && token === null) {
    Message.error('登录超时,请重新登录!');
    next({ path: '/login' })
  } else {
    next()
  }
  next()
})

export default router
