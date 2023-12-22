<template>
  <v-container>
    <div style="margin: 10px">
      <v-text-field v-model="search" :placeholder="'Nom du Livreur'" clearable dense rounded></v-text-field>
    </div>
    <v-data-table
      :headers="headers"
      :items="livreurs"
      :server-items-length="totalElements"
      :options.sync="options"
      :loading="loading"
      class="elevation-1"
    >
      <template v-slot:[`item.disponibility`]="{ item }">
        <v-icon size="small" color="success" v-if="item.disponibility">
          mdi-checkbox-marked-circle
        </v-icon>
        <v-icon size="small" color="error" v-if="!item.disponibility">
          mdi-close-circle
        </v-icon>
      </template>
      <template v-slot:[`item.actions`]="{ item }">
        <v-icon small class="mr-2" @click="openConsultDialog(item)">
          mdi-eye
        </v-icon>
        <v-icon small class="mr-2" @click="openEditDialog(item)">
          mdi-pencil
        </v-icon>
        <v-icon small @click="openDeleteDialog(item)"> mdi-delete </v-icon>
      </template>
    </v-data-table>

    <!-- Delete Confirmation Dialog -->
    <v-dialog v-model="deleteDialog" persistent max-width="300px">
      <v-card>
        <v-card-title class="headline">Confirm Delete</v-card-title>
        <v-card-text>Are you sure you want to delete this item?</v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="green darken-1" text @click="closeDeleteDialog"
            >Cancel</v-btn
          >
          <v-btn color="red darken-1" text @click="confirmDelete"
            >Confirm</v-btn
          >
        </v-card-actions>
      </v-card>
    </v-dialog>
    <!-- Add this inside your <template> tag in LivreursTable.vue -->

    <!-- Edit Dialog -->
    <v-dialog v-model="editDialog" max-width="500px">
      <v-card>
        <v-card-title> Edit Livreur </v-card-title>
        <v-card-text>
          <v-container>
            <v-form ref="editForm">
              <v-text-field
                v-model="editedItem.name"
                label="Name"
                required
              ></v-text-field>
              <v-switch
                v-model="editedItem.disponibility"
                label="disponibility"
              ></v-switch>
              <template v-slot:activator="{ on }">
                <v-text-field
                  v-model="date"
                  label="Picker in menu"
                  prepend-icon="event"
                  readonly
                  v-on="on"
                ></v-text-field>
              </template>
              <v-date-picker
                :max="new Date().toISOString().substr(0, 10)"
                min="1950-01-01"
                @change="save"
              >
              </v-date-picker>
            </v-form>
          </v-container>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="blue darken-1" text @click="closeEditDialog"
            >Cancel</v-btn
          >
          <v-btn color="blue darken-1" text @click="saveEdit">Save</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <!-- Consult Dialog -->
    <v-dialog v-model="consultDialog" max-width="500px">
      <v-card>
        <v-card-title> Livreur Details </v-card-title>
        <v-card-text>
          <p>ID: {{ consultedItem.id }}</p>
          <p>Name: {{ consultedItem.name }}</p>
          <!-- Add other details as necessary -->
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="blue darken-1" text @click="consultDialog = false"
            >Close</v-btn
          >
        </v-card-actions>
      </v-card>
    </v-dialog>

    <!-- Snackbar for Notifications -->
    <v-snackbar v-model="snackbar.show" :color="snackbar.color" bottom right>
      {{ snackbar.message }}
      <v-btn color="white" text @click="snackbar.show = false">Close</v-btn>
    </v-snackbar>
  </v-container>
</template>

<script>
import LivreurService from "@/services/LivreurService";

export default {
  data() {
    return {
      headers: [
        { title: "ID",align: 'start', key: "id" },
        { title: "Name", align: 'center',key: "name" },
        { title: "Creation Date",align: 'center', key: "creationDate" },
        { title: "disponibilité",align: 'center', key: "disponibility" },
        { title: "Actions",align: 'end', key: "actions", sortable: false },
      ],
      livreurs: [],
      options: {},
      totalElements: 0,
      loading: false,
      deleteDialog: false,
      selectedLivreur: null,
      snackbar: { show: false, color: "", message: "" },
      editDialog: false,
      consultDialog: false,
      editedItem: {}, // This will hold the item being edited
      consultedItem: {}, // This will hold the item being consulted
      search: "",
    };
  },
  watch: {
    options: {
      handler() {
        this.fetchLivreurs();
      },
      deep: true,
    },
    search(search) {
      const { page, itemsPerPage } = this.options;
      LivreurService.searchLivreurs(search, page, itemsPerPage)
        .then((response) => {
          this.livreurs = response.data.content;
          this.totalElements = response.data.totalElements;
        })
        .catch((error) => {
          console.error(error);
          this.showSnackbar("red", "Failed to load data");
        })
        .finally(() => (this.loading = false));
    },
  },
  methods: {
    fetchLivreurs() {
      this.loading = true;
      const { page, itemsPerPage } = this.options;
      LivreurService.getLivreurs(page, itemsPerPage)
        .then((response) => {
          this.livreurs = response.data.content;
          this.totalElements = response.data.totalElements;
        })
        .catch((error) => {
          console.error(error);
          this.showSnackbar("red", "Failed to load data");
        })
        .finally(() => (this.loading = false));
    },
    openDeleteDialog(item) {
      this.selectedLivreur = item;
      this.deleteDialog = true;
    },
    closeDeleteDialog() {
      this.deleteDialog = false;
      this.selectedLivreur = null;
    },
    confirmDelete() {
      LivreurService.deleteLivreur(this.selectedLivreur.id)
        .then(() => {
          this.showSnackbar("green", "Item deleted successfully");
          this.fetchLivreurs();
        })
        .catch((error) => {
          console.error(error);
          this.showSnackbar("red", "Failed to delete item");
        })
        .finally(() => {
          this.deleteDialog = false;
          this.selectedLivreur = null;
        });
    },
    showSnackbar(color, message) {
      this.snackbar.color = color;
      this.snackbar.message = message;
      this.snackbar.show = true;
    },
    openEditDialog(item) {
      this.editedItem = Object.assign({}, item); // Create a copy of the item
      this.editDialog = true;
    },

    closeEditDialog() {
      this.editDialog = false;
    },

    saveEdit() {
      LivreurService.updateLivreur(this.editedItem.id, this.editedItem)
        .then(() => {
          // Handle success
          this.showSnackbar("green", "Livreur updated successfully");
          this.showSnackbar = true;
          this.fetchLivreurs(); // Refresh the table data
        })
        .catch((error) => {
          // Handle error
          this.showSnackbar("red", "Error updating livreur: " + error.message);
          this.showSnackbar = true;
        })
        .finally(() => {
          this.closeEditDialog();
        });
    },

    openConsultDialog(item) {
      this.consultedItem = item; // Directly reference the item
      this.consultDialog = true;
    },
  },
  mounted() {
    this.fetchLivreurs();
  },
};
</script>
