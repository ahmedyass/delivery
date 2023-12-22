import axios from "axios";

const API_URL = "http://localhost:8080/api/v1/livreurs";

export default {
  getLivreurs(page, size) {
    return axios.get(`${API_URL}?page=${page}&size=${size}`);
  },
  searchLivreurs(name, page, size) {
    return axios.get(`${API_URL}?page=${page}&size=${size}&name=${name}`);
  },
  getLivreur(id) {
    return axios.get(`${API_URL}/${id}`);
  },
  deleteLivreur(id) {
    return axios.delete(`${API_URL}/${id}`);
  },
  updateLivreur(id, livreur) {
    return axios.put(`${API_URL}/${id}`, livreur);
  },
  addLivreur(livreur) {
    return axios.post(API_URL, livreur);
  },
};
