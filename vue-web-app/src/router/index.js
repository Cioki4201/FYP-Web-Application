import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import GamePage from '../views/GamePage.vue'
import MyList from '../views/MyList.vue'

const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView
  },
  {
    path:'/game/:id',
    name:'GamePage',
    component: GamePage,
    props: true
  },
  // MyList Page
  {
    path: '/mylist/:username',
    name: 'MyList',
    component: MyList,
    props: true
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
