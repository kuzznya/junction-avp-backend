import Vue from 'vue'
import Vuex from 'vuex'


Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    id: '',
    username: "",
    token: "",
    data: [],
    reviews: [],
    checkpoint: {},
  },
  mutations: {
    addId(state, id){
      state.id = id;
    },
    changeUsername (state, [username, token]) {
      state.username = username;
      state.token = token;
    },
    addCourses(state, Courses){
      if (state.data.length === 0) {
        state.data.push(Courses);
      }
      else {
        state.data[0] = Courses
      }
    },
    addStages(state, Stages){
      if (state.data.length === 1) {
        state.data[1] = Stages
      }
      else {
        state.data.push(Stages);
      }
      console.log(state.data);
    },
    moveToMain(state){
      while (state.data.length > 1) {
        state.data.pop();
      }
    },
    addTasks(state, Tasks){
      state.data.push(Tasks.tasks);
      if(Tasks.checkpoint !== null) {
        state.data.checkpoint = Tasks.checkpoint;
        state.data.checkpoint.isCheckpoint = true;
        state.data[state.data.length - 1].push(state.data.checkpoint);
      }
    },
    moveToStages(state){
      while (state.data.length > 2) {
        state.data.pop();
      }
    },
    addReviews(state, Reviews){
      state.reviews = Reviews.map((item) => (item.status !== 'NEW' || 'DECLINED'));
    }
  },
})
