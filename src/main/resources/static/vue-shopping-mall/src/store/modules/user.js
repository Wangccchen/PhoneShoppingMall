// store/modules/user.js
import { getInfoByName,updateInfo } from '../../api/user';
const state = {
    userInfo: null
  };
  
  const mutations = {
    setUserInfo(state, userInfo) {
      state.userInfo = userInfo;
    },
    clearUserInfo(state) {
      state.userInfo = null;
    },
    updateUserInfo(state, { email, address, phoneNumber }) {
      // 如果 userInfo 为 null，则初始化为一个空对象
      if (!state.userInfo) {
        state.userInfo = {};
      }
  
      // 更新字段
      state.userInfo.email = email;
      state.userInfo.address = address;
      state.userInfo.phoneNumber = phoneNumber;
      

    
    },
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
    async updateUser({ commit, state }) {
      try {
        await updateInfo(state.userInfo);
      } catch (error) {
        console.error("Failed to update user:", error);
      }
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
  