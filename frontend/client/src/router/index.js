import Vue from 'vue';
import VueRouter from 'vue-router';
import LivreursView from '../views/LivreursView.vue';

Vue.use(VueRouter);

const routes = [
  {
    path: '/livreurs',
    name: 'Livreurs',
    component: LivreursView
  },
  // other routes...
];

const router = new VueRouter({
  routes
});

export default router;
