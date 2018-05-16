

const LocalStorageUtil = {
  getItem: function(key) {
    var value = localStorage.getItem(key)
    return JSON.parse(value)
  },
  setItem: function(key, value) {
    value = JSON.stringify(value)
    return localStorage.setItem(key, value)
  },
  removeItem: function(key) {
    return localStorage.removeItem(key)
  }
}

export default LocalStorageUtil
