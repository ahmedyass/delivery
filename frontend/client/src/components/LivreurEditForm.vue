<template>
    <v-dialog v-model="dialog" max-width="500px">
      <v-card>
        <v-card-title>Edit Livreur</v-card-title>
        <v-card-text>
          <v-container>
            <v-form ref="form" v-model="valid">
              <v-text-field v-model="livreur.name" label="Name" :rules="[rules.required]"></v-text-field>
              <!-- Add other fields as needed -->
            </v-form>
          </v-container>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="blue darken-1" text @click="close">Cancel</v-btn>
          <v-btn color="green darken-1" text @click="save">Save</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </template>
  
  <script>
  export default {
    props: {
      value: Boolean
    },
    data() {
      return {
        dialog: false,
        valid: true,
        livreur: {
          id: null,
          name: '',
        },
        rules: {
          required: value => !!value || 'Required.'
        }
      };
    },
    watch: {
      value(val) {
        this.dialog = val;
      },
      dialog(val) {
        this.$emit('input', val);
      }
    },
    methods: {
      open(livreur) {
        this.livreur = { ...livreur }; // Clone the livreur object
        this.dialog = true;
      },
      close() {
        this.dialog = false;
      },
      save() {
        if (this.$refs.form.validate()) {
          this.$emit('save', this.livreur);
          this.close();
        }
      }
    }
  };
  </script>
  