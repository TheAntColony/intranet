import Client from "./Client";

function getToken(email, password) {
  var data = {
    email: email,
    password: password,
    rememberMe: false
  }
  
  return Client.post('/token', data);
}

const Authentication = { 
  getToken
};

export default Authentication;
