// store/modules/user.js
import { getInfoByName } from '../../api/user';
const state = {
    userInfo: null
  };
  
  const mutations = {
    setUserInfo(state, userInfo) {
      state.userInfo = userInfo;
    },
    clearUserInfo(state) {
      state.userInfo = null;
    }
  };
  
  const actions = {
    async fetchUserInfo({ commit }, username) {
      try {
        const res = await getInfoByName(username);
        if (res.data.code === 1) {
          const userInfo = res.data.data;
          commit("setUserInfo", userInfo);
        } else {
          // 处理错误情况
          console.error("Failed to fetch user information:", res.data.msg);
        }
      } catch (error) {
        // 处理请求失败情况
        console.error("Error fetching user information:", error);
      }
    },
    clearUserInfo({ commit }) {
      commit("clearUserInfo");
    },
    setUserInfo({ commit }, userInfo) {
      commit('setUserInfo', userInfo);
    },
  };
  
  const getters = {
    isLoggedIn: (state) => {
      return state.userInfo !== null;
    }
  };
  
  export default {
    namespaced: true,
    state,
    mutations,
    actions,
    getters
  };
  