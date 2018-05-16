import React, {Component} from 'react'

import {
  Route,
  Redirect,
  Switch
} from 'react-router-dom'

import UserList from '../containers/UserList/UserList'
import ProtectedHome from '../containers/ProtectedHome/ProtectedHome'
import UserDetail from '../containers/UserDetail/UserDetail'
import LeaveCalendar from '../containers/LeaveCalendar/LeaveCalendar'
import LeaveRequest from '../containers/LeaveRequest/LeaveRequest'
import LeaveEvaluate from '../containers/LeaveEvaluate/LeaveEvaluate'
import ProjectList from '../containers/ProjectList/ProjectList'
import ProjectCreate from '../containers/ProjectCreate/ProjectCreate';

class Protected extends Component {
  render() {
    return (
    <Switch>
      <Route path='/' exact component={ProtectedHome} />

      <Route path='/users' exact component={UserList} />
      <Route path='/users/:id/:type' component={UserDetail} />
      <Route path='/profile' component={UserDetail} />

      <Route path='/leaves' exact component={LeaveCalendar} />
      <Route path='/leaves/request' exact component={LeaveRequest} />
      <Route path='/leaves/evaluate' exact component={LeaveEvaluate} />

      <Route path='/projects' exact component={ProjectList} />
      <Route path='/projects/create' exact component={ProjectCreate} />
      <Route path='/projects/:id' exact component={ProjectList} />
      <Redirect to='/404' />
    </Switch>)
  }
}

export default Protected
