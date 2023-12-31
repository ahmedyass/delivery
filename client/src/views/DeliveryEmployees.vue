<script setup>
import axios from 'axios'
import { onMounted, ref, getCurrentInstance, computed, nextTick } from 'vue'

const headers = ref([
  { title: 'Boat Type', align: 'start', key: 'name' },
  { title: 'Date', align: 'center', key: 'creationDate' },
  { title: 'Disponibilité', align: 'center', key: 'disponibility' },
  { title: 'Actions', align: 'end', key: 'actions' }
])
const items = ref([])
const dialog = ref(false)
const dialogDelete = ref(false)
const ctx = getCurrentInstance()
const editedIndex = ref(-1)
const editedItem = ref({
  name: '',
  creationDate: null,
  disponibility: false
})
const defaultItem = ref({
  name: '',
  creationDate: null,
  disponibility: false
})

const creationDate = computed({
  get: () => new Date(editedItem.value.creationDate).toISOString().split('T')[0],
  set: (creationDate) => editedItem.value.creationDate = creationDate,
})

const fetchData = async () => {
  try {
    const response = await axios.get(`http://localhost:8080/api/v1/livreurs`)
    items.value = response.data["content"]
  } catch (error) {
    console.error('Error fetching data:', error)
  }
}
onMounted(async () => {
  await fetchData()
})

const goToEmployee = (id) => {
  ctx?.proxy.$router.push({ name: 'employee', params: { id } })
}
const create = async ()=>{
  const response = await axios.post(`http://localhost:8080/api/v1/livreurs`,{
    
      name: editedItem.value.name,
      creationDate: new Date(editedItem.value.creationDate),
      disponibility: editedItem.value.disponibility
    
  })
  items.value.unshift(response.data)
}
const update =async (id)=>{

  const response = await axios.put(`http://localhost:8080/api/v1/livreurs/${id}`,{
    
      name: editedItem.value.name,
      creationDate: new Date(editedItem.value.creationDate),
      disponibility: editedItem.value.disponibility
    
  })
  items.value.splice(editedIndex.value,1,response.data)
}
const save =async () => {
  if (editedIndex.value > -1) {
    await update(editedItem.value.id)
  
  } else {
     await create()
  }
  dialog.value = false
}


const formTitle = computed(() => {
  return editedIndex.value === -1 ? 'New Item' : 'Edit Item'
})
const editItem = (item) => {
  editedIndex.value = items.value.indexOf(item)
  editedItem.value = Object.assign({}, item)
  dialog.value = true
}

const deleteItem = (item) => {
  editedIndex.value = items.value.indexOf(item)
  editedItem.value = Object.assign({}, item)
  dialogDelete.value = true
}

const deleteItemConfirm = async () => {
   await axios.delete(`http://localhost:8080/api/v1/livreurs/${editedItem.value.id}`)
  items.value.splice(editedIndex.value, 1)
  closeDelete()
}

const close = () => {
  dialog.value = false
  nextTick(() => {
    editedItem.value = Object.assign({}, defaultItem.value)
    editedIndex.value = -1
  })
}

const closeDelete = () => {
  dialogDelete.value = false
  nextTick(() => {
    editedItem.value = Object.assign({}, defaultItem.value)
    editedIndex.value = -1
  })
}
</script>

<template>
  <v-data-table :headers="headers" :items="items" item-key="name" items-per-page="10">
    <template v-slot:top>
      <v-toolbar flat>
        <v-toolbar-title>Livreurs</v-toolbar-title>
        <v-divider class="mx-4" inset vertical></v-divider>
        <v-spacer></v-spacer>
        <v-dialog v-model="dialog" max-width="500px">
          <template v-slot:activator="{ props }">
            <v-btn color="primary" dark class="mb-2" v-bind="props"> Ajouter un livreur </v-btn>
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
                  <v-col cols="12" sm="6" md="4" style="display:flex;justify-content:center;align-items:center">
                    <input label="Disponible" type="checkbox" v-model="editedItem.disponibility" />
                    <strong style="margin-left:0.5em">Disponible</strong> 
                  </v-col>
                </v-row>
              </v-container>
            </v-card-text>

            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn color="blue-darken-1" variant="text" @click="close"> Cancel </v-btn>
              <v-btn color="blue-darken-1" variant="text" @click="save"> Save </v-btn>
            </v-card-actions>
          </v-card>
        </v-dialog>
        <v-dialog v-model="dialogDelete" max-width="500px">
          <v-card>
            <v-card-title class="text-h5">Are you sure you want to delete this item?</v-card-title>
            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn color="blue-darken-1" variant="text" @click="closeDelete">Cancel</v-btn>
              <v-btn color="blue-darken-1" variant="text" @click="deleteItemConfirm">OK</v-btn>
              <v-spacer></v-spacer>
            </v-card-actions>
          </v-card>
        </v-dialog>
      </v-toolbar>
    </template>
    <template v-slot:[`item.creationDate`]="{ item }">
    {{new Date(item.creationDate).toLocaleDateString()}}
    </template>
    <template v-slot:[`item.disponibility`]="{ item }">

      <v-icon size="small" color="success"  v-if="item.disponibility" > mdi-checkbox-marked-circle
 </v-icon>
       <v-icon size="small" color="error"  v-if="!item.disponibility" >  mdi-close-circle </v-icon>
    </template>
    <template v-slot:[`item.actions`]="{ item }">
      <v-icon size="small" class="me-2" @click="editItem(item)"> mdi-pencil </v-icon>
      <v-icon size="small" class="me-2" @click="deleteItem(item)"> mdi-delete </v-icon>
      <v-icon size="small" @click="goToEmployee(item.id)"> mdi-eye </v-icon>
    </template>
  </v-data-table>
</template>
