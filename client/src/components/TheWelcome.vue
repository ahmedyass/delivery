<script setup>
import WelcomeItem from './WelcomeItem.vue'
import DocumentationIcon from './icons/IconDocumentation.vue'
import ToolingIcon from './icons/IconTooling.vue'
import CommunityIcon from './icons/IconCommunity.vue'
</script>

<template>
  <WelcomeItem>
    <template #icon>
      <DocumentationIcon />
    </template>
    <template #heading>Efficacité</template>

    Nous optimisons nos opérations pour offrir des livraisons rapides et sans tracas, aussi bien
    pour nos clients que pour nos équipes
  </WelcomeItem>

  <WelcomeItem>
    <template #icon>
      <ToolingIcon />
    </template>
    <template #heading>Fiabilité</template>
    Nous assurons la livraison en temps et en heure de vos colis de manière sécurisée pour instaurer
    une relation de confiance avec nos clients
  </WelcomeItem>

  <WelcomeItem>
    <template #icon>
      <CommunityIcon />
    </template>
    <template #heading>Transparence</template>
    Nous proposons un suivi en temps réel et une communication claire pour tenir nos clients
    informés du statut de leurs livraisons
  </WelcomeItem>
</template>
