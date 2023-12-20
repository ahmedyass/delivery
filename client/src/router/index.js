import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import DeliveryEmployees from '../views/DeliveryEmployees.vue'
import DeliveryEmployee from '../views/DeliveryEmployee.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    //to delete
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    // Delivery employees list
    {
      path: '/employees',
      name: 'employees',
      component: DeliveryEmployees
    },
    {
      path: '/employee/:id',
      name: 'employee',
      component: DeliveryEmployee
    }
  ]
})

export default router
