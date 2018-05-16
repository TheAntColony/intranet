import Client from "./Client"
import UserAuth from './UserAuth'
import LocalStorage from '../utils/LocalStorageUtil'

function getWithOffsetAndLimit(offset, limit) {
  return Client.get(`/api/users/${offset}/${limit}`);
}

function getById(id) {
  return Client.get(`/api/users/${id}`)
}

function getTotal() {
  return Client.get('/api/users/total')
}

function getInfo() {
  return Client.get('/api/users/me')
}

function post(data) {
  return Client.post('/api/users', data)
}

function postLeaves(userId, data) {
  if(!userId) {
    var userInfo = LocalStorage.getItem(UserAuth.userInfoKey)
    userId = userInfo.id
  }
  
  return Client.post(`/api/users/leaves/${userId}`, data)
}

function deleteResource(id) {
  return Client.deleteResource(`/api/users/${id}`)
}

const User = { getWithOffsetAndLimit, getById, getTotal, getInfo, post, postLeaves, deleteResource};
export default User
