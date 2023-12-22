import { createApp } from 'vue'
import App from './App.vue'
import vuetify from './plugins/vuetify'
import { loadFonts } from './plugins/webfontloader'
import '@formkit/themes/genesis'
import { genesisIcons } from '@formkit/icons'
import { createProPlugin, toggle } from '@formkit/pro'
import { plugin, defaultConfig } from '@formkit/vue'


loadFonts()
const proPlugin = createProPlugin('fk-125d8e1552e', {
  toggle,
})
const app =createApp(App)
app.use(vuetify)
app.use(plugin, defaultConfig({theme: 'genesis' , icons: {
    ...genesisIcons
  },
  plugins: [proPlugin],
  }))
app.mount('#app')
