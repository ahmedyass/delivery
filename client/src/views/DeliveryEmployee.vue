<script setup>
import { onMounted, ref, getCurrentInstance } from 'vue'
const employee = ref(null)
const onEdit = ref(false)
const ctx = getCurrentInstance()
const getEmployee = (id) => {
  return { id: 1, name: 'test', date: '2023-11-15T02:30', deliveries: 13 }
}
const openEdit = () => {
  onEdit.value = true
}
const save = () => {
  onEdit.value = false
}
onMounted(async () => {
  if (!ctx?.proxy.$route?.params?.id) throw new Error('The id is unavailable')
  employee.value = await getEmployee(ctx?.proxy.$route?.params?.id)
})
</script>

<template>
  <div style="maring: 0; top: 0" v-if="employee">
    <div class="d-flex flex-column">
      <div style="width: 300px">
        <div class="d-flex">
          <div class="f-1 text-center" v-if="!onEdit">Nom:</div>
          <div class="ml-2 f-2 text-center" v-if="!onEdit">{{ employee.name }}</div>
          <v-text-field label="Nom" v-model="employee.name" v-if="onEdit" />
        </div>
        <div class="d-flex">
          <div class="f-1 text-center" v-if="!onEdit">Date:</div>
          <div class="ml-2 f-2 text-center" v-if="!onEdit">{{ employee.date }}</div>
          <v-text-field type="datetime-local" label="Date" v-model="employee.date" v-if="onEdit" />
        </div>
        <div class="d-flex">
          <div class="f-1 text-center" v-if="!onEdit">Livraisons:</div>
          <div class="ml-2 f-2 text-center" v-if="!onEdit">{{ employee.deliveries }}</div>
          <v-text-field label="Livraisons" v-model="employee.deliveries" v-if="onEdit" />
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
