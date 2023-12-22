<template>
  <v-dialog v-model="dialog" max-width="300px">
    <v-card>
      <v-card-title class="headline">Confirm Deletion</v-card-title>
      <v-card-text>Are you sure you want to delete this item?</v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="green darken-1" text @click="confirmDelete">Confirm</v-btn>
        <v-btn color="red darken-1" text @click="cancel">Cancel</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
export default {
  props: {
    value: {
      type: Boolean,
      default: false
    }
  },
  computed: {
    dialog: {
      get() { return this.value; },
      set(value) { this.$emit('input', value); }
    }
  },
  methods: {
    open() {
      this.dialog = true;
    },
    confirmDelete() {
      this.$emit('confirm-delete');
      this.dialog = false;
    },
    cancel() {
      this.dialog = false;
    }
  }
};
</script>
