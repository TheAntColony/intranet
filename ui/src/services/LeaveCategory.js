import Client from "./Client";

function get() {
  return Client.get(`/api/leaves/categories`)
}

function getById(id) {
  return Client.get(`/api/leaves/categories/${id}`)
}

function post(data) {
  return Client.post('/api/leaves/categories', data)
}

function deleteResource(id) {
  return Client.deleteResource(`/api/leaves/categories/${id}`)
}

const LeaveCategory = { get, getById, post , deleteResource};
export default LeaveCategory
