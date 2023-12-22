<template>
  <v-container>
    <div style="margin: 10px">
      <v-text-field
        v-model="search"
        :placeholder="'Nom du Livreur'"
        clearable
        dense
        rounded
      ></v-text-field>
    </div>
    <v-data-table
      :headers="headers"
      :items="livreurs"
      :server-items-length="totalElements"
      :options.sync="options"
      :loading="loading"
      class="elevation-1"
    >
      <template v-slot:top>
        <v-toolbar flat>
          <v-toolbar-title>Livreurs</v-toolbar-title>
          <v-divider class="mx-4" inset vertical></v-divider>
          <v-spacer></v-spacer>
          <v-dialog v-model="dialog" max-width="500px">
            <template v-slot:activator="{ props }">
              <v-btn color="primary" dark class="mb-2" v-bind="props">
                Ajouter un livreur
              </v-btn>
            </template>
            <v-card>
              <v-card-title>
                <span class="text-h5">{{ formTitle }}</span>
              </v-card-title>

              <v-card-text>
                <v-container>
                  <v-row>
                    <v-col cols="12" sm="6" md="4">
                      <FormKit
                        validation="required"
                        validation-visibility="live"
                        type="text"
                        name="name"
                        id="name"
                        label="Name*"
                        help="Your full name"
                        placeholder="“Jon Doe”"
                        v-model="editedItem.name"
                      />
                    </v-col>
                    <v-col cols="12" sm="6" md="4">
                      <FormKit
                        type="date"
                        label="Date de créaction"
                        help="Entrer la date de création"
                        v-model="creationDate"
                      />
                    </v-col>
                    <v-col
                      cols="12"
                      sm="6"
                      md="4"
                      style="
                        display: flex;
                        justify-content: center;
                        align-items: center;
                      "
                    >
                      <input
                        label="Disponible"
                        type="checkbox"
                        v-model="editedItem.disponibility"
                      />
                      <strong style="margin-left: 0.5em">Disponible</strong>
                    </v-col>
                  </v-row>
                </v-container>
              </v-card-text>

              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="blue-darken-1" variant="text" @click="close">
                  Cancel
                </v-btn>
                <v-btn color="blue-darken-1" variant="text" @click="save">
                  Save
                </v-btn>
              </v-card-actions>
            </v-card>
          </v-dialog>
        </v-toolbar>
      </template>
      <template v-slot:[`item.creationDate`]="{ item }">
        {{ new Date(item.creationDate).toLocaleDateString() }}
      </template>
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
        { title: "ID", align: "start", key: "id" },
        { title: "Name", align: "center", key: "name" },
        { title: "Creation Date", align: "center", key: "creationDate" },
        { title: "disponibilité", align: "center", key: "disponibility" },
        { title: "Actions", align: "end", key: "actions", sortable: false },
      ],
      livreurs: [],
      options: {},
      totalElements: 0,
      loading: false,
      deleteDialog: false,
      selectedLivreur: null,
      snackbar: { show: false, color: "", message: "" },
      consultDialog: false,
      editedItem: {
        id: "",
        name: "",
        creationDate: new Date(),
        disponibility: false,
      }, // This will hold the item being edited
      consultedItem: {}, // This will hold the item being consulted
      search: "",
      editedIndex: -1,
      dialog: false,
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
      this.dialog = true;
    },
    saveEdit() {
      LivreurService.updateLivreur(this.editedItem.id, this.editedItem)
        .then(() => {
          // Handle success
          this.showSnackbar("green", "Livreur updated successfully");
          this.fetchLivreurs(); // Refresh the table data
        })
        .catch((error) => {
          // Handle error
          this.showSnackbar("red", "Error updating livreur: " + error.message);
        });
    },
    saveCreate() {
      LivreurService.addLivreur(this.editedItem)
        .then(() => {
          // Handle success
          this.showSnackbar("green", "Livreur created successfully");
          this.fetchLivreurs(); // Refresh the table data
        })
        .catch((error) => {
          // Handle error
          this.showSnackbar("red", "Error updating livreur: " + error.message);
        });
    },
    editItem(item) {
      this.editedIndex = this.livreurs.indexOf(item);
      this.editedItem = Object.assign({}, item);
      this.dialog = true;
    },
    openConsultDialog(item) {
      this.consultedItem = item; // Directly reference the item
      this.consultDialog = true;
    },
    async save() {
      if (this.editedIndex > -1) {
        await this.saveEdit(this.editedItem.id);
      } else {
        await this.saveCreate();
      }
      this.dialog = false;
      this.editedItem = {
        id: "",
        name: "",
        creationDate: new Date(),
        disponibility: false,
      };
    },
    close() {
      this.dialog = false;
      this.$nextTick(() => {
        this.editedItem = Object.assign(
          {},
          { id: "", name: "", creationDate: new Date(), disponibility: false }
        );
        this.editedIndex = -1;
      });
    },
  },
  computed: {
    formTitle() {
      return this.editedIndex === -1 ? "New Item" : "Edit Item";
    },
    creationDate: {
      get() {
        return new Date(this.editedItem.creationDate)
          .toISOString()
          .split("T")[0];
      },
      set(creationDate) {
        this.editedItem.creationDate = creationDate;
      },
    },
  },
  mounted() {
    this.fetchLivreurs();
  },
};
</script>
