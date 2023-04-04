import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import GamePage from '../views/GamePage.vue'
import MyList from '../views/MyList.vue'
import SearchResultsView from '../views/SearchResultsView.vue'
import BrowseView from '@/views/BrowseView.vue'

const routes = [
  // Home Page
  {
    path: '/',
    name: 'home',
    component: HomeView
  },
  // Game Page
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
  },
  // Not Logged In MyList Page
  {
    path: '/mylist/',
    name: 'MyListNotLoggedIn',
    component: MyList,
  },
  // Browse Games Page
  {
    path: '/browse',
    name: 'BrowseGames',
    component: BrowseView
  },
  // Search Results Page
  {
    path: "/search",
    name: "SearchResultsView",
    component: SearchResultsView,
    props: (route) => ({
      games: JSON.parse(route.query.games),
      imagesFetched: JSON.parse(route.query.imagesFetched),
      searching: JSON.parse(route.query.searching),
    }),
  },
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
