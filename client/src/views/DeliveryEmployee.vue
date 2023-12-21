<script setup>
import { onMounted, ref, getCurrentInstance ,computed} from 'vue'
import axios from 'axios'
const employee = ref(null)
const onEdit = ref(false)
const ctx = getCurrentInstance()
const getEmployee = async (id) => {
  const response =  await axios.get(`http://localhost:8080/api/v1/livreurs/${id}`)
  return response.data
}
const update =async ()=>{
 await axios.put(`http://localhost:8080/api/v1/livreurs/${ctx?.proxy.$route?.params?.id}`,{
      name: employee.value.name,
      creationDate: new Date(employee.value.creationDate),
      disponibility: employee.value.disponibility
  })
}
const creationDate = computed({
  get: () => new Date(employee.value.creationDate).toISOString().split('T')[0],
  set: (creationDate) => employee.value.creationDate = creationDate,
})
const openEdit = () => {
  onEdit.value = true
}
const save = async () => {
  await update()
  onEdit.value = false
}
onMounted(async () => {
  if (!ctx?.proxy.$route?.params?.id) throw new Error('The id is unavailable')
  employee.value = await getEmployee(ctx?.proxy.$route?.params?.id)
})
</script>

<template>
  <div style="margin: 0; top: 0" v-if="employee">
    <div class="d-flex flex-column">
      <div style="width: 300px">
        <div class="d-flex">
          <div class="f-1 text-center" v-if="!onEdit">Nom:</div>
          <div class="ml-2 f-2 text-center" v-if="!onEdit">{{ employee.name }}</div>
          <!-- <v-text-field label="Nom" v-model="employee.name" v-if="onEdit" /> -->
          <FormKit
          v-if="onEdit"
                      validation="required"
                      validation-visibility="live"
                      type="text"
                      name="name"
                      id="name"
                      label="Name*"
                      help="Your full name"
                      placeholder="“Jon Doe”"
                      v-model="employee.name"
                    />
        </div>
        <div class="d-flex">
          <div class="f-1 text-center" v-if="!onEdit">Date:</div>
          <div class="ml-2 f-2 text-center" v-if="!onEdit">{{new Date( employee.creationDate).toLocaleDateString() }}</div>
           
          <!-- <v-text-field type="datetime-local" label="Date" v-model="employee.date" v-if="onEdit" /> -->
          <FormKit
                      type="date"
                      label="Date de créaction"
                      help="Entrer la date de création"
                      v-model="creationDate"
                      v-if="onEdit"
                    />
        </div>
        <div class="d-flex">
          <div class="f-1 text-center" v-if="!onEdit">Disponibilité:</div>
          <div class="ml-2 f-2 text-center" v-if="!onEdit"><v-icon size="small" color="success"  v-if="employee.disponibility" > mdi-checkbox-marked-circle
 </v-icon>
       <v-icon size="small" color="error"  v-if="!employee.disponibility" >  mdi-close-circle </v-icon></div>
          <!-- <v-text-field label="Livraisons" v-model="employee.deliveries" v-if="onEdit" /> -->
          <div style="display:flex;justify-content:center;align-items:center" v-if="onEdit">
                    <input label="Disponible" type="checkbox" v-model="employee.disponibility" />
                    <strong style="margin-left:0.5em">Disponible</strong> 
                  </div>
        </div>
      </div>
      <v-btn v-if="!onEdit" @click="openEdit"
        >Edit
        <template v-slot:loader>
          <v-progress-linear indeterminate></v-progress-linear> </template
      ></v-btn>
      <v-btn v-if="onEdit" @click="save"
        >Enregistrer
        <template v-slot:loader>
          <v-progress-linear indeterminate></v-progress-linear> </template
      ></v-btn>
    </div>
  </div>
</template>
<style lang="scss">
.d-flex {
  display: flex;
  align-items: center;
  justify-content: center;
}
.flex-column {
  flex-direction: column;
}
.ml-2 {
  margin-left: 12px;
}
.f-2 {
  font-size: 36px;
}
.f-1 {
  font-size: 20px;
}
.text-center {
  text-align: center;
}
</style>
