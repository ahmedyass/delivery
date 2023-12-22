<template>
    <div>
      <v-btn icon @click="viewLivreur">
        <v-icon>mdi-eye</v-icon>
      </v-btn>
      <v-btn icon @click="editLivreur">
        <v-icon>mdi-pencil</v-icon>
      </v-btn>
      <v-btn icon color="red" @click="confirmDelete">
        <v-icon>mdi-delete</v-icon>
      </v-btn>
    </div>
  </template>
  
  <script>
  export default {
    props: {
      livreurId: {
        type: Number,
        required: true
      }
    },
    methods: {
      viewLivreur() {
        this.$emit('view', this.livreurId);
      },
      editLivreur() {
        this.$emit('edit', this.livreurId);
      },
      confirmDelete() {
        this.$emit('request-delete', this.livreurId);
      }
    }
  };
  </script>
  