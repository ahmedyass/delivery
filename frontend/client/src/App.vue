<template>
  <v-app>
    <v-main>
      <LivreurTable/>
    </v-main>
  </v-app>
</template>

<script>
import LivreurTable from './components/LivreurTable.vue'

export default {
  name: 'App',

  components: {
    LivreurTable,
  },

  data: () => ({
    //
  }),
}
</script>
