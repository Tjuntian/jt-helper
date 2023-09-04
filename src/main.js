import Vue from 'vue'
import App from './App.vue'
import router from './router'
import axios from 'axios'
import { Message } from 'element-ui'
import $ from 'jquery'

window.$ = $  //jquery原因：设置变量但未使用，编辑器会报错。

import './plugins/element.js'
import './assets/css/global.css'
import './assets/css/scroll.css'
import './assets/js/ployfill.js'//解决警告报错


/*---------解决报错ResizeObserver loop limit exceeded----------------*/
/*---------加入main.js和报错页面皆可----------------*/
const debounce = (fn, delay) => {
  let timer = null;
  return function() {
    let context = this;
    let args = arguments;
    clearTimeout(timer);
    timer = setTimeout(function() {
      fn.apply(context, args);
    }, delay);
  }
}
const _ResizeObserver = window.ResizeObserver;
window.ResizeObserver = class ResizeObserver extends _ResizeObserver {
  constructor(callback) {
    callback = debounce(callback, 16);
    super(callback);
  }
}
/*---------解决报错ResizeObserver loop limit exceeded----------------*/

// 请求根路径
axios.defaults.baseURL = '/api/'


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

// 请求拦截器
axios.interceptors.request.use(config => {
  const token = getCookie('token');
  config.headers.Authorization = token
  return config;
}, function(error) {
  Message.error('请求超时~,请稍后再试~');
  return Promise.reject(error);
})


// 添加响应拦截器
axios.interceptors.response.use(function(response) {
  if (response.data.code === 401) {
    Message.error('登录超时,请重新登录!');
    router.push('/login')
  }
  return response;
}, function(error) {
  // Message.error('请求超时~,请稍后再试~');
  return Promise.reject(error);
});


// 注册axios全局对象
Vue.prototype.$http = axios
Vue.config.productionTip = false

new Vue({
  router,
  render: h => h(App)
}).$mount('#app')
