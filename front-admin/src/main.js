import Vue from 'vue'
import App from './App.vue'
import store from './store/index'
import "core-js/stable";
import "regenerator-runtime/runtime";

new Vue({
  el: '#app',
  render: h => h(App),
  store
})
