import Authentication from './Authentication'
import User from './User'
import LocalStorage from '../utils/LocalStorageUtil'

const UserAuth = {
  storageKey: 'token',
  userInfoKey: 'userInfo',
  authentication: Authentication,
  
  isAuthenticated() {
    var authorization = this.getAuthorization()
    return authorization !== null
  },
  getAuthorization() {
    var authObj = LocalStorage.getItem(this.storageKey)
    return authObj ? `${authObj.schema} ${authObj.token}` : null 
  },
  authenticate(username, password, cb) {
    this.authentication.getToken(username, password)
      .then((token) => {
        LocalStorage.setItem(this.storageKey, token)
        User.getInfo()
          .then((info) => {
            LocalStorage.setItem(this.userInfoKey, info)
            cb(null, token)
          })
      })
      .catch((e) => {
        cb(e, null)
      })
  },
  signout(cb) {
    LocalStorage.removeItem(this.storageKey)
    setTimeout(cb, 100);
  }
}

export default UserAuth
