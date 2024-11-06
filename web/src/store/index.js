import { createStore } from 'vuex'

const MEMBER = "MEMBER";

export default createStore({
  state: {
    // 去SessionStorage里面去读，如果里面有值就读，没有就是空对象
    member: window.SessionStorage.get(MEMBER) || {}
  },
  getters: {
  },
  mutations: {
    setMember (state, _member) {
      state.member = _member;
      // set的同时，放进SessionStorage中，以便拿出来
      window.SessionStorage.set(MEMBER, _member)
    }
  },
  actions: {
  },
  modules: {
  }
})
