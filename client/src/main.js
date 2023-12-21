import './assets/main.css'
import '@mdi/font/css/materialdesignicons.css'
import '@formkit/themes/genesis'
import { genesisIcons } from '@formkit/icons'
import { createProPlugin, toggle } from '@formkit/pro'

import 'vuetify/styles'
import { createVuetify } from 'vuetify'
import * as components from 'vuetify/components'
import * as directives from 'vuetify/directives'
import { plugin, defaultConfig } from '@formkit/vue'

import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
const proPlugin = createProPlugin('fk-125d8e1552e', {
  toggle,
})
const app = createApp(App)

app.use(router)
const vuetify = createVuetify({
  components,
  directives
})
app.use(vuetify)
app.use(plugin, defaultConfig({theme: 'genesis' , icons: {
  ...genesisIcons
},
plugins: [proPlugin],
}))
app.mount('#app')
