import React, {Component} from 'react'
import {
  Switch,
  Route
} from 'react-router-dom'


import ProtectedContent from '../containers/ProtectedContent/ProtectedContent'
import Login from '../containers/Login/Login'
import Signup from '../containers/Signup/Signup'
import ProtectedRoute from '../components/ProtectedRoute'
import NotFound from '../containers/Exception/404'
import ServerError from '../containers/Exception/500'

class Main extends Component {
  render() {
    return (
      <Switch>
        <Route path='/404' render={NotFound}/>
        <Route path='/500' render={ServerError} />
        <Route path='/login' component={Login} />
        <Route path='/signup' component={Signup} />
        
        <ProtectedRoute path='/' component={ProtectedContent} />
      </Switch>)
  }
}

export default Main
