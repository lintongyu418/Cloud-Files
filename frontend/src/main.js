// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import store from '@/store/index.js'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import uploader from 'vue-simple-uploader'
import globalFunction from '@/globalFunctions.js'
import * as filters from '@/globalFilters.js'
import "@/router/before"

Vue.config.productionTip = false

Vue.use(ElementUI);
Vue.use(uploader);
Vue.prototype.$EventBus = new Vue()

for(const key in globalFunction) {
  Vue.prototype[key] = globalFunction[key]
}

Object.keys(filters).forEach(key => {
  Vue.filter(key, filters[key])
})

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  components: { App },
  template: '<App/>',
})
