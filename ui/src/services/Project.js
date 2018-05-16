import Client from './Client';

function get() {
  return Client.get(`/api/projects`);
}

function getById(id) {
  return Client.get(`/api/projects/${id}`)
}

function post(data) {
  return Client.post('/api/projects', data)
}

function deleteResource(id) {
  return Client.deleteResource(`/api/projects/${id}`)
}

const Project = { get, getById, post , deleteResource};
export default Project
