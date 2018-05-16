import React from 'react'
import {
  Route,
  Redirect
} from "react-router-dom";
import Home from '../containers/Home/Home'

import UserAuth from '../services/UserAuth'

const ProtectedRoute = ({ component: Component, ...rest }) => (
  <Route
    {...rest}
    render={ props => 
      {
        var redirect = function(pathname) {
          return (
            <Redirect
              to={{
                pathname: pathname,
                state: { from: props.location }
              }}
            />
          )
        }

        var isHomePage = props.location.pathname === '/'
        var isLoginPage = props.location.pathname === '/login'
        var isSignupPage = props.location.pathname === '/signup'

        var renderComponent = (
          <Component {...props} />
        )

        if(UserAuth.isAuthenticated()) {
          if(isLoginPage || isSignupPage) {
            return redirect('/')
          }

          return renderComponent
        } else {
          if(isLoginPage || isSignupPage) {
            return redirect(props.location.pathname)
          }



          return isHomePage ? (<Home />) : redirect('/login')
        }
      }
    }
  />
);

export default ProtectedRoute
