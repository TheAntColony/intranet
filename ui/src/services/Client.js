/* eslint-disable no-undef */

import UserAuth from './UserAuth'
import LocalStorage from '../utils/LocalStorageUtil'

const routesWithoutAuthorization = ['/token']

function coreRequest(route, headers, method, payload) {
  var data = payload ? JSON.stringify(payload) : null;

  addAuthorizationHeaderIfExist(route, headers)

  return fetch(route, {
      headers: new Headers({
        ...headers
      }),
      method: method,
      body: data
    })
    .then(checkStatus)
    .then(parseJSON)
}

function addAuthorizationHeaderIfExist(route, headers) {
  var token = LocalStorage.getItem(UserAuth.storageKey)
  var isRouteWithAutorization = routesWithoutAuthorization.indexOf(route) === -1

  if(token && isRouteWithAutorization) {
    headers['Authorization'] = `${token.schema} ${token.token}`
  }
}

function getCoreHeaders() {
 return {
    'Accept': 'application/json',
    'Content-Type': 'application/json'
  };
}

function get(route) {
  return coreRequest(route, getCoreHeaders(), 'GET', null);
}

function post(route, payload) {
  return coreRequest(route, getCoreHeaders(), 'POST', payload);
}

function put(route, payload) {
  return coreRequest(route, getCoreHeaders(), 'PUT', payload);
}

function deleteResource(route) {
  return coreRequest(route, getCoreHeaders(), 'DELETE', null);
}

function checkStatus(response) {
  if (response.status >= 200 && response.status < 300) {
    return response;
  }

  const error = new Error(`HTTP Error ${response.statusText}`);
  error.status = response.statusText;
  error.response = response;
  console.log(error); // eslint-disable-line no-console
  throw error;
}

function parseJSON(response) {
  return response.json();
}

const Client = { get , post, put, deleteResource };
export default Client;
