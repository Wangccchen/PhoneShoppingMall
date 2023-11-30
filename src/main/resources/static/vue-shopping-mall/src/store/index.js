// store/index.js

import Vue from 'vue';
import Vuex from 'vuex';
import userModule from './modules/user.js';

Vue.use(Vuex);

const store = new Vuex.Store({
  modules: {
    user: userModule,
    // 可以添加其他模块
  }
});

export default store;

