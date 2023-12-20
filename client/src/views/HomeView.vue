<script setup>
import TheWelcome from '../components/TheWelcome.vue'
import Dashboard from '../components/Dashboard.vue'
</script>

<template>
  <div>
    <header>
      <div class="wrapper">
        <Dashboard msg="Delivrao App" />
      </div>
    </header>
    <main>
      <TheWelcome />
    </main>
  </div>
</template>
