
const userNameKey = 'websocket_username'

export function setUsername(username){
  return localStorage.setItem(userNameKey,username)
}

export function getUsername(){
  return localStorage.getItem(userNameKey)
}

export function removeUsername(){
  return localStorage.removeItem(userNameKey)
}
