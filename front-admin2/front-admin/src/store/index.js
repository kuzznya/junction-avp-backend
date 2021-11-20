import Vue from 'vue'
import Vuex from 'vuex'


Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    username: "Maxim",
    token: "kek",
    data: [
      [
        {
          "id": 0,
          "name": "string1",
          "description": "string"
        },
        {
          "id": 1,
          "name": "string2",
          "description": "string"
        }
      ],
    ]
  },
  mutations: {
    changeUsername (state, username, token) {
      state.username = username;
      state.token = token;
    },
    addStages(state, Stages){
      state.data.push(Stages);
    },
    moveToMain(state){
      while (state.data.length > 1) {
        state.data.pop();
      }
    },
    addTasks(state, Tasks){
      state.data.push(Tasks.tasks);
      state.data[state.data.length - 1].push(Tasks.checkpoint);
    },
    moveToStages(state){
      while (state.data.length > 2) {
        state.data.pop();
      }
    }
  },
})
